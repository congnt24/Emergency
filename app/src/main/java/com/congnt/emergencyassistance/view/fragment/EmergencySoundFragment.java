package com.congnt.emergencyassistance.view.fragment;

import android.view.View;

import com.congnt.androidbasecomponent.Awesome.AwesomeFragment;
import com.congnt.androidbasecomponent.utility.SoundUtil;
import com.congnt.emergencyassistance.R;

public class EmergencySoundFragment extends AwesomeFragment implements View.OnClickListener {
    private boolean isHelpmePlay = false;
    private boolean isSirenPlay = false;

    public static AwesomeFragment newInstance() {
        return new EmergencySoundFragment();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_emergency_sound;
    }

    @Override
    protected void initAll(View rootView) {
        rootView.findViewById(R.id.btn_helpme_male).setOnClickListener(this);
        rootView.findViewById(R.id.btn_helpme_female).setOnClickListener(this);
        rootView.findViewById(R.id.btn_siren).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        SoundUtil.getInstance().stop();
        switch (v.getId()) {
            case R.id.btn_helpme_male:
                if (!isHelpmePlay) {
                    isHelpmePlay = true;
                    SoundUtil.getInstance().playSoundRepeat(getActivity(), R.raw.help_male);
                } else {
                    isHelpmePlay = false;
                    SoundUtil.getInstance().stop();
                }
                break;
            case R.id.btn_helpme_female:
                if (!isHelpmePlay) {
                    isHelpmePlay = true;
                    SoundUtil.getInstance().playSoundRepeat(getActivity(), R.raw.help_female);
                } else {
                    isHelpmePlay = false;
                    SoundUtil.getInstance().stop();
                }
                break;
            case R.id.btn_siren:
                if (!isSirenPlay) {
                    isSirenPlay = true;
                    SoundUtil.getInstance().playSoundRepeat(getActivity(), R.raw.police_siren);
                } else {
                    isSirenPlay = false;
                    SoundUtil.getInstance().stop();
                }
                break;
        }
    }
}