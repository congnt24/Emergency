package com.congnt.emergencyassistance;

import android.content.Context;

import com.congnt.androidbasecomponent.Awesome.AwesomeSharedPreferences;
import com.congnt.emergencyassistance.entity.ItemSettingSpeech;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

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
    };
    public SingleSharedPreferences<List<ItemSettingSpeech>> emergency_command = new SingleSharedPreferences<List<ItemSettingSpeech>>() {
        @Override
        protected String ID() {
            return "emergency_command";
        }

        @Override
        protected Type getType() {
            return new TypeToken<List<ItemSettingSpeech>>() {}.getType();
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
