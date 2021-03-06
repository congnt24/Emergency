package com.congnt.androidbasecomponent.view.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.View;

import com.congnt.androidbasecomponent.R;
import com.congnt.androidbasecomponent.utility.StringUtil;

/**
 * Created by congnt24 on 24/09/2016.
 */

public class DialogBuilder {
    public static AlertDialog.Builder messageDialog(Context context, String title, @Nullable String message, @Nullable int theme) {
        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(context, theme != 0 ? theme : android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(context, theme != 0 ? theme : android.R.style.Theme_DeviceDefault_Dialog_Alert);
        }
        builder.setTitle(title)
                .setPositiveButton(R.string.name_positive_button, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        if (!StringUtil.isNullOrEmpty(message)){
            builder.setMessage(message);
        }
        return builder;
    }

    public static AlertDialog.Builder confirmDialog(Context context, String title, @Nullable String message, @Nullable int theme, final DialogInterface.OnClickListener onclick) {
        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(context, theme != 0 ? theme : android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(context, theme != 0 ? theme : android.R.style.Theme_DeviceDefault_Dialog_Alert);
        }
        builder.setTitle(title)
                .setPositiveButton(R.string.name_positive_button, onclick)
                .setNegativeButton(R.string.name_negative_button, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        if (!StringUtil.isNullOrEmpty(message)){
            builder.setMessage(message);
        }
        return builder;
    }

    public static AlertDialog.Builder yesNoDialog(Context context, String title, @Nullable String message, @Nullable int theme, final DialogInterface.OnClickListener yesClick, DialogInterface.OnClickListener noClick) {
        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(context, theme != 0 ? theme : android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(context, theme != 0 ? theme : android.R.style.Theme_DeviceDefault_Dialog_Alert);
        }
        builder.setTitle(title)
                .setPositiveButton(R.string.name_yes_button, yesClick)
                .setNegativeButton(R.string.name_no_button, noClick);
        if (!StringUtil.isNullOrEmpty(message)){
            builder.setMessage(message);
        }
        return builder;
    }

    public static AlertDialog.Builder customDialog(Context context, String title, View customLayout, @Nullable int theme, final DialogInterface.OnClickListener onclick) {
        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(context, theme != 0 ? theme : android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(context, theme != 0 ? theme : android.R.style.Theme_DeviceDefault_Dialog_Alert);
        }
        builder.setTitle(title)
                .setView(customLayout)
                .setPositiveButton(R.string.name_positive_button, onclick)
                .setNegativeButton(R.string.name_negative_button, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        return builder;
    }
}
