package ru.sbsoft.client.components;

import com.google.gwt.user.client.ui.Widget;
import ru.sbsoft.svc.widget.core.client.Window;
import ru.sbsoft.client.components.form.BaseForm;

/**
 *
 * @author Fedor Resnyanskiy
 */
public class ElementUtils {

    private ElementUtils() {
    }

//    public static <T> T findParent(IHasParentElement element, Class<T> clazz) {
//        final IElementContainer parent = element.getParentElement();
//
//        if (element == parent) {
//            return null;
//        }
//
//        if (parent.getClass() == clazz) {
//            return (T) parent;
//        }
//
//        if (parent instanceof IHasParentElement) {
//            return findParent((IHasParentElement) parent, clazz);
//        }
//
//        return null;
//    }
//
//    public static IWindow findWindow(IHasParentElement element) {
//        return findParent(element, IWindow.class);
//    }
    @Deprecated
    public static BaseForm findForm(Widget w) {
        if (w == null) {
            return null;
        }
        Widget parent = w.getParent();
        while (parent != null) {
            if (parent instanceof Window) {
                return (BaseForm) BaseForm.forSvcWindow((Window) parent);
            }
            parent = parent.getParent();
        }
        return null;
    }

    @Deprecated
    public static BaseWindow findWindow(Widget w) {
        if (w == null) {
            return null;
        }
        Widget parent = w.getParent();
        while (parent != null) {
            if (parent instanceof Window) {
                return (BaseWindow) BaseWindow.forSvcWindow((Window) parent);
            }
            parent = parent.getParent();
        }
        return null;
    }
}
