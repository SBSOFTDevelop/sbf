package ru.sbsoft.client.components.form;

import com.google.gwt.core.client.GWT;
import com.sencha.gxt.cell.core.client.form.ComboBoxCell;
import com.sencha.gxt.widget.core.client.form.SimpleComboBox;
import ru.sbsoft.client.components.form.model.IntStrComboBoxModel;
import ru.sbsoft.client.components.form.model.IntStrComboBoxProperties;

/**
 * Визуальный компонент для отображения выпадающего списка пар, состоящих из строкового значения, отображаемого пользователю, и идентификатора типа integer.
 * Для задания значение надо использовать метод {@link #add(java.lang.Object)} с {@link IntStrComboBoxModel}
 */
public class IntStrComboBox extends SimpleComboBox<IntStrComboBoxModel> {

    public static final IntStrComboBoxProperties SIMPLE_PROP = GWT.create(IntStrComboBoxProperties.class);

    public IntStrComboBox() {
        super(SIMPLE_PROP.name());
        this.setForceSelection(true);
        this.setTypeAhead(true);
        this.setTriggerAction(ComboBoxCell.TriggerAction.ALL);
        this.setEditable(false);
    }

    public Integer getIntValue() {
        final IntStrComboBoxModel currentValue = getCurrentValue();
        return currentValue != null ? currentValue.getId() : null;
    }

    private IntStrComboBoxModel findElement(final Integer value) {
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

    public void setIntValue(final Integer value, boolean fireEvent) {
        setValue(findElement(value), fireEvent);
    }

    public void setIntValue(final Integer value) {
        setIntValue(value, false);
    }
}
