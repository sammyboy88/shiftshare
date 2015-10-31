package com.example.calshare.model;

import com.example.calshare.db.CalshareContract;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by root on 29/08/15.
 */
@DatabaseTable(tableName = CalshareContract.DateShiftLink.TABLE_NAME)
public class DateShiftLink {

    @DatabaseField(id = true)
    private String date; // yyyyMMdd

    @DatabaseField
    private String shift;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getShift() {
        return shift;
    }

    public void setShift(String shift) {
        this.shift = shift;
    }
}
