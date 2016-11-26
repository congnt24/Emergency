package com.congnt.emergencyassistance;

import android.*;

/**
 * Created by congnt24 on 04/10/2016.
 */

public class AppConfig {
    public static String[] locationPermission = new String[]{
            android.Manifest.permission.ACCESS_FINE_LOCATION,
            android.Manifest.permission.ACCESS_COARSE_LOCATION};
    public static final String GOOGLE_PLACE_KEY ="AIzaSyBu4nSwHTsmLNX-O8s3zTZjraqHu81IB9g";
    public static final String GOOGLE_PLACE_URL_BASE ="https://maps.googleapis.com/maps/";
    public static final String API_URL_BASE = "https://congnt24.github.io/";
    public static final String TEMPLATE_GOOGLEMAP_PLACE = "https://www.google.com/maps/place/";

    public static final String FOLDER_MEDIA = "EmergencyAssistance";
    public static final String DETECT_ACCIDENT = "DETECT_ACCIDENT";
    public static final String PARSE_APP_ID = "KSJ4KLJ5KJK435J3KSS9F9D8S9F8SD98F9SDF";
    public static final String PARSE_CLIENT_KEY = "KSJ4KLJ5KJK435J3KSS9F9D8S9F8SD98F9SDF";
    public static final String PARSE_SERVER = "http://192.168.1.169:1337/parse";
    public static final long PARSE_DELAY_DURATION = 5000;
    public static final int PARSE_UPDATE_LOCATION_DURATION = 0;
    public static final int PARSE_UPDATE_LOCATION_DISPLACEMENT = 0;
}
