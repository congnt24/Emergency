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

    public String getLastAddress() {
        return lastAddress;
    }

    public void setLastAddress(String lastAddress) {
        this.lastAddress = lastAddress;
    }

    private String lastAddress;
    private long request_displacement = 0;
    private long request_duration = 0;

    public Location getLastLocation() {
        return lastLocation;
    }

    public void setLastLocation(Location lastLocation) {
        this.lastLocation = lastLocation;
    }

    public long getRequest_displacement() {
        return request_displacement;
    }

    public void setRequest_displacement(long request_displacement) {
        this.request_displacement = request_displacement;
    }

    public long getRequest_duration() {
        return request_duration;
    }

    public void setRequest_duration(long request_duration) {
        this.request_duration = request_duration;
    }

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
