package com.example.calshare.application;

import android.app.Application;

import com.example.calshare.db.DateShiftDatabaseManager;

import net.danlew.android.joda.JodaTimeAndroid;

/**
 * Created by root on 15/08/15.
 */
public class ApplicationMain extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        JodaTimeAndroid.init(this);
    }
}
