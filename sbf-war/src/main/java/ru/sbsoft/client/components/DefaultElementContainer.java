package ru.sbsoft.client.components;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Контейнер элементов пользовательского интерфейса.
 *
 * @author Fedor Resnyanskiy
 */
public class DefaultElementContainer implements IElementContainer {

    private final List<IElement> childElements = new ArrayList<>();

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
        return Objects.equals(e1, e2);
    }

}
