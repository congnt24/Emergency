package com.congnt.emergencyassistance.view.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Handler;
import android.widget.FrameLayout;

import com.congnt.androidbasecomponent.utility.CommunicationUtil;
import com.congnt.androidbasecomponent.utility.ImageUtil;
import com.congnt.androidbasecomponent.utility.VibratorUtil;
import com.congnt.androidbasecomponent.view.dialog.DialogBuilder;
import com.congnt.androidbasecomponent.view.widget.TransparentSurfaceView;
import com.congnt.emergencyassistance.R;

public class DialogEmergencyActivity extends Activity implements Camera.PictureCallback {

    private FrameLayout previewHolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alert_dialog);
        previewHolder = (FrameLayout) findViewById(R.id.surfaceView);
        final TransparentSurfaceView cameraView = new TransparentSurfaceView(this, Camera.open());
        previewHolder.addView(cameraView);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                cameraView.getCamera().takePicture(null, null, DialogEmergencyActivity.this);
            }
        }, 1000);

        if (getIntent().getExtras() != null) {
            Bundle b = getIntent().getExtras();
            String type = b.getString("type", "POLICE");
            final String number = b.getString("number", "113");
            VibratorUtil.vibrate(this, 2000);
            DialogBuilder.yesNoDialog(this, "WARNING", "Would you want to call " + type, R.style.AppTheme2_AlertDialogStyle, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    CommunicationUtil.dialTo(DialogEmergencyActivity.this, number);
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

    @Override
    public void onPictureTaken(byte[] data, Camera camera) {
        ImageUtil.createFileFromData(data);
    }
}
