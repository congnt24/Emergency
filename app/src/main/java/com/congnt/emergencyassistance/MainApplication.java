package com.congnt.emergencyassistance;

import android.app.Application;

import com.beardedhen.androidbootstrap.TypefaceProvider;
import com.congnt.androidbasecomponent.utility.PermissionUtil;
import com.facebook.drawee.backends.pipeline.Fresco;

/**
 * Created by congnt24 on 01/10/2016.
 */

public class MainApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Fresco.initialize(this);
        PermissionUtil.getInstance(this);
        TypefaceProvider.registerDefaultIconSets();
    }
}
