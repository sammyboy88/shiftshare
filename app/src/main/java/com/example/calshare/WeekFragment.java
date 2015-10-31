package com.example.calshare;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;

import com.example.calshare.db.CalshareDatabaseHelper;
import com.example.calshare.db.DateShiftDatabaseManager;
import com.example.calshare.model.DateShiftLink;
import com.example.calshare.view.DateTextView;
import com.j256.ormlite.dao.Dao;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WeekFragment extends Fragment {

    private LocalDate[] localDates;
    private static final int ARRAY_SIZE = 7;
    private CalshareDatabaseHelper mDbHelper;
    private WeekGridAdapterNew weekGridAdapter;
    private GridView weekGridview;


	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        Log.i("WeekFragment", "onCreateView");
        mDbHelper = new CalshareDatabaseHelper(getActivity());
        View v = inflater.inflate(R.layout.week_layout_new, container, false);
        weekGridview = (GridView) v.findViewById(R.id.weekGridView);
        int height = weekGridview.getHeight();

        LocalDate todayLocalDate = new LocalDate(new DateTime());
        localDates = getLocalDates(todayLocalDate);

        weekGridAdapter = new WeekGridAdapterNew(WeekFragment.this.getActivity(),
                DateShiftDatabaseManager.getInstance().getWeekDateToShiftMap(), localDates);
        weekGridview.setAdapter(weekGridAdapter);

        weekGridview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            /**
             * Get the date that the user clicked on
             * @param adapterView
             * @param view
             * @param i
             * @param l
             * @return
             */
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                // get the month and year
                WeekGridAdapterNew weekGridAdapter = (WeekGridAdapterNew) adapterView.getAdapter();

                // get the day
                LinearLayout linearLayout = (LinearLayout) view;
                DateTextView textView = (DateTextView) linearLayout.getChildAt(0);
                //Log.i("dialog", textView.getText().toString());
                //Log.i("dialog", textView.getLocalDate().toString());

                DialogFragment dialogFragment = new ShiftSelectorDialogFragment();
                Bundle bundle = new Bundle();
                bundle.putSerializable("localDate", textView.getLocalDate());
                dialogFragment.setArguments(bundle);
                dialogFragment.show(getFragmentManager(), "df");
                return false;
            }
        });

        return v;
    }

    @Override
    public void onStart() {
        Log.i("WeekFragment", "onStart");
        super.onStart();
    }


    // invoked by CalendarPagerAdapter
    public void refreshAdapter() {
        weekGridAdapter.refreshAdapter(DateShiftDatabaseManager.getInstance().getWeekDateToShiftMap());
        Log.i("WeekFragment", "refreshAdapter");
        weekGridview.invalidateViews();
    }

    private LocalDate[] getLocalDates(LocalDate currentWeekDate) {
        // set up the day numbers
        int todayDayOfWeek = currentWeekDate.getDayOfWeek();
        if (todayDayOfWeek == 7) {
            todayDayOfWeek = 0;
        }

        LocalDate firstDayOfWeekDate = currentWeekDate.minusDays(todayDayOfWeek);

        LocalDate[] localDates = new LocalDate[ARRAY_SIZE];
        LocalDate dateOfWeek = firstDayOfWeekDate;
        for (int i = 0; i < ARRAY_SIZE ; i++) {
            localDates[i] = dateOfWeek;
            dateOfWeek = dateOfWeek.plusDays(1);
        }
        return localDates;
    }

    // Should only be required when shift assignment is changed in the shift selection dialog
    private class ReadDateShiftLinksTask extends AsyncTask<LocalDate, Void, List<DateShiftLink>> {

        @Override
        protected List<DateShiftLink> doInBackground(LocalDate... params) {

            try {
                if (mDbHelper == null) {
                    mDbHelper = new CalshareDatabaseHelper(getActivity());
                }
                Dao<DateShiftLink, String> dateShiftLinksDao = mDbHelper.getDateShiftLinkDao();
                return dateShiftLinksDao.queryForAll();
            }
            catch (SQLException e) {
                Log.e("ReadDateShiftLinksTask", e.getMessage(), e);
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(List<DateShiftLink> results) {
            Log.i("WeekFragment", "onPostExecute, results lenght = " + results.size());
            // convert results into a map of DateString-> ShiftString
            Map<String, String> dateToShiftMap = new HashMap<String, String>();
            for (DateShiftLink dateShiftLink : results) {
                //Log.i("WeekFragment", "getting shifts for week: " + dateShiftLink.getDate());
                dateToShiftMap.put(dateShiftLink.getDate(), dateShiftLink.getShift());
            }

//            if (weekGridAdapter == null) {
//                weekGridAdapter = new WeekGridAdapter(WeekFragment.this.getActivity(), dateToShiftMap, localDates);
//                weekGridview.setAdapter(weekGridAdapter);
//            }
//            else { // replace the contents of the adapter
                weekGridAdapter.refreshAdapter(dateToShiftMap);
                Log.i("WeekFragment", "onPostExecute - invalidate views");
                weekGridview.invalidateViews();
            //}
            //customAdapter.setSelectedRows(mSelectedArray);

        }

    }
}
