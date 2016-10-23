package com.congnt.emergencyassistance.entity;

import android.net.Uri;

/**
 * Created by congnt24 on 20/10/2016.
 */

public class ItemContact {
    private String uriContact;
    private String contactName;
    private String contactNumber;

    public ItemContact(String uriContact, String contactName, String contactNumber) {
        this.uriContact = uriContact;
        this.contactName = contactName;
        this.contactNumber = contactNumber;
    }

    public String getContactName() {
        return contactName;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public Uri getUriContact() {
        return Uri.parse(uriContact);
    }
}
