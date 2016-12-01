package com.congnt.emergencyassistance.view.fragment;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.congnt.androidbasecomponent.Awesome.AwesomeFragment;
import com.congnt.androidbasecomponent.utility.AndroidUtil;
import com.congnt.androidbasecomponent.utility.CommunicationUtil;
import com.congnt.androidbasecomponent.utility.FormatUtil;
import com.congnt.androidbasecomponent.view.dialog.DialogBuilder;
import com.congnt.androidbasecomponent.view.speechview.RecognitionProgressView;
import com.congnt.androidbasecomponent.view.widget.FlatButtonWithIconTop;
import com.congnt.emergencyassistance.AppConfig;
import com.congnt.emergencyassistance.MainApplication;
import com.congnt.emergencyassistance.MySharedPreferences;
import com.congnt.emergencyassistance.OnEventListener;
import com.congnt.emergencyassistance.R;
import com.congnt.emergencyassistance.entity.EventBusEntity.EBE_DetectAccident;
import com.congnt.emergencyassistance.entity.EventBusEntity.EBE_StartLocationFollowService;
import com.congnt.emergencyassistance.entity.EventBusEntity.EBE_StartLocationService;
import com.congnt.emergencyassistance.entity.EventBusEntity.EBE_StartStopService;
import com.congnt.emergencyassistance.entity.ItemCountryEmergencyNumber;
import com.congnt.emergencyassistance.entity.firebase.User;
import com.congnt.emergencyassistance.entity.parse.ParseFollow;
import com.congnt.emergencyassistance.util.CountryUtil;
import com.congnt.emergencyassistance.view.activity.EmergencyStateActivity;
import com.congnt.emergencyassistance.view.activity.LoginActivity;
import com.congnt.emergencyassistance.view.activity.MainActivity;
import com.congnt.emergencyassistance.view.dialog.DialogFollow;
import com.congnt.emergencyassistance.view.dialog.DialogSendSMS;
import com.congnt.reversegeocodecountry.GeocodeCountry;
import com.congnt.reversegeocodecountry.ReverseGeocodeCountry;
import com.parse.ParseException;
import com.parse.SaveCallback;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

import cn.iwgang.countdownview.CountdownView;

/**
 * Created by congnt24 on 25/09/2016.
 */

public class MainFragment extends AwesomeFragment implements View.OnClickListener {
    private static final String TAG = "MainFragment";
    private static final int DIALOG_POLICE = 1;
    private static final int DIALOG_AMBULANCE = 3;
    private static final int DIALOG_FIRE = 2;
    public boolean isCancel;
    public CardView layout_detect_accident;
    private RecognitionProgressView speechView;
    private FlatButtonWithIconTop btn_police;
    private FlatButtonWithIconTop btn_fire;
    private FlatButtonWithIconTop btn_ambulance;
    //    private SwitchCompat btn_start_stop;
    private ImageButton ib_start_recognition;
    private TextView tvAcc;
    private TextView tvSpeed;
    private TextView tvMaxSpeed;
    private TextView tvDistance;
    //Map fragment
    private MapFragmentWithFusedLocationLite mapFragment;
    private ItemCountryEmergencyNumber countrynumber;
    private ImageButton ibWalkMode;
    private ImageButton ibTimmer;
    private CountdownView cdvTimmer;
    private LinearLayout layoutCountDown;
    private Button btnCancel;
    private MainActivity mainActivity;
    private ImageButton ibLocation;
    //    private ImageButton ibFollow;
    private boolean isShareLocation;
    private Handler handler = new Handler();
    private String currentParseId;
    private boolean isStartRecognition;
    private DialogFollow dialogFollow;

    public static AwesomeFragment newInstance() {
        return new MainFragment();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_main;
    }

