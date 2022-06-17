package ru.sbsoft.common.validation;

/**
 * @author balandin
 * @since Jan 30, 2015 3:13:58 PM
 */
//TODO deprecated
public class NumClassifiers {

    public static final String EMPTY = "Пустое значение";
    public static final String INVALID_FORMAT = "Ошибочный формат";
    public static final String INVALID_KEY = "Ошибка проверки контрольной суммы";

    protected NumClassifiers() {
    }

    public static boolean valid(String value, String pattern, boolean isFullComplete) {
        if (value == null || pattern == null) {
            return false;
        }
        final int length = value.length();
        if (pattern.length() != length) {
            return false;
        }
        for (int i = 0; i < length; i++) {
            char c = value.charAt(i);
            char p = pattern.charAt(i);
            if (p == '_') {
                if (c == '_') {
                    if (isFullComplete) {
                        return false;
                    }
                } else if (!(c >= '0' && c <= '9')) {
                    return false;
                }
            } else if (p != c) {
                return false;
            }
        }
        return true;
    }
}
