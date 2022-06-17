package ru.sbsoft.client.utils;

import ru.sbsoft.svc.core.client.util.Margins;
import ru.sbsoft.svc.widget.core.client.container.VerticalLayoutContainer;

/**
 * Параметры отображения компонента для {@link VerticalLayoutContainer}.
 * @author balandin
 * @since Mar 27, 2013 7:23:40 PM
 */
public class VLD extends VerticalLayoutContainer.VerticalLayoutData {

    public VLD() {
    }

    public VLD(double width, double height) {
        super(width, height);
    }

    public VLD(double width, double height, Margins margins) {
        super(width, height, margins);
    }
}
