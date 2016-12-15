package com.congnt.emergencyassistance.view.activity;

import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.congnt.androidbasecomponent.Awesome.AwesomeActivity;
import com.congnt.androidbasecomponent.Awesome.AwesomeLayout;
import com.congnt.androidbasecomponent.adapter.ViewPagerAdapter;
import com.congnt.androidbasecomponent.annotation.Activity;
import com.congnt.androidbasecomponent.annotation.NavigateUp;
import com.congnt.androidbasecomponent.utility.FileUtil;
import com.congnt.androidbasecomponent.view.utility.TabLayoutUtil;
import com.congnt.emergencyassistance.AppConfig;
import com.congnt.emergencyassistance.MainActionBar;
import com.congnt.emergencyassistance.R;
import com.congnt.emergencyassistance.view.fragment.AudioGalleryFragment;
import com.etiennelawlor.imagegallery.library.ImageGalleryFragment;
import com.etiennelawlor.imagegallery.library.activities.FullScreenImageGalleryActivity;
import com.etiennelawlor.imagegallery.library.activities.ImageGalleryActivity;
import com.etiennelawlor.imagegallery.library.adapters.FullScreenImageGalleryAdapter;
import com.etiennelawlor.imagegallery.library.adapters.ImageGalleryAdapter;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by congnt24 on 12/12/2016.
 */
@NavigateUp
@Activity(transitionAnim = Activity.AnimationType.ANIM_LEFT_TO_RIGHT,
        actionbarType = Activity.ActionBarType.ACTIONBAR_CUSTOM,
        mainLayoutId = R.layout.viewpager)
public class ManageActivity extends AwesomeActivity {
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
        initManageGallery();
        //init viewpager
        setupViewPager(mainView);
    }

    private void setupViewPager(View root) {
        ViewPager viewPager = (ViewPager) root.findViewById(R.id.viewPager);
        List<Fragment> listFragment = new ArrayList<>();
        Bundle bundle = new Bundle();
        ArrayList<String> alist = new ArrayList<String>();
        List<File> list = FileUtil.getListFile(new File(Environment.getExternalStorageDirectory() + "/" + AppConfig.FOLDER_MEDIA), ".jpg");
        if (list != null) {
            for (File file : list) {
                alist.add(file.getAbsolutePath());
            }
        }
        bundle.putStringArrayList(ImageGalleryActivity.KEY_IMAGES, alist);
        bundle.putString(ImageGalleryActivity.KEY_TITLE, "Manage");

        ImageGalleryFragment imageGalleryFragment = ImageGalleryFragment.newInstance(bundle);
        listFragment.add(imageGalleryFragment);
        listFragment.add(new AudioGalleryFragment());
//        listFragment.add(WalkingFragment.newInstance());
        viewPager.setAdapter(new ViewPagerAdapter<>(getSupportFragmentManager(), listFragment));
        viewPager.setOffscreenPageLimit(listFragment.size() - 1);
        //Setup TabLayout
        setupTabLayout(root, viewPager);
    }

    private void setupTabLayout(View root, ViewPager viewPager) {
        TabLayout tabLayout = (TabLayout) root.findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);
//        TabLayoutUtil.setCustomLayouts(tabLayout, R.layout.item_tab_tablayout);
//        TabLayoutUtil.setIcons(tabLayout, R.drawable.ic_home, R.drawable.ic_report_problem, R.drawable.ic_location_on, R.drawable.ic_directions_run);
        TabLayoutUtil.setTexts(tabLayout, "Photo", "Audio");
        //Best Solution
//        TabLayoutUtil.setColorSelectorIcons(tabLayout, R.color.tab_icon);
    }

    private void initManageGallery() {
        ImageGalleryFragment.setImageThumbnailLoader(new ImageGalleryAdapter.ImageThumbnailLoader() {
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

}
