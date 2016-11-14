package com.congnt.emergencyassistance.entity;

public class DetectAccident {
    public double accelerometer;
    public double speed;
    public double distance;

    public DetectAccident(double accelerometer, double speed) {
        this.accelerometer = accelerometer;
        this.speed = speed;
    }

    public DetectAccident(double accelerometer, double speed, double distance) {
        this.accelerometer = accelerometer;
        this.speed = speed;
        this.distance = distance;
    }
}