package ru.sbsoft.client.components.lookup;

import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Event.NativePreviewEvent;
import ru.sbsoft.svc.core.client.dom.XElement;
import ru.sbsoft.svc.widget.core.client.menu.Menu;
import ru.sbsoft.client.components.form.LookupField;
import ru.sbsoft.client.components.grid.BaseGrid;

/**
 * @author balandin
 * @since Jul 12, 2013 2:44:49 PM
 */
public class LookupGridMenu extends Menu {

    private final LookupField field;
    private final BaseGrid grid;

    public LookupGridMenu(LookupField field, BaseGrid grid) {
        super();

        this.field = field;
        this.grid = grid;

        add(grid);
        plain = true;
        showSeparator = false;
        setFocusOnShow(false);
        setEnableScrolling(false);
    }

    @Override
    public void focus() {
        super.focus();
        grid.getGrid().focus();
    }

    @Override
    protected void onPreviewEvent(NativePreviewEvent pe) {
        if (pe.getTypeInt() == Event.ONKEYPRESS) {
            return;
        }
        int type = pe.getTypeInt();
        switch (type) {
            case Event.ONMOUSEDOWN:
            case Event.ONMOUSEWHEEL:
            case Event.ONSCROLL:
                XElement target = pe.getNativeEvent().getEventTarget().cast();
                if (ff(field.getFieldKey(), target) || ff(field.getFieldName(), target)) {
                    return;
                }
                if (!getElement().isOrHasChild(target)) {
                    hide(true);
                }
        }

    }

    private boolean ff(LookupComboBox cb, XElement target) {
        return cb.getCell().getAppearance().triggerIsOrHasChild(cb.getElement(), target);
    }
}
