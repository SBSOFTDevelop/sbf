package ru.sbsoft.client.components.grid.aggregate.result;

/**
 *
 * @author Kiselev
 */
public interface IResultHandler<T> {
    String formatResultVal(T o);
}
