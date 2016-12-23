package com.congnt.emergencyassistance;

/**
 * Created by congnt24 on 04/10/2016.
 */

public class AppConfig {
    public static final String POLICE = "police";
    public static final String FIRE = "fire";
    public static final String AMBULANCE = "ambulance";
    public static final String GOOGLE_PLACE_KEY ="AIzaSyBu4nSwHTsmLNX-O8s3zTZjraqHu81IB9g";
    public static final String GOOGLE_PLACE_URL_BASE ="https://maps.googleapis.com/maps/";
    public static final String API_URL_BASE = "https://congnt24.github.io/";
    public static final String TEMPLATE_GOOGLEMAP_PLACE = "https://www.google.com/maps/place/";
    public static final String FOLDER_MEDIA = "EmergencyAssistance";
    public static final String DETECT_ACCIDENT = "DETECT_ACCIDENT";
    public static final String DETECT_TIMMER = "DETECT_TIMMER";
    public static final String PARSE_APP_ID = "4dxj5BVSUkLohTyfZr54p2NWegFMXOJGhHmBGh7H";//KSJ4KLJ5KJK435J3KSS9F9D8S9F8SD98F9SDF
    public static final String PARSE_CLIENT_KEY = "9nLjjVyYiSbwze7hNPQAyPa1FNp7ToRo88XdtkQW";
    public static final String PARSE_SERVER = "https://parseapi.back4app.com";
    public static final long PARSE_DELAY_DURATION = 5000;
    public static final int UPDATE_LOCATION_DURATION = 3000;
    public static final int UPDATE_LOCATION_DISPLACEMENT = 2000;
    public static final int ZOOM_LEVEL = 13;
    public static String[] locationPermission = new String[]{
            android.Manifest.permission.ACCESS_FINE_LOCATION,
            android.Manifest.permission.ACCESS_COARSE_LOCATION};
}
