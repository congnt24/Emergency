package com.congnt.reversegeocodecountry;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by congnt24 on 02/11/2016.
 */

public class ReverseGeocodeCountry {
    public static ReverseGeocodeCountry instance;
    private Context context;
    private List<GeocodeCountry> listGeocodeCountry;

    public ReverseGeocodeCountry(Context context) {
        this.context = context;
        listGeocodeCountry = parsePolygon();
    }

    public static ReverseGeocodeCountry getInstance(Context context) {
        if (instance == null) instance = new ReverseGeocodeCountry(context);
        return instance;
    }

    private List<GeocodeCountry> parseJson2GetGeocode() {
        return new Gson().fromJson(getTextFromRaw(), new TypeToken<List<GeocodeCountry>>() {
        }.getType());
    }

    private String getTextFromRaw() {
        try {
            InputStream in_s = context.getResources().openRawResource(R.raw.country);
            byte[] b = new byte[in_s.available()];
            in_s.read(b);
            return new String(b);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    private List<GeocodeCountry> parsePolygon() {
        List<GeocodeCountry> listGeocodeCountry = parseJson2GetGeocode();
        for (int i = 0; i < listGeocodeCountry.size(); i++) {
            if (listGeocodeCountry.get(i).geometry.type.equalsIgnoreCase("polygon")) {
                listGeocodeCountry.get(i).geometry.polygon = new Gson().fromJson(String.valueOf(listGeocodeCountry.get(i).geometry.coordinates), new TypeToken<List<Object>>() {
                }.getType());
            } else {
                listGeocodeCountry.get(i).geometry.multiPolygon = new Gson().fromJson(String.valueOf(listGeocodeCountry.get(i).geometry.coordinates), new TypeToken<List<List<Object>>>() {
                }.getType());
            }
        }
        return listGeocodeCountry;
    }

    public GeocodeCountry getCountryByLatLng(double lat, double lng) {
        for (int i = 0; i < listGeocodeCountry.size(); i++) {
            //each country
            if (listGeocodeCountry.get(i).geometry.type.equalsIgnoreCase("polygon")) {
                List<Object> polygon = listGeocodeCountry.get(i).geometry.polygon;
                if (isInPolygon(polygon, lat, lng)) return listGeocodeCountry.get(i);
            } else {
                List<List<Object>> multiPolygon = listGeocodeCountry.get(i).geometry.multiPolygon;
                for (int i1 = 0; i1 < multiPolygon.size(); i1++) {
                    if (isInPolygon(multiPolygon.get(i1), lat, lng))
                        return listGeocodeCountry.get(i1);
                }
            }
        }
        return null;
    }

    private boolean isInPolygon(List<Object> polygon, double x, double y) {
        boolean evenOdd = false;    //odd
        List<Location> listLocation = getListLocation(String.valueOf(polygon.get(0)));
        int i, j;
        for (i = 0, j = listLocation.size() - 1; i < listLocation.size(); j = i++) {
            Location A = listLocation.get(i);
            Location B = listLocation.get(j);
            if (((A.y > y) != (B.y > y)) && (x < (A.x - B.x) * (y - A.y) / (A.y - B.y) + A.x)) {
                evenOdd = !evenOdd;
            }
        }
        return evenOdd;
    }

    private List<Location> getListLocation(String str) {
        List<Location> listLocation = new ArrayList<>();
        str = str.replaceAll("\\[", "").replaceAll("\\]\\]", "").replaceAll("\\s+", "");
        String[] strs = str.split("\\],");
        for (int i = 0; i < strs.length; i++) {
            listLocation.add(new Location(strs[i]));
        }
        return listLocation;
    }

    public class Location {
        public double x;//Lat
        public double y;//Long

        public Location(double x, double y) {
            this.x = x;
            this.y = y;
        }

        public Location(String splitByComma) {
            String[] strs = splitByComma.split(",", 2);
            this.x = Double.parseDouble(strs[1]);
            this.y = Double.parseDouble(strs[0]);
        }
    }
}
