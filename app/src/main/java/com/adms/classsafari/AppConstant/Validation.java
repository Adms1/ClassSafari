package com.adms.classsafari.AppConstant;

import java.util.regex.Pattern;

import static com.adms.classsafari.AppConstant.AppConfiguration.EMAIL_REGEX;

public class Validation {

    public static boolean checkEmail(String email) {
        boolean flag = true;
        if (email.isEmpty()) {
            flag = false;
        } else if (!Pattern.compile(EMAIL_REGEX).matcher(email).matches()) {
            flag = false;
        }
        return flag;
    }

    public static boolean checkPassword(String password) {
        boolean flag = true;
        if (password.isEmpty()) {
            flag = false;
        } else if (isValidPassword(password)) {
            flag = false;
        }
        return flag;
    }
    public static boolean checkMobile(String text) {
        boolean flag = true;
        if (text.isEmpty()) {
            flag = false;
        } else if (text.length() <= 9) {
            flag = false;
        }
        return flag;
    }
    public static boolean isValidPassword(final String password) {
        boolean flag = true;
        if (password.length() >= 4 && password.length()<=8) {
            flag = false;
        }
        return flag;
    }

}
