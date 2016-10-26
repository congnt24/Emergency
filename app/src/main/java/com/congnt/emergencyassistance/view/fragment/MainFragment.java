package com.congnt.emergencyassistance.view.fragment;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.location.Location;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.SwitchCompat;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TimePicker;
import android.widget.Toast;

import com.congnt.androidbasecomponent.Awesome.AwesomeFragment;
import com.congnt.androidbasecomponent.utility.CommunicationUtil;
import com.congnt.androidbasecomponent.view.dialog.DialogBuilder;
import com.congnt.androidbasecomponent.view.fragment.MapFragmentWithFusedLocation;
import com.congnt.androidbasecomponent.view.speechview.RecognitionProgressView;
import com.congnt.androidbasecomponent.view.widget.FlatButtonWithIconTop;
import com.congnt.emergencyassistance.MySharedPreferences;
import com.congnt.emergencyassistance.R;
import com.congnt.emergencyassistance.entity.EventBusEntity.EBE_Result;
import com.congnt.emergencyassistance.entity.EventBusEntity.EBE_RmsdB;
import com.congnt.emergencyassistance.entity.EventBusEntity.EBE_StartStopService;
import com.congnt.emergencyassistance.entity.ItemCountryEmergencyNumber;
import com.congnt.emergencyassistance.entity.firebase.LocationForFirebase;
import com.congnt.emergencyassistance.view.activity.MainActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import cn.iwgang.countdownview.CountdownView;

/**
 * Created by congnt24 on 25/09/2016.
 */

public class MainFragment extends AwesomeFragment implements View.OnClickListener, MapFragmentWithFusedLocation.OnMapListener {
    private static final int DIALOG_POLICE = 1;
    private static final int DIALOG_AMBULANCE = 3;
    private static final int DIALOG_FIRE = 2;
    public boolean isCancel;
    private RecognitionProgressView speechView;
    private int number_police = 113;
    private int number_fire = 114;
    private int number_ambulance = 115;
    private FlatButtonWithIconTop btn_police;
    private FlatButtonWithIconTop btn_fire;
    private FlatButtonWithIconTop btn_ambulance;
    private SwitchCompat btn_start_stop;
    //Map fragment
    private MapFragmentWithFusedLocation mapFragment;
    private ItemCountryEmergencyNumber countrynumber;
    private ImageButton ibWalkMode;
    private ImageButton ibTimmer;
    private CountdownView cdvTimmer;
    //Firebase defination
    private FirebaseDatabase firebaseDatabase;

    public static AwesomeFragment newInstance() {
        return new MainFragment();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_main;
    }

    @Override
    protected void initAll(View rootView) {
        EventBus.getDefault().register(this);
        //init firebase
        firebaseDatabase = FirebaseDatabase.getInstance();
        //get views
        countrynumber = ((MainActivity) getActivity()).countrynumber;
        ibWalkMode = (ImageButton) rootView.findViewById(R.id.ib_walk_mode);
        ibTimmer = (ImageButton) rootView.findViewById(R.id.ib_timmer);
        cdvTimmer = (CountdownView) rootView.findViewById(R.id.cdv_timmer);
        //Init timmer
        initTimmer();
        //Init speech recognizer
        setupSpeechRecognitionService(rootView);
        setupEmergencyNumber(rootView);

        //Bind Map Fragment
        mapFragment = (MapFragmentWithFusedLocation) getChildFragmentManager().findFragmentById(R.id.map_fragment);
        mapFragment.setUpdatable(true);
        mapFragment.setScrollGesturesEnabled(true);
        mapFragment.setOnMapListener(this);

        //Update friend location real time
        firebaseDatabase.getReference().child("users").child("ntc0222@gmailcom").child("location").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                LocationForFirebase friendLocation = dataSnapshot.getValue(LocationForFirebase.class);
                Location location = new Location("");
                location.setLatitude(friendLocation.getLat());
                location.setLongitude(friendLocation.getLng());
//              move marker
                mapFragment.addMarker("ntc0222@gmail.com", location);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void initTimmer() {
        cdvTimmer.setOnCountdownEndListener(new CountdownView.OnCountdownEndListener() {
            @Override
            public void onEnd(CountdownView cv) {
                cv.stop();
                cv.setVisibility(View.GONE);
            }
        });
        ibTimmer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), TimePickerDialog.THEME_HOLO_DARK, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        if (!isCancel) {
                            isCancel = false;
                            long time = (hourOfDay * 60 + minute) * 60000;
                            if (time > 0) {
                                cdvTimmer.setVisibility(View.VISIBLE);
                                cdvTimmer.start(time);
                            } else {
                                Toast.makeText(getActivity(), getString(R.string.timmer_cannot_be_0), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }, 0, 0, true);

                timePickerDialog.setButton(DialogInterface.BUTTON_NEGATIVE, getContext().getText(android.R.string.cancel), new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        isCancel = true;
                        dialog.dismiss();
                    }
                });
                timePickerDialog.setButton(DialogInterface.BUTTON_POSITIVE, getContext().getText(android.R.string.ok), new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        isCancel = false;
                        dialog.dismiss();
                    }
                });
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
        speechView.setBars_count(7);
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
        btn_start_stop = (SwitchCompat) rootView.findViewById(R.id.switch_start_service);
        btn_start_stop.setOnClickListener(this);
        //If service is running, change start of btn to starty
        btn_start_stop.setChecked(MySharedPreferences.getInstance(getActivity()).isListening.load(false));
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
            case R.id.switch_start_service:
                SwitchCompat switchCompat = (SwitchCompat) v;
                if (!switchCompat.isChecked()) {
                    EventBus.getDefault().post(new EBE_StartStopService(false));
                    speechView.stop();
                } else {
                    EventBus.getDefault().post(new EBE_StartStopService(true));
                    speechView.play();
                }
                break;
            case R.id.btn_call_ambulance:
                call(DIALOG_AMBULANCE);
                break;
            case R.id.btn_call_fire:
                call(DIALOG_FIRE);
                break;
            case R.id.btn_call_police:
                call(DIALOG_POLICE);
                break;
        }
    }

    public void call(int dialogType){
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
                CommunicationUtil.dialTo(getActivity(), finalNumber);
            }
        }).create().show();
    }

    @Override
    public void onLocationChange(Location location) {
//        mapFragment.animateCamera(location, 13);
        //update location to friend using firebase if option share location is enable
        if (MySharedPreferences.getInstance(getActivity()).shareLocationState.load(false)) {//is share location
            //Get firebase user
            FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
            if (firebaseUser == null) {//Not login, require login before share location
                //TODO: Nothing while user not login
            } else {//if User islogged, change data on database
                LocationForFirebase fLocation = new LocationForFirebase(location.getLatitude(), location.getLongitude());
                firebaseDatabase.getReference().child("users").child(firebaseUser.getEmail().replace(".", "")).child("location").setValue(fLocation);
            }

        }

    }

    @Override
    public void onConnected() {

    }

    //Eventbus subscriber


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
    }
}
