package com.congnt.emergencyassistance.view.activity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.congnt.androidbasecomponent.utility.PermissionUtil;
import com.congnt.emergencyassistance.MySharedPreferences;
import com.congnt.emergencyassistance.R;
import com.karumi.dexter.MultiplePermissionsReport;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by congnt24 on 28/11/2016.
 */

public class SplashActivity extends AppCompatActivity{
    private static final String TAG = "SplashActivity";
    private String[] permission = new String[]{
            Manifest.permission.CALL_PHONE
            , Manifest.permission.GET_ACCOUNTS
            , Manifest.permission.SEND_SMS
            , Manifest.permission.ACCESS_COARSE_LOCATION
            , Manifest.permission.ACCESS_FINE_LOCATION
            , Manifest.permission.READ_CONTACTS
            , Manifest.permission.RECORD_AUDIO
            , Manifest.permission.CAMERA
            , Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    private List<String> listPer = new ArrayList<>();

    private Button btn_grant;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //REQUEST PERMISSION AND REQUIRE
        if (!PermissionUtil.getInstance(this).checkMultiPermission(permission)) {
            for (String s : permission) {
                if (!PermissionUtil.getInstance(this).checkPermission(s)) {
                    listPer.add(s);
                }
            }

            setContentView(R.layout.activity_splash);
            btn_grant = (Button) findViewById(R.id.btn_grant);
            btn_grant.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "onCreate: " + listPer.size());
                    PermissionUtil.getInstance(SplashActivity.this).requestPermissions(new PermissionUtil.MultiPermissionListenerGranted() {
                        @Override
                        public void onPermissionGranted(MultiplePermissionsReport response) {
                            if (response.areAllPermissionsGranted()){
                                startActivity(new Intent(SplashActivity.this, TutorialActivity.class));
                                finish();
                            }
                        }
                    }, listPer.toArray(new String[]{}));
                }
            });
        }else{

            //Show tutorial at the first time
            if (MySharedPreferences.getInstance(this).isFirstTime.load(true)) {
                startActivity(new Intent(this, TutorialActivity.class));
            } else {
                startActivity(new Intent(this, MainActivity.class));
            }

            finish();
        }
    }
}
