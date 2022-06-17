package ru.sbsoft.sbf.app.form;

/**
 *
 * @author Fedor Resnyanskiy
 * @param <EVENT>
 */
public interface IHandler<EVENT extends IEvent> extends IDummyHandler<EVENT> {

    void onHandle(EVENT event);
}
