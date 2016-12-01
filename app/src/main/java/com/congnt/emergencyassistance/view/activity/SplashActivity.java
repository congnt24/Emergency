package com.congnt.emergencyassistance.view.activity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.congnt.androidbasecomponent.utility.PermissionUtil;
import com.congnt.emergencyassistance.R;
import com.karumi.dexter.MultiplePermissionsReport;

/**
 * Created by congnt24 on 28/11/2016.
 */

public class SplashActivity extends AppCompatActivity{
    private String[] permission = new String[]{
            Manifest.permission.CALL_PHONE
            , Manifest.permission.ACCESS_COARSE_LOCATION
            , Manifest.permission.ACCESS_FINE_LOCATION
            , Manifest.permission.READ_CONTACTS
            , Manifest.permission.RECORD_AUDIO
            , Manifest.permission.CAMERA
            , Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    private Button btn_grant;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //REQUEST PERMISSION AND REQUIRE
        if (!PermissionUtil.getInstance(this).checkMultiPermission(permission)) {
            setContentView(R.layout.activity_splash);
            btn_grant = (Button) findViewById(R.id.btn_grant);
            btn_grant.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PermissionUtil.getInstance(SplashActivity.this).requestPermissions(new PermissionUtil.MultiPermissionListenerGranted() {
                        @Override
                        public void onPermissionGranted(MultiplePermissionsReport response) {
                            if (response.areAllPermissionsGranted()){
                                startActivity(new Intent(SplashActivity.this, MainActivity.class));
                                finish();
                            }
                        }
                    }, permission);
                }
            });
        }else{
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
    }
}
