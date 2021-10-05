package ru.sbsoft.client.components.form;

import ru.sbsoft.client.components.form.model.BooleanStrComboBoxModel;
import ru.sbsoft.client.components.form.model.BooleanStrComboBoxProperties;
import com.google.gwt.core.client.GWT;
import com.sencha.gxt.cell.core.client.form.ComboBoxCell;
import com.sencha.gxt.widget.core.client.form.SimpleComboBox;

public class BooleanStrComboBox extends SimpleComboBox<BooleanStrComboBoxModel> {

    public static final BooleanStrComboBoxProperties SIMPLE_PROP = GWT.create(BooleanStrComboBoxProperties.class);

    public BooleanStrComboBox() {
        super(SIMPLE_PROP.name());
        this.setForceSelection(true);
        this.setTypeAhead(true);
        this.setTriggerAction(ComboBoxCell.TriggerAction.ALL);
        this.setEditable(false);
    }

    public Boolean getBooleanValue() {
        final BooleanStrComboBoxModel currentValue = getCurrentValue();
        return currentValue != null ? currentValue.getId() : null;
    }

    private BooleanStrComboBoxModel findElement(Boolean value) {
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

    public void setBooleanValue(final Boolean value, boolean fireEvent) {
        setValue(findElement(value), fireEvent);
    }

    public void setBooleanValue(final Boolean value) {
        setBooleanValue(value, false);
    }
}
