package ru.sbsoft.client.components.form;

import ru.sbsoft.sbf.svc.components.VerticalFieldSet;
import ru.sbsoft.svc.widget.core.client.form.TextArea;

/**
 * Текстовая область (Area) с заголовком.
 * @author Sokoloff
 */
public class TextFieldSet extends VerticalFieldSet {

    private final static int TEXT_HEIGHT = 40;
    private TextArea field;

    public TextFieldSet(String caption) {
        super(caption);

        field = new TextArea();
        field.setHeight(TEXT_HEIGHT);
        addField(field);
    }

    public boolean isAllowBlank() {
        return field.isAllowBlank();
    }

    public void setAllowBlank(boolean allowBlank) {
        field.setAllowBlank(allowBlank);
    }

    public TextArea getFieldText() {
        return field;
    }

    public void setFieldTextHeight(int height) {
        field.setHeight(height);
    }

    public String getValue() {
        return field.getValue();
    }

    public void setValue(final String value) {
        field.setValue(value);
    }
}
