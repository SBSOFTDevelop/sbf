package ru.sbsoft.sbf.app.model;

public interface ModelValueProvider<VALUE, MODEL> {

    VALUE getValue(MODEL model);

    void setValue(VALUE value, MODEL model);
}
