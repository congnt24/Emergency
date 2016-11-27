package com.congnt.emergencyassistance.services;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.congnt.androidbasecomponent.utility.GoogleApiUtil;
import com.congnt.androidbasecomponent.utility.LocationUtil;
import com.congnt.androidbasecomponent.utility.PermissionUtil;
import com.congnt.emergencyassistance.AppConfig;
import com.congnt.emergencyassistance.MainApplication;
import com.congnt.emergencyassistance.MySharedPreferences;
import com.congnt.emergencyassistance.entity.EventBusEntity.EBE_StartLocationFollowService;
import com.congnt.emergencyassistance.entity.EventBusEntity.EBE_StartLocationService;
import com.congnt.emergencyassistance.entity.firebase.User;
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
import com.parse.SaveCallback;

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
    private User user;
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
        mLocationRequest = new LocationRequest();
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
            user = MySharedPreferences.getInstance(this).userProfile.load(null);
            updateLocationToParseServer();
        } else {
            stopUpdateLocationToParseSErver();
        }
        startListening();
    }

    @Override
    protected void initStart() {
        LocationUtil.setLocationRequest(mLocationRequest, ((MainApplication) getApplication()).getRequest_displacement()
                , ((MainApplication) getApplication()).getRequest_duration());
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
        ((MainApplication) getApplication()).setLastLocation(location);
        EventBus.getDefault().post(location);
        if (isFollow) {
            listLocation.add(new LocationFollow(application.lastLocation).toJson());
            if (TextUtils.isEmpty(currentParseId)) {
                final ParseFollow parseFollow = new ParseFollow(user.getEmail(), "0", "0", listLocation);
                parseFollow.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null) {
                            currentParseId = parseFollow.getObjectId();
                        }
                    }
                });
            } else {
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

    private void updateLocationToParseServer() {
        isFollow = true;
        //change duration and displacement
        cache_displacement = application.getRequest_displacement();
        cache_duration = application.getRequest_duration();
        application.setRequest_displacement(0);
        application.setRequest_duration(2000);
        listLocation.clear();
        currentParseId = "";

    }

    private void stopUpdateLocationToParseSErver() {
        isFollow = false;
        application.setRequest_displacement(cache_displacement);
        application.setRequest_duration(cache_duration);
    }
}
