package com.congnt.emergencyassistance.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.speech.SpeechRecognizer;
import android.util.Log;
import android.widget.Toast;

import com.congnt.emergencyassistance.MySharedPreferences;
import com.congnt.emergencyassistance.activities.DialogEmergencyActivity;
import com.congnt.emergencyassistance.entity.ItemSettingSpeech;

import java.util.List;

/**
 * Created by congnt24 on 12/10/2016.
 */

public class DetectBySpeechRecervier extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // Vibrate the mobile phone
        Bundle extras = intent.getExtras();
        if (extras != null) {
            List<String> matches = extras
                    .getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
            String text = "";
            for (String r : matches)
                text += r + "\n";

            Log.d("AAAA", text);
            Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
            List<ItemSettingSpeech> list = MySharedPreferences.getInstance(context).emergency_command.load(null);
            for (ItemSettingSpeech item : list) {
                for (String str : matches) {
                    if (item.getCommand().trim().equalsIgnoreCase(str.trim())){
                        Intent i = new Intent();
                        i.setClassName(context, DialogEmergencyActivity.class.getName());
                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        Bundle b = new Bundle();
                        b.putString("type", item.getEmergencyType().toString());
                        i.putExtras(b);
                        context.startActivity(i);
                        return;
                    }
                }
            }
        }
    }
}
