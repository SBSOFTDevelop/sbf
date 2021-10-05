package ru.sbsoft.common.validation;

/**
 *
 * @author frolikov
 */
//TODO deprecated
public class OKPO extends NumClassifiers{
    public static int LENGTH_8 = 8;
    public static int LENGTH_10 = 10;

    public static final String NULL_8 = "________";
    public static final String PTRN_8 = "99999999";
    public static final String PTRN_10 = "9999999999";
    public static final String NULL_10 = "__________";
    
    public static String[] getErrorMesages(String value) {
        if (value == null) {
            return new String[]{EMPTY};
        }
        if (!formatted(value, true)) {
            String[] errors = new String[3];
            errors[0] = INVALID_FORMAT;
            errors[1] = "ОКПО ЮЛ - 8 цифр";
            errors[2] = "ОКПО ИП - 10 цифр";
            return errors;
        }
        return null;
    }

    public static boolean formatted(String value, boolean isFullComplete) {
        if (value == null) {
            return false;
        }

        if (value.endsWith("__") && value.length() == LENGTH_10) {
            value = value.substring(0, LENGTH_8);
        }

        final int l = value.length();
        if (l == LENGTH_10 || value.length() == LENGTH_8) {
            return valid(value, l == LENGTH_10 ? NULL_10 : NULL_8, isFullComplete);
        }
        return false;
    }
    
}
