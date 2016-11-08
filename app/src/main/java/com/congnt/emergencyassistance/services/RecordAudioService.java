package com.congnt.emergencyassistance.services;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.congnt.androidbasecomponent.utility.FileUtil;
import com.congnt.emergencyassistance.MySharedPreferences;
import com.congnt.emergencyassistance.entity.EventBusEntity.EBE_StartStopService;
import com.congnt.emergencyassistance.util.AudioRecorder;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;

/**
 * Created by congnt24 on 08/11/2016.
 */

public class RecordAudioService extends Service {
    private static final String TAG = "RecordAudioService";
    private Handler handler;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        handler = new Handler();
        initAudioRecoder();
        return START_NOT_STICKY;
    }

    private void initAudioRecoder() {
        if (MySharedPreferences.getInstance(this).pref.getBoolean("setting_record_audio", false)) {
            final AudioRecorder audioRecorder = new AudioRecorder("/EmergencyAssistance/" + FileUtil.createUniqueName("AUDIO", "3gp"));
            final long duration = Long.parseLong(MySharedPreferences.getInstance(this).pref.getString("setting_record_time", "5")) * 1000;
            //Turn off speech recognizer
            if (MySharedPreferences.getInstance(this).isListening.load(false)) {
                EventBus.getDefault().post(new EBE_StartStopService(false));
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        EventBus.getDefault().post(new EBE_StartStopService(true));
                    }
                }, duration);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            audioRecorder.start();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    audioRecorder.stop();
                                    stopSelf();
                                }
                            }, duration);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }, 500);
            } else {
                try {
                    audioRecorder.start();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            audioRecorder.stop();
                            stopSelf();
                        }
                    }, duration);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
