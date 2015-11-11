package com.example.calshare;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.calshare.adapter.ShiftListAdapter;
import com.example.calshare.db.CalshareContract;
import com.example.calshare.db.CalshareDatabaseHelper;
import com.example.calshare.model.DateShiftLink;
import com.example.calshare.model.Shift;
import com.j256.ormlite.dao.Dao;

import org.joda.time.LocalDate;
import org.joda.time.YearMonth;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static com.example.calshare.R.layout.shift_list_row;


public class ShiftSelectorDialogFragment extends DialogFragment {

    private CalshareDatabaseHelper mDbHelper;
    private ListView mShiftListView;
    private ShiftListAdapter mListAdapter;

    private YearMonth yearMonth;
    private LocalDate localDate;


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        yearMonth = (YearMonth)getArguments().get("yearMonth");
        localDate = (LocalDate)getArguments().get("localDate");

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View dialogView = getActivity().getLayoutInflater().inflate(R.layout.shift_list_dialog, null);
        builder.setView(dialogView);

        mShiftListView = (ListView) dialogView.findViewById(R.id.shiftListView);
        // click listener for the listView for when user short presses on a list item
        mShiftListView.setOnItemClickListener(new ShiftItemClickListener());

        // Create the AlertDialog object and return it
        return builder.create();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = super.onCreateView(inflater, container, savedInstanceState);
        mDbHelper = new CalshareDatabaseHelper(getActivity());
        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        new ReadShiftsTask().execute(new Void[]{});
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        Log.i("ShiftSelectorDialogFrag", "onDismiss");
        Activity activity = getActivity();
        if (activity instanceof CalendarActivity) {
            ((CalendarActivity)activity).refreshFragments();
        }
    }


    /**
     * When a shift in the list is clicked, associate the date with the shift
     */
    private class ShiftItemClickListener implements ListView.OnItemClickListener {

        /**
         * Load the DateShiftLink from the database, set the shift
         * Then exit the dialog
         *
         * @param parent
         * @param view
         * @param position
         * @param id
         */
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            TableLayout tableLayout = (TableLayout)view;
            CharSequence shiftName = getTitleFromShiftRow(tableLayout);
            if (shiftName != null && shiftName.length() != 0) {

                String localDateStr = localDate.toString("YYYYMMdd");
                List<DateShiftLink> dateShiftLinks = null;
                Dao<DateShiftLink, String> dateShiftLinkDao = null;
                DateShiftLink dateShiftLink = null;

                try {
                    dateShiftLinkDao = mDbHelper.getDateShiftLinkDao();
                    dateShiftLinks = dateShiftLinkDao.queryForEq(
                            CalshareContract.DateShiftLink.COLUMN_NAME_SHIFT_DATE, localDateStr);

                } catch (SQLException e) {
                    e.printStackTrace();
                }

                // DateShiftLink exists - update and save it.
                if (dateShiftLinks != null && dateShiftLinks.size() == 1) {
                    //Log.i(ShiftSelectorDialogFragment.class.getName(), "found DateShiftLink object with date \'" + localDateStr + "\'");
                    dateShiftLink = dateShiftLinks.get(0);
                    dateShiftLink.setShift(shiftName.toString());

                    // Save the changes
                    try {
                        dateShiftLinkDao.update(dateShiftLink);
                        //Log.i(ShiftSelectorDialogFragment.class.getName(), "saved DateShiftLink");
                    }
                    catch (SQLException e) {
                        Log.e(ShiftSelectorDialogFragment.class.getName(), e.getMessage(), e);
                        e.printStackTrace();
                    }

                }
                else {
                    // create a new DateShiftLink and save it
                    //Log.i(ShiftSelectorDialogFragment.class.getName(), "saving DateShiftLink object with date \'" + localDateStr + "\', shift=" + shiftName);
                    dateShiftLink = new DateShiftLink();
                    dateShiftLink.setShift(shiftName.toString());
                    dateShiftLink.setDate(localDateStr);
                    new CreateDateShiftLinkTask().execute(dateShiftLink);
                }
            }

            ShiftSelectorDialogFragment.this.dismiss();

        }

        /**
         * A helper method to get the title from a TableLayout view object corresponding to a shift row
         *
         * @param tableLayout
         * @return
         */
        CharSequence getTitleFromShiftRow(TableLayout tableLayout) {
            TableRow row = (TableRow)tableLayout.getChildAt(0);
            TextView textView = (TextView)row.getChildAt(0);
            CharSequence title = textView.getText();
            return title;
        }
    }

    // Copied from ShiftActivity
    class ReadShiftsTask extends AsyncTask<Void, Void, List<Shift>> {

        @Override
        protected List<com.example.calshare.model.Shift> doInBackground(Void... params) {

            try {
                Dao<Shift, String> shiftDao = mDbHelper.getDao(com.example.calshare.model.Shift.class);
                return shiftDao.queryForAll();
            }
            catch (SQLException e) {
                e.printStackTrace();
                return new ArrayList<Shift>();
            }
        }

        @Override
        protected void onPostExecute(List<com.example.calshare.model.Shift> results) {

            if (mListAdapter == null) {
                mListAdapter = new ShiftListAdapter(ShiftSelectorDialogFragment.this.getActivity(), shift_list_row, results);
                mShiftListView.setAdapter(mListAdapter);
            }
            else { // replace the contents of the adapter
                mListAdapter.refreshAdapter(results);

            }

        }

    }

    /**
     * Create a new DateShiftLink
     * @author silkyoak
     *
     */
    class CreateDateShiftLinkTask extends AsyncTask<DateShiftLink, Void, Void> {

        @Override
        protected Void doInBackground(DateShiftLink... params) {
            long newRowId = 0;

            try {
                newRowId = mDbHelper.getDateShiftLinkDao().create(params[0]);
            }
            catch (SQLException e) {
                e.printStackTrace();
            }

            System.out.println("new DateShiftLink row id: " + newRowId);
            return null;
        }


    }
}
