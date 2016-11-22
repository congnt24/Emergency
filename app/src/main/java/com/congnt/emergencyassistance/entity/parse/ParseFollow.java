package com.congnt.emergencyassistance.entity.parse;

import com.parse.ParseObject;

import java.util.List;

/**
 * Created by congnt24 on 22/11/2016.
 */

public class ParseFollow extends ParseObject {
    public ParseFollow() {
    }

    public ParseFollow(String user, String acc, String speed, List<LocationFollow> list) {
        put("user", user);
        put("accelerometer", acc);
        put("speed", speed);
        addAll("", list);
    }

    public ParseFollow(String theClassName) {
        super(theClassName);
    }

    public String getUser() {
        return getString("user");
    }

    public String getAcc() {
        return getString("accelerometer");
    }

    public String getSpeed() {
        return getString("speed");
    }

    public List<LocationFollow> getList() {
        return getList("locations");
    }

    public class LocationFollow {
        public String lat;
        public String lng;

        public LocationFollow(String lat, String lng) {
            this.lat = lat;
            this.lng = lng;
        }
    }
}
