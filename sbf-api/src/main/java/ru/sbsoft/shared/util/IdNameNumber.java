package ru.sbsoft.shared.util;

/**
 *
 * @author vk
 */
public abstract class IdNameNumber<T extends Number> extends IdName<T> {
    
    public IdNameNumber(T id, String name) {
        super(id, name);
    }

    protected IdNameNumber() {
    }
}
