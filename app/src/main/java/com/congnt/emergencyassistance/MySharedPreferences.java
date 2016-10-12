package com.congnt.emergencyassistance;

import android.content.Context;

import com.congnt.androidbasecomponent.Awesome.AwesomeSharedPreferences;

/**
 * Created by congnt24 on 12/10/2016.
 */

public class MySharedPreferences extends AwesomeSharedPreferences {
    public static MySharedPreferences instance;
    public SingleSharedPreferences<Boolean> isListening = new SingleSharedPreferences<Boolean>() {
        @Override
        protected String ID() {
            return "IS_LISTENING";
        }

        @Override
        public void save(Boolean aBoolean) {
            super.save(aBoolean);
        }

        @Override
        public Boolean load(Boolean defaultT) {
            return super.load(defaultT);
        }
    };

    public MySharedPreferences(Context context) {
        super(context);
    }

    public static MySharedPreferences getInstance(Context context) {
        if (instance == null) instance = new MySharedPreferences(context);
        return instance;
    }

}
