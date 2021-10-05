package ru.sbsoft.common.validation;

/**
 * @author balandin
 * @since Feb 2, 2015 4:40:26 PM
 */
//TODO deprecated
public class OGRN extends NumClassifiers {

    public static int LENGTH_15 = 15;
    public static int LENGTH_13 = 13;

    public static final String NULL_15 = "_______________";
    public static final String PTRN_15 = "999999999999999";
    public static final String PTRN_13 = "9999999999999";
    public static final String NULL_13 = "_____________";

    public static String[] getErrorMesages(String value) {
        if (value == null) {
            return new String[]{EMPTY};
        }
        if (!formatted(value, true)) {
            String[] errors = new String[3];
            errors[0] = INVALID_FORMAT;
            errors[1] = "ОГРН ИП (ОГРНИП) - 15 цифр";
            errors[2] = "ОГРН ЮЛ - 13 цифр";
            return errors;
        }
        return null;
    }

    public static boolean formatted(String value, boolean isFullComplete) {
        if (value == null) {
            return false;
        }

        if (value.endsWith("__") && value.length() == LENGTH_15) {
            value = value.substring(0, LENGTH_13);
        }

        final int l = value.length();
        if (l == LENGTH_13 || value.length() == LENGTH_15) {
            return valid(value, l == LENGTH_13 ? NULL_13 : NULL_15, isFullComplete);
        }
        return false;
    }
}
