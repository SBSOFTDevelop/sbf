package ru.sbsoft.client.components.form;

import com.google.gwt.user.client.ui.IsWidget;
import ru.sbsoft.svc.core.client.util.Margins;
import ru.sbsoft.svc.widget.core.client.container.CssFloatLayoutContainer;

/**
 * Два поля, сгруппированых горизонтально.
 *
 * @author panarin
 */
public class TwoFieldHorizontalContainer extends CssFloatLayoutContainer {

    private final static Margins M1 = new Margins(0, 2, 0, 0);
    private final static Margins M2 = new Margins(0, 0, 0, 2);
    public final static CssFloatLayoutContainer.CssFloatData DEFAULT1 = new CssFloatLayoutContainer.CssFloatData(0.5, M1);
    public final static CssFloatLayoutContainer.CssFloatData DEFAULT2 = new CssFloatLayoutContainer.CssFloatData(0.5, M2);

    public TwoFieldHorizontalContainer(final IsWidget field1, final IsWidget field2) {
        this(field1, field2, -1);
    }

    public TwoFieldHorizontalContainer(final IsWidget field1, final IsWidget field2, double part) {
        super();

        CssFloatLayoutContainer.CssFloatData layout1 = DEFAULT1;
        CssFloatLayoutContainer.CssFloatData layout2 = DEFAULT2;
        if (0 < part && part < 1) {
            layout1 = new CssFloatLayoutContainer.CssFloatData(part, M1);
            layout2 = new CssFloatLayoutContainer.CssFloatData(1 - part, M2);
        }

        add(field1, layout1);
        add(field2, layout2);
    }
}
