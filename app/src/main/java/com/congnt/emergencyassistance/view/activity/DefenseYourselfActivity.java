package com.congnt.emergencyassistance.view.activity;

import android.view.View;

import com.congnt.androidbasecomponent.Awesome.AwesomeActivity;
import com.congnt.androidbasecomponent.Awesome.AwesomeLayout;
import com.congnt.androidbasecomponent.annotation.Activity;
import com.congnt.androidbasecomponent.annotation.NavigateUp;
import com.congnt.emergencyassistance.MainActionBar;
import com.congnt.emergencyassistance.R;

@Activity(mainLayoutId = R.layout.activity_profile,
        actionbarType = com.congnt.androidbasecomponent.annotation.Activity.ActionBarType.ACTIONBAR_CUSTOM)
@NavigateUp
public class DefenseYourselfActivity extends AwesomeActivity {

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

    }
}
