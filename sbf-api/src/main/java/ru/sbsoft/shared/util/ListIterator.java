package ru.sbsoft.shared.util;

import java.util.List;

/**
 *
 * @author vk
 */
public class ListIterator<T> implements ReusableIterator<T> {

    private final List<T> list;
    private int pos = 0;

    public ListIterator(List<T> list) {
        this.list = list;
    }

    @Override
    public void reset() {
        pos = 0;
    }

    @Override
    public T next() {
        if (pos >= list.size()) {
            return null;
        }
        return list.get(pos++);
    }
}
