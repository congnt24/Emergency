package com.congnt.emergencyassistance.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ToggleButton;

import com.congnt.androidbasecomponent.Awesome.AwesomeFragment;
import com.congnt.androidbasecomponent.utility.CommunicationUtil;
import com.congnt.androidbasecomponent.view.dialog.DialogBuilder;
import com.congnt.androidbasecomponent.view.speechview.RecognitionProgressView;
import com.congnt.androidbasecomponent.view.widget.FlatButtonWithIconTop;
import com.congnt.emergencyassistance.EventBusEntity.EBE_Result;
import com.congnt.emergencyassistance.EventBusEntity.EBE_RmsdB;
import com.congnt.emergencyassistance.EventBusEntity.EBE_StartStopService;
import com.congnt.emergencyassistance.MySharedPreferences;
import com.congnt.emergencyassistance.R;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Created by congnt24 on 25/09/2016.
 */

public class MainFragment extends AwesomeFragment implements View.OnClickListener {
    private static final int DIALOG_POLICE = 1;
    private static final int DIALOG_AMBULANCE = 3;
    private static final int DIALOG_FIRE = 2;
    private RecognitionProgressView speechView;
    private int number_police = 113;
    private int number_fire = 114;
    private int number_ambulance = 115;
    private FlatButtonWithIconTop btn_police;
    private FlatButtonWithIconTop btn_fire;
    private FlatButtonWithIconTop btn_ambulance;
    private ToggleButton btn_start_stop;

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
        int[] heights = {50, 66, 48, 70, 45, 34, 56, 67, 68, 45};

        speechView.setColors(colors);
        speechView.setBarMaxHeightsInDp(heights);
        btn_start_stop = (ToggleButton) rootView.findViewById(R.id.btn_start_stop);
        btn_start_stop.setOnClickListener(this);
        //If service is running, change start of btn to starty
        btn_start_stop.setChecked(MySharedPreferences.getInstance(getActivity()).isListening.load(false));
    }

    @Subscribe
    public void onResult(EBE_Result result) {

        for (String str :
                result.getValue()) {
            if (str.equalsIgnoreCase("help me")){

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

    public void setupEmergencyNumber(View rootView) {
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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_start_stop:
                ToggleButton tb = (ToggleButton) v;
                if (!tb.isChecked()) {
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
        DialogBuilder.confirmDialog(getActivity(), title, getActivity().getString(R.string.dialog_message_make_a_call) + " "+ finalNumber, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                CommunicationUtil.dialTo(getActivity(), finalNumber);
            }
        }).create().show();
    }
}
