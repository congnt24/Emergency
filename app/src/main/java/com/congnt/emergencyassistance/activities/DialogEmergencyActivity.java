package com.congnt.emergencyassistance.activities;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;

import com.congnt.androidbasecomponent.utility.CommunicationUtil;
import com.congnt.androidbasecomponent.utility.VibratorUtil;
import com.congnt.androidbasecomponent.view.dialog.DialogBuilder;

public class DialogEmergencyActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getIntent().getExtras()!=null) {
            Bundle b = getIntent().getExtras();
            String type = b.getString("type", "POLICE");
            final String number = b.getString("number", "113");
            VibratorUtil.vibrate(this, 2000);
            DialogBuilder.yesNoDialog(this, "WARNING", "Would you want to call " + type, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    CommunicationUtil.callTo(DialogEmergencyActivity.this, number);
                    dialog.dismiss();
                    finish();
                }
            }, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    finish();
                }
            }).setCancelable(false).create().show();
        }
    }
}
