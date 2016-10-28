package com.congnt.emergencyassistance.widget;

import android.Manifest;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.RemoteViews;

import com.congnt.emergencyassistance.MySharedPreferences;
import com.congnt.emergencyassistance.R;
import com.congnt.emergencyassistance.entity.ItemCountryEmergencyNumber;

/**
 * Created by congnt24 on 26/10/2016.
 */

public class ContactWidgetProvider extends AppWidgetProvider {

    private static String POLICE;
    private static String FIRE;
    private static String AMBULANCE;
    private ItemCountryEmergencyNumber country;

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager,
                         int[] appWidgetIds) {

        // Get all ids
        ComponentName thisWidget = new ComponentName(context, ContactWidgetProvider.class);
        int[] allWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);
        country = MySharedPreferences.getInstance(context).countryNumber.load(null);
        for (int widgetId : allWidgetIds) {
            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_contact_layout);
            if (country != null) {
                POLICE = country.police;
                FIRE = country.fire;
                AMBULANCE = country.ambulance;
                remoteViews.setTextViewText(R.id.police_text, country.police);
                remoteViews.setTextViewText(R.id.fire_text, country.fire);
                remoteViews.setTextViewText(R.id.ambulance_text, country.ambulance);
                //listener
                remoteViews.setOnClickPendingIntent(R.id.police_layout, getPendingSelfIntent(context, "police"));
                remoteViews.setOnClickPendingIntent(R.id.fire_layout, getPendingSelfIntent(context, "fire"));
                remoteViews.setOnClickPendingIntent(R.id.ambulance_layout, getPendingSelfIntent(context, "ambulance"));
            }
            appWidgetManager.updateAppWidget(widgetId, remoteViews);
        }
    }

    protected PendingIntent getPendingSelfIntent(Context context, String action) {
        Intent intent = new Intent(context, getClass());
        intent.setAction(action);
        return PendingIntent.getBroadcast(context, 0, intent, 0);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        if ("police".equalsIgnoreCase(intent.getAction())) {
            Log.d("AAAAa", "AAAAAAA " + POLICE);
            dialTo(context, POLICE);
        } else if ("fire".equalsIgnoreCase(intent.getAction())) {
            dialTo(context, FIRE);
        } else if ("ambulance".equalsIgnoreCase(intent.getAction())) {
            dialTo(context, AMBULANCE);
        }
    }

    public void dialTo(Context context, String phoneNumber) {
        Intent callIntent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phoneNumber));
        callIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(callIntent);
    }

    public void callTo(Context context, String phoneNumber) {
        Intent callIntent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phoneNumber));
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        context.startActivity(callIntent);
    }
}