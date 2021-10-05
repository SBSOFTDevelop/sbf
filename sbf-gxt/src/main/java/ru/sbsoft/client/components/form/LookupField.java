package ru.sbsoft.client.components.form;

import ru.sbsoft.sbf.gxt.utils.FieldUtils;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.editor.client.EditorError;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.ui.SimplePanel;
import com.sencha.gxt.messages.client.DefaultMessages;
import com.sencha.gxt.widget.core.client.Component;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.form.Validator;
import com.sencha.gxt.widget.core.client.form.error.DefaultEditorError;
import com.sencha.gxt.widget.core.client.menu.Item;
import com.sencha.gxt.widget.core.client.menu.Menu;
import com.sencha.gxt.widget.core.client.menu.MenuItem;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import ru.sbsoft.client.I18n;
import ru.sbsoft.client.components.AllowBlankControl;
import ru.sbsoft.sbf.gxt.components.FieldsContainer;
import ru.sbsoft.client.components.IFilterSource;
import ru.sbsoft.client.components.IFormMaker;
import ru.sbsoft.client.components.ILookupField;
import ru.sbsoft.client.components.ISelectBrowser;
import ru.sbsoft.client.components.ISelectBrowserMaker;
import ru.sbsoft.client.components.form.fields.ReadOnlyControl;
import ru.sbsoft.client.components.grid.BaseGrid;
import ru.sbsoft.client.components.lookup.LookupBrowserCell;
import ru.sbsoft.client.components.lookup.LookupComboBox;
import ru.sbsoft.client.components.lookup.LookupContext;
import ru.sbsoft.client.components.lookup.LookupGridMenu;
import ru.sbsoft.client.consts.SBFResources;
import ru.sbsoft.client.utils.ClientUtils;
import ru.sbsoft.client.utils.DefaultAsyncCallback;
import ru.sbsoft.client.utils.HLC;
import ru.sbsoft.common.Defs;
import ru.sbsoft.common.Strings;
import ru.sbsoft.shared.ComparisonEnum;
import ru.sbsoft.shared.filter.LookUpFilterInfo;
import ru.sbsoft.shared.filter.StringFilterInfo;
import ru.sbsoft.shared.model.LookupCellType;
import ru.sbsoft.shared.model.LookupInfoModel;
import ru.sbsoft.shared.api.i18n.consts.SBFEditorStr;

/**
 * Поле выбора связанной сущности при помощи встроенного браузера.
 *
 * @param <M> специальная модель данных, включающая идентификатор и отображаемы
 * значения для выбранной сущности.
 */
