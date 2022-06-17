package ru.sbsoft.client.components.grid.dlgbase;

import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.ui.Image;
import ru.sbsoft.svc.core.client.util.Margins;
import ru.sbsoft.svc.data.shared.ListStore;
import ru.sbsoft.svc.widget.core.client.container.SimpleContainer;
import ru.sbsoft.client.components.browser.filter.FieldsComboBox;
import ru.sbsoft.client.components.browser.filter.fields.CustomHolder;
import ru.sbsoft.client.consts.SBFResources;
import ru.sbsoft.client.utils.HLC;
import ru.sbsoft.client.utils.HLD;

/**
 *
 * @author Kiselev
 */
public class Item extends ItemBase {

    public static final HorizontalLayoutData MARGINS = new HLD(-1, 1, new Margins(0, 0, 0, 2));

    protected final FieldsComboBox fields;
    private final SimpleContainer error;

    public Item(ListStore<CustomHolder> store) {
        error = new SimpleContainer();
        error.setWidth("24px");
        error.getElement().getStyle().setPaddingTop(5, Style.Unit.PX);
        error.getElement().getStyle().setPaddingLeft(5, Style.Unit.PX);

        fields = new FieldsComboBox(store, null);

        addLeader();
        add(fields, HLC.CONST);
    }
    
    public void initFocus(){
        if(fields.isVisible()){
            fields.focus();
            fields.expand();
        }
    }

    public FieldsComboBox getFields() {
        return fields;
    }

    public SimpleContainer getErrorControl() {
        return error;
    }

    @Override
    public void clearValue() {
        fields.setValue(null, true);
    }

    @Override
    public void forceLayout() {
        if (isAttached()) {
            final int splitWidth = 35 * (getRootContainer().getDepth() - getLevel());
            error.setLayoutData(new HLD(-1, 1, new Margins(0, 0, 0, splitWidth)));
        }
        super.forceLayout();
    }

    public void setError(String message) {
        if (message == null) {
            error.clear();
            error.setToolTipConfig(null);
        } else {
            error.setWidget(new Image(SBFResources.GENERAL_ICONS.About16()));
            error.setToolTip(message);
        }
    }

}
