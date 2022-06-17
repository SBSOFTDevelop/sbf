package ru.sbsoft.sbf.app.form;

import java.util.Collection;

/**
 *
 * @author Fedor Resnyanskiy
 * @param <ELEMENT>
 */
public interface IFormElementContainer<ELEMENT extends IFormElement> {

    Collection<ELEMENT> getElements();

    void addElement(ELEMENT formElement);

    void removeElement(ELEMENT formElement);
}