public class LookupField<M extends LookupInfoModel> extends CustomField<M>
        implements AllowBlankControl, ReadOnlyControl, ILookupField<M> {

    private IFilterSource filterSource;
    //
    private M value;
    private final List<M> multiValues = new ArrayList<M>();
    private Boolean multiValuesIsTemporal = null;
    //
    private final LookupContext lookupContext = new LookupContext();
    private final LookupComboBox fieldKey;
    private final SimplePanel keySeparator;
    private final LookupComboBox fieldName;
    //
    private TextButton btnMenu;
    private TextButton btnSelect;
    private TextButton btnClear;
    //
    private Menu contextMenu;
    private MenuItem menuSelect;
    private MenuItem menuOpen;
    private MenuItem menuClear;
    //
    private IFormMaker formMaker;
    private ISelectBrowserMaker selectBrowserMaker;
    private ISelectBrowser selectBrowser;
    //
    private Validator<M> emptyValidator;
    private boolean allowBlank = true;
    private boolean readOnly;
    private boolean disabledKeySearch;
    private boolean disabledNameSearch;
    //
    private LookupGridMenu menu;

    private static class FixedTextButton extends TextButton {

        public FixedTextButton(ImageResource icon) {
            super(Strings.EMPTY, icon);
        }

//        @Override
//        public void setPixelSize(int width, int height) {
//            // в родном компоненте не учитывается размер нижнего бордера
//            // в итоге общая высота компонента становится больше
//            super.setPixelSize(width, height - 3);
//        }
    }

    public LookupField() {
        this(false);
    }

    public LookupField(boolean shortFormat) {
        this(shortFormat, null, 4);
    }

    public LookupField(IFilterSource filterSource) {
        this(false, filterSource, 4);
    }

    public LookupField(boolean shortFormat, IFilterSource filterSource) {
        this(shortFormat, filterSource, 4);
    }

    public LookupField(boolean shortFormat, IFilterSource filterSource, int separatorWidth) {
        super();

        this.filterSource = filterSource;

        FieldsContainer container = new FieldsContainer();
        setWidget(container);

        fieldKey = new LookupComboBox(new LookupBrowserCell(this, lookupContext, LookupCellType.KEY));
        fieldKey.setWidth(125);
        container.add(fieldKey, HLC.CONST);
        keySeparator = FieldUtils.createSeparator(separatorWidth);
        container.add(keySeparator, HLC.CONST);

        fieldName = new LookupComboBox(new LookupBrowserCell(this, lookupContext, LookupCellType.NAME));
        fieldName.setWidth(100);
        container.add(fieldName, HLC.FILL);
        container.add(FieldUtils.createSeparator(separatorWidth), HLC.CONST);

        if (shortFormat) {

            btnSelect = new FixedTextButton(SBFResources.GENERAL_ICONS.Choose16());
            btnSelect.setToolTip(I18n.get(SBFEditorStr.menuChoice));
            btnSelect.addSelectHandler(new SelectEvent.SelectHandler() {
                @Override
                public void onSelect(SelectEvent event) {
                    selectItem();
                }
            });

            container.add(btnSelect, HLC.CONST);
            container.add(FieldUtils.createSeparator(separatorWidth), HLC.CONST);

            btnClear = new FixedTextButton(SBFResources.BROWSER_ICONS.PinClear16());
            btnClear.setToolTip(I18n.get(SBFEditorStr.menuClear));
            btnClear.addSelectHandler(new SelectEvent.SelectHandler() {
                @Override
                public void onSelect(SelectEvent event) {
                    setValue(null, true);
                }
            });

            container.add(btnClear, HLC.CONST);

        } else {

            btnMenu = new FixedTextButton(SBFResources.GENERAL_ICONS.Choose16());
            btnMenu.setToolTip(I18n.get(SBFEditorStr.menuChoice));
            btnMenu.setMenu(createMenu());

            container.add(btnMenu, HLC.CONST);
        }

        setFormMaker(null);
        updateButtonsState();
        updateFields();
    }

    private Menu createMenu() {
        menuSelect = new MenuItem(I18n.get(SBFEditorStr.menuChoice), SBFResources.GENERAL_ICONS.Choose16());
        menuSelect.addSelectionHandler(new SelectionHandler<Item>() {
            @Override
            public void onSelection(SelectionEvent<Item> event) {
                selectItem();
            }
        });

        menuOpen = new MenuItem(I18n.get(SBFEditorStr.menuItem), SBFResources.BROWSER_ICONS.FilterTot16());
        menuOpen.addSelectionHandler(new SelectionHandler<Item>() {
            @Override
            public void onSelection(SelectionEvent<Item> event) {
                showForm();
            }
        });

        menuClear = new MenuItem(I18n.get(SBFEditorStr.menuClear), SBFResources.BROWSER_ICONS.PinClear16());
        menuClear.addSelectionHandler(new SelectionHandler<Item>() {
            @Override
            public void onSelection(SelectionEvent<Item> event) {
                setValue(null, true);
            }
        });

        contextMenu = new Menu();
        contextMenu.add(menuSelect);
        contextMenu.add(menuOpen);
        contextMenu.add(menuClear);
        return contextMenu;
    }

    private void selectItem() {
        final ISelectBrowser browser = getSelectBrowser(true);
        if (browser != null) {
            browser.setOwnerLookupField(this);
            browser.getGrid().setMarkedLookup(getMultiValues());
            browser.show();
            browser.getGrid().checkInitialized();
        }
    }

    public boolean prepareForLookup(LookupCellType cellType, String value) {
        StringFilterInfo f = new StringFilterInfo(null, value);
        f.setType(cellType.getFilterType());
        return lookup(f);
    }

    private boolean lookup(StringFilterInfo filter) {
        if (filter != null) {
            getGrid().setOneTimeFilter(filter);
        }
        return (filter != null);
    }

    public boolean isEmpty() {
        return value == null && (multiValues == null || multiValues.isEmpty());
    }

    @Override
    public boolean isReadOnly() {
        return readOnly;
    }

    @Override
    public void setReadOnly(boolean readOnly) {
        this.readOnly = readOnly;
        updateButtonsState();
        updateFields();
    }

    private class EmptyValidator implements Validator<M> {

        @Override
        public List<EditorError> validate(Editor<M> editor, M value) {
            if (isEmpty()) {
                List<EditorError> errors = new ArrayList<EditorError>();
                errors.add(new DefaultEditorError(editor, DefaultMessages.getMessages().textField_blankText(), ""));
                return errors;
            }
            return null;
        }
    }

    @Override
    public final void setAllowBlank(boolean value) {
        if (!value) {
            if (emptyValidator == null) {
                emptyValidator = new EmptyValidator();
            }
            if (!getValidators().contains(emptyValidator)) {
                getValidators().add(0, emptyValidator);
            }
        } else if (emptyValidator != null) {
            removeValidator(emptyValidator);
        }
        this.allowBlank = value;
        updateEnabledClear();
    }

    @Override
    public boolean isAllowBlank() {
        return allowBlank;
    }

    @Override
    public M getValue() {
        return value;
    }

    private void fireValueChangeEvent() {
        ValueChangeEvent.fire(this, value);
    }

    @Override
    public void setValue(final M value, boolean fireEvents) {
        multiValues.clear();

        fieldKey.finishEditing();
        fieldName.finishEditing();

        boolean changed = !ClientUtils.equals(this.value, value);
        this.value = value;

        String defValue = (value == null || value.getSemanticID() == null) ? null : "???";
        fieldKey.setValue(value == null ? null : Strings.coalesce(value.getSemanticKey(), defValue));
        fieldName.setValue(value == null ? null : Strings.coalesce(value.getSemanticName(), defValue));

        ((LookupBrowserCell) fieldKey.getCell()).initSearchHistory();
        ((LookupBrowserCell) fieldName.getCell()).initSearchHistory();

        if (changed && fireEvents) {
            fireValueChangeEvent();
        }

        fieldKey.validate();
        fieldName.validate();

        updateButtonsState();
    }

    public void internalClearValue(boolean fireEvents) {
        if (this.value == null) {
            return;
        }
        this.value = null;

        fieldKey.validate();
        fieldName.validate();

        if (fireEvents) {
            fireValueChangeEvent();
        }

        updateButtonsState();
    }

    @Override
    public void setValue(M value) {
        setValue(value, false);
    }

    @Override
    public void setValues(List<M> values) {
        setValues(values, true);
    }

    @Override
    public void setValues(List<M> values, boolean fireEvents) {
        if (values == null || values.isEmpty()) {
            setValue(null, fireEvents);
        } else if (values.size() == 1) {
            setValue(values.get(0), fireEvents);
        } else {
//            boolean temporal = getGrid().getMetaInfo().getTemporalKey() != null;
//            for (final M model : values) {
//                if (temporal && model.getSemanticID() == null) {
//                    throw new IllegalStateException(I18n.get(SBFExceptionStr.differentItems, values.get(0).toString(), model.toString()));
//                }
//            }
            setMultiValues(values, /*temporal,*/ fireEvents);
        }
        updateEnabledSelect();
        updateEnabledClear();
    }

    public String initMultySelectedFieldName(final List<M> markedRecords) {
        return multiValues.isEmpty() ? Strings.EMPTY : I18n.get(SBFEditorStr.labelMultipleChoice);
    }

    public String initMultySelectedFieldKey(final List<M> markedRecords) {
        return multiValues.isEmpty() ? Strings.EMPTY : "...";
    }

    private void setMultiValues(final List<M> markedRecords, /*boolean temporal,*/ boolean fireEvents) {
        final boolean changed = !ClientUtils.equals(multiValues, markedRecords);

        setValue(null, fireEvents);

        multiValues.clear();
        multiValues.addAll(Defs.coalesce(markedRecords, Collections.EMPTY_LIST));

        //multiValuesIsTemporal = Boolean.valueOf(temporal);
        fieldKey.finishEditing();
        fieldKey.setValue(initMultySelectedFieldKey(markedRecords));

        fieldKey.validate();

        fieldName.finishEditing();
        fieldName.initValue(initMultySelectedFieldName(markedRecords));
        fieldName.validate();

        updateEnabledSelect();
        updateEnabledClear();

        if (changed && fireEvents) {
            fireValueChangeEvent();
        }
    }

    public List<M> getMultiValues() {
        return new ArrayList<>(multiValues);
    }

    public final void setFormMaker(IFormMaker formMaker) {
        this.formMaker = formMaker;
        updateButtonsState();
    }

    private void showForm() {
        if (value == null) {
            return;
        }

        final BigDecimal id = value.getID();
        if (id == null) {
            // ACHTUNG
            return;
        }

        if (formMaker != null) {
            formMaker.createForm(new DefaultAsyncCallback<BaseForm>() {
                @Override
                public void onResult(BaseForm f) {
                    showForm(f, id);
                }
            });
            return;
        }

        final BaseGrid grid = getGrid();
        if (grid != null) {
            grid.showForm(id);
        }
    }

    private void showForm(BaseForm form, BigDecimal id) {
        if (form != null) {
            form.setOwnerGrid(null);
            form.show(id);
        }
    }

    public void setFilterSource(IFilterSource filterSource) {
        this.filterSource = filterSource;
    }

    public void setSelectBrowserMaker(final ISelectBrowserMaker selectBrowserMaker) {
        this.selectBrowserMaker = selectBrowserMaker;
        updateButtonsState();
        updateFields();
    }

    public LookupComboBox getFieldKey() {
        return fieldKey;
    }

    public LookupComboBox getFieldName() {
        return fieldName;
    }

    @Override
    public HandlerRegistration addValueChangeHandler(ValueChangeHandler<M> handler) {
        return addHandler(handler, ValueChangeEvent.getType());
    }

    public ISelectBrowser getSelectBrowser(final boolean initFilters) {
        if (selectBrowser == null) {
            if (selectBrowserMaker != null) {
                selectBrowser = selectBrowserMaker.createBrowser();
                selectBrowser.getGrid().setClearMarksOnDataLoad(false);
            }
        }
        if (selectBrowser != null && filterSource != null && initFilters) {
            selectBrowser.getGrid().setParentFilters(filterSource.getFilters());
        }
        return selectBrowser;
    }

    protected BaseGrid getGrid() {
        final ISelectBrowser b = getSelectBrowser(false);
        return b == null ? null : b.getGrid();
    }

    public LookupGridMenu getGridMenu() {
        if (menu == null) {
            menu = new LookupGridMenu(this, getGrid());
        }
        return menu;
    }

    private void updateFields() {
        if (lookupContext.isExpanded()) {
            fieldKey.collapse();
        }

        updateField(fieldKey, readOnly || disabledKeySearch);
        updateField(fieldName, readOnly || disabledNameSearch);
    }

    private void updateField(LookupComboBox field, boolean ro) {
        field.setReadOnly(ro);
        field.setHideTrigger(ro);
    }

    public void setFieldKeyWidth(int value) {
        this.fieldKey.setWidth(value);
    }

    private void updateButtonsState() {
        boolean r1 = updateEnabledSelect();
        boolean r2 = updateEnabledOpen();
        boolean r3 = updateEnabledClear();
        setEnabled(btnMenu, r1 || r2 || r3);
    }

    private boolean updateEnabledSelect() {
        boolean enabled = (!readOnly) && (selectBrowserMaker != null);
        setEnabled(menuSelect, enabled);
        setEnabled(btnSelect, enabled);
        return enabled;
    }

    private boolean updateEnabledOpen() {
        boolean enabled = (formMaker != null) || (selectBrowserMaker != null);
        enabled &= (value != null && value.getID() != null);
        setEnabled(menuOpen, enabled);
        return enabled;
    }

    private boolean updateEnabledClear() {
        boolean enabled = (!readOnly) && (!isEmpty()) && allowBlank;
        setEnabled(menuClear, enabled);
        setEnabled(btnClear, enabled);
        return enabled;
    }

    private void setEnabled(Component component, boolean enabled) {
        if (component != null) {
            component.setEnabled(enabled);
        }
    }

    public void setKeyFieldVisible(boolean visible) {
        fieldKey.setVisible(visible);
        keySeparator.setVisible(visible);
    }

    public LookUpFilterInfo createLookUpFilter(String fieldName) {
        final LookUpFilterInfo result = new LookUpFilterInfo(fieldName, (List<LookupInfoModel>) getLookupValues());
        result.setComparison(ComparisonEnum.eq);
        return result;
    }

    public List<M> getLookupValues() {
        if (value != null) {
            return Arrays.asList(value);
        }
        if (getGrid().isMarksAllowed() && multiValues != null && !multiValues.isEmpty()) {
            return getMultiValues();
        }
        return null;
    }

    public LookupField disableKeySearch() {
        return setKeySearch(false);
    }

    public boolean isDisabledKeySearch() {
        return disabledKeySearch;
    }

    public LookupField setKeySearch(boolean value) {
        this.disabledKeySearch = !value;
        updateFields();
        return this;
    }

    public LookupField disableNameSearch() {
        return setNameSearch(false);
    }

    public boolean isDisabledNameSearch() {
        return disabledNameSearch;
    }

    public LookupField setNameSearch(boolean value) {
        this.disabledNameSearch = !value;
        updateFields();
        return this;
    }

    public void onExpand() {
    }

    @Override
    public void selectValue(Runnable callback) {
        final BaseGrid g = getGrid();
        if (g.isMarksAllowed() && g.getTotalMarkedRecordsCount() > 0) {
            g.lookup(this, g.getMarkedRecords(), callback);
        } else if (g.getCurrentRecord() != null) {
            g.lookup(this, Collections.singletonList(g.getCurrentRecord().getRECORD_ID()), callback);
        }
    }

    public void setEmptyText(String text) {
        fieldName.setEmptyText(text);
    }
}
