/*
package com.congnt.emergencyassistance.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.WindowManager;
import android.widget.Toast;

import com.congnt.emergencyassistance.R;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;

public abstract class BaseSlidingActivity extends SlidingFragmentActivity {
    protected LeftMenuFragment mMenuFragment;
    private SlidingMenu mSlidingMenu;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        initSlidingMenu(savedInstanceState);
        setContentView(getLayout());
        initViews(savedInstanceState);
        initVariables(savedInstanceState);

    }

    public void startActivity(Class<?> cls, Bundle bundle) {
        Intent intent = new Intent(this, cls);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    public void startActivity(Class<?> cls) {
        Intent intent = new Intent(this, cls);
        startActivity(intent);
    }

    protected void initSlidingMenu(Bundle savedInstanceState) {
        // set the Behind View
        setBehindContentView(R.layout.menu_frame);
        if (savedInstanceState == null) {
            FragmentTransaction t = this.getSupportFragmentManager().beginTransaction();
            mMenuFragment = new LeftMenuFragment();
            t.replace(R.id.menu_frame, mMenuFragment);
            t.commit();
        } else {
            mMenuFragment = (LeftMenuFragment) this.getSupportFragmentManager().findFragmentById(R.id.menu_frame);
        }

        // customize the SlidingMenu
        mSlidingMenu = getSlidingMenu();
        mSlidingMenu.setMode(SlidingMenu.LEFT);
//      mSlidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
        mSlidingMenu.setShadowWidthRes(R.dimen.shadow_width);
        mSlidingMenu.setShadowDrawable(R.drawable.shadow);
        mSlidingMenu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
        mSlidingMenu.setFadeDegree(0.35f);
    }

    protected abstract int getLayout();

    protected abstract void initViews(Bundle savedInstanceState);

    protected abstract void initVariables(Bundle savedInstanceState);

    public void showToast(String message) {
        if (message == null || message.length() <= 0)
            return;
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();

    }

    public void openFragment(int resId, Class<? extends Fragment> fragmentClazz, boolean addBackStack) {
        openFragment(resId, fragmentClazz, null, addBackStack);
    }

    public void openFragment(int resId, Class<? extends Fragment> fragmentClazz, Bundle args, boolean addBackStack) {
        FragmentManager manager = getSupportFragmentManager();
        String tag = fragmentClazz.getName();
        try {
            boolean isExisted = manager.popBackStackImmediate(tag, 0);    // IllegalStateException
            if (!isExisted) {
                Fragment fragment;
                try {
                    fragment = fragmentClazz.newInstance();
                    if (args != null) {
                        fragment.setArguments(args);
                    }
                    FragmentTransaction transaction = manager.beginTransaction();
//                    transaction.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit);
                    transaction.replace(resId, fragment, tag);

                    if (addBackStack) {
                        transaction.addToBackStack(tag);
                    }
                    transaction.commitAllowingStateLoss();
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showDialog(DialogFragment fragment, String tag) {
        FragmentManager fm = getSupportFragmentManager();
        fragment.show(fm, tag);
    }
}*/
