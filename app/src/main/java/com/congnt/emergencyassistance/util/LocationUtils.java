package com.congnt.emergencyassistance.util;

import android.annotation.TargetApi;
import android.location.Location;
import android.os.Build;

public class LocationUtils {
    public final static float METERS_PER_SECOND_TO_MILES_PER_HOUR = (float) 2.23694;

    public static double distance(Location one, Location two) {
        int R = 6371000;
        Double dLat = toRad(two.getLatitude() - one.getLatitude());
        Double dLon = toRad(two.getLongitude() - one.getLongitude());
        Double lat1 = toRad(one.getLatitude());
        Double lat2 = toRad(two.getLatitude());
        Double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.sin(dLon / 2) * Math.sin(dLon / 2) * Math.cos(lat1) * Math.cos(lat2);
        Double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        Double d = R * c;

        return d;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    public static double speed(Location one, Location two) {
        double distance = LocationUtils.distance(one, two);
        Long diffns = two.getElapsedRealtimeNanos() - one.getElapsedRealtimeNanos();
        double seconds = diffns.doubleValue() / Math.pow(10, 9);

        double speedMph = distance / seconds;
        speedMph = speedMph * LocationUtils.METERS_PER_SECOND_TO_MILES_PER_HOUR;

        return speedMph;
    }

    private static double toRad(Double d) {
        return d * Math.PI / 180;
    }
}