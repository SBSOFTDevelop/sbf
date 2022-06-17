package ru.sbsoft.common;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;

/**
 * @author balandin
 * @since Apr 13, 2015 2:46:03 PM
 */
public class Defs {

    private static final int defValue = 0;
    private static final HashMap<Class, Object> defs = new HashMap<Class, Object>();

    static {
        defs.put(String.class, Strings.EMPTY);
        defs.put(BigDecimal.class, BigDecimal.valueOf(defValue));
        defs.put(BigInteger.class, BigInteger.valueOf(defValue));
        defs.put(Long.class, Long.valueOf(defValue));
        defs.put(Integer.class, Integer.valueOf(defValue));
    }

    private Defs() {
    }

    public static <T> T coalesce(T... values) {
        if (values != null && values.length > 0) {
            for (T t : values) {
                if (t != null) {
                    return t;
                }
            }
        }
        return null;
    }

    public static <T> T def(T... values) {
        T result = coalesce(values);
        if (result == null && values != null && values.length > 0) {
            final Class c = values.getClass().getComponentType();
            if (!defs.containsKey(c)) {
                throw new IllegalStateException();

            }
            final Object obj = defs.get(c);
            if (obj == null) {
                throw new IllegalStateException();
            }
            return (T) obj;
        }
        return null;
    }

//    public static <T> T coalesce(T... values) {
//        if (values != null && values.length > 0) {
//            for (T t : values) {
//                if (t != null) {
//                    return t;
//                }
//            }
//            final Class c = values.getClass().getComponentType();
//            if (Number.class.isAssignableFrom(c)) {
//                if (c == BigDecimal.class) {
//                    return (T) BigDecimal.valueOf(defValue);
//                } else if (c == BigInteger.class) {
//                    return (T) BigInteger.valueOf(defValue);
//                }
//                try {
//                    return (T) c.getConstructor(new Class[]{String.class}).newInstance(String.valueOf(defValue));
//                } catch (Exception ex) {
//                    throw new IllegalArgumentException(ex);
//                }
//            } else if (c == String.class) {
//                return (T) Strings.EMPTY;
//            }
//        }
//        return null;
//    }
}
