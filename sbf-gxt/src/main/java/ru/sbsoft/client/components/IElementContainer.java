package ru.sbsoft.client.components;

import java.util.List;

/**
 *
 * @author Fedor Resnyanskiy
 */
public interface IElementContainer {

    List<? extends IElement> getElements();

    /**
     * Иммет ли контейнер указанный элемент как дочерний любого уровня.
     *
     * @param element
     * @return
     */
    boolean hasElement(IElement element);

    /**
     * Имеет ли контейнер указанный элемент как дочерный первого уровня.
     *
     * @param child
     * @return
     */
    boolean hasChild(IElement child);
}
