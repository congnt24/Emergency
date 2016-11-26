package com.congnt.emergencyassistance.entity.parse;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.parse.ParseClassName;
import com.parse.ParseObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by congnt24 on 22/11/2016.
 */
@ParseClassName("Follow")
public class ParseFollow extends ParseObject {
    public ParseFollow() {
    }

    public ParseFollow(String user, String acc, String speed, List<String> list) {
        put("user", user);
        put("accelerometer", acc);
        put("speed", speed);
        addAll("locations", list);
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

    public List<String> getList() {
        return getList("locations");
    }

    public List<LatLng> getListLatLng() {
        List<String> list = getList();
        List<LatLng> listLatLng = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            LocationFollow lf = getLocationFollow(list.get(i));
            listLatLng.add(new LatLng(Double.parseDouble(lf.lat), Double.parseDouble(lf.lng)));
        }
        return listLatLng;
    }

    public static LocationFollow getLocationFollow(String s) {
        return new Gson().fromJson(s, LocationFollow.class);
    }

}
