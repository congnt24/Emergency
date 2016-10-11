package com.congnt.androidbasecomponent.view.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;

import com.congnt.androidbasecomponent.R;
import com.congnt.androidbasecomponent.utility.APILevel;

/**
 * Created by congnt24 on 24/09/2016.
 */

public class DialogBuilder {
    public static AlertDialog.Builder messageDialog(Context context, String title, String message) {
        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(context, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(context);
        }
        builder.setTitle(title)
                .setMessage(message)
                .setPositiveButton(R.string.name_positive_button, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        return builder;
    }

    public static AlertDialog.Builder confirmDialog(Context context, String title, String message, final DialogInterface.OnClickListener onclick) {
        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(context, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(context);
        }
        builder.setTitle(title)
                .setMessage(message)
                .setPositiveButton(R.string.name_positive_button, onclick)
                .setNegativeButton(R.string.name_negative_button, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        return builder;
    }

    public static AlertDialog.Builder yesNoDialog(Context context, String title, String message, final DialogInterface.OnClickListener yesClick, DialogInterface.OnClickListener noClick) {
        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(context, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(context);
        }
        builder.setTitle(title)
                .setMessage(message)
                .setPositiveButton(R.string.name_yes_button, yesClick)
                .setNegativeButton(R.string.name_no_button, noClick);
        return builder;
    }
}
