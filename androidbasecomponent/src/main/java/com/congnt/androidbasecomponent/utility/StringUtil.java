package com.congnt.androidbasecomponent.utility;

import java.util.regex.Pattern;

public class StringUtil {

    public static boolean checkEmail(String email) {
        Pattern EMAIL_ADDRESS_PATTERN = Pattern
                .compile("^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
        return EMAIL_ADDRESS_PATTERN.matcher(email).matches();
    }

    public static boolean isNullOrEmpty(String s) {
        if (s == null || s.isEmpty()) return true;
        return false;
    }

    public static boolean isExistNull(String... arr) {
        for (String item : arr) {
            if (item == null || item.isEmpty()) return true;
        }
        return false;
    }
}