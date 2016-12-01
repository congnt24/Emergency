package com.congnt.emergencyassistance.entity;

public class DetectAccident {
    public double accelerometer;
    public double speed;
    public double distance;
    public double maxSpeed;

    public DetectAccident(double accelerometer, double speed) {
        this.accelerometer = accelerometer;
        this.speed = speed;
    }

    public DetectAccident(double accelerometer, double speed, double distance) {
        this.accelerometer = accelerometer;
        this.speed = speed;
        this.distance = distance;
    }

    public DetectAccident(double accelerometer, double speed, double distance, double maxSpeed) {
        this.accelerometer = accelerometer;
        this.speed = speed;
        this.distance = distance;
        this.maxSpeed = maxSpeed;
    }
}