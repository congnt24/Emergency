package com.congnt.emergencyassistance.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.hardware.Camera;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.congnt.androidbasecomponent.adapter.AwesomeRecyclerAdapter;
import com.congnt.androidbasecomponent.utility.AndroidUtil;
import com.congnt.androidbasecomponent.utility.CameraUtil;
import com.congnt.androidbasecomponent.utility.CommunicationUtil;
import com.congnt.androidbasecomponent.utility.ImageUtil;
import com.congnt.androidbasecomponent.utility.SoundUtil;
import com.congnt.androidbasecomponent.utility.VibratorUtil;
import com.congnt.androidbasecomponent.view.widget.FlatButtonWithIconTop;
import com.congnt.androidbasecomponent.view.widget.TransparentSurfaceView;
import com.congnt.emergencyassistance.AppConfig;
import com.congnt.emergencyassistance.MainApplication;
import com.congnt.emergencyassistance.MySharedPreferences;
import com.congnt.emergencyassistance.R;
import com.congnt.emergencyassistance.adapter.ContactAdapter;
import com.congnt.emergencyassistance.entity.EventBusEntity.EBE_StartStopService;
import com.congnt.emergencyassistance.entity.ItemContact;
import com.congnt.emergencyassistance.entity.ItemCountryEmergencyNumber;
import com.congnt.emergencyassistance.services.RecordAudioService;
import com.congnt.emergencyassistance.view.dialog.DialogSendSMS;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class EmergencyStateActivity extends Activity implements Camera.PictureCallback, View.OnClickListener {
    private static final String TAG = "EmergencyStateActivity";
    public static boolean isRunning = false;
    private FrameLayout previewHolder;
    private Handler handler;
    private TextView txtProgress;
    private ProgressBar progressBar;
    private CountDownTimer countdownTimer;
    private long countDownTime;
    private FlatButtonWithIconTop btn_police;
    private FlatButtonWithIconTop btn_fire;
    private FlatButtonWithIconTop btn_ambulance;
    private ItemCountryEmergencyNumber countrynumber;
    //    private CircleImageView[] contacts = new CircleImageView[4];
    private List<ItemContact> listContact;
    private RecyclerView recyclerView;
    private ContactAdapter adapter;
    private java.lang.Runnable delayRunnable;
    private boolean hasSpeech;
    private long[] vibratorPattern = new long[]{2000, 200, 2000, 200};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!isRunning) {
            isRunning = true;
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
            AndroidUtil.holdWakeLock(this);
        } else {
            finish();
        }
        SoundUtil.setStreamMute(this, false);
        setContentView(R.layout.activity_alert_dialog_new);
        hasSpeech = MySharedPreferences.getInstance(this).isListening.load(false);
        //init variables
        handler = new Handler();
        countDownTime = Long.parseLong(MySharedPreferences.getInstance(this).pref.getString("setting_countdown_time", "10")) * 1000;
        countrynumber = MySharedPreferences.getInstance(this).countryNumber.load(null);
        initViews();
        if (VibratorUtil.hasVibrator(this)) {
            VibratorUtil.vibrate(this, vibratorPattern, 0);
        }

        //Show dialog
        if (getIntent().getExtras() != null) {
            Bundle b = getIntent().getExtras();
            //If is detecting accident
            final String type = b.getString("type", "POLICE");
            delayRunnable = new Runnable() {
                @Override
                public void run() {
                    finishCountdown(type);
                }
            };
            if (type.equalsIgnoreCase(AppConfig.DETECT_ACCIDENT)) {
                countDownTime = Long.parseLong(MySharedPreferences.getInstance(this).pref.getString("setting_countdown_time", "30")) * 1000;
            } else {
            }
            //stop speech service
            if (hasSpeech) EventBus.getDefault().post(new EBE_StartStopService(false));
            handler.postDelayed(delayRunnable, countDownTime);
        } else {
            //stop speech service
            if (hasSpeech) EventBus.getDefault().post(new EBE_StartStopService(false));
            handler.postDelayed(delayRunnable, countDownTime);
        }
        initEvent();
    }

    private void finishCountdown(String type) {

        if (VibratorUtil.hasVibrator(this)) {
            VibratorUtil.cancel(this);
        }
        //Recorder
        startService(new Intent(EmergencyStateActivity.this, RecordAudioService.class));
        List<ItemContact> listContact = MySharedPreferences.getInstance(this).listContact.load(null);
        if (listContact != null) {
            //SMS
            if (MySharedPreferences.getInstance(this).pref.getBoolean("setting_contact_sms", false)) {
                if (listContact.size() > 0) {
                    Location location = ((MainApplication) getApplication()).lastLocation;
                    for (int i = 0; i < listContact.size() && i < 3; i++) {
                        CommunicationUtil.sendSmsAuto(listContact.get(i).getContactNumber(), DialogSendSMS.getSettingSpeech(this).templateMessage.get(0).message + "\n" +
                                AppConfig.TEMPLATE_GOOGLEMAP_PLACE + location.getLatitude() + "," + location.getLongitude());
                    }
                }
            }
            //Call

            if (MySharedPreferences.getInstance(this).listContact.load(new ArrayList<ItemContact>()).size() > 0 &&
                    MySharedPreferences.getInstance(this).pref.getBoolean("setting_contact_call", false)) {
                CommunicationUtil.callTo(EmergencyStateActivity.this, listContact.get(0).getContactNumber());
            } else {
                switch (type) {
                    case AppConfig.FIRE:
                        CommunicationUtil.callTo(EmergencyStateActivity.this, countrynumber.fire);
                        break;
                    case AppConfig.AMBULANCE:
                        CommunicationUtil.callTo(EmergencyStateActivity.this, countrynumber.ambulance);
                        break;
                    default:
                        CommunicationUtil.callTo(EmergencyStateActivity.this, countrynumber.police);
                        break;
                }
            }
        } else {
            switch (type) {
                case AppConfig.FIRE:
                    CommunicationUtil.callTo(EmergencyStateActivity.this, countrynumber.fire);
                    break;
                case AppConfig.AMBULANCE:
                    CommunicationUtil.callTo(EmergencyStateActivity.this, countrynumber.ambulance);
                    break;
                default:
                    CommunicationUtil.callTo(EmergencyStateActivity.this, countrynumber.police);
                    break;
            }
        }
        finish();
    }

    private void initViews() {
        previewHolder = (FrameLayout) findViewById(R.id.surfaceView);
        txtProgress = (TextView) findViewById(R.id.txtProgress);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        findViewById(R.id.layout_countdown).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                countdownTimer.cancel();
                //stop speech service
                if (hasSpeech) EventBus.getDefault().post(new EBE_StartStopService(true));
                finish();
            }
        });
        //bind contact
        setupEmergencyNumber();
        setupContacts();
        //Twinkle background
        View layoutParent = findViewById(R.id.layout_parent);
        twinkleBackground(layoutParent, 600, ResourcesCompat.getColor(getResources(), R.color.colorPrimary, null), ResourcesCompat.getColor(getResources(), R.color.colorPrimaryDark, null));

    }

    private void setupContacts() {
        listContact = MySharedPreferences.getInstance(this).listContact.load(new ArrayList<ItemContact>());
        if (listContact.size() > 3) {
            int size = listContact.size();
            for (int i = 3; i < size; i++) {
                listContact.remove(3);
            }
        }
        recyclerView = (RecyclerView) findViewById(R.id.recycler_contact);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        adapter = new ContactAdapter(this, listContact, new AwesomeRecyclerAdapter.OnClickListener<ItemContact>() {
            @Override
            public void onClick(ItemContact item, int position) {
                CommunicationUtil.callTo(EmergencyStateActivity.this, item.getContactNumber());
                finish();
            }
        });
        recyclerView.setAdapter(adapter);
    }

    private void initEvent() {
        //Alert sound
//        if (MySharedPreferences.getInstance(this).pref.getBoolean("setting_mute_speech", false)){
//        }
        SoundUtil.getInstance().playSound(this, R.raw.alert_sound);
        //Vibrator
        //TODO: vibrator
        // Take picture
        initPicture();
        //Count down timmer
        countDown(countDownTime);
    }

    private void initPicture() {
        try {
            if (MySharedPreferences.getInstance(this).pref.getBoolean("setting_take_picture", false)) {
                final TransparentSurfaceView cameraView = new TransparentSurfaceView(this, Camera.open());
                previewHolder.addView(cameraView);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        cameraView.getCamera().takePicture(null, null, new Camera.PictureCallback() {
                            @Override
                            public void onPictureTaken(byte[] data, Camera camera) {
                                ImageUtil.createImageFromData(data, AppConfig.FOLDER_MEDIA);
                                Camera cam = cameraView.getCamera();
                                cam.stopPreview();
                                cam.release();
                                cam = Camera.open(Camera.CameraInfo.CAMERA_FACING_FRONT);
                                try {
                                    cam.setPreviewDisplay(cameraView.getHolder());
                                    cam.setParameters(CameraUtil.getCameraParam(cam));
                                    cam.startPreview();
                                    final Camera finalCamera = cam;
                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            finalCamera.takePicture(null, null, EmergencyStateActivity.this);
                                        }
                                    }, 500);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }
                }, 1000);
            }
        } catch (Exception e) {
        }
    }

    public void countDown(final long duration) {
        progressBar.setMax((int) (duration / 1000));
        countdownTimer = new CountDownTimer(duration, 1000) {
            long duration2 = duration + 1000;

            @Override
            public void onTick(long millisUntilFinished) {
                progressBar.setProgress((int) ((duration2 - millisUntilFinished) / 1000));
                txtProgress.setText((duration2 - millisUntilFinished) / 1000 + " s");
            }

            @Override
            public void onFinish() {
                progressBar.setProgress(0);
                txtProgress.setText("Your danger!");
                progressBar.setOnClickListener(null);
                Drawable background;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    background = getDrawable(R.drawable.bg_danger_progress);
                } else {
                    background = getResources().getDrawable(R.drawable.bg_danger_progress);
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    progressBar.setBackground(background);
                } else {
                    progressBar.setBackgroundDrawable(background);
                }
//                progressBar.setProgressDrawable(null);
            }
        }.start();
    }

    @Override
    public void onPictureTaken(byte[] data, Camera camera) {
        ImageUtil.createImageFromData(data, "EmergencyAssistance");
    }

    public void twinkleBackground(final View layout, int ms, final int... colors) {
        final AnimationDrawable drawable = new AnimationDrawable();
        final Handler handler = new Handler();
        for (int i = 0; i < colors.length; i++) {
            drawable.addFrame(new ColorDrawable(colors[i]), ms);
        }
        drawable.setOneShot(false);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            layout.setBackground(drawable);
        } else {
            layout.setBackgroundDrawable(drawable);
        }
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                drawable.start();
            }
        }, 100);
    }

    @Override
    public void finish() {
        isRunning = false;
        AndroidUtil.releaseWakeLock();
        if (VibratorUtil.hasVibrator(this)) {
            VibratorUtil.cancel(this);
        }
        try {
            SoundUtil.getInstance().stop();
            handler.removeCallbacks(delayRunnable);
        } catch (Exception e) {
            super.finish();
        }
        super.finish();
    }

    /**
     * Setup emergency number for a country
     */
    public void setupEmergencyNumber() {
        btn_police = (FlatButtonWithIconTop) findViewById(R.id.btn_call_police);
        btn_fire = (FlatButtonWithIconTop) findViewById(R.id.btn_call_fire);
        btn_ambulance = (FlatButtonWithIconTop) findViewById(R.id.btn_call_ambulance);
        //Set Value
        btn_police.setText("" + countrynumber.police);
        btn_fire.setText("" + countrynumber.fire);
        btn_ambulance.setText("" + countrynumber.ambulance);
        //SetOnClick
        btn_police.setOnClickListener(this);
        btn_fire.setOnClickListener(this);
        btn_ambulance.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_call_ambulance:
                CommunicationUtil.callTo(this, countrynumber.police);
                break;
            case R.id.btn_call_fire:
                CommunicationUtil.callTo(this, countrynumber.fire);
                break;
            case R.id.btn_call_police:
                CommunicationUtil.callTo(this, countrynumber.ambulance);
                break;
        }
        finish();
    }
}
