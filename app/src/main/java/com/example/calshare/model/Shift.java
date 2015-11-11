package com.example.calshare.model;

import com.example.calshare.db.CalshareContract;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = CalshareContract.Shift.TABLE_NAME)
public class Shift {
    
    @DatabaseField(id = true)
    private String title;
    @DatabaseField
    private String starttime;
    @DatabaseField
    private String endtime;
 
    
    public Shift() {
        // ORMLite needs a no-arg constructor 
    }
    public Shift(String title, String starttime, String endtime) {
        this.title = title;
        this.starttime = starttime;
        this.endtime = endtime;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getStarttime() {
        return starttime;
    }
    public void setStarttime(String starttime) {
        this.starttime = starttime;
    }
    public String getEndtime() {
        return endtime;
    }
    public void setEndtime(String endtime) {
        this.endtime = endtime;
    }

    public String toString() {
        return getTitle() + "\t(" + getStarttime() + " - " + getEndtime() + ")";
    }
}