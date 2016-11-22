/*
package com.congnt.emergencyassistance.services;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

*/
/**
 * Created by congnt24 on 30/10/2016.
 *//*

public class PushNotificationGetInstanceIDService extends FirebaseInstanceIdService {
    private static final String TAG = "PushNotificationGetInst";
    @Override
    public void onTokenRefresh() {

        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + refreshedToken);

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        sendRegistrationToServer(refreshedToken);
    }

    private void sendRegistrationToServer(String refreshedToken) {
        //Up[date to firebase database
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null) {
            FirebaseDatabase.getInstance().getReference().child("users").child(firebaseUser.getEmail().replace(".", "")).child("deviceid").setValue(refreshedToken);
        }
    }
}
*/
