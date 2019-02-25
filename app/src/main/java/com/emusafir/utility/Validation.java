package com.emusafir.utility;

public class Validation {

    public final static boolean isDouble(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public final static boolean isValidRadius(String str) {
        try {
            if (str != null && str.length() > 0) {
                if (Integer.parseInt(str) > 0)
                    return true;
                else
                    return false;

            } else {
                return false;
            }
        }
        catch (NumberFormatException e){
            return false;
        }
    }

    public final static boolean isValidString(String str) {
        if (str != null && str.length() > 0) {
            return true;
        } else {
            return false;
        }
    }

    public final static boolean isValidDescription(String str) {
        if (str != null && str.length() > 10) {
            return true;
        } else {
            return false;
        }
    }

    public final static boolean isValidEmail(CharSequence target) {
        if (target == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target)
                    .matches();
        }
    }

    public static final boolean isValidPhoneNumber(CharSequence target) {
        if (target.length() < 10) {
            return false;
        } else {
            return android.util.Patterns.PHONE.matcher(target).matches();
        }
    }

    // validating password with retype password
    public static boolean isValidPassword(String password) {
        //check that there are letters
        //check if there are any numbers
        //check any valid special characters
        if (password.length() < 6)
            return false;
        if (password.contains(" "))
            return false;
        else
            return true;
//        Pattern pattern;
//        Matcher matcher;
//        final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[A-Z])(?=.*[+=%@#$^&!])(?=\\S+$).{4,}$";
//        pattern = Pattern.compile(PASSWORD_PATTERN);
//        matcher = pattern.matcher(password);
//
//        return matcher.matches();

    }

    // validating password with retype password
    public static boolean isValidLoginPassword(String password) {
        //check that there are letters
        //check if there are any numbers
        //check any valid special characters
        if (password.length() < 1)
            return false;
        else
            return true;

    }
}
