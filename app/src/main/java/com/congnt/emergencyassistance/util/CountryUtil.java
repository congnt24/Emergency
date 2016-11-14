package com.congnt.emergencyassistance.util;

import android.content.Context;

import com.congnt.androidbasecomponent.utility.FileUtil;
import com.congnt.emergencyassistance.R;
import com.congnt.emergencyassistance.entity.ItemCountryEmergencyNumber;
import com.congnt.emergencyassistance.entity.ItemCountryEmergencyNumberWrapper;
import com.congnt.emergencyassistance.entity.SettingSpeech;
import com.google.gson.Gson;

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
        return new Gson().fromJson(FileUtil.loadFileFromRaw(context, R.raw.emergency_numbey_by_country), ItemCountryEmergencyNumberWrapper.class);
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

    public static SettingSpeech getSettingSpeechByCountry(Context context, String countryCode){
        SettingSpeech[] settingSpeechs = new Gson().fromJson(FileUtil.loadFileFromRaw(context, R.raw.json_speech_by_country), SettingSpeech[].class);
        for (int i = 0; i < settingSpeechs.length; i++) {
            if (countryCode.equalsIgnoreCase(settingSpeechs[i].countryCode)) {
                return settingSpeechs[i];
            }
        }
        return null;
    }

}
