package com.example.calshare.adapter;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;

import com.example.calshare.MonthFragment;
import com.example.calshare.WeekFragment;
import com.example.calshare.YearFragment;
import com.example.calshare.db.DateShiftDatabaseManager;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.YearMonth;

/**
 * Created on 18/08/15.
 */
public class CalendarPagerAdapter extends FragmentStatePagerAdapter {

    private Context mContext;
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


    public void invalidateOtherFragments(final Fragment currentFragment) {
        new Thread(new Runnable() {
            public void run() {
                if (currentFragment instanceof MonthFragment) {
                    ((WeekFragment) fragments[1]).invalidateViews();
                }
                else if (currentFragment instanceof WeekFragment) {
                    ((MonthFragment) fragments[2]).invalidateViews();
                }
            }
        }).start();

    }

    public void switchToMonth(YearMonth yearMonth) {
        ((MonthFragment)fragments[2]).switchToMonth(yearMonth);
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
                CharSequence month = ((MonthFragment) fragments[2]).getYearMonth().toString("MMM");
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
