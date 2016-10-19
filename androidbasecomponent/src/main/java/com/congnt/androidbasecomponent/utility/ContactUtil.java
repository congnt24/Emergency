package com.congnt.androidbasecomponent.utility;

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.ContactsContract;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by congnt24 on 19/10/2016.
 */

public class ContactUtil {

    public static String retrieveContactName(Context context, Uri uriContact) {
        String contactName = null;
        // querying contact data store
        Cursor cursor = context.getContentResolver().query(uriContact, null, null, null, null);
        if (cursor.moveToFirst()) {
            // DISPLAY_NAME = The display name for the contact.
            // HAS_PHONE_NUMBER =   An indicator of whether this contact has at least one phone number.
            contactName = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
        }
        cursor.close();
        return contactName;

    }

    public static String retrieveContactId(Context context, Uri uriContact) {
        String contactID = null;
        // getting contacts ID
        Cursor cursorID = context.getContentResolver().query(uriContact,
                new String[]{ContactsContract.Contacts._ID},
                null, null, null);
        if (cursorID.moveToFirst()) {
            contactID = cursorID.getString(cursorID.getColumnIndex(ContactsContract.Contacts._ID));
        }
        cursorID.close();
        return contactID;
    }

    public static String retrieveContactNumber(Context context, Uri uriContact) {
        String contactNumber = null;
        // Using the contact ID now we will get contact phone number
        Cursor cursorPhone = context.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER},

                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ? AND " +
                        ContactsContract.CommonDataKinds.Phone.TYPE + " = " +
                        ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE,

                new String[]{retrieveContactId(context, uriContact)},
                null);

        if (cursorPhone.moveToFirst()) {
            contactNumber = cursorPhone.getString(cursorPhone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
        }
        cursorPhone.close();
        return contactNumber;
    }

    public static Bitmap retrieveContactPhoto(Context context, Uri uriContact) {
        Bitmap photo = null;
        try {
            InputStream inputStream = ContactsContract.Contacts.openContactPhotoInputStream(context.getContentResolver(),
                    ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, new Long(retrieveContactId(context, uriContact))));

            if (inputStream != null) {
                photo = BitmapFactory.decodeStream(inputStream);
            }
            assert inputStream != null;
            inputStream.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return photo;
    }

}
