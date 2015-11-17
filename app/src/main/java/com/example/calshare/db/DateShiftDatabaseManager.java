package com.example.calshare.db;

import android.app.Activity;
import android.util.Log;

import com.example.calshare.model.DateShiftLink;
import com.example.calshare.model.Shift;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by silkyoak on 31/10/15.
 */
public class DateShiftDatabaseManager {

    private CalshareDatabaseHelper mDbHelper;
    private List<DateShiftLink> dateShiftLinkCache;
    private List<Shift> shiftCache;
    private static DateShiftDatabaseManager dateShiftDatabaseManager;

    public static synchronized void initialise(Activity context) {
        if (dateShiftDatabaseManager == null) {
            dateShiftDatabaseManager = new DateShiftDatabaseManager(context);
        }
    }

    public static DateShiftDatabaseManager getInstance() {
        if (dateShiftDatabaseManager == null) {
            throw new RuntimeException("Initialize must be called first");
        }
        return dateShiftDatabaseManager;
    }

    private DateShiftDatabaseManager(Activity context) {
        mDbHelper = new CalshareDatabaseHelper(context);
        loadFromDb();
    }

    public synchronized void loadFromDb() {
        try {
            Dao<DateShiftLink, String> dateShiftLinksDao = mDbHelper.getDateShiftLinkDao();
            dateShiftLinkCache = dateShiftLinksDao.queryForAll();

            // load the shift objects from DB too
            Dao<Shift, String> shiftDao = mDbHelper.getDao(Shift.class);
            shiftCache = shiftDao.queryForAll();
        }
        catch (SQLException e) {
            Log.e("ReadDateShiftLinksTask", e.getMessage(), e);
            e.printStackTrace();
        }
    }

    public Map<String, Shift> getMonthDateToShiftMap() {
        Map<String, Shift> dateToShiftMap = new HashMap<String, Shift>();
        for (DateShiftLink dateShiftLink : dateShiftLinkCache) {
            //Log.i("WeekFragment", "getting shifts for week: " + dateShiftLink.getDate());
            dateToShiftMap.put(dateShiftLink.getDate(), getShift(dateShiftLink.getShift()));
        }
        return dateToShiftMap;
    }

    public Map<String, Shift> getWeekDateToShiftMap() {
        Map<String, Shift> dateToShiftMap = new HashMap<String, Shift>();
        for (DateShiftLink dateShiftLink : dateShiftLinkCache) {
            //Log.i("WeekFragment", "getting shifts for week: " + dateShiftLink.getDate());
            dateToShiftMap.put(dateShiftLink.getDate(), getShift(dateShiftLink.getShift()));
        }
        return dateToShiftMap;
    }

    private Shift getShift(String shiftName) {
        for (Shift s : shiftCache) {
            if (s.getTitle().equals(shiftName)) {
                return s;
            }
        }
        return null;
    }

}
