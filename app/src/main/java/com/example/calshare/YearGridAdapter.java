package com.example.calshare;

import android.app.Activity;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.joda.time.LocalDateTime;

public class YearGridAdapter implements ListAdapter {

	private String[] months = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec" };
	private Activity context;
	private int year;
	
	public YearGridAdapter(Activity ctx) {
		context = ctx;
		// default year
		year = new LocalDateTime().getYear();
	}

	public int getYear() {
		return this.year;
	}

	@Override
	public int getCount() {
		return 12;
	}

	@Override
	public Object getItem(int index) {
		return months[index];
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
        LinearLayout view = (LinearLayout)vi.inflate(R.layout.year_grid_cell, null);
        TextView textView = (TextView)view.getChildAt(0);
		
		// set minimum height to the height of the tab content area divided by number of rows
		//DisplayMetrics metrics = context.getResources().getDisplayMetrics();
		//int height = metrics.heightPixels;
		//View tabHost = context.findViewById(R.id.tabhost);
		
		//View tabContent = context.findViewById(R.id.tabContent);
		

        View viewPager = context.findViewById(R.id.calendarViewPager);
        View pagerTitleStrip = context.findViewById(R.id.pager_title_strip);

        int pagerTitleStripHeight = pagerTitleStrip.getHeight();
        int viewPagerHeight = viewPager.getHeight();

        //View tabContent = context.findViewById(R.id.tabContent);

        //int tabHostHeight = tabHost.getHeight();
        //System.out.println("tabhost height: " + tabHostHeight);
        //System.out.println("tabContent height: " + tabContentHeight);
        view.setMinimumHeight((viewPagerHeight - pagerTitleStripHeight)/ 4);
		//textView.setMinimumHeight((tabHostHeight-tabContentHeight)/4);
		textView.setText(months[position]);
		return view;
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
