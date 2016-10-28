package com.congnt.emergencyassistance.view.activity;

import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.TextView;

import com.congnt.androidbasecomponent.Awesome.AwesomeActivity;
import com.congnt.androidbasecomponent.Awesome.AwesomeLayout;
import com.congnt.androidbasecomponent.annotation.Activity;
import com.congnt.androidbasecomponent.annotation.NavigateUp;
import com.congnt.emergencyassistance.MainActionBar;
import com.congnt.emergencyassistance.MySharedPreferences;
import com.congnt.emergencyassistance.R;
import com.congnt.emergencyassistance.entity.ItemCountryEmergencyNumber;
import com.congnt.emergencyassistance.util.CountryUtil;
import com.congnt.emergencyassistance.widget.ContactWidgetProvider;
import com.sithagi.countrycodepicker.CountryPicker;
import com.sithagi.countrycodepicker.CountryPickerListener;

import org.greenrobot.eventbus.EventBus;

@Activity(mainLayoutId = R.layout.activity_change_country,
        actionbarType = Activity.ActionBarType.ACTIONBAR_CUSTOM)
@NavigateUp
public class ChangeCountryActivity extends AwesomeActivity implements CountryPickerListener {
    public TextView tv_police, tv_fire, tv_ambulance, tv_note, tv_current_country;

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
        tv_current_country = (TextView) mainView.findViewById(R.id.tv_current_country);
        ItemCountryEmergencyNumber country = MySharedPreferences.getInstance(this).countryNumber.load(null);
        if (country != null) {
            updateData(country);
        }
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
        } else {
            country = new ItemCountryEmergencyNumber(code, name, "911", "911", "911", "");
        }
        updateData(country);
        MySharedPreferences.getInstance(this).countryNumber.save(country);
        EventBus.getDefault().post(country);
    }

    public void updateData(ItemCountryEmergencyNumber country) {
        tv_police.setText(country.police);
        tv_fire.setText(country.fire);
        tv_ambulance.setText(country.ambulance);
        tv_note.setText(country.notes);
        tv_current_country.setText(getString(R.string.current_country) + " " + country.countryName);
        updateWidget();
    }

    public void updateWidget() {
        Intent intent = new Intent(this, ContactWidgetProvider.class);
        intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        int[] ids = {R.xml.widget_contact};
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);
        sendBroadcast(intent);
    }
}
