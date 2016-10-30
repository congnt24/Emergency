package com.congnt.emergencyassistance.view.activity;

import android.app.Activity;
import android.os.Bundle;

import com.congnt.androidbasecomponent.utility.CommunicationUtil;
import com.congnt.emergencyassistance.MySharedPreferences;
import com.congnt.emergencyassistance.entity.ItemCountryEmergencyNumber;

/**
 * Created by congnt24 on 28/10/2016.
 */

public class CallActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getIntent()!= null){
            ItemCountryEmergencyNumber country = MySharedPreferences.getInstance(this).countryNumber.load(null);
            boolean isCall = MySharedPreferences.getInstance(this).pref.getBoolean("setting_call_to", false);
            if (country!=null) {
                if (getIntent().getStringExtra("type").equalsIgnoreCase("police")) {
                    if (isCall){
                        CommunicationUtil.callTo(this, country.police);
                    }else {
                        CommunicationUtil.dialTo(this, country.police);
                    }
                }
                if (getIntent().getStringExtra("type").equalsIgnoreCase("fire")) {
                    if (isCall){
                        CommunicationUtil.callTo(this, country.fire);
                    }else {
                        CommunicationUtil.dialTo(this, country.fire);
                    }
                }
                if (getIntent().getStringExtra("type").equalsIgnoreCase("ambulance")) {
                    if (isCall){
                        CommunicationUtil.callTo(this, country.ambulance);
                    }else {
                        CommunicationUtil.dialTo(this, country.ambulance);
                    }
                }
            }
        }
    }
}
