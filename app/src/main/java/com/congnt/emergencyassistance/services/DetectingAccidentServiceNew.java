package com.congnt.emergencyassistance.services;

import android.annotation.TargetApi;
import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener2;
import android.hardware.SensorManager;
import android.location.Location;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import com.congnt.emergencyassistance.AppConfig;
import com.congnt.emergencyassistance.MainApplication;
import com.congnt.emergencyassistance.MySharedPreferences;
import com.congnt.emergencyassistance.entity.DetectAccident;
import com.congnt.emergencyassistance.entity.EventBusEntity.EBE_DetectAccident;
import com.congnt.emergencyassistance.entity.EventBusEntity.EBE_StartDetectingAccident;
import com.congnt.emergencyassistance.util.LocationUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

@TargetApi(Build.VERSION_CODES.KITKAT)
public class DetectingAccidentServiceNew extends BaseForegroundService implements SensorEventListener2 {
    private static final String TAG = "DetectingAccident";
    private static final int DETECTING_ACCIDENT_FLAGS = 102;
    private static final double VELOCITY_THRESHOLD = 10;
    private static double ACCELERATION_THRESHOLD = 3 * 9.8;
    private static final double ACCIDENT_THRESHOLD = 1;
    private static final double ACCIDENT_LOW_SPEED_THRESHOLD = 2;
    private static final double SSD_THRESHOLD = 2.06;
    private static final double MPH2KMH = 1.60934;
    private static final double MS2KMH = 3.6;
    public double maxSpeed, distance;
    protected AudioManager mAudioManager;
    Location prevLocation;
    private long lastUpdate = 0;
    private int DURATION = 300;
    private List<Double> list = new ArrayList<>();
    private double SSD = 0;
    private Notification notification;
    private SensorManager senSensorManager;
    private Sensor senAccelerometer;
    private Location previousLocation;
    private double currentAcceleration;
    private double currentSpeech = 0;
    private boolean speedAboveThreshold = false;
    private boolean stillInCar = false;
    private int counter = 0;
    private Timer timer;

