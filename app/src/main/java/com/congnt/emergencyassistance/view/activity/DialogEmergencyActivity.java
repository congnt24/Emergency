package com.congnt.emergencyassistance.view.activity;

import android.app.Activity;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Handler;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.congnt.androidbasecomponent.utility.ImageUtil;
import com.congnt.androidbasecomponent.view.widget.TransparentSurfaceView;
import com.congnt.emergencyassistance.R;

public class DialogEmergencyActivity extends Activity implements Camera.PictureCallback {

    private FrameLayout previewHolder;
    private Handler handler;
    private TextView txtProgress;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alert_dialog_new);
        handler = new Handler();
        //Take picture
        previewHolder = (FrameLayout) findViewById(R.id.surfaceView);
        final TransparentSurfaceView cameraView = new TransparentSurfaceView(this, Camera.open());
        previewHolder.addView(cameraView);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                cameraView.getCamera().takePicture(null, null, DialogEmergencyActivity.this);
            }
        }, 1000);
        //Show countdown
        txtProgress = (TextView) findViewById(R.id.txtProgress);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        countDown();

        //Show dialog
//        if (getIntent().getExtras() != null) {
//            Bundle b = getIntent().getExtras();
//            String type = b.getString("type", "POLICE");
//            final String number = b.getString("number", "113");
//            VibratorUtil.vibrate(this, 2000);
//            DialogBuilder.yesNoDialog(this, "WARNING", "Would you want to call " + type, R.style.AppTheme2_AlertDialogStyle, new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                    CommunicationUtil.dialTo(DialogEmergencyActivity.this, number);
//                    dialog.dismiss();
//                    finish();
//                }
//            }, new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                    dialog.dismiss();
//                    finish();
//                }
//            }).setCancelable(false).create().show();
//        }
    }

    public void countDown() {
        new Thread(new Runnable() {
            int pStatus = 10;

            @Override
            public void run() {
                while (pStatus > 0) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            progressBar.setProgress(100 - pStatus * 10);
                            txtProgress.setText(pStatus + " s");
                        }
                    });
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    pStatus--;
                }
            }
        }).start();
    }

    @Override
    public void onPictureTaken(byte[] data, Camera camera) {
        ImageUtil.createFileFromData(data);
    }
}
