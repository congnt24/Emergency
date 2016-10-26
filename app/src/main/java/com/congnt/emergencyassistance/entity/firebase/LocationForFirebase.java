package com.congnt.emergencyassistance.entity.firebase;

/**
 * Created by congnt24 on 26/10/2016.
 */

public class LocationForFirebase {
    private Double lat;
    private Double lng;
    private float accuracy;

    public LocationForFirebase(Double lat, Double lng) {
        this.lat = lat;
        this.lng = lng;
    }

    //Default constructor for firebase
    public LocationForFirebase() {
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLng() {
        return lng;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }
}
