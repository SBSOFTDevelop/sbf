package ru.sbsoft.shared.meta;

import ru.sbsoft.shared.util.IdName;

public class IdNameWrapper<T> extends Wrapper<IdName<T>> {

    public IdNameWrapper() {
    }

    public IdNameWrapper(IdName<T> value) {
        super(value);
    }

    @Override
    public void setValue(IdName<T> value) {
        super.setValue(value);
    }

    @Override
    public IdName<T> getValue() {
        return super.getValue();
    }
}
