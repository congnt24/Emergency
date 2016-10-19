package com.congnt.emergencyassistance.activities;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.TextView;

import com.congnt.androidbasecomponent.Awesome.AwesomeActivity;
import com.congnt.androidbasecomponent.Awesome.AwesomeLayout;
import com.congnt.androidbasecomponent.annotation.Activity;
import com.congnt.androidbasecomponent.annotation.NavigateUp;
import com.congnt.emergencyassistance.MainActionBar;
import com.congnt.emergencyassistance.R;
import com.congnt.emergencyassistance.entity.ItemCountryEmergencyNumber;
import com.congnt.emergencyassistance.util.CountryUtil;
import com.sithagi.countrycodepicker.CountryPicker;
import com.sithagi.countrycodepicker.CountryPickerListener;

@Activity(mainLayoutId = R.layout.activity_change_country,
        actionbarType = Activity.ActionBarType.ACTIONBAR_CUSTOM)
@NavigateUp
public class ChangeCountryActivity extends AwesomeActivity implements CountryPickerListener {
    public TextView tv_police, tv_fire, tv_ambulance, tv_note;

    @Override
    protected int getLayoutId() {
        return 0;
    }

    @Override
    protected AwesomeLayout getCustomActionBar() {
        return new MainActionBar(this);
    }

    @Override
    protected void initialize(View mainView) {
        tv_police = (TextView) mainView.findViewById(R.id.tv_police);
        tv_fire = (TextView) mainView.findViewById(R.id.tv_fire);
        tv_ambulance = (TextView) mainView.findViewById(R.id.tv_ambulance);
        tv_note = (TextView) mainView.findViewById(R.id.tv_note);

        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        CountryPicker picker = new CountryPicker();
        picker.setListener(this);
        transaction.replace(R.id.countrypicker_container, picker);
        transaction.commit();
        CountryUtil.getInstance(this);
    }

    @Override
    public void onSelectCountry(String name, String code, String dialCode) {
        ItemCountryEmergencyNumber country = CountryUtil.getInstance(this).getItemCountryByCode(code);
        if (country != null) {
            tv_police.setText(country.police);
            tv_fire.setText(country.fire);
            tv_ambulance.setText(country.ambulance);
            tv_note.setText(country.notes);
        } else {
            tv_police.setText("911");
            tv_fire.setText("911");
            tv_ambulance.setText("911");
            tv_note.setText("");
        }
    }
}
