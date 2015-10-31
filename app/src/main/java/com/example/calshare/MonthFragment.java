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
import com.example.calshare.model.DateShiftLink;
import com.example.calshare.view.DateTextView;
import com.j256.ormlite.dao.Dao;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.YearMonth;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MonthFragment extends Fragment {

    private CalshareDatabaseHelper mDbHelper;
    private MonthGridAdapter monthGridAdapter;
    private GridView monthGridView;
    private final static int GRID_SIZE = 42;
    private LocalDate[] localDates;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        mDbHelper = new CalshareDatabaseHelper(getActivity());

        View v = inflater.inflate(R.layout.month_layout, container, false);
        monthGridView = (GridView) v.findViewById(R.id.monthGridView);
        int height = monthGridView.getHeight();

        monthGridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

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
                MonthGridAdapter monthGridAdapter = (MonthGridAdapter)adapterView.getAdapter();
                YearMonth yearMonth = monthGridAdapter.getYearMonth();
                Log.i("dialog", yearMonth.toString());

                // get the day
                LinearLayout linearLayout = (LinearLayout)view;
                DateTextView textView = (DateTextView)linearLayout.getChildAt(0);
                Log.i("dialog", textView.getText().toString());
                Log.i("dialog", textView.getLocalDate().toString());

                DialogFragment dialogFragment = new ShiftSelectorDialogFragment();
                Bundle bundle = new Bundle();
                bundle.putSerializable("yearMonth", yearMonth);
                bundle.putSerializable("localDate", textView.getLocalDate());
                dialogFragment.setArguments(bundle);
                dialogFragment.show(getFragmentManager(), "df");
                return false;
            }
        });

        LocalDate todayLocalDate = new LocalDate(new DateTime());
        localDates = getLocalDates(todayLocalDate);

        monthGridAdapter = new MonthGridAdapter(MonthFragment.this.getActivity(), null, localDates);
        monthGridView.setAdapter(monthGridAdapter);

        return v;

    }

    @Override
    public void onStart() {
        super.onStart();
        // load DateShiftLinks
        Log.i("MonthFragment", "onStart");
        reload();
    }

    /**
     * Invoked by CalendarActivity
     */
    public void reload() {
        LocalDate todayLocalDate = new LocalDate(new DateTime());
        localDates = getLocalDates(todayLocalDate);
        new ReadDateShiftLinksTask().execute(localDates);
    }


    private LocalDate[] getLocalDates(LocalDate currentMonthDate) {
        // set up the day numbers
        int todayDayOfMonth = currentMonthDate.getDayOfMonth();
        int daysInMonth = currentMonthDate.dayOfMonth().getMaximumValue();
        LocalDate firstDayOfMonthDate = currentMonthDate.minusDays(todayDayOfMonth - 1);
        int firstDayOfMonthDayOfWeek = firstDayOfMonthDate.getDayOfWeek() % 7;

        // get the number of days in the previous month
        LocalDate lastDayOfPreviousMonth = currentMonthDate.minusDays(todayDayOfMonth);
        // get date to start from in previous month
        LocalDate startDateInPreviousMonth = lastDayOfPreviousMonth.minusDays(firstDayOfMonthDayOfWeek - 1);
        int startDateInPreviousMonthDayOfMonth = startDateInPreviousMonth.getDayOfMonth();

        LocalDate[] localDates = new LocalDate[GRID_SIZE];
        LocalDate dateOfMonth = startDateInPreviousMonth;
        for (int i = 0; i < GRID_SIZE ; i++) {
            localDates[i] = dateOfMonth;
            dateOfMonth = dateOfMonth.plusDays(1);
        }
        return localDates;
    }

    private class ReadDateShiftLinksTask extends AsyncTask<LocalDate, Void, List<DateShiftLink>> {

        @Override
        protected List<DateShiftLink> doInBackground(LocalDate... params) {

            try {
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
            Log.i("MonthFragment", "onPostExecute, results lenght = " + results.size());
            // convert results into a map of DateString-> ShiftString
            Map<String, String> dateToShiftMap = new HashMap<String, String>();
            for (DateShiftLink dateShiftLink : results) {
                //Log.i("MonthFra#onPostExecute", "getting shifts for month: " + dateShiftLink.getDate());
                dateToShiftMap.put(dateShiftLink.getDate(), dateShiftLink.getShift());
            }

            monthGridAdapter.refreshAdapter(dateToShiftMap);
                Log.i("MonthFragment", "onPostExecute - invalidate views");
            monthGridView.invalidateViews();

            //customAdapter.setSelectedRows(mSelectedArray);

        }

    }


}
