package com.congnt.emergencyassistance.util;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;

import com.congnt.emergencyassistance.R;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;

/**
 * Created by congnt24 on 23/10/2016.
 */

public class FireBaseUtil {

    private static GoogleSignInOptions getGoogleSigninOption(Context context) {
        return new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(context.getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
    }

    public static GoogleApiClient buildGoogleApiClient(AppCompatActivity activity, GoogleApiClient.OnConnectionFailedListener listener) {
        return new GoogleApiClient.Builder(activity)
                .enableAutoManage(activity /* FragmentActivity */, listener /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, getGoogleSigninOption(activity))
                .build();
    }
}
