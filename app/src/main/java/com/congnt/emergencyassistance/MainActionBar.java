package com.congnt.emergencyassistance;

import android.content.Context;

import com.congnt.androidbasecomponent.Awesome.AwesomeActionBar;
import com.congnt.androidbasecomponent.annotation.ActionBar;

/**
 * Created by congnt24 on 25/09/2016.
 */
@ActionBar(actionbarType = ActionBar.ActionbarType.DEFAULT_SEARCH)
public class MainActionBar extends AwesomeActionBar {
    public MainActionBar(Context context) {
        super(context);
    }

    @Override
    protected void initialize() {

    }
}
