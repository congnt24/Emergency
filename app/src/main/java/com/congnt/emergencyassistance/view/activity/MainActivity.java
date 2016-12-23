package com.congnt.emergencyassistance.view.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.SwitchCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.congnt.androidbasecomponent.Awesome.AwesomeActivity;
import com.congnt.androidbasecomponent.Awesome.AwesomeFragment;
import com.congnt.androidbasecomponent.Awesome.AwesomeLayout;
import com.congnt.androidbasecomponent.adapter.ViewPagerAdapter;
import com.congnt.androidbasecomponent.annotation.Activity;
import com.congnt.androidbasecomponent.annotation.NavigationDrawer;
import com.congnt.androidbasecomponent.utility.AndroidUtil;
import com.congnt.androidbasecomponent.utility.CommunicationUtil;
import com.congnt.androidbasecomponent.utility.IntentUtil;
import com.congnt.androidbasecomponent.utility.LocationUtil;
import com.congnt.androidbasecomponent.utility.NetworkUtil;
import com.congnt.androidbasecomponent.utility.PackageUtil;
import com.congnt.androidbasecomponent.view.dialog.DialogBuilder;
import com.congnt.androidbasecomponent.view.utility.TabLayoutUtil;
import com.congnt.emergencyassistance.MainActionBar;
import com.congnt.emergencyassistance.MainApplication;
import com.congnt.emergencyassistance.MySharedPreferences;
import com.congnt.emergencyassistance.R;
import com.congnt.emergencyassistance.entity.EventBusEntity.EBE_StartDetectingAccident;
import com.congnt.emergencyassistance.entity.ItemCountryEmergencyNumber;
import com.congnt.emergencyassistance.entity.ItemSettingSpeech;
import com.congnt.emergencyassistance.entity.SettingSpeech;
import com.congnt.emergencyassistance.entity.firebase.User;
import com.congnt.emergencyassistance.services.DetectingAccidentServiceNew;
import com.congnt.emergencyassistance.services.LocationService;
import com.congnt.emergencyassistance.services.SpeechRecognitionServiceNew;
import com.congnt.emergencyassistance.util.CountryUtil;
import com.congnt.emergencyassistance.view.fragment.EmergencySoundFragment;
import com.congnt.emergencyassistance.view.fragment.MainFragment;
import com.congnt.emergencyassistance.view.fragment.NearByFragment;
import com.etiennelawlor.imagegallery.library.activities.FullScreenImageGalleryActivity;
import com.etiennelawlor.imagegallery.library.activities.ImageGalleryActivity;
import com.etiennelawlor.imagegallery.library.adapters.FullScreenImageGalleryAdapter;
import com.etiennelawlor.imagegallery.library.adapters.ImageGalleryAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.congnt.emergencyassistance.EmergencyType.AMBULANCE;
import static com.congnt.emergencyassistance.EmergencyType.FIRE;
import static com.congnt.emergencyassistance.EmergencyType.POLICE;


@Activity(transitionAnim = Activity.AnimationType.ANIM_LEFT_TO_RIGHT
        , actionbarType = Activity.ActionBarType.ACTIONBAR_CUSTOM
        , mainLayoutId = R.layout.layout_main
        , enableSearch = true)
@NavigationDrawer
public class MainActivity extends AwesomeActivity implements NavigationView.OnNavigationItemSelectedListener {
    public static final int REQUEST_LOGIN_FOR_SHARE_LOCATION = 3;
    private static final String TAG = "MainActivity";
    private static final int REQUEST_PROFILE = 1;
    private static final int REQUEST_LOGIN = 2;
    public ItemCountryEmergencyNumber countrynumber;
    public boolean isLogged;
    public MainApplication mainApplication;
    private DrawerLayout mDrawer;
    private ImageView mImgHeader;
    private TextView mTvHeader;
    private SwitchCompat switchCompat;
    private boolean isLoadUserProfile = true;
    private Intent service;
    private MainFragment mainFragment;
    private Intent detectingAccidentService;

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
        mainApplication = (MainApplication) getApplication();
        service = new Intent(this, SpeechRecognitionServiceNew.class);

        initRequire();

        //update locale
        countrynumber = MySharedPreferences.getInstance(this).countryNumber.load(null);
        if (countrynumber != null) {
            AndroidUtil.updateLocaleByCountry(this, countrynumber.countryCode);
            Log.d(TAG, "Mainactivity: "+countrynumber.countryCode +" - "+ Locale.getDefault().getCountry());
        }

        //Init Service
        if (!AndroidUtil.isServiceRunning(this, SpeechRecognitionServiceNew.class)) {
            MySharedPreferences.getInstance(this).isListening.save(false);
            startService(service);
        }
        //Detect service
        startService(new Intent(MainActivity.this, LocationService.class));
        detectingAccidentService = new Intent(MainActivity.this, DetectingAccidentServiceNew.class);
//        startService(new Intent(MainActivity.this, DetectingAccidentService.class));

