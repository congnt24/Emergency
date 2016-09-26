package com.congnt.emergencyassistance;

import android.Manifest;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.MenuItem;
import android.view.View;

import com.congnt.androidbasecomponent.Awesome.AwesomeActivity;
import com.congnt.androidbasecomponent.Awesome.AwesomeFragment;
import com.congnt.androidbasecomponent.Awesome.AwesomeLayout;
import com.congnt.androidbasecomponent.adapter.ViewPagerAdapter;
import com.congnt.androidbasecomponent.annotation.Activity;
import com.congnt.androidbasecomponent.annotation.NavigationDrawer;
import com.congnt.androidbasecomponent.utility.PermissionUtil;
import com.congnt.emergencyassistance.fragments.MainFragment;
import com.congnt.emergencyassistance.fragments.WalkingFragment;

import java.util.ArrayList;
import java.util.List;


@Activity(transitionAnim = Activity.AnimationType.ANIM_LEFT_TO_RIGHT
        , actionbarType = Activity.ActionBarType.ACTIONBAR_CUSTOM
        , mainLayoutId = R.layout.layout_main
        , enableSearch = true)
@NavigationDrawer
public class MainActivity extends AwesomeActivity implements NavigationView.OnNavigationItemSelectedListener {
    private String[] permission = new String[]{Manifest.permission.ACCESS_FINE_LOCATION
            , Manifest.permission.ACCESS_COARSE_LOCATION
            , Manifest.permission.RECORD_AUDIO
            , Manifest.permission.CAMERA};

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
        //REQUEST PERMISSION
        if (!PermissionUtil.getInstance(this).checkMultiPermission(permission)) {
            PermissionUtil.getInstance(this).requestPermissions(permission);
        }
        //init nav
        getNavigationView().inflateHeaderView(R.layout.nav_main_header);
        getNavigationView().inflateMenu(R.menu.nav_main_menu);
        getNavigationView().setNavigationItemSelectedListener(this);
        //init viewpager
        ViewPager viewPager = (ViewPager) mainView.findViewById(R.id.viewPager);
        TabLayout tabLayout = (TabLayout) mainView.findViewById(R.id.tabLayout);
        List<AwesomeFragment> listFragment = new ArrayList<>();
        listFragment.add(MainFragment.newInstance());
        listFragment.add(WalkingFragment.newInstance());
        listFragment.add(WalkingFragment.newInstance());
        listFragment.add(WalkingFragment.newInstance());
        viewPager.setAdapter(new ViewPagerAdapter<>(getSupportFragmentManager(), listFragment));
        viewPager.setOffscreenPageLimit(listFragment.size()-1);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.getTabAt(0).setIcon(R.drawable.ic_mic);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_search);
        tabLayout.getTabAt(2).setIcon(R.drawable.ic_mic);
        tabLayout.getTabAt(3).setIcon(R.drawable.ic_search);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return true;
    }
}
