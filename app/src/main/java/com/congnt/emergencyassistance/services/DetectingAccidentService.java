package com.congnt.emergencyassistance.services;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener2;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationManager;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.congnt.androidbasecomponent.utility.GoogleApiUtil;
import com.congnt.androidbasecomponent.utility.PermissionUtil;
import com.congnt.emergencyassistance.AppConfig;
import com.congnt.emergencyassistance.MainApplication;
import com.congnt.emergencyassistance.R;
import com.congnt.emergencyassistance.entity.DetectAccident;
import com.congnt.emergencyassistance.entity.EventBusEntity.EBE_DetectAccident;
import com.congnt.emergencyassistance.entity.EventBusEntity.EBE_StartDetectingAccident;
import com.congnt.emergencyassistance.util.LocationUtils;
import com.congnt.emergencyassistance.view.activity.MainActivity;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

@TargetApi(Build.VERSION_CODES.KITKAT)
public class DetectingAccidentService extends Service implements SensorEventListener2, LocationListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    private static final String TAG = "DetectingAccident";
    private static final int FOREGROUND_FLAGS = 102;
    private static final double VELOCITY_THRESHOLD = 24;
    private static final double ACCELERATION_THRESHOLD = 3 * 9.8;
    private static final double ACCIDENT_THRESHOLD = 1;
    private static final double ACCIDENT_LOW_SPEED_THRESHOLD = 2;
    protected AudioManager mAudioManager;
    long lastUpdate = 0;
    int DURATION = 1000;
    List<Double> list = new ArrayList<>();
    private Notification notification;
    private SensorManager senSensorManager;
    private Sensor senAccelerometer;
    private Location previousLocation;
    private LocationManager locationManager;
    private String[] locationPermission = new String[]{
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION};
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private double currentAcceleration;
    private double currentSpeech = 0;
    private boolean speedAboveThreshold = false;
    private boolean stillInCar = false;
    private int counter = 0;
    private Timer timer;
    private double SSD = 0;
    private double SSD_THRESHOLD = 2.06;

    @Override
    public void onCreate() {
        super.onCreate();
        EventBus.getDefault().register(this);
        mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        //Init sensor manager
        senSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        senAccelerometer = senSensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        //Location manager
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        mGoogleApiClient = GoogleApiUtil.getInstance().getGoogleApiClient(this, this, this);
        // Create intent that will bring our app to the front, as if it was tapped in the app
        // launcher
        Intent showTaskIntent = new Intent(getApplicationContext(), MainActivity.class);
        showTaskIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        notification = new NotificationCompat.Builder(this)
                .setTicker("Listening for detecting accident while traveling in the car")
                .setContentTitle("Detecting accident")
                .setContentText("Listening for your accident")
                .setWhen(System.currentTimeMillis())
                .setContentIntent(PendingIntent.getActivity(getApplicationContext(), 0, showTaskIntent, PendingIntent.FLAG_UPDATE_CURRENT))
                .setSmallIcon(R.mipmap.ic_launcher)
                .build();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void sendBroadcastToReceiver(Bundle b) {
        Intent i = new Intent("com.congnt.emergencyasistance.ACCIDENT_RECEIVER");
        i.putExtras(b);
        sendBroadcast(i);
    }

    @Subscribe
    public void onEvent(EBE_StartDetectingAccident item) {
        if (item.getValue()) {
            startListening();
        } else {
            stopListening();
        }
    }

    private void startListening() {
        senSensorManager.registerListener(this, senAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        requestLocationUpdate();
        startForeground(FOREGROUND_FLAGS, notification);

        //Start timer
        timer = new Timer();
        counter = 0;
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                counter++;
            }
        }, 1000, 1000);

    }

    private void stopListening() {
        senSensorManager.unregisterListener(this);
        removeLocationUpdate();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            stopForeground(STOP_FOREGROUND_DETACH);
        } else {
            stopForeground(true);
        }
    }

    private void requestLocationUpdate() {
        if (mGoogleApiClient != null && PermissionUtil.getInstance(this)
                .checkMultiPermission(locationPermission)) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }
    }

    private void removeLocationUpdate() {
        if (mGoogleApiClient != null && PermissionUtil.getInstance(this)
                .checkMultiPermission(locationPermission)) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }
    }
    //Implement method

    public boolean estimateAccident() {
        EventBus.getDefault().post(new EBE_DetectAccident(new DetectAccident(currentAcceleration, currentSpeech)));
        double accident = currentAcceleration / ACCELERATION_THRESHOLD;
        if (accident >= ACCIDENT_THRESHOLD/* && currentSpeech >= VELOCITY_THRESHOLD*/) {
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

    @Override
    public void onFlushCompleted(Sensor sensor) {

    }

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

    public double calculateAcceleration(float[] values) {
        int sum = 0;
        for (int i = 0; i < values.length; i++) {
            sum += values[i] * values[i];
        }
        return Math.sqrt(sum);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void onLocationChanged(Location location) {
        ((MainApplication) getApplication()).lastLocation = location;
        double lat = (location.getLatitude());
        double lng = (location.getLongitude());
        DecimalFormat df = new DecimalFormat("#.##");
        if (previousLocation == null) {
            previousLocation = location;
        } else {
            currentSpeech = LocationUtils.speed(previousLocation, location);
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
        String speedMphFormatted = df.format(currentSpeech);
        Log.d(TAG, "onLocationChanged: lat: " + lat + " Lng: " + lng);
        Log.d(TAG, "onLocationChanged: has speed: " + location.hasSpeed() + " Speed calculate: " + String.valueOf(speedMphFormatted) + " - getSpeed(): " + location.getSpeed());
        Toast.makeText(this, "getSpeed(): " + location.getSpeed(), Toast.LENGTH_SHORT).show();
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

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setSmallestDisplacement(0);
        mLocationRequest.setInterval(0);
        mLocationRequest.setFastestInterval(0);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
//        requestLocationUpdate();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}