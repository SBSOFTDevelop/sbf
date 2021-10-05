package ru.sbsoft.common.validation;

/**
 * @author balandin
 * @since Jan 30, 2015 5:39:21 PM
 */
//TODO deprecated
public class INN extends NumClassifiers {

    public static int LENGTH_12 = 12;
    public static int LENGTH_10 = 10;

    public static final String NULL_12 = "____________";
    public static final String PTRN_12 = "999999999999";
    public static final String PTRN_10 = "9999999999";
    public static final String NULL_10 = "__________";

    public static String[] getErrorMesages(String value) {
        if (value == null) {
            return new String[]{EMPTY};
        }
        if (!formatted(value, true)) {
            String[] errors = new String[3];
            errors[0] = INVALID_FORMAT;
            errors[1] = "ИНН ИП,ФЛ - 12 цифр";
            errors[2] = "ИНН ЮЛ - 10 цифр";
            return errors;
        }
        return null;
    }

    public static boolean formatted(String value, boolean isFullComplete) {
        if (value == null) {
            return false;
        }

        if (value.endsWith("__") && value.length() == LENGTH_12) {
            value = value.substring(0, LENGTH_10);
        }

        final int l = value.length();
        if (l == LENGTH_10 || value.length() == LENGTH_12) {
            return valid(value, l == LENGTH_10 ? NULL_10 : NULL_12, isFullComplete);
        }
        return false;
    }
}
