package com.congnt.emergencyassistance.activities;

import android.Manifest;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.congnt.androidbasecomponent.Awesome.AwesomeActivity;
import com.congnt.androidbasecomponent.Awesome.AwesomeFragment;
import com.congnt.androidbasecomponent.Awesome.AwesomeLayout;
import com.congnt.androidbasecomponent.adapter.ViewPagerAdapter;
import com.congnt.androidbasecomponent.annotation.Activity;
import com.congnt.androidbasecomponent.annotation.NavigationDrawer;
import com.congnt.androidbasecomponent.utility.AndroidUtil;
import com.congnt.androidbasecomponent.utility.PermissionUtil;
import com.congnt.androidbasecomponent.view.utility.TabLayoutUtil;
import com.congnt.emergencyassistance.MainActionBar;
import com.congnt.emergencyassistance.MySharedPreferences;
import com.congnt.emergencyassistance.R;
import com.congnt.emergencyassistance.fragments.EmergencySoundFragment;
import com.congnt.emergencyassistance.fragments.MainFragment;
import com.congnt.emergencyassistance.fragments.NearByFragment;
import com.congnt.emergencyassistance.fragments.WalkingFragment;
import com.congnt.emergencyassistance.services.SpeechRecognitionService;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

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
    private DrawerLayout mDrawer;
    private ImageView mImgHeader;
    private TextView mTvHeader;

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
        // Find our drawer view
        setupNavigationView();
        //init viewpager
        setupViewPager(mainView);
    }

    public void setupNavigationView(){
        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        View headerView = getNavigationView().inflateHeaderView(R.layout.nav_main_header);
        mImgHeader = (ImageView) headerView.findViewById(R.id.imageView);
        mTvHeader = (TextView) headerView.findViewById(R.id.tvUser);
        //Bind data
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user !=null){
            Picasso.with(this).load(user.getPhotoUrl()).into(mImgHeader);
            mTvHeader.setText(user.getDisplayName());
        }
        mImgHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                mDrawer.closeDrawer(Gravity.LEFT);
            }
        });
        getNavigationView().inflateMenu(R.menu.nav_main_menu);
        getNavigationView().setNavigationItemSelectedListener(this);
    }

    public void setupViewPager(View root) {
        ViewPager viewPager = (ViewPager) root.findViewById(R.id.viewPager);
        List<AwesomeFragment> listFragment = new ArrayList<>();
        listFragment.add(MainFragment.newInstance());
        listFragment.add(EmergencySoundFragment.newInstance());
        listFragment.add(NearByFragment.newInstance());
        listFragment.add(WalkingFragment.newInstance());
        viewPager.setAdapter(new ViewPagerAdapter<>(getSupportFragmentManager(), listFragment));
        viewPager.setOffscreenPageLimit(listFragment.size() - 1);
        //Setup TabLayout
        setupTabLayout(root, viewPager);
    }

    public void setupTabLayout(View root, ViewPager viewPager) {
        TabLayout tabLayout = (TabLayout) root.findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);
        TabLayoutUtil.setCustomLayouts(tabLayout, R.layout.item_tab_tablayout);
        TabLayoutUtil.setIcons(tabLayout, R.drawable.ic_home, R.drawable.ic_report_problem, R.drawable.ic_place, R.drawable.ic_directions_run);
//        TabLayoutUtil.setTexts(tabLayout, "Home", "Setting", "Near By", "Walking");
        //Best Solution
        TabLayoutUtil.setColorSelectorIcons(tabLayout, R.color.tab_icon);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!AndroidUtil.isServiceRunning(this, SpeechRecognitionService.class)){
            MySharedPreferences.getInstance(this).isListening.save(false);
        }
        Intent service = new Intent(this, SpeechRecognitionService.class);
        startService(service);

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.nav_profile:
                startActivity(new Intent(this, ProfileActivity.class));
                break;
            case R.id.nav_defense_yourself:
                startActivity(new Intent(this, DefenseYourselfActivity.class));
                break;
            case R.id.nav_change_country:
                startActivity(new Intent(this, ChangeCountryActivity.class));
                break;
            case R.id.nav_setting:
                startActivity(new Intent(this, SettingsActivity.class));
                break;
        }

        mDrawer.closeDrawer(Gravity.LEFT);
        return true;
    }
}
