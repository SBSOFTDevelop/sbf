package ru.sbsoft.common;

/**
 * @author balandin
 * @since Mar 13, 2013 3:35:18 PM
 */
public class Strings {

    public static String EMPTY = "";

    public static boolean isEmpty(String value) {
        return isEmpty(value, true);
    }

    public static boolean isEmpty(String value, boolean trim) {
        if (value == null) {
            return true;
        }
        if (trim) {
            value = value.trim();
        }
        return value.isEmpty();
    }

    public static boolean isNotNullEmpty(final String source) {
        if (null != source && source.isEmpty()) {
            return true;
        }
        return false;
    }

    public static String capitalize(String name) {
        if (null != name && name.length() > 0) {
            return new StringBuilder().append(Character.toUpperCase(name.charAt(0)))
                .append(name.substring(1))
                .toString();
        }
        return name;
    }
    
    public static String normalizeLine(String str) {
//        return str == null ? "" : str.replaceAll("[\\x00-\\x1F]", " ").trim().replaceAll(" {2,}", " ");
        return str == null ? "" : str.replaceAll("[\\p{Z}\\p{C}]+", " ").trim();
    }

    public static String clean(String value) {
        return clean(value, true);
    }

    public static String clean(String value, boolean trim) {
        if (value == null) {
            return null;
        }
        if (trim) {
            value = value.trim();
        }
        if (value.isEmpty()) {
            return null;
        }
        return value;
    }

    public static boolean isDigit(String value) {
        if (clean(value) == null) {
            return false;
        }
        for (char c : value.toCharArray()) {
            if (!isDigit(c)) {
                return false;
            }
        }
        return true;
    }

    public static boolean isDigit(char c) {
        return c >= '0' && c <= '9';
    }

    public static boolean equals(String s1, String s2) {
        return coalesce(s1, EMPTY).equals(coalesce(s2, EMPTY));
    }

    public static String join(final String separator, final Object... values) {
        return join(values, separator);
    }
    
    public static String join(final boolean skipNulls, final String separator, final Object... values) {
        return join(values, separator, skipNulls);
    }
    
    public static String join(final Object[] values, final String separator) {
        return join(values, null, separator);
    }

    public static String join(final Object[] values, String elementDecorator, final String separator) {
        return join(values, elementDecorator, separator, false);
    }

    public static String join(final Object[] values, final String separator, boolean skipNulls) {
        return join(values, null, separator, skipNulls);
    }

    public static String join(final Object[] values, String elementDecorator, final String separator, boolean skipNulls) {
        elementDecorator = clean(elementDecorator, false);
        StringBuilder s = new StringBuilder();
        for (Object v : values) {
            if (v == null && skipNulls) {
                continue;
            }
            if (s.length() != 0) {
                s.append(separator);
            }
            if (elementDecorator == null) {
                s.append(v);
            } else {
                s.append(elementDecorator);
                s.append(v);
                s.append(elementDecorator);
            }
        }
        return s.toString();
    }

    public static String coalesce(String s) {
        return coalesce(s, EMPTY);
    }

    public static String coalesce(String... values) {
        return Defs.coalesce(values);
    }

    public static String lpad(String s, char c, int length) {
        if (s.length() >= length) {
            return s;
        }
        StringBuilder sb = new StringBuilder(s);
        while (sb.length() < length) {
            sb.insert(0, c);
        }
        return sb.toString();
    }

    public static String replicate(final Object pattern, final int length, final String separator) {
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < length; i++) {
            if (s.length() != 0 && separator != null) {
                s.append(separator);
            }
            s.append(pattern);
        }
        return s.toString();
    }

    public static String repl(String value, int count) {
        return replicate(value, count, null);
    }

    public static String trim(String value) {
        if (value == null) {
            return null;
        }
        return value.trim();
    }
    
    public static String formatEasy(String format, Object... values) {
        if (null == format) {
            return null;
        }
        if (null == values || values.length == 0) {
            return format;
        }
        for (Object value : values) {
            format = format.replaceFirst("&", value.toString());
        }
        return format;
    }
}
