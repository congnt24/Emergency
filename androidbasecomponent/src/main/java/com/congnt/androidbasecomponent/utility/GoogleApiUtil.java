package com.congnt.androidbasecomponent.utility;

import android.content.Context;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

/**
 * Created by congnt24 on 08/11/2016.
 */

public class GoogleApiUtil {
    public static GoogleApiUtil instance;
    private GoogleApiClient mGoogleApiClient;

    public static GoogleApiUtil getInstance() {
        if (instance == null) {
            instance = new GoogleApiUtil();
        }
        return instance;
    }

    public synchronized GoogleApiClient getGoogleApiClient(Context context, GoogleApiClient.ConnectionCallbacks connectionCallbacks, GoogleApiClient.OnConnectionFailedListener failedListener) {
//        if (mGoogleApiClient == null) {
        GoogleApiClient.Builder builder = new GoogleApiClient.Builder(context)
                .addApi(LocationServices.API);
        if (connectionCallbacks != null) {
            builder.addConnectionCallbacks(connectionCallbacks);
        }
        if (failedListener != null) {
            builder.addOnConnectionFailedListener(failedListener);
        }
        mGoogleApiClient = builder.build();
        mGoogleApiClient.connect();
//        }
        return mGoogleApiClient;
    }

}
