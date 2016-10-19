package com.congnt.emergencyassistance.util;

import android.content.Context;
import android.content.res.Resources;

import com.congnt.emergencyassistance.R;
import com.congnt.emergencyassistance.entity.ItemCountryEmergencyNumber;
import com.congnt.emergencyassistance.entity.ItemCountryEmergencyNumberWrapper;
import com.google.gson.Gson;

import java.io.InputStream;

/**
 * Created by congnt24 on 19/10/2016.
 */

public class CountryUtil {
    public static CountryUtil instance;
    private ItemCountryEmergencyNumberWrapper listCountry;

    public CountryUtil(Context context) {
        listCountry = getListCountry(context);
    }

    public static CountryUtil getInstance(Context context) {
        if (instance == null) {
            instance = new CountryUtil(context);
        }
        return instance;
    }

    public ItemCountryEmergencyNumberWrapper getListCountry(Context context) {
        return new Gson().fromJson(loadFileFromRaw(context), ItemCountryEmergencyNumberWrapper.class);
    }

    public ItemCountryEmergencyNumber getItemCountryByCode(String countryCode) {
        for (ItemCountryEmergencyNumber item :
                listCountry.countries) {
            if (item.countryCode.equalsIgnoreCase(countryCode)) {
                return item;
            }
        }
        return null;
    }

    public ItemCountryEmergencyNumber getItemCountryByName(String countryName) {
        for (ItemCountryEmergencyNumber item :
                listCountry.countries) {
            if (item.countryName.equalsIgnoreCase(countryName)) {
                return item;
            }
        }
        return null;
    }

    public String loadFileFromRaw(Context context) {
        try {
            Resources res = context.getResources();
            InputStream in_s = res.openRawResource(R.raw.emergency_numbey_by_country);
            byte[] b = new byte[in_s.available()];
            in_s.read(b);
            return new String(b);
        } catch (Exception e) {
            // e.printStackTrace();
            return null;
        }
    }
}