        //init nav
        // Find our drawer view
        setupNavigationView();
        //init viewpager
        setupViewPager(mainView);
        initManageGallery();
    }

    private void initRequire() {
        //Requite network
        if (!NetworkUtil.isNetworkConnected(this)) {
            DialogBuilder.confirmDialog(this, getString(R.string.require_network), getString(R.string.require_network_message)
                    , R.style.AppTheme2_AlertDialogStyle, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            IntentUtil.requestNetwork(MainActivity.this);
                        }
                    }).create().show();
        }
        //Require gps, speech2text
        if (!PackageUtil.isInstalled(this, PackageUtil.GOOGLE_APP)) {
            DialogBuilder.confirmDialog(this, getString(R.string.require_google_app), getString(R.string.require_google_app_message)
                    , R.style.AppTheme2_AlertDialogStyle, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            PackageUtil.openPlayStore(MainActivity.this, PackageUtil.GOOGLE_APP);
                        }
                    }).create().show();
        }
        //require gps
        if (!LocationUtil.isGpsEnable(this)) {
            DialogBuilder.confirmDialog(this, getString(R.string.enable_gps), getString(R.string.enable_gps_message)
                    , R.style.AppTheme2_AlertDialogStyle, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            startActivity(intent);
                        }
                    }).create().show();
        }
    }

    public boolean initNetwork() {
        //Requite network
        if (!NetworkUtil.isNetworkConnected(this)) {
            Toast.makeText(this, "Please connect your device with internet.", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    public void initSpeechCommand(String countryCode) {
        if (MySharedPreferences.getInstance(this).emergency_command.load(null) == null) {
            //get default command by country
            //init json
            SettingSpeech setting = CountryUtil.getSettingSpeechByCountry(this, countryCode);
            if (setting == null) {
                setting = CountryUtil.getSettingSpeechByCountry(this, "US");
            }
            if (setting != null) {
                List<ItemSettingSpeech> list = new ArrayList<>();
                Log.d(TAG, "initSpeechCommand: " + setting.fire.size() + " " + setting.ambulance.size() + " - " + countryCode);
                for (int i = 0; i < setting.fire.size(); i++) {
                    list.add(new ItemSettingSpeech(setting.fire.get(i).command, FIRE));
                }
                for (int i = 0; i < setting.ambulance.size(); i++) {
                    list.add(new ItemSettingSpeech(setting.ambulance.get(i).command, AMBULANCE));
                }
                for (int i = 0; i < setting.police.size(); i++) {
                    list.add(new ItemSettingSpeech(setting.police.get(i).command, POLICE));
                }
                MySharedPreferences.getInstance(this).emergency_command.save(list);
            }

        }
    }

    private void setupNavigationView() {
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

    private void setupViewPager(View root) {
        ViewPager viewPager = (ViewPager) root.findViewById(R.id.viewPager);
        List<AwesomeFragment> listFragment = new ArrayList<>();
        mainFragment = (MainFragment) MainFragment.newInstance();
        listFragment.add(mainFragment);
        listFragment.add(EmergencySoundFragment.newInstance());
        listFragment.add(NearByFragment.newInstance());
//        listFragment.add(WalkingFragment.newInstance());
        viewPager.setAdapter(new ViewPagerAdapter<>(getSupportFragmentManager(), listFragment));
        viewPager.setOffscreenPageLimit(listFragment.size() - 1);
        //Setup TabLayout
        setupTabLayout(root, viewPager);
    }

    private void setupTabLayout(View root, ViewPager viewPager) {
        TabLayout tabLayout = (TabLayout) root.findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);
        TabLayoutUtil.setCustomLayouts(tabLayout, R.layout.item_tab_tablayout);
        TabLayoutUtil.setIcons(tabLayout, R.drawable.ic_home, R.drawable.ic_surround_sound_black_36dp, R.drawable.ic_location_on, R.drawable.ic_directions_run);
//        TabLayoutUtil.setTexts(tabLayout, "Home", "Setting", "Near By", "Walking");
        //Best Solution
        TabLayoutUtil.setColorSelectorIcons(tabLayout, R.color.tab_icon);
    }

    private void initManageGallery() {
        ImageGalleryActivity.setImageThumbnailLoader(new ImageGalleryAdapter.ImageThumbnailLoader() {
            @Override
            public void loadImageThumbnail(ImageView iv, String imageUrl, int dimension) {
                if (!TextUtils.isEmpty(imageUrl)) {
                    Picasso.with(iv.getContext()).load(new File(imageUrl))
                            .resize(dimension, dimension).centerCrop().into(iv);
                } else {
                    iv.setImageDrawable(null);
                }
            }
        });

        FullScreenImageGalleryActivity.setFullScreenImageLoader(new FullScreenImageGalleryAdapter.FullScreenImageLoader() {
            @Override
            public void loadFullScreenImage(ImageView iv, String imageUrl, int width, final LinearLayout bglinearLayout) {
                if (!TextUtils.isEmpty(imageUrl)) {
                    Picasso.with(iv.getContext()).load(new File(imageUrl))
                            .resize(width, 0).into(iv/*, new Callback() {
                        @Override
                        public void onSuccess() {
                            bglinearLayout.setBackgroundColor(Color.WHITE);
                        }

                        @Override
                        public void onError() {

                        }
                    }*/);
                } else {
                    iv.setImageDrawable(null);
                }
            }
        });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        mDrawer.closeDrawer(Gravity.LEFT);
        switch (item.getItemId()) {
            case R.id.nav_profile:
                startActivityForResult(new Intent(this, ProfileActivity.class), REQUEST_PROFILE);
                break;
            case R.id.nav_emergency_contact:
                startActivity(new Intent(this, ContactActivity.class));
                break;
            case R.id.nav_defense_yourself:
                startActivity(new Intent(this, DefenseYourselfActivity.class));
                break;
            case R.id.nav_change_country:
                startActivity(new Intent(this, ChangeCountryActivity.class));
                break;
            case R.id.nav_library:

                Intent intent = new Intent(this, ManageActivity.class);
                /*Bundle bundle = new Bundle();
                ArrayList<String> alist = new ArrayList<String>();
                List<File> list = FileUtil.getListFile(new File(Environment.getExternalStorageDirectory() + "/" + AppConfig.FOLDER_MEDIA), ".jpg");
                if (list != null) {
                    for (File file : list) {
                        alist.add(file.getAbsolutePath());
                    }
                }
                bundle.putStringArrayList(ImageGalleryActivity.KEY_IMAGES, alist);
                bundle.putString(ImageGalleryActivity.KEY_TITLE, "Manage Photo");
                intent.putExtras(bundle);*/
                startActivity(intent);
                break;
            case R.id.nav_follow:
                startActivity(new Intent(this, FollowActivity.class));
                break;
            case R.id.nav_share:
                CommunicationUtil.shareAll(this, "Share", getString(R.string.share_app_content));
                break;
            case R.id.nav_tutorial:
                startActivity(new Intent(this, TutorialActivity.class));
                break;
            case R.id.nav_setting:
                startActivity(new Intent(this, SettingsActivity.class));
                break;
            case R.id.nav_logout:
                userLogout();
                break;
        }
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
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {//Require login
                //Start listening detect service
                if (isChecked) {
                    startService(detectingAccidentService);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            EventBus.getDefault().post(new EBE_StartDetectingAccident(true));
                        }
                    }, 500);
                    mainFragment.layout_detect_accident.setVisibility(VISIBLE);
                } else {
                    EventBus.getDefault().post(new EBE_StartDetectingAccident(false));
                    stopService(detectingAccidentService);
                    mainFragment.layout_detect_accident.setVisibility(GONE);
                }
            }
        });
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_PROFILE:
                    bindUserDataLogin();
                    break;
                case REQUEST_LOGIN:
                    bindUserDataLogin();
                    break;
                case REQUEST_LOGIN_FOR_SHARE_LOCATION:
                    bindUserDataLogin();
                    if (isLogged) {
                        switchCompat.setChecked(MySharedPreferences.getInstance(this).shareLocationState.load(false));
                    }
                    break;
            }
        }
    }

    @Override
    protected void onResume() {
        bindUserDataLogin();
        super.onResume();
    }

    /**
     * Update user profile when user was login or update profile
     */
    public void bindUserDataLogin() {
        //Bind user
        FirebaseUser fuser = FirebaseAuth.getInstance().getCurrentUser();
        User user = MySharedPreferences.getInstance(this).userProfile.load(null);
        if (fuser != null) {
            if (user == null) {
                user = new User();
            }
            isLogged = true;
            user.setEmail(fuser.getEmail());
            user.setName(fuser.getDisplayName());
            user.setPhotoUrl(fuser.getPhotoUrl().toString());
            MySharedPreferences.getInstance(this).userProfile.save(user);
            mTvHeader.setText(user.getName());
            Picasso.with(this).load(fuser.getPhotoUrl()).into(mImgHeader);
        } else {
            isLogged = false;
            if (fuser != null) {
                mTvHeader.setText(fuser.getDisplayName());
            }
        }
    }

    private void userLogout() {
        FirebaseUser fuser = FirebaseAuth.getInstance().getCurrentUser();
        MySharedPreferences.getInstance(this).userProfile.save(null);
        if (fuser != null) {
            isLogged = false;
            FirebaseAuth.getInstance().signOut();
        }
    }

}
