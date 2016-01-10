package com.shopfitt.android.Utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validation {
    private static  final int EMAIL_MINIMUM_LENGTH = 7;
    private static final int PASSWORD_MINIMUM_LENGTH = 5;

    private static final String emailRegex = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
            + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    private static final String nameRegex = "^[a-zA-Z ]*$";

    /**
     * Validates an email
     *
     * @param emailId email id to validate
     * @return true if email is valid else false
     */
    public static boolean isValidEmailPattern(String emailId) {
        if (emailId.length() >= EMAIL_MINIMUM_LENGTH) {
            Pattern pattern = Pattern.compile(emailRegex);
            Matcher matcher = pattern.matcher(emailId);
            return matcher.matches();
        }
        return false;
    }

    /**
     * Validates password - alpha numeric and length above 5
     *
     * @param password password to validate
     * @return true if it is valid password else false
     */
    public static boolean isValidPassword(String password) {
        return password.length() >= PASSWORD_MINIMUM_LENGTH;
    }
}
