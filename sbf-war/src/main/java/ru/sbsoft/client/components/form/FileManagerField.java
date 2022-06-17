package ru.sbsoft.client.components.form;

import ru.sbsoft.sbf.svc.components.FieldsContainer;
import ru.sbsoft.sbf.svc.utils.FieldUtils;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.logical.shared.HasValueChangeHandlers;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Widget;
import ru.sbsoft.svc.core.client.Style;
import ru.sbsoft.svc.widget.core.client.button.TextButton;
import ru.sbsoft.svc.widget.core.client.form.TextField;
import ru.sbsoft.svc.widget.core.client.menu.Item;
import ru.sbsoft.svc.widget.core.client.menu.Menu;
import ru.sbsoft.svc.widget.core.client.menu.MenuItem;
import ru.sbsoft.client.I18n;
import ru.sbsoft.client.components.*;
import ru.sbsoft.client.components.form.fields.ReadOnlyControl;
import ru.sbsoft.client.consts.SBFResources;
import ru.sbsoft.client.utils.HLC;
import ru.sbsoft.common.Strings;
import ru.sbsoft.shared.api.i18n.consts.SBFGeneralStr;

/**
 *
 * @author Sokoloff
 */
public class FileManagerField
        extends CustomField<String>
        implements HasValueChangeHandlers<String>, ReadOnlyControl, IHasParentElement {

    private final String EMPTY_VALUE = '<' + I18n.get(SBFGeneralStr.labelFileNotLoaded) + '>';
    private MenuItem menuLoad;
    private MenuItem menuUnload;
    private MenuItem menuClear;
    //
    private String value;
    private final TextField fieldFileName;
    private boolean readOnly;

    private final DefaultContainerHolder containerHolder = new DefaultContainerHolder();

    public FileManagerField() {
        FieldsContainer container = new FieldsContainer();
        setWidget(container);
        fieldFileName = new TextField();
        fieldFileName.setWidth(100);
        fieldFileName.setReadOnly(true);
        readOnly = false;
        container.add(fieldFileName, HLC.FILL);
        container.add(FieldUtils.createSeparator(), HLC.CONST);
        TextButton btnMenu = new TextButton(Strings.EMPTY, SBFResources.GENERAL_ICONS.Choose16());
        btnMenu.setToolTip(I18n.get(SBFGeneralStr.labelSelect));
        btnMenu.setMenu(createMenu());
        container.add(btnMenu, HLC.CONST);
        menuClear.addSelectionHandler(new SelectionHandler<Item>() {
            @Override
            public void onSelection(SelectionEvent<Item> event) {
                setValue(null);
            }
        });
    }

    private Menu createMenu() {
        menuLoad = new MenuItem(I18n.get(SBFGeneralStr.labelLoad), SBFResources.APP_ICONS.LoadFile());
        menuUnload = new MenuItem(I18n.get(SBFGeneralStr.labelOpen), SBFResources.APP_ICONS.UnloadFile());
        menuClear = new MenuItem(I18n.get(SBFGeneralStr.labelClear), SBFResources.APP_ICONS.DeleteFile());
        //
        Menu contextMenu = new Menu() {
            @Override
            public void show(Element elem, Style.AnchorAlignment alignment) {
                show(elem, new Style.AnchorAlignment(Style.Anchor.TOP_RIGHT, Style.Anchor.BOTTOM_RIGHT, true), 0, 24);
            }
        };
        contextMenu.add(menuLoad);
        contextMenu.add(menuUnload);
        contextMenu.add(menuClear);
        return contextMenu;
    }

    public boolean isEmpty() {
        return null == value || value.isEmpty();
    }

    private void updateEnableLoad() {
        boolean isEnable = !readOnly;
        if (isEnable) {
            BaseForm form = ElementUtils.findForm((Widget) this);
            if (null != form) {

                isEnable = !form.isAppendMode();
            }
        }
        menuLoad.setEnabled(isEnable);
    }

    private void updateEnableUnload() {
        menuUnload.setEnabled(!isEmpty());
    }

    private void updateEnableClear() {
        menuClear.setEnabled(!isEmpty());
    }

    private void updateEnableFields() {
        updateEnableLoad();
        updateEnableUnload();
        updateEnableClear();
    }

    @Override
    public void setValue(String value) {
        this.value = value;
        fieldFileName.finishEditing();
        if (isEmpty()) {
            fieldFileName.setValue(EMPTY_VALUE);
        } else {
            fieldFileName.setValue(value);
        }
        fireValueChangeEvent();
        updateEnableFields();
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public boolean isReadOnly() {
        return readOnly;
    }

    @Override
    public void setReadOnly(boolean readOnly) {
        this.readOnly = readOnly;
        updateEnableFields();
    }

    private void fireValueChangeEvent() {
        ValueChangeEvent.fire(this, value);
    }

    public HandlerRegistration addLoadHandler(SelectionHandler<Item> handler) {
        return menuLoad.addSelectionHandler(handler);
    }

    public HandlerRegistration addUnloadHandler(SelectionHandler<Item> handler) {
        return menuUnload.addSelectionHandler(handler);
    }

    public HandlerRegistration addClearHandler(SelectionHandler<Item> handler) {
        return menuClear.addSelectionHandler(handler);
    }

    @Override
    public HandlerRegistration addValueChangeHandler(ValueChangeHandler<String> handler) {
        return addHandler(handler, ValueChangeEvent.getType());
    }

    @Override
    public void setParentElement(IElementContainer parent) {
        containerHolder.setParentElement(parent);
    }

    @Override
    public IElementContainer getParentElement() {
        return containerHolder.getParentElement();
    }

}
