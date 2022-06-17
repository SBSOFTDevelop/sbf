package ru.sbsoft.sbf.app.form;

/**
 *
 * @author Fedor Resnyanskiy
 * @param <VALUE>
 */
public interface IHasValue<VALUE> {

    VALUE getValue();

    void setValue(VALUE value);
}
