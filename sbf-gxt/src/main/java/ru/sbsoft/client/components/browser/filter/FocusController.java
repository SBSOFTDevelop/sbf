package ru.sbsoft.client.components.browser.filter;

import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.Component;
import com.sencha.gxt.widget.core.client.container.Container;
import com.sencha.gxt.widget.core.client.event.FocusEvent;

/**
 * @author balandin
 * @since Nov 9, 2015
 */
public class FocusController {

    private static final String FOCUS_MARK = "focus";

    private FocusController() {
    }

    public static void applyFocusListener(Container container, FocusEvent.FocusHandler handler) {
        for (int i = 0, size = container.getWidgetCount(); i < size; i++) {
            final Widget w = container.getWidget(i);
            if (w instanceof Container) {
                applyFocusListener((Container) w, handler);
            } else if (w instanceof Component) {
                applyFocusListener((Component) w, handler);
            }
        }
    }

    public static void applyFocusListener(Component component, FocusEvent.FocusHandler handler) {
        HandlerRegistration h = (HandlerRegistration) component.getData(FOCUS_MARK);
        if (h == null) {
            h = component.addFocusHandler(handler);
            component.setData(FOCUS_MARK, h);
        }
    }

    public static void removeFocusListener(Widget w) {
        if (w instanceof Component) {
            Component component = (Component) w;
            HandlerRegistration h = (HandlerRegistration) component.getData(FOCUS_MARK);
            if (h != null) {
                h.removeHandler();
                component.setData(FOCUS_MARK, null);
            }
        }
    }
}
