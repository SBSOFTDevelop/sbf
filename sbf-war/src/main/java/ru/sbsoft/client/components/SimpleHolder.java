package ru.sbsoft.client.components;

/**
 *
 * @author Fedor Resnyanskiy
 */
public class SimpleHolder<T> {

    private T item;

    public void setItem(T item) {
        this.item = item;
    }

    public T getItem() {
        return item;
    }
}
