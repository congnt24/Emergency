package com.congnt.emergencyassistance.view.activity;


import android.content.DialogInterface;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
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

import java.util.ArrayList;
import java.util.List;

import static com.congnt.emergencyassistance.EmergencyType.AMBULANCE;
import static com.congnt.emergencyassistance.EmergencyType.FIRE;
import static com.congnt.emergencyassistance.EmergencyType.POLICE;

public class SettingsActivity extends AppCompatPreferenceActivity {

    private static Preference.OnPreferenceChangeListener sBindPreferenceSummaryToValueListener = new Preference.OnPreferenceChangeListener() {
        @Override
        public boolean onPreferenceChange(Preference preference, Object value) {
            String stringValue = value.toString();

            if (preference instanceof ListPreference) {
                // For list preferences, look up the correct display value in
                // the preference's 'entries' list.
                ListPreference listPreference = (ListPreference) preference;
                int index = listPreference.findIndexOfValue(stringValue);

                // Set the summary to reflect the new value.
                preference.setSummary(
                        index >= 0
                                ? listPreference.getEntries()[index]
                                : null);

            } else {
                // For all other preferences, set the summary to the value's
                // simple string representation.
                preference.setSummary(stringValue);
            }
            return true;
        }
    };

    private static void bindPreferenceSummaryToValue(Preference preference) {
        // Set the listener to watch for value changes.
        preference.setOnPreferenceChangeListener(sBindPreferenceSummaryToValueListener);

        // Trigger the listener immediately with the preference's
        // current value.
        sBindPreferenceSummaryToValueListener.onPreferenceChange(preference,
                PreferenceManager
                        .getDefaultSharedPreferences(preference.getContext())
                        .getString(preference.getKey(), ""));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupActionBar();
    }

    private void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            // Show the Up button in the action bar.
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    protected boolean isValidFragment(String fragmentName) {
        return PreferenceFragment.class.getName().equals(fragmentName)
                || SpeechPreferenceFragment.class.getName().equals(fragmentName);
    }

    @Override
    public void onBuildHeaders(List<Header> target) {
        super.onBuildHeaders(target);
        loadHeadersFromResource(R.xml.pref_headers, target);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //INNER CLASS

    public static class SpeechPreferenceFragment extends PreferenceFragment{
        private SettingSpeechAdapter adapter;
        private List<ItemSettingSpeech> list;
        private LayoutInflater li;
        private View dialogView;

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
//            addPreferencesFromResource(R.xml.pref_general);
//            setHasOptionsMenu(true);
            // Bind the summaries of EditText/List/Dialog/Ringtone preferences
            // to their values. When their values change, their summaries are
            // updated to reflect the new value, per the Android Design
            // guidelines.
//            bindPreferenceSummaryToValue(findPreference("example_text"));
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
                list.add(new ItemSettingSpeech("Help Me", EmergencyType.POLICE));
                list.add(new ItemSettingSpeech("Help Me", EmergencyType.POLICE));
                list.add(new ItemSettingSpeech("Help Me", EmergencyType.POLICE));
                list.add(new ItemSettingSpeech("Help Me", EmergencyType.POLICE));
            }else{
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

        private AlertDialog initDialog(final int position){
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

        public EmergencyType getTypeFromId(int rbId){
            switch (rbId){
                case R.id.dialog_rb_police:
                    return POLICE;
                case R.id.dialog_rb_fire:
                    return FIRE;
                case R.id.dialog_rb_ambulance:
                    return AMBULANCE;
            }
            return POLICE;
        }

        public int getIdFromType(EmergencyType type){
            switch (type){
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

}
