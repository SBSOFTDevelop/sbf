package ru.sbsoft.shared.util;

/**
 *
 * @author vk
 */
public interface ReusableIterator<T> extends SimpleIterator<T>{
    void reset();
}
