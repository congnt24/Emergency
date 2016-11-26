package com.congnt.emergencyassistance.entity.parse;

import android.location.Location;

import com.google.gson.Gson;

public class LocationFollow {
    public String lat;
    public String lng;

    public LocationFollow(String lat, String lng) {
        this.lat = lat;
        this.lng = lng;
    }

    public LocationFollow(Location location) {

        this.lat = ""+location.getLatitude();
        this.lng = ""+location.getLongitude();
    }

    public String toJson(){
        return new Gson().toJson(this);
    }
}