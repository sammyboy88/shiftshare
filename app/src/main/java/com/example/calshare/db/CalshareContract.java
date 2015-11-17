package com.example.calshare.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

public final class CalshareContract {

	public CalshareContract() {}

	public static abstract class Shift implements BaseColumns {
		public static final String TABLE_NAME = "shift";
//		//public static final String COLUMN_NAME_SHIFT_ID = "shiftid";
		public static final String COLUMN_NAME_SHIFT_TITLE = "title";
	}

    public static abstract class DateShiftLink implements BaseColumns {
        public static final String TABLE_NAME = "date_shift_link";
        public static final String COLUMN_NAME_SHIFT_DATE = "date";
    }


}