    @Override
    protected void initAll(View rootView) {
        mainActivity = (MainActivity) getActivity();
        EventBus.getDefault().register(this);
        //init firebase
        //get views
        countrynumber = ((MainActivity) getActivity()).countrynumber;
        ibWalkMode = (ImageButton) rootView.findViewById(R.id.ib_walk_mode);
        ibTimmer = (ImageButton) rootView.findViewById(R.id.ib_timmer);
        ibLocation = (ImageButton) rootView.findViewById(R.id.ib_location);
//        ibFollow = (ImageButton) rootView.findViewById(R.id.ib_follow);
        layoutCountDown = (LinearLayout) rootView.findViewById(R.id.layout_countdown);
        cdvTimmer = (CountdownView) rootView.findViewById(R.id.cdv_timmer);
        btnCancel = (Button) rootView.findViewById(R.id.btn_cancel);
        layout_detect_accident = (CardView) rootView.findViewById(R.id.layout_detect_accident);
        tvAcc = (TextView) rootView.findViewById(R.id.tv_acc);
        tvSpeed = (TextView) rootView.findViewById(R.id.tv_speed);
        tvMaxSpeed = (TextView) rootView.findViewById(R.id.tv_max_speed);
        tvDistance = (TextView) rootView.findViewById(R.id.tv_distance);
        ib_start_recognition = (ImageButton) rootView.findViewById(R.id.ib_start_recognition);
        //Init timmer
        initTimmer();
        //Init speech recognizer
        setupSpeechRecognitionService(rootView);
        setupEmergencyNumber(rootView);
        initMapFragment();
        initListener();
    }

