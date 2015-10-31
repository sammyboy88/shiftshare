package com.example.calshare;

import android.app.Activity;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.example.calshare.view.DateTextView;

import org.joda.time.LocalDate;

import java.util.HashMap;
import java.util.Map;

public class WeekGridAdapterNew implements ListAdapter {

	private String[] days = {"Sun", "Mon", "Tue", "Wed", "Thur", "Fri", "Sat" };
	private Activity context;
    private Map<String, String> dateToShiftMap = new HashMap<String, String>();
    private LocalDate[] localDates;
    private LocalDate todayDate;


    public WeekGridAdapterNew(Activity ctx, Map<String, String> results, LocalDate[] localDates) {
        context = ctx;
        dateToShiftMap = results;
        this.localDates = localDates;
        todayDate = new LocalDate();
    }

    public synchronized void refreshAdapter(Map<String, String> newDateToShiftMap) {
        dateToShiftMap = newDateToShiftMap;
    }
	
	@Override
	public int getCount() {
		return 8;
	}

	@Override
	public Object getItem(int index) {
		if (index == 7) { return null; }
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
		LinearLayout linearLayout = (LinearLayout)vi.inflate(R.layout.week_grid_cell, null);
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
		linearLayout.setMinimumHeight((viewPagerHeight - pagerTitleStripHeight) / 4);

        if (position == 7) {
            dateTextView.setText("");
        }
        else {
            if (todayDate.equals(localDates[position])) {
                linearLayout.setBackgroundResource(R.drawable.today_grid_item_selector);
            }
            LocalDate currentPositionDate = localDates[position];
            CharSequence dayText = days[position] + "\n" + currentPositionDate.toString("dd/MM/YYYY");
            dateTextView.setText(dayText);

            if (dateToShiftMap != null) {
                String shiftName = dateToShiftMap.get(localDates[position].toString("YYYYMMdd"));
                if (shiftName != null) {
                    shiftNameTextView.setText(shiftName);

                }
            }
			dateTextView.setLocalDate(localDates[position]); // used by ItemLongClick in the WeekFragment
        }


		return linearLayout;
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
