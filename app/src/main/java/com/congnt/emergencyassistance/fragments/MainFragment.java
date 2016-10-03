package com.congnt.emergencyassistance.fragments;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.congnt.androidbasecomponent.Awesome.AwesomeFragment;
import com.congnt.androidbasecomponent.view.speechview.RecognitionProgressView;
import com.congnt.androidbasecomponent.view.widget.FlatButtonWithIconTop;
import com.congnt.emergencyassistance.EventBusEntity.EBE_StartStopService;
import com.congnt.emergencyassistance.R;
import com.congnt.emergencyassistance.services.SpeechRecognitionService;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Created by congnt24 on 25/09/2016.
 */

public class MainFragment extends AwesomeFragment implements View.OnClickListener {
    private RecognitionProgressView speechView;
    private Intent service;
    private int number_police = 113;
    private int number_fire = 114;
    private int number_ambulance = 115;
    private FlatButtonWithIconTop btn_police;
    private FlatButtonWithIconTop btn_fire;
    private FlatButtonWithIconTop btn_ambulance;

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
        setupSpeechRecognitionService(rootView);
        setupEmergencyNumber(rootView);
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
        int[] heights = {60, 76, 58, 80, 55, 44, 66, 77, 88, 55};

        speechView.setColors(colors);
        speechView.setBarMaxHeightsInDp(heights);
        service = new Intent(context, SpeechRecognitionService.class);

        rootView.findViewById(R.id.btn_start).setOnClickListener(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onResult(String str) {
        speechView.stop();
        speechView.play();
        Toast.makeText(getActivity(), str, Toast.LENGTH_SHORT).show();
    }

    @Subscribe
    public void onRmsChanged(Float rmsdB) {
        speechView.onRmsChanged(rmsdB);
    }

    @Subscribe
    public void onBeginOrEndOfSpeech(Integer x) {
        if (x == 0) {
            speechView.onBeginningOfSpeech();
            speechView.play();
        } else if (x == 1) {
            speechView.onEndOfSpeech();
            speechView.stop();
        }

    }

    public void setupEmergencyNumber(View rootView) {
//        BootstrapButtonGroup btn_group = (BootstrapButtonGroup) rootView.findViewById(R.id.btn_group);
        btn_police = (FlatButtonWithIconTop) rootView.findViewById(R.id.btn_call_police);
        btn_fire = (FlatButtonWithIconTop) rootView.findViewById(R.id.btn_call_fire);
        btn_ambulance = (FlatButtonWithIconTop) rootView.findViewById(R.id.btn_call_ambulance);
        //Set Value
        btn_police.setText("" + number_police);
        btn_fire.setText("" + number_fire);
        btn_ambulance.setText("" + number_ambulance);
        //SetOnClick
        btn_police.setOnClickListener(this);
        btn_fire.setOnClickListener(this);
        btn_ambulance.setOnClickListener(this);
    }


    @Override
    public void onStart() {
        super.onStart();
        getActivity().startService(service);
    }

    @Override
    public void onStop() {
//        EventBus.getDefault().unregister(this);
//        stopService(service);
        super.onStop();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_start:
                ToggleButton tb = (ToggleButton) v;
                if (!tb.isChecked()) {
                    EventBus.getDefault().post(new EBE_StartStopService(false));
                    speechView.stop();
                } else {
                    EventBus.getDefault().post(new EBE_StartStopService(true));
                }
//                speechView.play();
                break;
//            case R.id.btn_call_ambulance:
//                CommunicationUtil.dialTo(getActivity(), btn_ambulance.getText().toString());
//                break;
//            case R.id.btn_call_fire:
//                CommunicationUtil.dialTo(getActivity(), btn_fire.getText().toString());
//                break;
//            case R.id.btn_call_police:
//                CommunicationUtil.dialTo(getActivity(), btn_police.getText().toString());
//                break;
        }
    }
}
