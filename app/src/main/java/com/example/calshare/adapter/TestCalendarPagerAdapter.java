package com.example.calshare.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;

import com.example.calshare.TestMonthFragment;
import com.example.calshare.WeekVerticalPagerFragment;
import com.example.calshare.TestYearFragment;

import org.joda.time.DateTime;

/**
 * Created by silkyoak on 15-11-19.
 */
public class TestCalendarPagerAdapter extends FragmentStatePagerAdapter {

    private Context mContext;
    private Fragment[] fragments = new Fragment[5];

    public TestCalendarPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;

        fragments[0] = TestYearFragment.instantiate(mContext, TestYearFragment.class.getName());
        fragments[1] = WeekVerticalPagerFragment.instantiate(mContext, WeekVerticalPagerFragment.class.getName());
        fragments[2] = TestMonthFragment.instantiate(mContext, TestMonthFragment.class.getName());
        fragments[3] = TestYearFragment.instantiate(mContext, TestYearFragment.class.getName());
        fragments[4] = WeekVerticalPagerFragment.instantiate(mContext, WeekVerticalPagerFragment.class.getName());
    }

    @Override
    public Fragment getItem(int position) {
        Log.i("CalendarPageAdapter", "position=" + position);

        return fragments[position];
    }

    @Override
    public int getCount() {
        return 5;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        DateTime todayDateTime = new DateTime();
        CharSequence year = todayDateTime.toString("yyyy");
        CharSequence week = "Week " + todayDateTime.getWeekOfWeekyear();
        switch (position) {
            case 0 :
                return year;
            case 1 :
                return week;
            case 2 :
                CharSequence month = "blah";
                return month;
            case 3:
                return year;
            case 4:
                return week;
            default:
                return "";
        }
    }

}
