package com.congnt.emergencyassistance.view.activity;


import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.SwitchPreference;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;

import com.congnt.emergencyassistance.R;
import com.congnt.emergencyassistance.view.fragment.SettingSpeechFragment;

import java.util.List;

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

            } else if (preference instanceof SwitchPreference) {

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

    private static void bindPreferenceSwitchToValue(Preference preference) {
        // Set the listener to watch for value changes.
        preference.setOnPreferenceChangeListener(sBindPreferenceSummaryToValueListener);

        // Trigger the listener immediately with the preference's
        // current value.
        sBindPreferenceSummaryToValueListener.onPreferenceChange(preference,
                PreferenceManager
                        .getDefaultSharedPreferences(preference.getContext())
                        .getBoolean(preference.getKey(), false));
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
                || SettingSpeechFragment.class.getName().equals(fragmentName)
                || SettingEmergencyFragment.class.getName().equals(fragmentName)
                || SettingGeneralFragment.class.getName().equals(fragmentName)
                || SettingAutoFragment.class.getName().equals(fragmentName);
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

    public static class SettingGeneralFragment extends PreferenceFragment {

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_general);
            bindPreferenceSummaryToValue(findPreference("setting_auto_detect_countdown_time"));
            bindPreferenceSwitchToValue(findPreference("setting_enable_volume"));
//            bindPreferenceSwitchToValue(findPreference("setting_call_to"));
            bindPreferenceSummaryToValue(findPreference("setting_acce_threshold"));
            bindPreferenceSwitchToValue(findPreference("setting_mute_speech"));
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

    public static class SettingEmergencyFragment extends PreferenceFragment {

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_emergency);
            bindPreferenceSwitchToValue(findPreference("setting_take_picture"));
            bindPreferenceSwitchToValue(findPreference("setting_record_audio"));
            bindPreferenceSwitchToValue(findPreference("setting_contact_call"));
            bindPreferenceSwitchToValue(findPreference("setting_contact_sms"));
            bindPreferenceSummaryToValue(findPreference("setting_countdown_time"));
            bindPreferenceSummaryToValue(findPreference("setting_record_time"));
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

    public static class SettingAutoFragment extends PreferenceFragment {

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_auto_detect);
            bindPreferenceSummaryToValue(findPreference("setting_auto_detect_countdown_time"));
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
