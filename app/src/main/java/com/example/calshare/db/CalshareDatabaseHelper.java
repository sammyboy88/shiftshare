package com.example.calshare.db;

import java.sql.SQLException;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.calshare.model.DateShiftLink;
import com.example.calshare.model.Shift;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

public class CalshareDatabaseHelper extends OrmLiteSqliteOpenHelper {

	public static final String DB_NAME = "Calshare.db";
	public static final int DB_VERSION = 1;
	
	private Dao<Shift, String> shiftDao = null;
    private Dao<DateShiftLink, String> dateShiftLinkDao = null;
	
	public CalshareDatabaseHelper(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
	}
	
	@Override
	public void onCreate(SQLiteDatabase arg0, ConnectionSource connectionSource) {
		try {
			Log.i(CalshareDatabaseHelper.class.getName(), "onCreate");
			TableUtils.createTable(connectionSource, Shift.class);
            TableUtils.createTable(connectionSource, DateShiftLink.class);
		} 
		catch (SQLException e) {
			Log.e(CalshareDatabaseHelper.class.getName(), "Can't create database", e);
			throw new RuntimeException(e);
		}		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource, int oldVersion,
			int newVersion) {
		
		try {
			Log.i(CalshareDatabaseHelper.class.getName(), "onUpgrade");
			TableUtils.dropTable(connectionSource, Shift.class, true);
            TableUtils.dropTable(connectionSource, DateShiftLink.class, true);
			// after we drop the old databases, we create the new ones
			onCreate(db, connectionSource);
		} 
		catch (SQLException e) {
			Log.e(CalshareDatabaseHelper.class.getName(), "Can't drop databases", e);
			throw new RuntimeException(e);
		}
	}
	
	public Dao<Shift, String> getShiftDao() throws SQLException {
		if (shiftDao == null) {
			shiftDao = getDao(Shift.class);
		}
		return shiftDao;
	}

    public Dao<DateShiftLink, String> getDateShiftLinkDao() throws SQLException {
        if (dateShiftLinkDao == null) {
            dateShiftLinkDao = getDao(DateShiftLink.class);
        }
        return dateShiftLinkDao;
    }

}
