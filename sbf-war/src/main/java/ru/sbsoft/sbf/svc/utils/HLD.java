package ru.sbsoft.sbf.svc.utils;

import ru.sbsoft.svc.core.client.util.Margins;
import ru.sbsoft.svc.widget.core.client.container.HorizontalLayoutContainer;

/**
 * Параметры отображения компонента для {@link HorizontalLayoutContainer}.
 *
 * @author balandin
 * @since Jul 10, 2013 12:08:10 PM
 */
public class HLD extends HorizontalLayoutContainer.HorizontalLayoutData {

    public HLD() {
    }

    public HLD(double width, double height) {
        super(width, height);
    }

    public HLD(double width, double height, Margins margins) {
        super(width, height, margins);
    }
}
