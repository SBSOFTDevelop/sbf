package ru.sbsoft.client.components;

import java.util.ArrayList;
import java.util.List;

/**
 * Контейнер элементов пользовательского интерфейса.
 *
 * @author Fedor Resnyanskiy
 */
public class DefaultElementContainer implements IElementContainer {

    private List<IElement> childElements = new ArrayList<IElement>();

    @Override
    public List<? extends IElement> getElements() {
        return new ArrayList<IElement>(childElements);
    }

    @Override
    public boolean hasElement(IElement element) {
        for (IElement e : childElements) {
            if (elementsEquals(e, element)) {
                return true;
            }
            if (e instanceof IElementContainer
                    && ((IElementContainer) e).hasElement(element)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean hasChild(IElement child) {
        for (IElement e : childElements) {
            if (elementsEquals(e, child)) {
                return true;
            }
        }
        return false;
    }

    private static boolean elementsEquals(IElement e1, IElement e2) {
        return (e1 == null && e2 == null)
                || e1 == e2
                || (e1 != null && e1.equals(e2));
    }

}