    private void initMapFragment() {
        //Bind Map Fragment
        mapFragment = (MapFragmentWithFusedLocationLite) getChildFragmentManager().findFragmentById(R.id.map_fragment);
        mapFragment.setUpdatable(false);
        mapFragment.setScrollGesturesEnabled(true);
//        mapFragment.setListener(new MapFragmentWithFusedLocationLite.OnMapListener() {
//            @Override
//            public void onMapReady() {
//                mapFragment.setMyLocationEnabled(false);
//            }
//        });
        mainActivity.mainApplication.setRequest_duration(2000);
        mainActivity.mainApplication.setRequest_displacement(0);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                EventBus.getDefault().post(new EBE_StartLocationService(true, false));
            }
        }, 1500);
    }

    private void initListener() {
        ibLocation.setOnClickListener(this);
//        ibFollow.setOnClickListener(this);
        ibWalkMode.setOnClickListener(this);
//        btn_start_stop.setOnClickListener(this);
        ib_start_recognition.setOnClickListener(this);
        btn_police.setOnClickListener(this);
        btn_fire.setOnClickListener(this);
        btn_ambulance.setOnClickListener(this);
    }

    private void initTimmer() {
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cdvTimmer.stop();
                layoutCountDown.setVisibility(View.GONE);
            }
        });
        cdvTimmer.setOnCountdownEndListener(new CountdownView.OnCountdownEndListener() {
            @Override
            public void onEnd(CountdownView cv) {
                cv.stop();
                layoutCountDown.setVisibility(View.GONE);
                //Start emergency dialog
                Intent intent = new Intent(getActivity(), EmergencyStateActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
        ibTimmer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), TimePickerDialog.THEME_HOLO_DARK, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        long time = (hourOfDay * 60 + minute) * 60000;
                        Toast.makeText(mainActivity, "Alert will be show in " + time / 1000 + " s", Toast.LENGTH_SHORT).show();
                        if (time > 0) {
                            layoutCountDown.setVisibility(View.VISIBLE);
                            cdvTimmer.start(time);
                        } else {
                            Toast.makeText(getActivity(), getString(R.string.timmer_cannot_be_0), Toast.LENGTH_SHORT).show();
                        }
                    }
                }, 0, 0, true);

                timePickerDialog.show();
            }
        });
    }

    /**
     * Setup Speech Recognition service
     */
    public void setupSpeechRecognitionService(View rootView) {
        Context context = getActivity();
        speechView = (RecognitionProgressView) rootView.findViewById(R.id.speechview);
        speechView.setBars_count(5);
        int[] colors = {
                ContextCompat.getColor(context, R.color.color1),
                ContextCompat.getColor(context, R.color.color2),
                ContextCompat.getColor(context, R.color.color3),
                ContextCompat.getColor(context, R.color.color4),
                ContextCompat.getColor(context, R.color.color5),
                ContextCompat.getColor(context, R.color.color1),
                ContextCompat.getColor(context, R.color.color2),
                ContextCompat.getColor(context, R.color.color3),
                ContextCompat.getColor(context, R.color.color4),
                ContextCompat.getColor(context, R.color.color5)
        };
        int[] heights = {30, 46, 28, 40, 25, 14, 36, 37, 48, 25};

        speechView.setColors(colors);
        speechView.setBarMaxHeightsInDp(heights);
//        btn_start_stop = (SwitchCompat) rootView.findViewById(R.id.switch_start_service);
        //If service is running, change start of btn to starty
//        btn_start_stop.setChecked(MySharedPreferences.getInstance(getActivity()).isListening.load(false));
        if (MySharedPreferences.getInstance(getActivity()).isListening.load(false)) {
            ib_start_recognition.setImageResource(R.drawable.ic_volume_up_white_36dp);
        }
    }

    /**
     * Setup emergency number for a country
     *
     * @param rootView
     */
    public void setupEmergencyNumber(View rootView) {
        btn_police = (FlatButtonWithIconTop) rootView.findViewById(R.id.btn_call_police);
        btn_fire = (FlatButtonWithIconTop) rootView.findViewById(R.id.btn_call_fire);
        btn_ambulance = (FlatButtonWithIconTop) rootView.findViewById(R.id.btn_call_ambulance);
        //Set Value
        setupEmergencyText();
    }

    private void setupEmergencyText() {
        if (countrynumber == null) return;
        btn_police.setText("" + countrynumber.police);
        btn_fire.setText("" + countrynumber.fire);
        btn_ambulance.setText("" + countrynumber.ambulance);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ib_start_recognition:
                if (isStartRecognition) {
                    ib_start_recognition.setImageResource(R.drawable.ic_volume_off_white_36dp);
                    EventBus.getDefault().post(new EBE_StartStopService(false));
                    speechView.stop();
                } else {
                    ib_start_recognition.setImageResource(R.drawable.ic_volume_up_white_36dp);
                    EventBus.getDefault().post(new EBE_StartStopService(true));
                    speechView.play();
                }
                isStartRecognition = !isStartRecognition;
                break;
            case R.id.btn_call_ambulance:
                actionCall(DIALOG_AMBULANCE);
                break;
            case R.id.btn_call_fire:
                actionCall(DIALOG_FIRE);
                break;
            case R.id.btn_call_police:
                actionCall(DIALOG_POLICE);
                break;
            case R.id.ib_walk_mode:
                DialogSendSMS dialogSendSMS = new DialogSendSMS();
                Location location = ((MainApplication) getActivity().getApplication()).getLastLocation();
                if (location != null) {
                    Bundle b = new Bundle();
                    b.putString("location", location.getLatitude() + "," + location.getLongitude());
                    dialogSendSMS.setArguments(b);
                } else {
                    Toast.makeText(mainActivity, getString(R.string.location_not_avaiable), Toast.LENGTH_SHORT).show();
                }
                dialogSendSMS.show(getChildFragmentManager(), "SMS");
                break;
            case R.id.ib_location:
                if (!isShareLocation) {  //enable
                    actionShareLocation();
                    dialogFollow = new DialogFollow();
                    dialogFollow.setOnEventListener(new OnEventListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            ibLocation.setImageResource(R.drawable.ic_location_on);
                            isShareLocation = true;
                            if (!TextUtils.isEmpty(currentParseId)) {
                                EventBus.getDefault().post(new EBE_StartLocationFollowService(true, currentParseId));
                            }
                        }

                        @Override
                        public void onError(Void aVoid) {

                        }
                    });
                    dialogFollow.show(getChildFragmentManager(), "Follow");
                } else {    //diable
                    ibLocation.setImageResource(R.drawable.ic_location_off);
                    isShareLocation = false;
                    //stop follow service
                    EventBus.getDefault().post(new EBE_StartLocationFollowService(false));
//                    stopUpdateLocationToParseSErver();
                }
                break;
            case R.id.ib_follow:
                DialogFollow dialogFollow = new DialogFollow();
                dialogFollow.show(getChildFragmentManager(), "Follow");
                break;
        }
    }

    private void actionShareLocation() {
        if (mainActivity.isLogged) {
//            MySharedPreferences.getInstance(mainActivity).
            MySharedPreferences.getInstance(mainActivity).shareLocationState.save(isShareLocation);
//            if (isShareLocation) {
                //run follow service
                User user = MySharedPreferences.getInstance(mainActivity).userProfile.load(null);
                final ParseFollow parseFollow = new ParseFollow(user.getEmail(), "0", "0", new ArrayList<String>());
                parseFollow.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null) {
                            currentParseId = parseFollow.getObjectId();
                            Log.d(TAG, "actionShareLocation: ID "+currentParseId);
                            dialogFollow.getEtContent().append(currentParseId);
                        }
                    }
                });
