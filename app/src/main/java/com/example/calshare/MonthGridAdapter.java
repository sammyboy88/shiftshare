package com.example.calshare;

import android.app.Activity;
import android.database.DataSetObserver;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.example.calshare.db.CalshareContract;
import com.example.calshare.db.CalshareDatabaseHelper;
import com.example.calshare.model.DateShiftLink;
import com.example.calshare.model.Shift;
import com.example.calshare.view.DateTextView;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.YearMonth;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MonthGridAdapter extends BaseAdapter implements ListAdapter {

	private Activity context;
    private LocalDate todayDate;
    private Map<String, String> dateToShiftMap = new HashMap<String, String>();
    private LocalDate[] localDates;

	public MonthGridAdapter(Activity ctx, Map<String, String> results, LocalDate[] localDates) {
		context = ctx;
        dateToShiftMap = results;
        this.localDates = localDates;
        todayDate = new LocalDate();

	}

    public synchronized void refreshAdapter(Map<String, String> newDateToShiftMap) {
        dateToShiftMap = newDateToShiftMap;
    }

    /**
     * Returns the month+year this MonthAdapter is displaying
     * @return
     */
    public YearMonth getYearMonth() {
        return new YearMonth(todayDate);
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
        LinearLayout linearLayout = (LinearLayout)vi.inflate(R.layout.month_grid_cell, null);
        DateTextView dateTextView = (DateTextView)linearLayout.getChildAt(0);
        TextView shiftNameTextView = (TextView)linearLayout.getChildAt(1);

        // calculate the height of the layout
        View viewPager = context.findViewById(R.id.calendarViewPager);
        View pagerTitleStrip = context.findViewById(R.id.pager_title_strip);
        View monthDayOfWeekHeader = context.findViewById(R.id.month_dayofweek_Header);

        int pagerTitleStripHeight = pagerTitleStrip.getHeight();
        int viewPagerHeight = viewPager.getHeight();
        int monthDayOfWeekHeaderHeight = monthDayOfWeekHeader.getHeight();

        linearLayout.setMinimumHeight((viewPagerHeight - (pagerTitleStripHeight + monthDayOfWeekHeaderHeight)) / 6);

        // configure the view depending on if the date is in the current month or if the date is today
        int todayDayOfMonth = todayDate.getDayOfMonth();

        if (localDates[position] != null) {

            // if the cell is for today
            if (todayDate.equals(localDates[position])) {
                linearLayout.setBackgroundResource(R.drawable.today_grid_item_selector);
            }
            // the cell is for a day that's not in the current month
            else if (todayDate.getMonthOfYear() != localDates[position].getMonthOfYear()) {
                linearLayout.setBackgroundResource(R.drawable.excluded_grid_item_selector);
            }
        }
        dateTextView.setText(Integer.toString(localDates[position].getDayOfMonth()));
        dateTextView.setLocalDate(localDates[position]);

        if (dateToShiftMap != null) {
            String shiftName = dateToShiftMap.get(localDates[position].toString("YYYYMMdd"));
            if (shiftName != null) {
                shiftNameTextView.setText(shiftName);
            }
        }

		return linearLayout;
	}

    @Override
    public long getItemId(int i) {
        return 0;
    }
}
