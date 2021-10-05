package ru.sbsoft.common.validation;

/**
 * @author balandin(olgin)
 * @since Jul 21, 2015 18:59:21 PM
 */
//TODO deprecated
public class KPP extends NumClassifiers {

    public static int LENGTH_9 = 9;

    public static final String PTRN_9 = "999999999";
    public static final String NULL_9 = "_________";

    public static String[] getErrorMesages(String value) {
        if (value == null) {
            return new String[]{EMPTY};
        }
        if (!formatted(value, true)) {
            String[] errors = new String[2];
            errors[0] = INVALID_FORMAT;
            errors[1] = "КПП - 9 цифр";
    //        errors[2] = " ";
            return errors;
        }
        return null;
    }

    public static boolean formatted(String value, boolean isFullComplete) {
        if (value == null) {
            return false;
        }

        if (value.endsWith("__") && value.length() == LENGTH_9) {
            value = value.substring(0, LENGTH_9);
        }

        final int l = value.length();
        if (l == LENGTH_9) {
            return valid(value, l == LENGTH_9 ? NULL_9 : NULL_9, isFullComplete);
        }
        return false;
    }
}
