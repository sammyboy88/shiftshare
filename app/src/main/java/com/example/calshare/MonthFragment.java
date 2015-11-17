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
import android.widget.TextView;

import com.example.calshare.db.CalshareDatabaseHelper;
import com.example.calshare.db.DateShiftDatabaseManager;
import com.example.calshare.model.Shift;
import com.example.calshare.view.DateLinearLayout;
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
                DateLinearLayout linearLayout = (DateLinearLayout)view;
                DateTextView dateTextView = (DateTextView)linearLayout.getChildAt(0);

                if (shiftDetailTextView != null) {
                    updateShiftDetailTextView(dateTextView.getLocalDate());
                }

                // set the background of the currently select view and reset the background of the previously selected view
                CalendarActivity calendarActivity = (CalendarActivity)getActivity();
                Log.i("MonthFragment", "previously selected Date " + calendarActivity.getSelectedDate());
                if (calendarActivity.getSelectedDate() != null) {
                    // restore previously selected date
                    LinearLayout previouslySelectedView = getLayoutFromDate(adapterView, calendarActivity.getSelectedDate());
                    Log.i("MonthFragment", "previously selected view " + previouslySelectedView);

                    if (previouslySelectedView != null) {
                        monthGridAdapter.setBackground(previouslySelectedView, calendarActivity.getSelectedDate());
                    }
                }

                // select
                calendarActivity.setSelectedDate(dateTextView.getLocalDate());
                //calendarActivity.setSelectedBackgroundResid(linearLayout.getBackgroundResource());
                Log.i("MonthFragment", "setBackgroundDrawable " + linearLayout.getBackground());
                linearLayout.setBackgroundResource(R.drawable.normal_grid_item_border_selected);

                calendarActivity.invalidateOtherFragments(MonthFragment.this);
            }
        });

        YearMonth todayYearMonth = new YearMonth(new DateTime());

        monthGridAdapter = new MonthGridAdapter(MonthFragment.this.getActivity(),
                DateShiftDatabaseManager.getInstance().getMonthDateToShiftMap(), todayYearMonth);
        monthGridView.setAdapter(monthGridAdapter);

        return v;

    }

    private LinearLayout getLayoutFromDate(AdapterView<?> adapterView, LocalDate localDate) {
        int size = adapterView.getCount();
        for (int i = 0; i < size; i++) {
            LinearLayout linearLayout = (LinearLayout)adapterView.getChildAt(i);
            DateTextView dateTextView = (DateTextView)linearLayout.getChildAt(0);
            if (localDate.equals(dateTextView.getLocalDate())) {
                Log.i("MonthFragment", "getLayoutFromDate " + localDate);
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
        //Log.i("MonthFragment", "onStart");
        CalendarActivity calendarActivity = (CalendarActivity)getActivity();
        if (calendarActivity.getSelectedDate() != null) {
            updateShiftDetailTextView(calendarActivity.getSelectedDate());
        }
    }


    // invoked by CalendarPagerAdapter after long click
    public void refreshAdapter() {
        if (monthGridAdapter != null) {
            monthGridAdapter.refreshAdapter(DateShiftDatabaseManager.getInstance().getMonthDateToShiftMap());
            //Log.i("MonthFragment", "refreshAdapter");
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

    // invoked when user clicks on a month square in the YearView
    public void switchToMonth(YearMonth yearMonth) {
        if (monthGridAdapter != null && monthGridView != null) {
            monthGridAdapter.setYearMonth(yearMonth);
            monthGridView.invalidateViews();
        }
    }

    public YearMonth getYearMonth() {
        if (monthGridAdapter != null) {
            return monthGridAdapter.getYearMonth();
        }
        else {
            return new YearMonth(new DateTime());
        }
    }


}
