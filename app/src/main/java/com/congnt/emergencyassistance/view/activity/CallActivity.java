package com.congnt.emergencyassistance.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.congnt.androidbasecomponent.utility.IntentUtil;
import com.congnt.emergencyassistance.MySharedPreferences;
import com.congnt.emergencyassistance.entity.ItemCountryEmergencyNumber;

/**
 * Created by congnt24 on 28/10/2016.
 */

public class CallActivity extends Activity {
    private static final int REQUEST_CALL = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getIntent()!= null){
            Intent intent = null;
            ItemCountryEmergencyNumber country = MySharedPreferences.getInstance(this).countryNumber.load(null);
            boolean isCall = MySharedPreferences.getInstance(this).pref.getBoolean("setting_call_to", false);
            if (country!=null) {
                if (getIntent().getStringExtra("type").equalsIgnoreCase("police")) {
                    if (isCall){
                        intent = IntentUtil.getIntentCallTo(country.police);
                    }else {
                        intent = IntentUtil.getIntentDialTo(country.police);
                    }
                }
                if (getIntent().getStringExtra("type").equalsIgnoreCase("fire")) {
                    if (isCall){
                        intent = IntentUtil.getIntentCallTo(country.fire);
                    }else {
                        intent = IntentUtil.getIntentDialTo(country.fire);
                    }
                }
                if (getIntent().getStringExtra("type").equalsIgnoreCase("ambulance")) {
                    if (isCall){
                        intent = IntentUtil.getIntentCallTo(country.ambulance);
                    }else {
                        intent = IntentUtil.getIntentDialTo(country.ambulance);
                    }
                }
                if (intent != null) {
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                } else {
                    finish();
                }
            }
        } else {
            finish();
        }
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (requestCode == REQUEST_CALL){
//            finish();
//        }
//    }
}
