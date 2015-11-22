package com.example.calshare;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;

import com.example.calshare.adapter.TestCalendarPagerAdapter;
import com.example.calshare.adapter.CircularViewPagerHandler;
import com.example.calshare.db.DateShiftDatabaseManager;

import org.joda.time.LocalDate;

/**
 * Created by silkyoak on 15-11-19.
 */
public class TestCalendarActivity extends FragmentActivity {

    private TestCalendarPagerAdapter calendarPagerAdapter;
    private ViewPager viewPager;

    // to share selected date across fragments
    private LocalDate selectedDate;


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
        calendarPagerAdapter = new TestCalendarPagerAdapter(this, getSupportFragmentManager());
        viewPager.setAdapter(calendarPagerAdapter);

        final CircularViewPagerHandler circularViewPagerHandler = new CircularViewPagerHandler(viewPager);
        //circularViewPagerHandler.setOnPageChangeListener(createOnPageChangeListener());
        viewPager.setOnPageChangeListener(circularViewPagerHandler);
    }

    public void invalidateOtherFragments(Fragment currentFragment) {
        //calendarPagerAdapter.invalidateOtherFragments(currentFragment);
    }

}
