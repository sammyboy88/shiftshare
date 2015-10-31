package com.example.calshare;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;

import com.example.calshare.adapter.CalendarPagerAdapter;
import com.example.calshare.adapter.CircularViewPagerHandler;

public class CalendarActivity extends FragmentActivity {

	//private FragmentTabHost mTabHost;
    private CalendarPagerAdapter calendarPagerAdapter;
    private ViewPager viewPager;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calendar_activity);

        viewPager = (ViewPager) findViewById(R.id.calendarViewPager);
        calendarPagerAdapter = new CalendarPagerAdapter(this, getSupportFragmentManager());
        viewPager.setAdapter(calendarPagerAdapter);

        final CircularViewPagerHandler circularViewPagerHandler = new CircularViewPagerHandler(viewPager);
        //circularViewPagerHandler.setOnPageChangeListener(createOnPageChangeListener());
        viewPager.setOnPageChangeListener(circularViewPagerHandler);
	}

    public void refreshFragments() {
        Log.i("CalendarActivity", "refreshFragments");
        calendarPagerAdapter.refreshFragments();
    }

}
