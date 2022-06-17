package ru.sbsoft.client.components.form;

import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;
import ru.sbsoft.svc.widget.core.client.button.TextButton;
import ru.sbsoft.svc.widget.core.client.event.SelectEvent;
import ru.sbsoft.svc.widget.core.client.event.SelectEvent.SelectHandler;
import ru.sbsoft.svc.widget.core.client.form.Field;
import ru.sbsoft.svc.widget.core.client.form.FormPanelHelper;
import ru.sbsoft.svc.widget.core.client.form.IsField;
import ru.sbsoft.svc.widget.core.client.form.ValueBaseField;
import ru.sbsoft.svc.widget.core.client.toolbar.SeparatorToolItem;
import ru.sbsoft.svc.widget.core.client.toolbar.ToolBar;
import ru.sbsoft.client.I18n;
import ru.sbsoft.client.components.BaseWindow;
import ru.sbsoft.client.components.grid.BaseGrid;
import ru.sbsoft.client.consts.SBFResources;
import ru.sbsoft.client.utils.ClientUtils;
import ru.sbsoft.client.utils.VLC;
import ru.sbsoft.common.Strings;
import ru.sbsoft.sbf.svc.utils.FieldUtils;
import ru.sbsoft.shared.FilterInfo;
import ru.sbsoft.shared.api.i18n.consts.SBFFormStr;
import ru.sbsoft.shared.api.i18n.consts.SBFGeneralStr;

import java.util.List;

/**
 * Базовый класс диалогов фильтра.
 *
 * @deprecated filter of this type is not used in browsers any more
 */
public abstract class BaseFormFilter extends BaseWindow {

    private SimplePageFormContainer container;
    private TextButton btnApply;
    private TextButton btnClear;

    public BaseFormFilter(final String header) {
        this(header, 150);
    }

    public BaseFormFilter(final String header, int labelWidth) {
        super();
        getWindow().getHeader().setIcon(SBFResources.BROWSER_ICONS.Filter16());
        getWindow().getHeader().setText(I18n.get(SBFFormStr.labelFilter));
        setModal(true);
        fillViewForm(labelWidth);
    }

    public abstract List<FilterInfo> getFilters();

    protected abstract void fillFilterPage(final SimplePageFormContainer pageContainer);

    protected abstract boolean setFilterValue(FilterInfo config);

    private void fillViewForm(int labelWidth) {
        final SelectHandler selectHandler = createSelectHandler();

        final ToolBar toolBar = new ToolBar();
        toolBar.setSpacing(2);
        toolBar.add(createButton(SBFResources.GENERAL_ICONS.Exit(), I18n.get(SBFGeneralStr.labelClose) + " / "
                + I18n.get(SBFGeneralStr.cancel), selectHandler));
        toolBar.add(btnApply = createButton(SBFResources.GENERAL_ICONS.OK(), I18n.get(SBFGeneralStr.labelApply), selectHandler));
        toolBar.add(new SeparatorToolItem());
        toolBar.add(btnClear = createButton(SBFResources.EDITOR_ICONS.Undo(), I18n.get(SBFFormStr.labelClearFields), selectHandler));

        container = new SimplePageFormContainer(labelWidth);
        container.getElement().getStyle().setBackgroundColor("white");
        fillFilterPage(container);
        container.updateLabels();

        addRegion(toolBar, VLC.CONST);
        addRegion(container, VLC.FILL);
    }

    protected TextButton createButton(ImageResource icon, String tooltip, SelectHandler handler) {
        final TextButton button = new TextButton(Strings.EMPTY, icon);
        if (tooltip != null) {
            button.setToolTip(tooltip);
        }
        button.addSelectHandler(handler);
        return button;
    }

    private SelectHandler createSelectHandler() {
        return new SelectHandler() {
            @Override
            public void onSelect(SelectEvent event) {
                if (event.getSource() == btnClear) {
                    clearFields();
                    return;
                }

                if (event.getSource() == btnApply) {
                    String message = validate() ? getCustomValidateMessage() : I18n.get(SBFFormStr.msgNotFilledFields);
                    if (message != null) {
                        ClientUtils.message(I18n.get(SBFFormStr.captCheckData), message);
                        return;
                    }
                    // grid.setFilters(getFilters());
                }

                hide();
            }
        };
    }

    private void clearFields() {
        clearFields(container);
    }

    private static void clearFields(HasWidgets container) {
        for (Widget widget : container) {
            if (widget instanceof TextFilterField) {
                ((TextFilterField) widget).reinit();
            } else if (widget instanceof ValueBaseField) {
                ValueBaseField valueBaseField = (ValueBaseField) widget;
                if (FieldUtils.hasEmptyValidator(valueBaseField)) {
                    if (valueBaseField.getOriginalValue() != null) {
                        valueBaseField.setValue(valueBaseField.getOriginalValue());
                    }
                } else {
                    valueBaseField.clear();
                }
            } else if (widget instanceof IsField) {
                ((IsField) widget).clear();
            } else if (widget instanceof HasWidgets) {
                clearFields((HasWidgets) widget);
            }
        }
    }

    public final void setFilters(BaseGrid grid, List<FilterInfo> filters) {
        clearFields();
        for (FilterInfo filter : filters) {
            setFilterValue(filter);
        }
    }

    private boolean validate() {
        boolean ok = true;
        final List<IsField<?>> fields = FormPanelHelper.getFields(getWindow());
        for (IsField<?> field : fields) {
            if (field instanceof Field) {
                 field.finishEditing();
            }
            ok &= field.isValid(true);
        }
        return ok;
    }

    public boolean checkValid() {
        return validate() && getCustomValidateMessage() == null;
    }

    public String getCustomValidateMessage() {
        return null;
    }

    @Override
    public void show() {
        super.show();
        final List<IsField<?>> fields = FormPanelHelper.getFields(getWindow());
        for (IsField<?> field : fields) {
            if (field instanceof Field) {
                 field.finishEditing();
            }
            field.validate(false);
        }
    }
}
