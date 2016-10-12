package com.congnt.emergencyassistance;

import android.app.Application;

import com.congnt.androidbasecomponent.utility.PermissionUtil;

/**
 * Created by congnt24 on 01/10/2016.
 */

public class MainApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        PermissionUtil.getInstance(this);
    }
}
