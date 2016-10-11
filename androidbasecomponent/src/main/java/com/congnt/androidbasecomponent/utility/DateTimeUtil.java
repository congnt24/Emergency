package com.congnt.androidbasecomponent.utility;

import android.text.format.DateFormat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Util to calculate and convert date time
 */
public class DateTimeUtil {
    public static final String FORMAT_FULL = "yyyy-MM-dd HH:mm:ss'";
    public static final String FORMAT_DATE = "yyyy-MM-dd";
    public static final String FORMAT_HOUR = "HH:mm:ss";

    public static String getCurrentTimeInString(DateType type) {
        String format = null;
        switch (type) {
            case FULL:
                format = FORMAT_FULL;
                break;
            case DATE:
                format = FORMAT_DATE;
                break;
            case HOUR:
                format = FORMAT_HOUR;
                break;
        }
        DateFormat df = new DateFormat();
        return (String) df.format(format != null ? format : FORMAT_DATE, new Date());
    }

    public static Date getDateFromString(DateType type, String s) {
        String format = null;
        switch (type) {
            case FULL:
                format = FORMAT_FULL;
                break;
            case DATE:
                format = FORMAT_DATE;
                break;
            case HOUR:
                format = FORMAT_HOUR;
                break;
        }
        SimpleDateFormat formatter = new SimpleDateFormat(format != null ? format : FORMAT_DATE);
        try {
            return formatter.parse(s);
        } catch (ParseException e) {
            return new Date();
        }
    }

    public static String convertFormat(String date_s, String oldFormat, String newFormat) {
        SimpleDateFormat dt = new SimpleDateFormat(oldFormat);
        Date date = null;
        try {
            date = dt.parse(date_s);
            // *** same for the format String below
            SimpleDateFormat dt1 = new SimpleDateFormat(newFormat);
            return dt1.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }

    public enum DateType {
        FULL, DATE, HOUR
    }
}