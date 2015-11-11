package com.example.calshare;

import android.graphics.drawable.Drawable;
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
import android.widget.TextView;

import com.example.calshare.db.CalshareDatabaseHelper;
import com.example.calshare.db.DateShiftDatabaseManager;
import com.example.calshare.model.Shift;
import com.example.calshare.view.DateTextView;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.YearMonth;

import java.util.Map;

public class MonthFragment extends Fragment {

    private CalshareDatabaseHelper mDbHelper;
    private MonthGridAdapter monthGridAdapter;
    private GridView monthGridView;
    private TextView shiftDetailTextView;
    private final static int GRID_SIZE = 42;
    private LocalDate[] localDates;

//    private LocalDate previouslyLongClickedDate;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        Log.i("MonthFragment", "onCreateView");
        mDbHelper = new CalshareDatabaseHelper(getActivity());

        View v = inflater.inflate(R.layout.month_layout, container, false);
        monthGridView = (GridView) v.findViewById(R.id.monthGridView);

        // set up ShiftDetail text view
        shiftDetailTextView = (TextView) v.findViewById(R.id.shiftDetailTextView);

        View viewPager = getActivity().findViewById(R.id.calendarViewPager);
        View pagerTitleStrip = getActivity().findViewById(R.id.pager_title_strip);
        View monthDayOfWeekHeader = getActivity().findViewById(R.id.month_dayofweek_Header);

        int pagerTitleStripHeight = pagerTitleStrip.getHeight();
        int viewPagerHeight = viewPager.getHeight();
        int monthDayOfWeekHeaderHeight = getActivity().getResources().getInteger(R.integer.month_header_height);
        Log.i("MonthFragment", "month header height " + Integer.toString(monthDayOfWeekHeaderHeight));

        monthGridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            /**
             * Open the Shift selection dialog
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

                // we keep track of what date was long clicked
                CalendarActivity calendarActivity = (CalendarActivity)getActivity();
                calendarActivity.setSelectedDate(textView.getLocalDate());
                //previouslyLongClickedDate = textView.getLocalDate();

                DialogFragment dialogFragment = new ShiftSelectorDialogFragment();
                Bundle bundle = new Bundle();
                bundle.putSerializable("yearMonth", yearMonth);
                bundle.putSerializable("localDate", textView.getLocalDate());
                dialogFragment.setArguments(bundle);
                dialogFragment.show(getFragmentManager(), "df");
                return false;
            }
        });

        monthGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            /**
             * Set the shift details at the bottom of the month view
             * @param adapterView
             * @param view
             * @param i
             * @param l
             */
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                // get the day
                LinearLayout linearLayout = (LinearLayout)view;
                DateTextView dateTextView = (DateTextView)linearLayout.getChildAt(0);

                if (shiftDetailTextView != null) {
                    //Log.i("MonthFragment", "shiftDetailTextView height " + shiftDetailTextView.getHeight());
                    updateShiftDetailTextView(dateTextView.getLocalDate());
                }

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

                calendarActivity.invalidateOtherFragments(MonthFragment.this);
            }
        });

        LocalDate todayLocalDate = new LocalDate(new DateTime());
        localDates = getLocalDates(todayLocalDate);

        monthGridAdapter = new MonthGridAdapter(MonthFragment.this.getActivity(),
                DateShiftDatabaseManager.getInstance().getMonthDateToShiftMap(), localDates);
        monthGridView.setAdapter(monthGridAdapter);

        return v;

    }

    private LinearLayout getLayoutFromDate(AdapterView<?> adapterView, LocalDate localDate) {
        int size = adapterView.getCount();
        for (int i = 0; i < size; i++) {
            LinearLayout linearLayout = (LinearLayout)adapterView.getChildAt(i);
            DateTextView dateTextView = (DateTextView)linearLayout.getChildAt(0);
            if (localDate.equals(dateTextView.getLocalDate())) {
                return linearLayout;
            }

        }
        return null;
    }

    /**
     * Updates shift Detail text view with latest data from database
     * @param selectedDate
     */
    private void updateShiftDetailTextView(LocalDate selectedDate) {
        Map<String, Shift> dateToShiftMap = DateShiftDatabaseManager.getInstance().getMonthDateToShiftMap();
        Shift shift = dateToShiftMap.get(selectedDate.toString("YYYYMMdd"));
        if (shift != null) {
            shiftDetailTextView.setText(shift.toString());
        }
        else {
            shiftDetailTextView.setText("");
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.i("MonthFragment", "onStart");
        CalendarActivity calendarActivity = (CalendarActivity)getActivity();
        if (calendarActivity.getSelectedDate() != null) {
            updateShiftDetailTextView(calendarActivity.getSelectedDate());
        }
    }


    // invoked by CalendarPagerAdapter after long click
    public void refreshAdapter() {
        if (monthGridAdapter != null) {
            monthGridAdapter.refreshAdapter(DateShiftDatabaseManager.getInstance().getMonthDateToShiftMap());
            Log.i("MonthFragment", "refreshAdapter");
            monthGridView.invalidateViews();
        }
        CalendarActivity calendarActivity = (CalendarActivity)getActivity();
        if (shiftDetailTextView != null && calendarActivity.getSelectedDate() != null) {
            updateShiftDetailTextView(calendarActivity.getSelectedDate());
        }
    }

    // force refresh of selected date
    // invokes post() because called in worker thread
    public void invalidateViews() {
        if (monthGridView != null) {
            Log.i("MonthFragment", "invalidating MonthFragment " + this);
            monthGridView.post(new Runnable() {
                public void run() {
                    monthGridView.invalidateViews();
                    CalendarActivity calendarActivity = (CalendarActivity) getActivity();
                    if (calendarActivity.getSelectedDate() != null) {
                        updateShiftDetailTextView(calendarActivity.getSelectedDate());
                    }
                }
            });;
        }
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



}
