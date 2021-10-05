package ru.sbsoft.common.validation;

/**
 * @author balandin(olgin)
 * @since Jul 21, 2015 18:59:21 PM
 */
//TODO deprecated 
public class Account extends NumClassifiers {

    public static int LENGTH_20 = 20;
    public static int LENGTH_11 = 11;

    public static final String NULL_20 = "____________________";
    public static final String PTRN_20 = "99999999999999999999";
    public static final String NULL_11 = "___________";
    public static final String PTRN_11 = "99999999999";

    public static String[] getErrorMesages(String value) {
        if (value == null) {
            return new String[]{EMPTY};
        }
        if (!formatted(value, true)) {
            String[] errors = new String[3];
            errors[0] = INVALID_FORMAT;
            errors[1] = "Допустимая длина текста может быть 11";
            errors[2] = "или 20 символов";
            return errors;
        }
        return null;
    }

    public static boolean formatted(String value, boolean isFullComplete) {
        if (value == null) {
            return false;
        }

        if (value.endsWith("_________") && value.length() == LENGTH_20) {
            value = value.substring(0, LENGTH_11);
        }

        final int l = value.length();
        if (l == LENGTH_20 || value.length() == LENGTH_11) {
            return valid(value, l == LENGTH_20 ? NULL_20 : NULL_11, isFullComplete);
        }
        return false;
    }
}
