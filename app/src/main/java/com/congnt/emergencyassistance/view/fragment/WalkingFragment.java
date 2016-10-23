package com.congnt.emergencyassistance.view.fragment;

import android.view.View;

import com.congnt.androidbasecomponent.Awesome.AwesomeFragment;
import com.congnt.emergencyassistance.R;

/**
 * Created by congnt24 on 25/09/2016.
 */

public class WalkingFragment extends AwesomeFragment {
    public static AwesomeFragment newInstance() {
        return new WalkingFragment();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_walk;
    }

    @Override
    protected void initAll(View rootView) {

    }
}
