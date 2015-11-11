package com.example.calshare;

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
import com.example.calshare.view.DateTextView;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;

public class WeekFragment extends Fragment {

    private LocalDate[] localDates;
    private static final int ARRAY_SIZE = 7;
    private CalshareDatabaseHelper mDbHelper;
    private WeekGridAdapter weekGridAdapter;
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
        View v = inflater.inflate(R.layout.week_layout, container, false);
        weekGridview = (GridView) v.findViewById(R.id.weekGridView);
        int height = weekGridview.getHeight();

        LocalDate selectedDate = ((CalendarActivity)getActivity()).getSelectedDate();
        if (selectedDate == null) {
            selectedDate = new LocalDate(new DateTime());
        }
        localDates = getLocalDates(selectedDate);

        weekGridAdapter = new WeekGridAdapter(WeekFragment.this.getActivity(),
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
                //WeekGridAdapter weekGridAdapter = (WeekGridAdapter) adapterView.getAdapter();

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

        weekGridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                // get the day
                LinearLayout linearLayout = (LinearLayout)view;
                DateTextView dateTextView = (DateTextView)linearLayout.getChildAt(0);

                // set the background of the currently select view and reset the background of the previously selected view
                CalendarActivity calendarActivity = (CalendarActivity)getActivity();
                if (calendarActivity.getSelectedDate() != null) {
                    // deselect previously selected
                    LinearLayout previouslySelectedView = getLayoutFromDate(adapterView, calendarActivity.getSelectedDate());
                    if (previouslySelectedView != null) {
                        previouslySelectedView.setBackgroundDrawable(calendarActivity.getSelectedBackgroundDrawable());
                    }
                }

                // select
                calendarActivity.setSelectedDate(dateTextView.getLocalDate());
                calendarActivity.setSelectedBackgroundDrawable(linearLayout.getBackground());
                linearLayout.setBackgroundResource(R.drawable.normal_grid_item_border_selected);
                calendarActivity.invalidateOtherFragments(WeekFragment.this);
            }
        });

        return v;
    }

    /**
     * Get the linear layout corresponding to the given date
     * @param adapterView
     * @param localDate
     * @return
     */
    private LinearLayout getLayoutFromDate(AdapterView<?> adapterView, LocalDate localDate) {
        for (int i = 0; i < ARRAY_SIZE; i++) {
            LinearLayout linearLayout = (LinearLayout)adapterView.getChildAt(i);
            DateTextView dateTextView = (DateTextView)linearLayout.getChildAt(0);
            if (localDate.equals(dateTextView.getLocalDate())) {
                return linearLayout;
            }

        }
        return null;
    }

    @Override
    public void onStart() {
        Log.i("WeekFragment", "onStart");
        super.onStart();
    }


    // invoked by CalendarPagerAdapter to refresh shifts after long click
    public void refreshAdapter() {
        if (weekGridAdapter != null) {
            weekGridAdapter.refreshAdapter(DateShiftDatabaseManager.getInstance().getWeekDateToShiftMap());
            Log.i("WeekFragment", "refreshAdapter");
            weekGridview.invalidateViews();
        }
    }

    // invoked by CalendarPagerAdapter to refresh background after normal click
    // called in worker thread thus invokes post() to invalidate views
    public void invalidateViews() {
        if (weekGridview != null) {
            Log.i("WeekFragment", "invalidating WeekFragment " + this);
            CalendarActivity calendarActivity = (CalendarActivity) getActivity();
            weekGridAdapter.updateLocalDates(getLocalDates(calendarActivity.getSelectedDate()));
            weekGridview.post(new Runnable() {
                public void run() {
                    weekGridview.invalidateViews();
                }
            });;
        }
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


}
