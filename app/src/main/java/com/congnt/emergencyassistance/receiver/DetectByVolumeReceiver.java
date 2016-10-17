package com.congnt.emergencyassistance.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.congnt.emergencyassistance.activities.DialogEmergencyActivity;

/**
 * Created by congnt24 on 13/10/2016.
 */

public class DetectByVolumeReceiver extends BroadcastReceiver{
    static long lastTime = 0;
    private static int count = 0;
    private static final int DURATION = 500;

    @Override
    public void onReceive(final Context context, Intent intent) {
        int volume = (Integer)intent.getExtras().get("android.media.EXTRA_VOLUME_STREAM_VALUE");
        if ((System.currentTimeMillis() - lastTime) > DURATION) {//First tap
            count = 1;
            lastTime = System.currentTimeMillis();
        }else{
            count++;
            lastTime = System.currentTimeMillis();
            if (count == 6){
                count=0;
                lastTime = 0;
                Intent i = new Intent();
                i.setClassName(context, DialogEmergencyActivity.class.getName());
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                Bundle b = new Bundle();
                b.putString("type", "POLICE");
                b.putString("number", "113");
                i.putExtras(b);
                context.startActivity(i);
            }
        }
    }

}