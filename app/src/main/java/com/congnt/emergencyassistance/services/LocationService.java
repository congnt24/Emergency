package com.congnt.emergencyassistance.services;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
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
import com.congnt.emergencyassistance.entity.EventBusEntity.EBE_StartLocationService;
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

/**
 * Created by congnt24 on 26/11/2016.
 */

public class LocationService extends BaseForegroundService<EBE_StartLocationService> implements LocationListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    protected void initCreate() {
        ticker = " Location service is running";
        title = " Location service";
        text = " Location service is running";
//        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        mGoogleApiClient = GoogleApiUtil.getInstance().getGoogleApiClient(this, this, this);
        mLocationRequest = new LocationRequest();
        mLocationRequest.setSmallestDisplacement(0);
        mLocationRequest.setInterval(0);
        mLocationRequest.setFastestInterval(0);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
    }

    @Override
    protected void initStart() {
        requestLocationUpdate();
    }

    @Override
    protected void initStop() {
        removeLocationUpdate();
    }

    @Override
    protected int getFlagForeground() {
        return 103;
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        ((MainApplication) getApplication()).lastLocation = location;
        EventBus.getDefault().post(location);
    }


    protected void requestLocationUpdate() {
        if (mGoogleApiClient != null && PermissionUtil.getInstance(this)
                .checkMultiPermission(AppConfig.locationPermission)) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }
    }

    protected void removeLocationUpdate() {
        if (mGoogleApiClient != null && PermissionUtil.getInstance(this)
                .checkMultiPermission(AppConfig.locationPermission)) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }
    }
}
