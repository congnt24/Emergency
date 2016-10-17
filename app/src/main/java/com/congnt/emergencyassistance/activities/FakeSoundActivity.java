package com.congnt.emergencyassistance.activities;

import android.view.View;

import com.congnt.androidbasecomponent.Awesome.AwesomeActivity;
import com.congnt.androidbasecomponent.Awesome.AwesomeLayout;
import com.congnt.androidbasecomponent.annotation.Activity;
import com.congnt.androidbasecomponent.utility.SoundUtil;
import com.congnt.emergencyassistance.MainActionBar;
import com.congnt.emergencyassistance.R;

@Activity(mainLayoutId = R.layout.activity_fake_sound,
        actionbarType = Activity.ActionBarType.ACTIONBAR_CUSTOM)
@com.congnt.androidbasecomponent.annotation.NavigateUp
public class FakeSoundActivity extends AwesomeActivity implements View.OnClickListener {

    private boolean isHelpmePlay = false;
    private boolean isSirenPlay = false;
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
        mainView.findViewById(R.id.btn_helpme).setOnClickListener(this);
        mainView.findViewById(R.id.btn_siren).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        SoundUtil.getInstance().stop();
        switch (v.getId()){
            case R.id.btn_helpme:
                if (!isHelpmePlay){
                    isHelpmePlay = true;
                    SoundUtil.getInstance().playSoundRepeat(this, R.raw.police_siren);
                }else{
                    isHelpmePlay = false;
                    SoundUtil.getInstance().stop();
                }
                break;
            case R.id.btn_siren:
                if (!isSirenPlay){
                    isSirenPlay = true;
                    SoundUtil.getInstance().playSoundRepeat(this, R.raw.police_siren);
                }else{
                    isSirenPlay = false;
                    SoundUtil.getInstance().stop();
                }
                break;
        }
    }
}
