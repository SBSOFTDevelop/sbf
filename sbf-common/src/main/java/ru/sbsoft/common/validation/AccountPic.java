package ru.sbsoft.common.validation;

/**
 * @author balandin(olgin)
 * @since Jul 21, 2015 18:59:21 PM
 */
//TODO deprecated 
public class AccountPic extends NumClassifiers {

    public static int LENGTH_20 = 20;

    public static final String NULL_20 = "____________________";
    public static final String PTRN_20 = "99999999999999999999";

    public static String[] getErrorMesages(String value) {
        if (value == null) {
            return new String[]{EMPTY};
        }
        if (!formatted(value, true)) {
            String[] errors = new String[2];
            errors[0] = INVALID_FORMAT;
            errors[1] = "Расчетный счет - 20 цифр";
            return errors;
        }
        return null;
    }

    public static boolean formatted(String value, boolean isFullComplete) {
        if (value == null) {
            return false;
        }

        if (value.endsWith("__") && value.length() == LENGTH_20) {
            value = value.substring(0, LENGTH_20);
        }

        final int l = value.length();
        if (l == LENGTH_20) {
            return valid(value, l == LENGTH_20 ? NULL_20 : NULL_20, isFullComplete);
        }
        return false;
    }
}
