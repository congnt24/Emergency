package com.congnt.emergencyassistance.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.congnt.androidbasecomponent.utility.VibratorUtil;

/**
 * Created by congnt24 on 12/10/2016.
 */

public class DetectAccidentRecervier extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        // Vibrate the mobile phone
        VibratorUtil.vibrate(context, 2000);
        Bundle extras = intent.getExtras();
        if (extras != null) {
            Toast.makeText(context, extras.getString("data") + "", Toast.LENGTH_SHORT).show();
        }
    }
}
