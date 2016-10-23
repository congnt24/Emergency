package com.congnt.emergencyassistance.view.activity;

import android.Manifest;
import android.content.Intent;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.SwitchCompat;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.congnt.androidbasecomponent.Awesome.AwesomeActivity;
import com.congnt.androidbasecomponent.Awesome.AwesomeFragment;
import com.congnt.androidbasecomponent.Awesome.AwesomeLayout;
import com.congnt.androidbasecomponent.adapter.ViewPagerAdapter;
import com.congnt.androidbasecomponent.annotation.Activity;
import com.congnt.androidbasecomponent.annotation.NavigationDrawer;
import com.congnt.androidbasecomponent.utility.AndroidUtil;
import com.congnt.androidbasecomponent.utility.LocationUtil;
import com.congnt.androidbasecomponent.utility.PackageUtil;
import com.congnt.androidbasecomponent.utility.PermissionUtil;
import com.congnt.androidbasecomponent.view.utility.TabLayoutUtil;
import com.congnt.emergencyassistance.MainActionBar;
import com.congnt.emergencyassistance.MySharedPreferences;
import com.congnt.emergencyassistance.R;
import com.congnt.emergencyassistance.entity.ItemCountryEmergencyNumber;
import com.congnt.emergencyassistance.entity.firebase.User;
import com.congnt.emergencyassistance.services.SpeechRecognitionService;
import com.congnt.emergencyassistance.util.CountryUtil;
import com.congnt.emergencyassistance.view.fragment.EmergencySoundFragment;
import com.congnt.emergencyassistance.view.fragment.MainFragment;
import com.congnt.emergencyassistance.view.fragment.NearByFragment;
import com.congnt.emergencyassistance.view.fragment.WalkingFragment;
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
    private static final int REQUEST_PROFILE = 1;
    private static final int REQUEST_LOGIN = 2;
    public ItemCountryEmergencyNumber countrynumber;
    private String[] permission = new String[]{Manifest.permission.ACCESS_FINE_LOCATION
            , Manifest.permission.ACCESS_COARSE_LOCATION
            , Manifest.permission.READ_CONTACTS
            , Manifest.permission.RECORD_AUDIO
            , Manifest.permission.CAMERA
            , Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    private DrawerLayout mDrawer;
    private ImageView mImgHeader;
    private TextView mTvHeader;
    private SwitchCompat switchCompat;
    private boolean isLoadUserProfile = true;

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
        //Require gps, speech2text
        if (!PackageUtil.isInstalled(this, PackageUtil.SPEECH_TO_TEXT)) {
            PackageUtil.openPlayStore(this, PackageUtil.SPEECH_TO_TEXT);
        }
        if (!LocationUtil.isGpsEnable(this)) {
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(intent);
        }
        //Init Service
        if (!AndroidUtil.isServiceRunning(this, SpeechRecognitionService.class)) {
            MySharedPreferences.getInstance(this).isListening.save(false);
        }
        Intent service = new Intent(this, SpeechRecognitionService.class);
        startService(service);
        //setup country
        setupCountry();
        //init nav
        // Find our drawer view
        setupNavigationView();
        //init viewpager
        setupViewPager(mainView);
    }

    private void setupCountry() {
        countrynumber = MySharedPreferences.getInstance(this).countryNumber.load(null);
        if (countrynumber == null) {
            String countryCode = AndroidUtil.getCountryCode();
            countrynumber = CountryUtil.getInstance(this).getItemCountryByCode(countryCode);
            MySharedPreferences.getInstance(this).countryNumber.save(countrynumber);
        }
    }

    public void setupNavigationView() {
        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        View headerView = getNavigationView().inflateHeaderView(R.layout.nav_main_header);
        mImgHeader = (ImageView) headerView.findViewById(R.id.imageView);
        mTvHeader = (TextView) headerView.findViewById(R.id.tvUser);
        //Bind data
        mImgHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(MainActivity.this, LoginActivity.class), REQUEST_LOGIN);
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
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_profile:
                startActivityForResult(new Intent(this, ProfileActivity.class), REQUEST_PROFILE);
                break;
            case R.id.nav_emergency_contact:
                startActivity(new Intent(this, EmergencyContactActivity.class));
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem item = menu.findItem(R.id.action_switch);
        item.setActionView(R.layout.menu_item_switch);
        switchCompat = (SwitchCompat) item.getActionView().findViewById(R.id.switch_compat);
        switchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                MySharedPreferences.getInstance(MainActivity.this).shareLocationState.save(isChecked);
            }
        });
        switchCompat.setChecked(MySharedPreferences.getInstance(this).shareLocationState.load(false));
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_PROFILE:
                    bindUserData();
                    break;
                case REQUEST_LOGIN:
                    bindUserData();
                    break;
            }
        }
    }

    @Override
    protected void onResume() {
        bindUserData();
        super.onResume();
    }

    public void bindUserData() {
        //Bind user
        FirebaseUser fuser = FirebaseAuth.getInstance().getCurrentUser();
        User user = MySharedPreferences.getInstance(this).userProfile.load(null);
        if (fuser != null) {
            if (user == null) {
                user = new User();
                user.setName(fuser.getDisplayName());
                MySharedPreferences.getInstance(this).userProfile.save(user);
                mTvHeader.setText(fuser.getDisplayName());
            } else {
                mTvHeader.setText(user.getName());
            }
            Picasso.with(this).load(fuser.getPhotoUrl()).into(mImgHeader);
        } else {
            if (user != null) {
                mTvHeader.setText(fuser.getDisplayName());
            }
        }
    }

}
