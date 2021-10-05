package ru.sbsoft.sbf.app.utils;

import java.util.*;

/**
 *
 * @author Fedor Resnyanskiy
 */
public class CollectionUtils {

    public static <T> Collection<T> wrapList(Collection<T> list) {
        return new ArrayList<>(list);
    }
}
