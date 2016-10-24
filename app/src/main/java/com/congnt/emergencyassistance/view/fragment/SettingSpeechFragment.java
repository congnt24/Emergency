package com.congnt.emergencyassistance.view.fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioGroup;

import com.congnt.androidbasecomponent.adapter.AwesomeRecyclerAdapter;
import com.congnt.androidbasecomponent.view.dialog.DialogBuilder;
import com.congnt.emergencyassistance.EmergencyType;
import com.congnt.emergencyassistance.MySharedPreferences;
import com.congnt.emergencyassistance.R;
import com.congnt.emergencyassistance.adapter.SettingSpeechAdapter;
import com.congnt.emergencyassistance.entity.ItemSettingSpeech;
import com.congnt.emergencyassistance.view.activity.AppCompatPreferenceActivity;

import java.util.ArrayList;
import java.util.List;

import static com.congnt.emergencyassistance.EmergencyType.AMBULANCE;
import static com.congnt.emergencyassistance.EmergencyType.FIRE;
import static com.congnt.emergencyassistance.EmergencyType.POLICE;

public class SettingSpeechFragment extends PreferenceFragment {
    private SettingSpeechAdapter adapter;
    private List<ItemSettingSpeech> list;
    private LayoutInflater li;
    private View dialogView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_setting_speech, container, false);
        //dialog
        li = LayoutInflater.from(getActivity());
        //Actionbar
        ActionBar actionBar = ((AppCompatPreferenceActivity) getActivity()).getSupportActionBar();
        actionBar.setTitle("Setting Speech");


        //Setting Recycler View
        RecyclerView recyclerview = (RecyclerView) rootView.findViewById(R.id.setting_speech_recyclerview);
        recyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        list = new ArrayList<>();
        if (MySharedPreferences.getInstance(getActivity()).emergency_command.load(null) == null) {
            //Add default command
            list.add(new ItemSettingSpeech("Help Me", POLICE));
            list.add(new ItemSettingSpeech("Help Me", POLICE));
            list.add(new ItemSettingSpeech("Help Me", POLICE));
            list.add(new ItemSettingSpeech("Help Me", POLICE));
        } else {
            list = MySharedPreferences.getInstance(getActivity()).emergency_command.load(null);
        }
        adapter = new SettingSpeechAdapter(getActivity(), list, new AwesomeRecyclerAdapter.OnClickListener<ItemSettingSpeech>() {
            @Override
            public void onClick(ItemSettingSpeech item, int position) {

                //Show dialog
                initDialog(position).show();
            }
        });
        recyclerview.setAdapter(adapter);
        return rootView;
    }

    private AlertDialog initDialog(final int position) {
        dialogView = li.inflate(R.layout.dialog_enter_command, null);
        final EditText input = (EditText) dialogView.findViewById(R.id.dialog_et_command);
        final RadioGroup radioGroup = (RadioGroup) dialogView.findViewById(R.id.dialog_rg_emergency_type);
        AlertDialog.Builder builder = DialogBuilder.confirmDialog(getActivity()
                , getResources().getString(R.string.dialog_title_entercommand)
                , null, R.style.AppTheme2_AlertDialogStyle
                , new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String result = input.getText().toString();
                        ItemSettingSpeech item = new ItemSettingSpeech(result, getTypeFromId(radioGroup.getCheckedRadioButtonId()));
                        list.remove(position);
                        list.add(position, item);
                        MySharedPreferences.getInstance(getActivity()).emergency_command.save(list);
                        adapter.notifyItemChanged(position);
                        dialog.dismiss();
                    }
                });
        builder.setView(dialogView);
        radioGroup.check(getIdFromType(list.get(position).getEmergencyType()));
        input.setText(list.get(position).getCommand());
        return builder.create();
    }

    public EmergencyType getTypeFromId(int rbId) {
        switch (rbId) {
            case R.id.dialog_rb_police:
                return POLICE;
            case R.id.dialog_rb_fire:
                return FIRE;
            case R.id.dialog_rb_ambulance:
                return AMBULANCE;
        }
        return POLICE;
    }

    public int getIdFromType(EmergencyType type) {
        switch (type) {
            case POLICE:
                return R.id.dialog_rb_police;
            case FIRE:
                return R.id.dialog_rb_fire;
            case AMBULANCE:
                return R.id.dialog_rb_ambulance;
        }
        return R.id.dialog_rb_police;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            getActivity().finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}