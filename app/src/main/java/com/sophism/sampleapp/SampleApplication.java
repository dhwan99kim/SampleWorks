package com.sophism.sampleapp;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.parse.Parse;
import com.parse.ParseInstallation;

/**
 * Created by D.H.KIM on 2016. 1. 15.
 */
public class SampleApplication extends Application{
    private final String TAG = "SampleApplication";
    public static SampleApplication app;
    public Context mContext;
    @Override
    public void onCreate(){
        super.onCreate();
        Log.v(TAG, "Application OnCreate");
        mContext = getApplicationContext();
        app = this;
        init();
    }

    public void init(){
        Parse.initialize(this, AppDefine.PARSE_APP_ID, AppDefine.PARSE_CLIENT_KEY);
        ParseInstallation installation = ParseInstallation.getCurrentInstallation();
        installation.add("device_id", "sophism");
        installation.saveInBackground();
    }


}
