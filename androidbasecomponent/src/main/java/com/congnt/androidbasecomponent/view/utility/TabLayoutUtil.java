package com.congnt.androidbasecomponent.view.utility;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.design.widget.TabLayout;
import android.support.v4.graphics.drawable.DrawableCompat;

/**
 * Created by congnt24 on 29/09/2016.
 */

public class TabLayoutUtil {

    public static void setCustomLayouts(TabLayout tabLayout, int layoutId) {
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            tabLayout.getTabAt(i).setCustomView(layoutId);
        }
    }

    public static void setTexts(TabLayout tabLayout, int... stringIds) {
        int max = tabLayout.getTabCount();
        if (max > stringIds.length) max = stringIds.length;
        for (int i = 0; i < max; i++) {
            tabLayout.getTabAt(i).setText(stringIds[i]);
        }
    }

    public static void setTexts(TabLayout tabLayout, String... strings) {
        int max = tabLayout.getTabCount();
        if (max > strings.length) max = strings.length;
        for (int i = 0; i < max; i++) {
            tabLayout.getTabAt(i).setText(strings[i]);
        }
    }

    public static void setIcons(TabLayout tabLayout, int... drawableIds) {
        int max = tabLayout.getTabCount();
        if (max > drawableIds.length) max = drawableIds.length;
        for (int i = 0; i < max; i++) {
            tabLayout.getTabAt(i).setIcon(drawableIds[i]);
        }
    }

    public static void setColorSelectorIcons(TabLayout tabLayout, int color_selector) {
        Context context = tabLayout.getRootView().getContext();
        ColorStateList colors;
        if (Build.VERSION.SDK_INT >= 23) {
            colors = context.getResources().getColorStateList(color_selector, context.getTheme());
        } else {
            colors = context.getResources().getColorStateList(color_selector);
        }

        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            Drawable icon = tab.getIcon();

            if (icon != null) {
                icon = DrawableCompat.wrap(icon);
                DrawableCompat.setTintList(icon, colors);
            }
        }
    }
}