//                updateLocationToParseServer();
//            }
        } else {
            //Start login activity for result
            Intent intent = new Intent(mainActivity, LoginActivity.class);
            startActivityForResult(intent, MainActivity.REQUEST_LOGIN_FOR_SHARE_LOCATION);
        }
    }

    public void actionCall(int dialogType) {
        String title = "";
        String number = null;
        switch (dialogType) {
            case DIALOG_POLICE:
                title = "Call Police:";
                number = btn_police.getText();
                break;
            case DIALOG_FIRE:
                title = "Call Fire:";
                number = btn_fire.getText();
                break;
            case DIALOG_AMBULANCE:
                title = "Call Ambulance:";
                number = btn_ambulance.getText();
                break;
        }
        final String finalNumber = number;
        DialogBuilder.confirmDialog(getActivity(), title, getActivity().getString(R.string.dialog_message_make_a_call) + " " + finalNumber
                , R.style.AppTheme2_AlertDialogStyle, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (MySharedPreferences.getInstance(getActivity()).pref.getBoolean("setting_call_to", false)) {
                            CommunicationUtil.callTo(getActivity(), finalNumber);
                        } else {
                            CommunicationUtil.dialTo(getActivity(), finalNumber);
                        }
                    }
                }).create().show();
    }

    //Eventbus subscriber
/*
    @Subscribe
    public void onResult(EBE_Result result) {

        for (String str :
                result.getValue()) {
            if (str.equalsIgnoreCase("help me")) {

            }
        }
        String text = "";
        for (String r : result.value)
            text += r + "\n";

//        Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT).show();
    }

    @Subscribe
    public void onRmsChanged(EBE_RmsdB rmsdB) {
        speechView.onRmsChanged(rmsdB.aFloat);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onBeginOrEndOfSpeech(Integer x) {
        if (x == 0) {
            speechView.onBeginningOfSpeech();
            speechView.play();
        } else if (x == 1) {
            speechView.onEndOfSpeech();
            speechView.stop();
        }

    }
*/

    /**
     * Update emergency number while country is changed
     *
     * @param countrynumber
     */
    @Subscribe
    public void onCountryChange(ItemCountryEmergencyNumber countrynumber) {
        btn_police.setText("" + countrynumber.police);
        btn_fire.setText("" + countrynumber.fire);
        btn_ambulance.setText("" + countrynumber.ambulance);
        MySharedPreferences.getInstance(mainActivity).emergency_command.save(null);
        mainActivity.initSpeechCommand(countrynumber.countryCode);
    }

    @Subscribe
    public void onUpdateAccelerometer(EBE_DetectAccident detectAccident) {
        tvAcc.setText(getString(R.string.accelerometer) + " " + FormatUtil.formatDouble(detectAccident.getValue().accelerometer, 4));
        tvSpeed.setText(getString(R.string.speed) + " " + FormatUtil.formatDouble(detectAccident.getValue().speed, 4) + " km/h");
        tvMaxSpeed.setText(getString(R.string.max_speed) + " " + FormatUtil.formatDouble(detectAccident.getValue().maxSpeed, 4) + " km/h");
        tvDistance.setText(getString(R.string.distance) + " " + FormatUtil.formatDouble(detectAccident.getValue().distance, 4));
    }

    @Subscribe
    public void onLocationChanged(Location location) {
//        Log.d(TAG, "onLocationChanged: "+location.getLongitude());
        //Check country by location
        setupCountry(location);
        mapFragment.animateCamera(location, AppConfig.ZOOM_LEVEL);
        mapFragment.updateLocation(location);
    }

    private void setupCountry(Location location) {
        countrynumber = MySharedPreferences.getInstance(mainActivity).countryNumber.load(null);
        if (countrynumber == null) {
            GeocodeCountry geocodeCountry = ReverseGeocodeCountry.getInstance(mainActivity).getCountryByLatLng(location.getLatitude(), location.getLongitude());
            if (geocodeCountry != null) {
                countrynumber = CountryUtil.getInstance(mainActivity).getItemCountryByName(geocodeCountry.getName());
            } else {
                String countryCode = AndroidUtil.getCountryCode();
                countrynumber = CountryUtil.getInstance(mainActivity).getItemCountryByCode(countryCode);
            }
            Log.d(TAG, "setupCountry: " + countrynumber.countryCode);
            setupEmergencyText();
            MySharedPreferences.getInstance(mainActivity).countryNumber.save(countrynumber);
            //update speech command by country
            mainActivity.initSpeechCommand(countrynumber.countryCode);
        }
    }

}
