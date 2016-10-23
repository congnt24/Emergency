package com.congnt.androidbasecomponent.utility;

import android.content.Context;
import android.content.Intent;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.provider.Settings;

/**
 * Created by congnt24 on 01/10/2016.
 */

public class IntentUtil {

    public static void requestLocation(Context context) {
        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        context.startActivity(intent);
    }

    public static void requestNetwork(Context context) {
        Intent intent = new Intent(Settings.ACTION_NETWORK_OPERATOR_SETTINGS);
        context.startActivity(intent);
    }

    public static Intent getContactIntent() {
        return new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
    }

    public static Intent getCameraIntent() {
        return new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
    }


}
