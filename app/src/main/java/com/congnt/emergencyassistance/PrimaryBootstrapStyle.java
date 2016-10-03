package com.congnt.emergencyassistance;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;

import com.beardedhen.androidbootstrap.api.attributes.BootstrapBrand;

/**
 * Created by congnt24 on 01/10/2016.
 */

public class PrimaryBootstrapStyle implements BootstrapBrand {
    Context context;

    @Override
    public int defaultFill(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return context.getColor(R.color.colorPrimary);
        } else {
            return context.getResources().getColor(R.color.colorPrimary);
        }
    }

    @Override
    public int defaultEdge(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return context.getColor(R.color.colorPrimary);
        } else {
            return context.getResources().getColor(R.color.colorPrimary);
        }
    }

    @Override
    public int defaultTextColor(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return context.getColor(R.color.md_white_1000);
        } else {
            return context.getResources().getColor(R.color.md_white_1000);
        }
    }

    @Override
    public int activeFill(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return context.getColor(R.color.colorPrimary);
        } else {
            return context.getResources().getColor(R.color.colorPrimary);
        }
    }

    @Override
    public int activeEdge(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return context.getColor(R.color.colorPrimary);
        } else {
            return context.getResources().getColor(R.color.colorPrimary);
        }
    }

    @Override
    public int activeTextColor(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return context.getColor(R.color.md_white_1000);
        } else {
            return context.getResources().getColor(R.color.md_white_1000);
        }
    }

    @Override
    public int disabledFill(Context context) {
        return 0;
    }

    @Override
    public int disabledEdge(Context context) {
        return 0;
    }

    @Override
    public int disabledTextColor(Context context) {
        return 0;
    }

    @Override
    public int getColor() {
        return Color.RED;
    }
}
