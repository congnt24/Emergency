package com.congnt.emergencyassistance.services;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.congnt.androidbasecomponent.utility.GoogleApiUtil;
import com.congnt.androidbasecomponent.utility.LocationUtil;
import com.congnt.androidbasecomponent.utility.PermissionUtil;
import com.congnt.emergencyassistance.AppConfig;
import com.congnt.emergencyassistance.MainApplication;
import com.congnt.emergencyassistance.entity.EventBusEntity.EBE_StartDetectingAccident;
import com.congnt.emergencyassistance.entity.EventBusEntity.EBE_StartLocationFollowService;
import com.congnt.emergencyassistance.entity.EventBusEntity.EBE_StartLocationService;
import com.congnt.emergencyassistance.entity.parse.LocationFollow;
import com.congnt.emergencyassistance.entity.parse.ParseFollow;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by congnt24 on 26/11/2016.
 */

public class LocationService extends BaseForegroundService implements LocationListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    List<String> listLocation = new ArrayList<>();
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private String currentParseId;
    private boolean isFollow;
    private MainApplication application;
    private long cache_displacement;
    private long cache_duration;

    @Override
    public void onCreate() {
        super.onCreate();
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


    @Override
    protected void initOnCreate() {
        ticker = " Location service is running";
        title = " Location service";
        text = " Location service is running";
        application = (MainApplication) getApplication();
        EventBus.getDefault().register(this);
        mGoogleApiClient = GoogleApiUtil.getInstance().getGoogleApiClient(this, this, this);
        mLocationRequest = LocationUtil.getLocationRequest(AppConfig.UPDATE_LOCATION_DISPLACEMENT, AppConfig.UPDATE_LOCATION_DURATION);
    }

    @Subscribe
    public void onEvent(EBE_StartLocationService item) {
        isForeground = ((EBE_StartLocationService) item).isForeground;
        if (item.getValue()) {
            startListening();
        } else {
            stopListening();
        }
    }

    @Subscribe
    public void onEvent(EBE_StartLocationFollowService item) {
        stopListening();
        if (item.getValue()) {
            isForeground = true;
            currentParseId = "" + item.objectId;
            isFollow = true;
            mLocationRequest = LocationUtil.getLocationRequest(0, 0);
        } else {
            isForeground = false;
            isFollow = false;
            mLocationRequest = LocationUtil.getLocationRequest(AppConfig.UPDATE_LOCATION_DISPLACEMENT, AppConfig.UPDATE_LOCATION_DURATION);
        }
        startListening();
        Log.d(TAG, "onEvent: Start Parser server");
    }


    @Subscribe
    public void onEvent(EBE_StartDetectingAccident item) {
        stopListening();
        if (item.getValue()) {
            isForeground = true;
            mLocationRequest = LocationUtil.getLocationRequest(0, 0);
        } else {
            isForeground = false;
            mLocationRequest = LocationUtil.getLocationRequest(AppConfig.UPDATE_LOCATION_DISPLACEMENT, AppConfig.UPDATE_LOCATION_DURATION);
        }
        startListening();
        Log.d(TAG, "onEvent: Start Detect server");
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
        if ((location.hasAccuracy()) && (location.getAccuracy() > 400.0F)) {
            return;
        }
        ((MainApplication) getApplication()).setLastLocation(location);
        Log.d(TAG, "onLocationChanged: xxxx ");
        EventBus.getDefault().post(location);
        if (isFollow) {
            Log.d(TAG, "onLocationChanged: Update parse server " + currentParseId);
            listLocation.add(new LocationFollow(application.lastLocation).toJson());
            ParseQuery<ParseFollow> query = ParseQuery.getQuery(ParseFollow.class);
            query.getInBackground(currentParseId, new GetCallback<ParseFollow>() {
                public void done(ParseFollow follow, ParseException e) {
                    if (e == null) {
                        follow.put("locations", listLocation);
                        follow.saveInBackground();
                    }
                }
            });
        }
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
