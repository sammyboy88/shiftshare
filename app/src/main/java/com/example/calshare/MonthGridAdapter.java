package com.example.calshare;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.example.calshare.model.Shift;
import com.example.calshare.view.DateLinearLayout;
import com.example.calshare.view.DateTextView;

import org.joda.time.LocalDate;
import org.joda.time.YearMonth;

import java.util.HashMap;
import java.util.Map;

public class MonthGridAdapter extends BaseAdapter implements ListAdapter {

    private final static int GRID_SIZE = 42;
	private Activity context;
    private LocalDate todayDate;
    private Map<String, Shift> dateToShiftMap = new HashMap<String, Shift>();
    private YearMonth yearMonth;

    // calculated from yearMonth
    private LocalDate[] localDates;


	public MonthGridAdapter(Activity ctx, Map<String, Shift> results, YearMonth newYearMonth) {
		context = ctx;
        dateToShiftMap = results;
        setYearMonth(newYearMonth);
        todayDate = new LocalDate();

	}

    public synchronized void refreshAdapter(Map<String, Shift> newDateToShiftMap) {
        dateToShiftMap = newDateToShiftMap;
    }

    /**
     * Returns the month+year this MonthAdapter is displaying
     * @return
     */
    public YearMonth getYearMonth() {
        return yearMonth;
    }

    public void setYearMonth(YearMonth yearMonth) {
        this.yearMonth = yearMonth;
        localDates = getLocalDates(yearMonth);
    }

	@Override
	public int getCount() {
		return 42;
	}

	@Override
	public Object getItem(int index) {
         return localDates[index];
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater vi = LayoutInflater.from(context);
        DateLinearLayout linearLayout = (DateLinearLayout)vi.inflate(R.layout.month_grid_cell, null);
        DateTextView dateTextView = (DateTextView)linearLayout.getChildAt(0);
        TextView shiftNameTextView = (TextView)linearLayout.getChildAt(1);

        // calculate the height of the layout
        View viewPager = context.findViewById(R.id.calendarViewPager);
        View pagerTitleStrip = context.findViewById(R.id.pager_title_strip);
        View monthDayOfWeekHeader = context.findViewById(R.id.month_dayofweek_Header);

        int pagerTitleStripHeight = pagerTitleStrip.getHeight();
        int viewPagerHeight = viewPager.getHeight();
        int monthDayOfWeekHeaderHeight = monthDayOfWeekHeader.getHeight();

        linearLayout.setMinimumHeight((viewPagerHeight - (pagerTitleStripHeight + monthDayOfWeekHeaderHeight)) / 7);

        // configure the view depending on if the date is in the current month or if the date is today
        int todayDayOfMonth = todayDate.getDayOfMonth();
        LocalDate currentPositionDate = localDates[position];
        setBackground(linearLayout, currentPositionDate);

        dateTextView.setText(Integer.toString(currentPositionDate.getDayOfMonth()));
        dateTextView.setLocalDate(currentPositionDate);

        // set the selected date
        // set the background of the currently select view
        CalendarActivity calendarActivity = (CalendarActivity) context;
        if (calendarActivity.getSelectedDate() != null && calendarActivity.getSelectedDate().equals(currentPositionDate)) {
            // select and back up drawable
            //calendarActivity.setSelectedBackgroundResid(linearLayout.getBackgroundResource());
            linearLayout.setBackgroundResource(R.drawable.normal_grid_item_border_selected);
        }

        if (dateToShiftMap != null) {
            Shift shiftObj = dateToShiftMap.get(currentPositionDate.toString("YYYYMMdd"));
            if (shiftObj != null) {
                shiftNameTextView.setText(shiftObj.getTitle());
            }
        }

		return linearLayout;
	}

    @Override
    public long getItemId(int i) {
        return 0;
    }


    private LocalDate[] getLocalDates(YearMonth yearMonth) {
        // set up the day numbers
        LocalDate firstDayOfMonthDate = new LocalDate(yearMonth.getYear(), yearMonth.getMonthOfYear(), 1);
        int firstDayOfMonthDayOfWeek = firstDayOfMonthDate.getDayOfWeek() % 7;

        // get the number of days in the previous month
        LocalDate lastDayOfPreviousMonth = firstDayOfMonthDate.minusDays(1);
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

    /**
     * Sets the background color of the month grid cell
     * @param linearLayout
     * @param currentPositionDate
     */
    void setBackground(LinearLayout linearLayout, LocalDate currentPositionDate) {
        if (currentPositionDate != null) {

            // if the cell is for today
            if (todayDate.equals(currentPositionDate)) {
                linearLayout.setBackgroundResource(R.drawable.today_grid_item_selector);
            }
            // the cell is for a day that's not in the current month
            else if (yearMonth.getMonthOfYear() != currentPositionDate.getMonthOfYear()) {
                linearLayout.setBackgroundResource(R.drawable.excluded_grid_item_selector);
            }
            else {
                linearLayout.setBackgroundResource(R.drawable.normal_grid_item_selector);
            }
        }
    }
}
