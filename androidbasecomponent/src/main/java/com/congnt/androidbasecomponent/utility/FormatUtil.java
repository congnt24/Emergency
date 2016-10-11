package com.congnt.androidbasecomponent.utility;


import android.support.annotation.Nullable;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.MessageFormat;
import java.util.Calendar;
import java.util.Locale;

public class FormatUtil {
    /**
     * Format "%.0nf"
     *
     * @param value
     * @param numberAfterDot Số chữ số sau dấu chấm
     * @return
     */
    public static String formatFloat(float value, int numberAfterDot) {
        String format = "%.0" + numberAfterDot + "f";
        return String.format(format, value);
    }

    /**
     * Convert 300,000,000,00,0,0,00,0, VND to 3000000000000
     *
     * @param str       String to format
     * @param splitChar "\\," or "\\." or anything
     * @return
     */
    public static String formatStringToBigDecimal(String str, char splitChar) {
        str = str.trim().split(" ")[0].replaceAll("\\,", "");
        return str;
    }


    /**
     * Format A BigDecimal to A String with format #,###.00
     *
     * @param value
     * @param numberAfterDot Số chữ số đằng sau dấu chấm
     * @return
     */
    public static String formatBigDecimalToString(BigDecimal value, int numberAfterDot) {
        String str = "#,###";
        for (int i = 0; i < numberAfterDot; i++) {
            if (i == 0) str += ".";
            str += "0";
        }
        DecimalFormat df = new DecimalFormat(str);
        return df.format(value);
    }

    /**
     * Format A BigDecimal to A String with any format
     *
     * @param format Định dạng: #.00 | # | #,### if null
     * @param value
     * @return
     */
    public static String formatBigDecimalToString(@Nullable String format, BigDecimal value) {
        String str = format;
        if (format == null) {
            str = "#,###";
        }
        DecimalFormat df = new DecimalFormat(str);
        return df.format(value);
    }

    /**
     * Get custom string with variable in the middle of string
     *
     * @param str   : Base string must contain {0} .... {n}
     * @param value : length must = n
     * @return String to show
     */
    public static String formatMessage(String str, String... value) {
        MessageFormat mf = new MessageFormat(str, Locale.ENGLISH);
        return mf.format(value);
    }

    /**
     * Format current time to send for cart
     *
     * @return
     */
    public static String getDateTime() {
        Calendar calendar = Calendar.getInstance();
        int d = calendar.get(Calendar.DAY_OF_MONTH);
        int m = calendar.get(Calendar.MONTH) + 1;

        String day, month;
        if (d <= 9) {
            day = "0" + d;
        } else {
            day = String.valueOf(d);
        }
        if (m <= 9) {
            month = "0" + m;
        } else {
            month = String.valueOf(m);
        }
        String s = calendar.get(Calendar.YEAR) + "-" + month + "-" + day;
        return s;
    }
}