    @Override
    protected void initOnCreate() {
        ticker = "Listening for detecting accident while traveling in the car";
        title = " Detecting accident service";
        text = " Listening for your accident";
        EventBus.getDefault().register(this);
//        mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        //Init sensor manager
        senSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        senAccelerometer = senSensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);

    }

    @Subscribe
    public void onEvent(EBE_StartDetectingAccident item) {
        if (item.getValue()) {
//            reset speed and distance
            maxSpeed = 0;
            distance = 0;
            ACCELERATION_THRESHOLD = Double.parseDouble(MySharedPreferences.getInstance(this).pref.getString("setting_acce_threshold", "29.4"));
            startListening();
        } else {
            stopListening();
        }
    }

    @Override
    protected void initStart() {
        senSensorManager.registerListener(this, senAccelerometer, SensorManager.SENSOR_DELAY_GAME);

    }

    @Override
    protected void initStop() {
        senSensorManager.unregisterListener(this);
    }

    @Override
    protected int getFlagForeground() {
        return DETECTING_ACCIDENT_FLAGS;
    }

    @Override
    public void onFlushCompleted(Sensor sensor) {

    }

    //Implement method

    @Override
    public void onSensorChanged(SensorEvent event) {
        Sensor mySensor = event.sensor;
        if (mySensor.getType() == Sensor.TYPE_LINEAR_ACCELERATION) {    //Acc theo 1 Oxy : 2 chieu
            long curTime = System.currentTimeMillis();
            // only allow one update every 100ms.
            if ((curTime - lastUpdate) > DURATION) {
                lastUpdate = curTime;
                currentAcceleration = calculateAcceleration(event.values);
                sendBroadcastForAccident(estimateAccident());
            }
        }
    }

    public boolean estimateAccident() {
        if (maxSpeed < currentSpeech) {
            maxSpeed = currentSpeech;
        }
        EventBus.getDefault().post(new EBE_DetectAccident(new DetectAccident(currentAcceleration, currentSpeech, distance, maxSpeed)));
        double accident = currentAcceleration / ACCELERATION_THRESHOLD;
        if (accident >= ACCIDENT_THRESHOLD && currentSpeech >= VELOCITY_THRESHOLD) {
            return true;
        }
        if (currentSpeech >= VELOCITY_THRESHOLD && counter < 30 && accident >= ACCIDENT_THRESHOLD) {
            return true;
        }
        if (counter > 30 && (accident + SSD / SSD_THRESHOLD) >= ACCIDENT_LOW_SPEED_THRESHOLD) {
            return true;
        }
        return false;

    }

    public double calculateAcceleration(float[] values) {
        int sum = 0;
        for (int i = 0; i < values.length; i++) {
//            Log.d(TAG, "calculateAcceleration: "+values[i]);
            sum += values[i] * values[i];
        }
        return Math.sqrt(sum);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Subscribe
    public void onLocationChanged(Location location) {
        if (!isListening) {
            return;
        }
        if (location.hasSpeed()) {
            if (prevLocation == null) {
                prevLocation = location;
            }
//            Location prevLocation = ((MainApplication) getApplication()).lastLocation;
            distance += LocationUtils.distance(prevLocation, location);
            prevLocation = location;
            ((MainApplication) getApplication()).lastLocation = location;
            double lat = (location.getLatitude());
            double lng = (location.getLongitude());
            DecimalFormat df = new DecimalFormat("#.##");
            if (previousLocation == null) {
                previousLocation = location;
            } else {
//            currentSpeech = LocationUtils.speed(previousLocation, location)*MPH2KMH;
                currentSpeech = location.getSpeed() * MS2KMH;
                if (currentSpeech > VELOCITY_THRESHOLD) {
                    speedAboveThreshold = true;
                } else {    //Di chuyển dưới tốc độ 24
                    if (speedAboveThreshold) {   // nếu là Giảm tốc độ đột ngột
                        counter = 0;
                        speedAboveThreshold = false;
                    } else {    //Nếu vẫn di chuyển tốc dộ chậm
                        if (counter < 30) {
                            if (counter > 15) {
                                list.add(currentSpeech);
                            }
                        } else {
                            list.add(currentSpeech);
                            if (counter % 15 == 0) {//Calculate SSD every 15s
                                SSD = calculateSSD(list.toArray(new Double[list.size()]));
                                Log.d(TAG, "onLocationChanged: SSD=" + SSD);
//                            if (SSD > 2.06) {
//                                stillInCar = true;
//                            } else {
//                                stillInCar = false;
//                            }
                            }
                        }
                    }
                }
                sendBroadcastForAccident(estimateAccident());
                previousLocation = location;
            }
            String speedFormatted = df.format(currentSpeech);
            Log.d(TAG, "onLocationChanged: lat: " + lat + " Lng: " + lng);
            Log.d(TAG, "onLocationChanged: has speed: " + location.hasSpeed() + " Speed calculate: " + String.valueOf(speedFormatted) + " - getSpeed(): " + location.getSpeed());
        }
//        Toast.makeText(this, "getSpeed(): " + location.getSpeed(), Toast.LENGTH_SHORT).show();
    }

    private void sendBroadcastForAccident(boolean isAccident) {
        if (isAccident) {
            Intent i = new Intent("com.congnt.emergencyasistance.ACCIDENT_RECEIVER");
            Bundle b = new Bundle();
            b.putString("type", AppConfig.DETECT_ACCIDENT);
            i.putExtras(b);
            sendBroadcast(i);
        }
    }

    public double calculateSSD(Double... arrays) {   //Độ lệch chuẩn
        double mean = 0;
        for (int i = 0; i < arrays.length; i++) {
            mean += arrays[i];
        }
        mean /= arrays.length;

        //SD
        double SD = 0;
        for (int i = 0; i < arrays.length; i++) {
            SD += Math.pow(arrays[i] - mean, 2);
        }
        SD = Math.sqrt(SD / arrays.length);
        return SD;
    }

}
