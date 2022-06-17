package ru.sbsoft.sbf.app.form;

/**
 *
 * @author Fedor Resnyanskiy
 * @param <OWNER>
 */
public interface IEvent<OWNER> {

    OWNER getOwner();
}
