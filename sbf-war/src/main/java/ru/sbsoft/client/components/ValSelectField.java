package ru.sbsoft.client.components;

import com.google.gwt.core.client.Callback;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.editor.client.EditorError;
import com.google.gwt.event.logical.shared.HasValueChangeHandlers;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import ru.sbsoft.svc.widget.core.client.button.SplitButton;
import ru.sbsoft.svc.widget.core.client.button.TextButton;
import ru.sbsoft.svc.widget.core.client.form.TextField;
import ru.sbsoft.svc.widget.core.client.form.Validator;
import ru.sbsoft.svc.widget.core.client.form.error.DefaultEditorError;
import java.util.Collections;
import java.util.List;
import ru.sbsoft.client.I18n;
import ru.sbsoft.client.components.AllowBlankControl;
import ru.sbsoft.client.components.actions.AbstractAction;
import ru.sbsoft.client.components.actions.Action;
import ru.sbsoft.client.components.actions.ActionManager;
import ru.sbsoft.client.components.actions.ActionMenu;
import ru.sbsoft.client.components.actions.ButtonIconSize;
import ru.sbsoft.client.components.form.CustomField;
import ru.sbsoft.client.consts.SBFResources;
import ru.sbsoft.client.utils.ClientUtils;
import ru.sbsoft.client.utils.DefaultAsyncCallback;
import ru.sbsoft.client.utils.HLC;
import ru.sbsoft.common.Strings;
import ru.sbsoft.sbf.svc.components.FieldsContainer;
import ru.sbsoft.sbf.svc.utils.FieldUtils;
import ru.sbsoft.shared.FilterInfo;
import ru.sbsoft.shared.api.i18n.consts.SBFEditorStr;
import ru.sbsoft.shared.api.i18n.consts.SBFGeneralStr;

/**
 *
 * @author Kiselev
 */
public class ValSelectField<T> extends CustomField<T> implements HasValueChangeHandlers<T>, AllowBlankControl {

    private final TextButton button;

    private final TextField field;
    private final IValSelectHandler<T> h;
    private T val = null;
    private boolean allowBlank = true;

    private boolean isCleansable = false;

    public ValSelectField(IValSelectHandler<T> h) {
        this(h, false);
    }

    public ValSelectField(IValSelectHandler<T> h, boolean isCleansable) {
        this.h = h;
        this.isCleansable = isCleansable;

        field = new TextField();
        field.setReadOnly(true);
        field.setAllowTextSelection(true);

        addValidator(new Validator<T>() {
            @Override
            public List<EditorError> validate(Editor<T> editor, T value) {
                if (!allowBlank && getValue() == null) {
                    return Collections.<EditorError>singletonList(new DefaultEditorError(editor, I18n.get(SBFGeneralStr.msgNeedFill), null));
                }

                clearInvalid();
                return null;
            }
        });

        //ImageResource treeImg = h instanceof TreeSelector<?> ? SBFResources.TREEMENU_ICONS.node_select_all() : SBFResources.APP_ICONS.Table();
        button = new SplitButton();

        Action selAction = new AbstractAction(I18n.get(SBFEditorStr.menuChoice), I18n.get(SBFEditorStr.menuChoice), SBFResources.GENERAL_ICONS.Choose16(), SBFResources.GENERAL_ICONS.Choose16()) {
            @Override
            protected void onExecute() {
                ValSelectField.this.h.selectVal(val, new DefaultAsyncCallback<T>() {
                    @Override
                    public void onResult(T result) {
                        applyChoice(result);
                    }
                });
            }

        };

        Action clearAction = new AbstractAction(I18n.get(SBFEditorStr.menuClear), I18n.get(SBFEditorStr.menuClear), SBFResources.BROWSER_ICONS.PinClear16(), SBFResources.BROWSER_ICONS.PinClear16()) {
            @Override
            protected void onExecute() {
                applyChoice(null);
            }

            @Override
            public boolean checkEnabled() {
                return ValSelectField.this.isCleansable && getValue() != null;
            }

        };

        ActionManager actionManager = new ActionManager();
        ActionMenu menu = new ActionMenu(actionManager, false);

        actionManager.bindButtonWithAction(button, selAction, ButtonIconSize.large);
        menu.addAction(selAction);
        menu.addAction(clearAction);

        button.setMenu(menu);

        FieldsContainer container = new FieldsContainer();
        container.add(field, HLC.FILL);
        container.add(FieldUtils.createSeparator(4), HLC.CONST);
        container.add(button, HLC.CONST);

        setWidget(container);
    }

    public void setCleansable(boolean isCleansable) {
        this.isCleansable = isCleansable;
    }
    
    protected void applyChoice(T value){
        setValue(value, true);
    }

    @Override
    public void setValue(T value) {
        setValue(value, true);
    }

    public void setValue(T value, boolean fireEvents) {
        T oldVal = val;
        val = value;
        field.setValue(val == null ? Strings.EMPTY : h.toString(val));

        if (fireEvents) {
            ValueChangeEvent.fireIfNotEqual(this, oldVal, value);
        }
    }

    public void setInitValue(T value, String displayValue) {
        T oldVal = val;
        val = value;
        field.setValue(displayValue);
        ValueChangeEvent.fireIfNotEqual(this, oldVal, value);
    }

    public String getText() {
        return field.getValue() == null ? Strings.EMPTY : field.getText();
    }

    @Override
    public T getValue() {
        return val;
    }

    @Override
    public boolean isReadOnly() {
        return !button.isEnabled();
    }

    @Override
    public void setReadOnly(boolean readOnly) {
        button.setEnabled(!readOnly);
    }

    @Override
    public HandlerRegistration addValueChangeHandler(ValueChangeHandler<T> handler) {
        return addHandler(handler, ValueChangeEvent.getType());
    }

    public void setSelectFilter(List<FilterInfo> filters) {
        setSelectFilter(filters, true);
    }

    public void setSelectFilter(List<FilterInfo> filters, final boolean clearIsNotExist) {
        final T oldVal = val;

        if (clearIsNotExist) {
            setValue(null, false);
        }
        h.setFilter(filters, new Callback<Void, Throwable>() {
            @Override
            public void onFailure(Throwable reason) {
                ClientUtils.alertException(reason);
            }

            @Override
            public void onSuccess(Void result) {
//                if (val != null && !h.isValExists(val)) {
//                    setValue(null, false);
//                }
                if (oldVal != null && h.isValExists(oldVal) && clearIsNotExist) {
                    setValue(oldVal, false);
                }
            }
        });
    }

    @Override
    public void setAllowBlank(boolean value) {
        this.allowBlank = value;
    }

    @Override
    public boolean isAllowBlank() {
        return allowBlank;
    }
}
