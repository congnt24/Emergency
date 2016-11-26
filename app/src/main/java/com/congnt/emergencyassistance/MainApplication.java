package com.congnt.emergencyassistance;

import android.app.Application;
import android.location.Location;

import com.congnt.androidbasecomponent.utility.PermissionUtil;
import com.congnt.emergencyassistance.entity.parse.ParseFollow;
import com.parse.Parse;
import com.parse.ParseObject;

/**
 * Created by congnt24 on 01/10/2016.
 */

public class MainApplication extends Application {
    public Location lastLocation;

    @Override
    public void onCreate() {
        super.onCreate();
        ParseObject.registerSubclass(ParseFollow.class);
        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId(AppConfig.PARSE_APP_ID)
                .clientKey(AppConfig.PARSE_CLIENT_KEY)
                .server(AppConfig.PARSE_SERVER)
                .build());
        PermissionUtil.getInstance(this);
    }

}
