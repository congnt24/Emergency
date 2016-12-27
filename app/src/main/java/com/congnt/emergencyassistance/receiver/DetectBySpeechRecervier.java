package com.congnt.emergencyassistance.receiver;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.speech.SpeechRecognizer;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.text.TextUtils;

import com.congnt.emergencyassistance.AppConfig;
import com.congnt.emergencyassistance.MySharedPreferences;
import com.congnt.emergencyassistance.entity.ItemSettingSpeech;
import com.congnt.emergencyassistance.view.activity.EmergencyStateActivity;

import java.util.List;

/**
 * Created by congnt24 on 12/10/2016.
 */

public class DetectBySpeechRecervier extends WakefulBroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (!EmergencyStateActivity.isRunning) {
            // Vibrate the mobile phone
            Bundle extras = intent.getExtras();
            if (extras != null) {
                if (!TextUtils.isEmpty(extras.getString("type")) && extras.getString("type").equalsIgnoreCase(AppConfig.DETECT_ACCIDENT)) {
                    Intent i = new Intent();
                    i.setClassName(context, EmergencyStateActivity.class.getName());
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    Bundle b = new Bundle();
                    b.putString("type", AppConfig.DETECT_ACCIDENT);
                    i.putExtras(b);
                    context.startActivity(i);
                } else {
                    List<String> matches = extras
                            .getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                    String text = "";
                    for (String r : matches)
                        text += r + "\n";
//                    Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
                    List<ItemSettingSpeech> list = MySharedPreferences.getInstance(context).emergency_command.load(null);
                    for (ItemSettingSpeech item : list) {
                        for (String str : matches) {
                            String command = item.getCommand().trim().toLowerCase();
                            String match = str.trim().toLowerCase();
                            if (command.equalsIgnoreCase(match) || match.contains(command)/* || command.contains(match)*/) {
                                Intent i = new Intent();
                                i.setClassName(context, EmergencyStateActivity.class.getName());
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
    }
}
