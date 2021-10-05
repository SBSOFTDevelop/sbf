package ru.sbsoft.client.components.form;

import ru.sbsoft.client.components.form.model.LongStrComboBoxModel;
import ru.sbsoft.client.components.form.model.LongStrComboBoxProperties;
import com.google.gwt.core.client.GWT;
import com.sencha.gxt.cell.core.client.form.ComboBoxCell;
import com.sencha.gxt.widget.core.client.form.SimpleComboBox;

public class LongStrComboBox extends SimpleComboBox<LongStrComboBoxModel> {

    public static final LongStrComboBoxProperties SIMPLE_PROP = GWT.create(LongStrComboBoxProperties.class);

    public LongStrComboBox() {
        super(SIMPLE_PROP.name());
        this.setForceSelection(true);
        this.setTypeAhead(true);
        this.setTriggerAction(ComboBoxCell.TriggerAction.ALL);
        this.setEditable(false);
    }

    public Long getLongValue() {
        final LongStrComboBoxModel currentValue = getCurrentValue();
        return currentValue != null ? currentValue.getId() : null;
    }

    private LongStrComboBoxModel findElement(final Long value) {
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

    public void setLongValue(final Long value, boolean fireEvent) {
        setValue(findElement(value), fireEvent);
    }

    public void setLongValue(final Long value) {
        setLongValue(value, false);
    }
}
