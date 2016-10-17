package com.congnt.emergencyassistance.fragments;

import android.view.View;

import com.congnt.androidbasecomponent.Awesome.AwesomeFragment;
import com.congnt.emergencyassistance.R;

/**
 * Created by congnt24 on 25/09/2016.
 */

public class SocialNetworkFragment extends AwesomeFragment {
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_social_network;
    }

    @Override
    protected void initAll(View rootView) {

    }

    public static AwesomeFragment newInstance() {
        return new SocialNetworkFragment();
    }
}
