package ru.sbsoft.client.components.form;

import ru.sbsoft.sbf.gxt.utils.FieldUtils;
import com.google.gwt.event.logical.shared.HasValueChangeHandlers;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.form.TextField;
import com.sencha.gxt.widget.core.client.form.Validator;
import ru.sbsoft.client.components.AllowBlankControl;
import ru.sbsoft.sbf.gxt.components.FieldsContainer;
import ru.sbsoft.client.components.form.fields.ReadOnlyControl;
import ru.sbsoft.client.consts.SBFResources;
import ru.sbsoft.client.utils.HLC;
import ru.sbsoft.common.Strings;

/**
 * Просто текстовое поле с кнопкой. 
 * В прикладном коде можно на кнопку повесить какое-либо действие.
 * @author balandin
 * @since Sep 25, 2013 1:18:42 PM
 * @deprecated NO USAGES FOUND
 */
public class TextLookupField extends CustomField<String>
	implements AllowBlankControl, ReadOnlyControl, HasValueChangeHandlers<String> {

    protected final TextButton button;
    protected final TextField field;

    public TextLookupField() {
        super();

        field = new TextField();
        button = new TextButton(Strings.EMPTY, SBFResources.APP_ICONS.Table());

        FieldsContainer container = new FieldsContainer();
        container.add(field, HLC.FILL);
        container.add(FieldUtils.createSeparator(), HLC.CONST);
        container.add(button, HLC.CONST);
		setWidget(container);
    }

    @Override
    public void addValidator(Validator<String> validator) {
        field.addValidator(validator);
    }

    @Override
    public void setValue(String value) {
        field.setValue(value);
    }

    public void setValue(final String value, boolean fireEvents) {
        field.setValue(value, fireEvents);
    }

    @Override
    public String getValue() {
        return field.getValue();
    }

    @Override
    public void setAllowBlank(boolean value) {
        field.setAllowBlank(value);
    }

    @Override
    public boolean isAllowBlank() {
        return field.isAllowBlank();
    }

    @Override
    public HandlerRegistration addValueChangeHandler(ValueChangeHandler<String> handler) {
        return field.addValueChangeHandler(handler);
    }

    public boolean isReadOnly() {
        return field.isReadOnly();
    }

    public void setReadOnly(boolean readOnly) {
        if (readOnly != isReadOnly()) {
            field.setReadOnly(readOnly);
            button.setEnabled(!readOnly);
        }
    }

    public TextButton getButton() {
        return button;
    }

    public TextField getField() {
        return field;
    }
}
