package com.congnt.emergencyassistance.view.activity;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.congnt.androidbasecomponent.Awesome.AwesomeActivity;
import com.congnt.androidbasecomponent.Awesome.AwesomeFragment;
import com.congnt.androidbasecomponent.Awesome.AwesomeLayout;
import com.congnt.androidbasecomponent.adapter.ViewPagerAdapter;
import com.congnt.androidbasecomponent.annotation.Activity;
import com.congnt.androidbasecomponent.annotation.NavigateUp;
import com.congnt.emergencyassistance.MainActionBar;
import com.congnt.emergencyassistance.MySharedPreferences;
import com.congnt.emergencyassistance.R;
import com.congnt.emergencyassistance.view.fragment.TutorialFragment;

import java.util.ArrayList;
import java.util.List;

import me.relex.circleindicator.CircleIndicator;


@Activity(transitionAnim = Activity.AnimationType.ANIM_LEFT_TO_RIGHT
        , actionbarType = Activity.ActionBarType.ACTIONBAR_NONE
        , mainLayoutId = R.layout.tutorial)
@NavigateUp
public class TutorialActivity extends AwesomeActivity {

    private ViewPager viewPager;

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
        setupViewPager(mainView);
    }

    public void setupViewPager(View root) {
        viewPager = (ViewPager) root.findViewById(R.id.viewpager);
        CircleIndicator indicator = (CircleIndicator) root.findViewById(R.id.indicator);
        List<AwesomeFragment> listFragment = new ArrayList<>();
        listFragment.add(TutorialFragment.newInstance(R.drawable.image_carcrash, R.string.tutorial_title_4, R.string.tutorial_desc_4, false));
        listFragment.add(TutorialFragment.newInstance(R.drawable.image_map, R.string.tutorial_title_1, R.string.tutorial_desc_1, false));
        listFragment.add(TutorialFragment.newInstance(R.drawable.image_ambulance, R.string.tutorial_title_2, R.string.tutorial_desc_2, false));
        listFragment.add(TutorialFragment.newInstance(R.drawable.image_app, R.string.tutorial_title_3, R.string.tutorial_desc_3, true));
        ViewPagerAdapter<AwesomeFragment> adapter = new ViewPagerAdapter<>(getSupportFragmentManager(), listFragment);
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(listFragment.size() - 1);
        indicator.setViewPager(viewPager);
        adapter.registerDataSetObserver(indicator.getDataSetObserver());
    }

    public void switchFragment() {
        int currentPosition = viewPager.getCurrentItem();
        if (currentPosition < viewPager.getAdapter().getCount() - 1) {
            viewPager.setCurrentItem(currentPosition + 1);
        } else {
            MySharedPreferences.getInstance(this).isFirstTime.save(false);
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
    }

}
