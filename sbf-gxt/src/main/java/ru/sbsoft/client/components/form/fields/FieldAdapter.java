package ru.sbsoft.client.components.form.fields;

import com.google.gwt.user.client.ui.HasWidgets;
import com.sencha.gxt.widget.core.client.form.Field;

/**
 *
 * @author balandin
 * @since Oct 8, 2013 2:05:35 PM
 */
public class FieldAdapter extends BaseAdapter {

    public FieldAdapter(Field field, HasWidgets mainControl) {
        super(field, field.getCell().isReadOnly(), mainControl);
    }

    private Field getField() {
        return (Field) getWidget();
    }

    @Override
    public void setReadOnly(boolean readOnly) {
        //fix bug
        // getField().setReadOnly(readOnly || isReadOnly() || getField().getCell().isReadOnly());
        getField().setReadOnly(readOnly || isReadOnly());
    }

    @Override
    public boolean isValid() {
        return getField().isValid();
    }
}
