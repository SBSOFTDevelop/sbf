package ru.sbsoft.sbf.svc.utils;

import ru.sbsoft.svc.core.client.util.Margins;
import ru.sbsoft.svc.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;

/**
 * Константы заполнения для
 * {@link ru.sbsoft.svc.widget.core.client.container.VerticalLayoutContainer}.
 *
 * @author balandin
 * @since Jan 21, 2013 12:09:04 PM
 */
public class VLC {

    public static final VerticalLayoutData FILL = new VerticalLayoutData(1, 1);
    public static final VerticalLayoutData CONST = new VerticalLayoutData(1, -1);
    public static final VerticalLayoutData CONST_M2 = new VerticalLayoutData(1, -1, new Margins(2));
    public static final VerticalLayoutData CONST_M3 = new VerticalLayoutData(1, -1, new Margins(3));
}
