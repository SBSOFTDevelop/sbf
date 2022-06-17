package ru.sbsoft.shared.meta;

import ru.sbsoft.shared.util.IdName;

public abstract class IdNameNumberWrapper<T extends Number> extends IdNameWrapper<T> {

    public IdNameNumberWrapper() {
    }

    public IdNameNumberWrapper(IdName<T> value) {
        super(value);
    }
}
