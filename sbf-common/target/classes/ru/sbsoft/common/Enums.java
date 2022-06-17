package ru.sbsoft.common;

import java.util.EnumSet;

/**
 * @author balandin
 * @since Aug 6, 2015
 */
public class Enums {

    private Enums() {
    }

    public static <E extends Enum<E>> E find(Class<E> ennnnnum, String key) {
        return find(ennnnnum, key, false);
    }

    public static <E extends Enum<E>> E valueOf(Class<E> ennnnnum, String key) {
        return find(ennnnnum, key, true);
    }

    public static <E extends Enum<E>> E find(Class<E> ennnnnum, String key, boolean failIfNotExists) {
        EnumSet<E> items = EnumSet.allOf(ennnnnum);
        for (E e : items) {
            if (e.name().equals(key)) {
                return e;
            }
        }
        if (failIfNotExists) {
            throw new IllegalArgumentException("enum [" + key + "] not found " + ennnnnum.getName());
        }
        return null;
    }
}
