package ru.sbsoft.common.validation;

/**
 * @author balandin
 * @since Jan 30, 2015 3:46:59 PM
 */
//TODO deprecated
public class SNILS extends NumClassifiers {

    public static final String NULLVAL = "___-___-___ __";
    public static final String PATTERN = "999-999-999 99";
    public static final int SIGNIFICANT_LENGTH = 11;
    //
    public static final int MAGIC_NUMBER = 1001998;

    public static String getErrorMesage(String value) {
        if (value == null) {
            return EMPTY;
        }
        if (!formatted(value, true)) {
            return INVALID_FORMAT;
        }
        if (!valid(value)) {
            return INVALID_KEY;
        }
        return null;
    }

    public static boolean formatted(String value, boolean isFullComplete) {
        return valid(value, NULLVAL, isFullComplete);
    }

    public static boolean valid(String value) {
        if (!valid(value, NULLVAL, true)) {
            return false;
        }

        String s = value.replaceAll("-| ", "");
        long num = Long.parseLong(s.substring(0, 9));
        if (num <= MAGIC_NUMBER) {
            return true;
        }

        int summ = 0;
        for (int pos = 1; pos <= 9; pos++) {
            summ += (pos * (num % 10));
            num = num / 10;
        }

        summ %= 101;
        summ %= 100;

        return summ == Integer.parseInt(s.substring(9, 11));
    }
}
