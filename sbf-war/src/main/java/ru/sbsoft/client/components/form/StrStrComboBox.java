package ru.sbsoft.client.components.form;

import com.google.gwt.core.client.GWT;
import ru.sbsoft.svc.cell.core.client.form.ComboBoxCell;
import ru.sbsoft.svc.widget.core.client.form.SimpleComboBox;
import ru.sbsoft.client.components.form.model.StrStrComboBoxModel;
import ru.sbsoft.client.components.form.model.StrStrComboBoxProperties;

/**
 * Визуальный компонент для отображения выпадающего списка пар, состоящих из строкового значения, отображаемого пользователю, и идентификатора типа string.
 * Для задания значение надо использовать метод {@link #add(java.lang.Object)} с {@link StrStrComboBoxModel}
 */
public class StrStrComboBox extends SimpleComboBox<StrStrComboBoxModel> {

    public static final StrStrComboBoxProperties SIMPLE_PROP = GWT.create(StrStrComboBoxProperties.class);

    public StrStrComboBox() {
        super(SIMPLE_PROP.name());
        this.setForceSelection(true);
        this.setTypeAhead(true);
        this.setTriggerAction(ComboBoxCell.TriggerAction.ALL);
        this.setEditable(false);
    }

    public String getStrValue() {
        if (null == getValue()) {
            return null;
        }
        return getValue().getId();
    }

    private StrStrComboBoxModel findElement(final String value) {
        if (null == value) {
            return null;
        }
        for (int i = 0; i < getStore().size(); i++) {
            if (getStore().get(i).getId().equals(value)) {
                return getStore().get(i);
            }
        }
        return null;
    }

    public void setStrValue(final String value, boolean fireEvent) {
        setValue(findElement(value), fireEvent);
    }

    public void setStrValue(final String value) {
        setStrValue(value, false);
    }
}
