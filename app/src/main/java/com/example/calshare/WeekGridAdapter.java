package com.example.calshare;

import android.app.Activity;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.example.calshare.model.Shift;
import com.example.calshare.view.DateLinearLayout;
import com.example.calshare.view.DateTextView;

import org.joda.time.LocalDate;

import java.util.HashMap;
import java.util.Map;

public class WeekGridAdapter implements ListAdapter {

	private String[] days = {"Sun", "Mon", "Tue", "Wed", "Thur", "Fri", "Sat" };
	private Activity context;
    private Map<String, Shift> dateToShiftMap = new HashMap<String, Shift>();
    private LocalDate[] localDates;
    private LocalDate todayDate;


    public WeekGridAdapter(Activity ctx, Map<String, Shift> results, LocalDate[] localDates) {
        context = ctx;
        dateToShiftMap = results;
        this.localDates = localDates;
        todayDate = new LocalDate();
    }

    public synchronized void refreshAdapter(Map<String, Shift> newDateToShiftMap) {
        dateToShiftMap = newDateToShiftMap;
    }

	public synchronized void updateLocalDates(LocalDate[] localDates) {
		this.localDates = localDates;
	}
	
	@Override
	public int getCount() {
		return 7;
	}

	@Override
	public Object getItem(int index) {
		return days[index];
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public int getItemViewType(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		LayoutInflater vi;
		vi = LayoutInflater.from(context);
		DateLinearLayout linearLayout = (DateLinearLayout)vi.inflate(R.layout.week_grid_cell, null);
		DateTextView dateTextView = (DateTextView)linearLayout.getChildAt(0);
		TextView shiftNameTextView = (TextView) linearLayout.getChildAt(1);

		// set minimum height to the height of the tab content area divided by number of rows
		//DisplayMetrics metrics = context.getResources().getDisplayMetrics();
		//int height = metrics.heightPixels;
		View viewPager = context.findViewById(R.id.calendarViewPager);
        View pagerTitleStrip = context.findViewById(R.id.pager_title_strip);
		
		//View tabContent = context.findViewById(R.id.tabContent);
		
		int pagerTitleStripHeight = pagerTitleStrip.getHeight();
		int viewPagerHeight = viewPager.getHeight();
		//System.out.println("tabhost height: " + tabHostHeight);
		//System.out.println("tabContent height: " + tabContentHeight);
		linearLayout.setMinimumHeight((viewPagerHeight - pagerTitleStripHeight) / 7);

		LocalDate currentPositionDate = localDates[position];
		setBackground(linearLayout, currentPositionDate);

		CharSequence dayText = days[position] + " " + currentPositionDate.toString("dd/MM/YYYY");
		dateTextView.setText(dayText);

		if (dateToShiftMap != null) {
			Shift shiftObj = dateToShiftMap.get(localDates[position].toString("YYYYMMdd"));
			if (shiftObj != null) {
				String shiftDescription = shiftObj.toString();
				shiftNameTextView.setText(shiftDescription);

			}
		}
		dateTextView.setLocalDate(localDates[position]); // used by ItemLongClick in the WeekFragment

		// set the selected date
        // set the background of the currently select view
        CalendarActivity calendarActivity = (CalendarActivity) context;
        if (calendarActivity.getSelectedDate() != null && calendarActivity.getSelectedDate().equals(currentPositionDate)) {
            // select and back up drawable
            //calendarActivity.setSelectedBackgroundResid(linearLayout.getBackgroundResource());
            linearLayout.setBackgroundResource(R.drawable.normal_grid_item_border_selected);
        }

		return linearLayout;
	}

	void setBackground(LinearLayout linearLayout, LocalDate currentPositionDate) {
		if (todayDate.equals(currentPositionDate)) {
			linearLayout.setBackgroundResource(R.drawable.today_grid_item_selector);
		}
		else {
			linearLayout.setBackgroundResource(R.drawable.normal_grid_item_selector);
		}
	}
	

	@Override
	public int getViewTypeCount() {
		// TODO Auto-generated method stub
		return 1;
	}

	@Override
	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isEmpty() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void registerDataSetObserver(DataSetObserver observer) {
		// TODO Auto-generated method stub

	}

	@Override
	public void unregisterDataSetObserver(DataSetObserver observer) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean areAllItemsEnabled() {
		return true;
	}

	@Override
	public boolean isEnabled(int position) {
		// TODO Auto-generated method stub
		return true;
	}

}
