package com.example.calshare.adapter;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;

import com.example.calshare.MonthFragment;
import com.example.calshare.MonthGridAdapter;
import com.example.calshare.WeekFragment;
import com.example.calshare.WeekGridAdapter;
import com.example.calshare.YearFragment;
import com.example.calshare.YearGridAdapter;
import com.example.calshare.db.DateShiftDatabaseManager;
import com.example.calshare.model.DateShiftLink;
import com.j256.ormlite.dao.Dao;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created on 18/08/15.
 */
public class CalendarPagerAdapter extends FragmentStatePagerAdapter {

    private Context mContext;

//    private YearGridAdapter yearGridAdapter;
//    private MonthGridAdapter monthGridAdapter;
//    private WeekGridAdapter weekGridAdapter;

    private Fragment[] fragments = new Fragment[5];

    public CalendarPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;

        fragments[0] = YearFragment.instantiate(mContext, YearFragment.class.getName());
        fragments[1] = WeekFragment.instantiate(mContext, WeekFragment.class.getName());
        fragments[2] = MonthFragment.instantiate(mContext, MonthFragment.class.getName());
        fragments[3] = YearFragment.instantiate(mContext, YearFragment.class.getName());
        fragments[4] = WeekFragment.instantiate(mContext, WeekFragment.class.getName());
    }

    @Override
    public Fragment getItem(int position) {
        Log.i("CalendarPageAdapter", "position=" + position);
//        if(position == 0 || position == 3) {
//            Log.i("CalendarPagerAdapter", "getItem 0||3 position=" + position);
//            return YearFragment.instantiate(mContext, YearFragment.class.getName());
//        } else if(position == 4 || position == 1) {
//            Log.i("CalendarPagerAdapter", "getItem 4||1 position=" + position);
//            currentWeekFragment = (WeekFragment)WeekFragment.instantiate(mContext, WeekFragment.class.getName());
//            return currentWeekFragment;
//        } else {
//            Log.i("CalendarPagerAdapter", "getItem position=" + position);
//            // need to retain the monthFragment reference to update it after the select shift dialog is closed
//            currentMonthFragment = (MonthFragment)MonthFragment.instantiate(mContext, MonthFragment.class.getName());
//            return currentMonthFragment;
//        }
        return fragments[position];
    }

    /**
     * Invoked by CalendarActivity after shift Select dialog is closed
     * @return
     */
    public void refreshFragments() {
        LocalDate todayLocalDate = null;
        new ReloadDateShiftsTask().execute(todayLocalDate);
    }

    @Override
    public int getCount() {
        return 5;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        DateTime todayDateTime = new DateTime();
        CharSequence year = todayDateTime.toString("yyyy");
        CharSequence month = todayDateTime.toString("MMM");
        CharSequence week = "Week " + todayDateTime.getWeekOfWeekyear();
        switch (position) {
            case 0 :
                return year;
            case 1 :
                return week;
            case 2 :
                return month;
            case 3:
                return year;
            case 4:
                return week;
            default:
                return "";
        }
    }

    private class ReloadDateShiftsTask extends AsyncTask<LocalDate, Void, Void> {

        @Override
        protected Void doInBackground(LocalDate... params) {
            DateShiftDatabaseManager.getInstance().loadFromDb();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            Log.i("MonthFragment", "onPostExecute, results " );

            ((WeekFragment)fragments[1]).refreshAdapter();
            ((MonthFragment)fragments[2]).refreshAdapter();
            ((WeekFragment)fragments[4]).refreshAdapter();
        }
    }
}
