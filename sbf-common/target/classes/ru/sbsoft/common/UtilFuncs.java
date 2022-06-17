package ru.sbsoft.common;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

/**
 * @author vk
 */
public final class UtilFuncs {

    public static boolean hasVal(String s) {
        return s != null && !s.trim().isEmpty();
    }

    public static boolean hasVal(List<?> l) {
        if (l != null && !l.isEmpty()) {
            for (Object o : l) {
                if (o != null) {
                    return true;
                }
            }
        }
        return false;
    }

    public static String reqVal(String s, String errMsg) {
        return reqVal(s, errMsg, UtilFuncs::hasVal);
    }

    public static List<?> reqVal(List<?> l, String errMsg) {
        return reqVal(l, errMsg, UtilFuncs::hasVal);
    }

    private static <T> T reqVal(T v, String errMsg, Function<T, Boolean> hasVal) {
        if (!hasVal.apply(v)) {
            throw new IllegalArgumentException(errMsg);
        }
        return v;
    }

    public static <E extends Comparable<E>> List<E> copySorted(List<E> l) {
        List<E> res = l == null || l.isEmpty() ? new ArrayList<>() : new ArrayList<>(l);

       // if (res != Collections.EMPTY_LIST)
            Collections.sort(res);
        return res;
    }

    public static <E extends Comparable<E>> boolean equalsSorted(List<E> l1, List<E> l2) {
        return copySorted(l1).equals(copySorted(l2));
    }

    private UtilFuncs() {
    }
}
