package com.example.calshare;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;

import com.example.calshare.adapter.CalendarPagerAdapter;
import com.example.calshare.adapter.CircularViewPagerHandler;
import com.example.calshare.db.DateShiftDatabaseManager;

import org.joda.time.LocalDate;
import org.joda.time.YearMonth;

public class CalendarActivity extends FragmentActivity {

    private CalendarPagerAdapter calendarPagerAdapter;
    private ViewPager viewPager;

    // to share selected date across fragments
    private LocalDate selectedDate;
    //private int selectedBackgroundResid;

//    public int getSelectedBackgroundResid() {
//        return this.selectedBackgroundResid;
//    }
//
//    public void setSelectedBackgroundResid(int selectedBackgroundResid) {
//        this.selectedBackgroundResid = selectedBackgroundResid;
//    }

    public LocalDate getSelectedDate() {
        return selectedDate;
    }

    public void setSelectedDate(LocalDate selectedDate) {
        this.selectedDate = selectedDate;
    }

    @Override
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calendar_activity);

        // initialise the DateShiftDbManager
        DateShiftDatabaseManager.initialise(this);

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

    public void invalidateOtherFragments(Fragment currentFragment) {
        calendarPagerAdapter.invalidateOtherFragments(currentFragment);
    }

    public void switchToMonth(YearMonth yearMonth) {
        calendarPagerAdapter.switchToMonth(yearMonth);
        viewPager.setCurrentItem(2);
    }

    public void addShift(View view) {
        Log.i("CalendarActivity", "addShift");
        Intent intent = new Intent(this, AddShiftActivity.class);
        startActivity(intent);
    }

}
