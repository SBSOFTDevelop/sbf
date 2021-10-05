package ru.sbsoft.client.components;

/**
 *
 * @author Fedor Resnyanskiy
 */
public interface IHasParentElement {

    void setParentElement(IElementContainer parent);

    IElementContainer getParentElement();
}
