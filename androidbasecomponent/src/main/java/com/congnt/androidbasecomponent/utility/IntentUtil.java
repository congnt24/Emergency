package com.congnt.androidbasecomponent.utility;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.provider.Settings;


/**
 * Created by congnt24 on 01/10/2016.
 */

public class IntentUtil {

    /**
     * Request location setting
     *
     * @param context
     */
    public static void requestLocation(Context context) {
        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        context.startActivity(intent);
    }

    /**
     * Request network setting
     * @param context
     */
    public static void requestNetwork(Context context) {
        Intent intent = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            intent = new Intent(Settings.ACTION_NETWORK_OPERATOR_SETTINGS);
        }else{
            intent = new Intent(Settings.ACTION_NETWORK_OPERATOR_SETTINGS);
        }
        context.startActivity(intent);
    }

    /**
     * get Intent to open contact and select one
     * @return
     */
    public static Intent getContactIntent() {
        return new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
    }

    /**
     * get Intent to open camera App and take picture
     * @return
     */
    public static Intent getCameraIntent() {
        return new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
    }

    public static Intent getIntentDialTo(String phoneNumber) {
        Intent callIntent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phoneNumber));
        callIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        return callIntent;
    }

    public static Intent getIntentCallTo(String phoneNumber) {
        Intent callIntent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phoneNumber));
        callIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        return callIntent;
    }

//    public static boolean isIntentAvaiable(Context context, String action){
//        PackageManager pm = context.getPackageManager();
//        List activities = pm.queryIntentActivities(new Intent(action), 0);
//        if (activities.size() != 0) {
//            return true;
//        }
//        return false;
//    }


}
