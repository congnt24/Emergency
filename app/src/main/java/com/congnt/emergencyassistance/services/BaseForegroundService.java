package com.congnt.emergencyassistance.services;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;

import com.congnt.emergencyassistance.R;
import com.congnt.emergencyassistance.entity.EventBusEntity.EBE_StartBase;
import com.congnt.emergencyassistance.entity.EventBusEntity.EBE_StartLocationService;
import com.congnt.emergencyassistance.view.activity.MainActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/**
 * Created by congnt24 on 26/11/2016.
 */

public abstract class BaseForegroundService extends Service {
    protected static final String TAG = "BaseForegroundService";
    protected Notification notification;
    protected String ticker, title, text;
    protected boolean isForeground = true;
    protected boolean isListening;

    @Override
    public void onCreate() {
        super.onCreate();
        initOnCreate();
        // Create intent that will bring our app to the front, as if it was tapped in the app
        // launcher
        Intent showTaskIntent = new Intent(getApplicationContext(), MainActivity.class);
        showTaskIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        notification = new NotificationCompat.Builder(this)
                .setTicker("" + ticker)
                .setContentTitle("" + title)
                .setContentText("" + text)
                .setWhen(System.currentTimeMillis())
                .setContentIntent(PendingIntent.getActivity(getApplicationContext(), 0, showTaskIntent, PendingIntent.FLAG_UPDATE_CURRENT))
                .setSmallIcon(R.mipmap.ic_launcher)
                .build();
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

    protected void startListening() {
        isListening = true;
        if (isForeground) {
            startForeground(getFlagForeground(), notification);
        }
        initStart();
    }

    protected void stopListening() {
        isListening= false;
        if (isForeground) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                stopForeground(STOP_FOREGROUND_DETACH);
            } else {
                stopForeground(true);
            }
        }
        initStop();
    }

    protected abstract void initOnCreate();

    protected abstract void initStart();

    protected abstract void initStop();

    protected abstract int getFlagForeground();

}
