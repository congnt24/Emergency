package com.congnt.emergencyassistance.view.fragment;

import android.view.View;
import android.widget.ImageButton;

import com.congnt.androidbasecomponent.Awesome.AwesomeFragment;
import com.congnt.androidbasecomponent.utility.SoundUtil;
import com.congnt.emergencyassistance.R;

public class EmergencySoundFragment extends AwesomeFragment implements View.OnClickListener {
    private boolean isHelpmeMalePlay = false;
    private boolean isHelpmeFemalePlay = false;
    private boolean isSirenPlay = false;
    private ImageButton male, female, siren;

    public static AwesomeFragment newInstance() {
        return new EmergencySoundFragment();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_emergency_sound;
    }

    @Override
    protected void initAll(View rootView) {
        male = (ImageButton) rootView.findViewById(R.id.btn_helpme_male);
        female = (ImageButton) rootView.findViewById(R.id.btn_helpme_female);
        siren = (ImageButton) rootView.findViewById(R.id.btn_siren);
        male.setOnClickListener(this);
        female.setOnClickListener(this);
        siren.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        SoundUtil.getInstance().stop();
        switch (v.getId()) {
            case R.id.btn_helpme_male:
                v.setSelected(!v.isSelected());
                female.setSelected(false);
                siren.setSelected(false);
                if (!isHelpmeMalePlay) {
                    isHelpmeMalePlay = true;
                    isSirenPlay = false;
                    isHelpmeFemalePlay = false;
                    SoundUtil.getInstance().playSoundRepeat(getActivity(), R.raw.help_male);
                } else {
                    isHelpmeMalePlay = false;
                    SoundUtil.getInstance().stop();
                }
                break;
            case R.id.btn_helpme_female:
                v.setSelected(!v.isSelected());
                male.setSelected(false);
                siren.setSelected(false);
                if (!isHelpmeFemalePlay) {
                    isHelpmeFemalePlay = true;
                    isHelpmeMalePlay = false;
                    isSirenPlay = false;
                    SoundUtil.getInstance().playSoundRepeat(getActivity(), R.raw.help_female);
                } else {
                    isHelpmeFemalePlay = false;
                    SoundUtil.getInstance().stop();
                }
                break;
            case R.id.btn_siren:
                v.setSelected(!v.isSelected());
                female.setSelected(false);
                male.setSelected(false);
                if (!isSirenPlay) {
                    isHelpmeFemalePlay = false;
                    isHelpmeMalePlay = false;
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
