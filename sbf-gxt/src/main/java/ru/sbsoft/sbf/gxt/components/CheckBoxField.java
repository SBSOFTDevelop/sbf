package ru.sbsoft.sbf.gxt.components;

import com.google.gwt.dom.client.Style;
import com.sencha.gxt.widget.core.client.form.CheckBox;
import ru.sbsoft.sbf.gxt.utils.FieldUtils;

/**
 * Компонент CheckBox с настроенным внешним видом.
 *
 * @author balandin
 */
public class CheckBoxField extends CheckBox {

    public CheckBoxField() {
        this(null);
    }

    @Override
    public void setReadOnly(boolean readOnly) {
        super.setReadOnly(readOnly); 
        setEnabled(!readOnly);
    }

    
    
    
    public CheckBoxField(String label) {
        super();

        if (label != null) {
            setBoxLabel(label);
        }
//getElement().getStyle().setColor(name);
        getElement().getStyle().setHeight(FieldUtils.FIELD_HEIGHT, Style.Unit.PX);
        getElement().getStyle().setWidth(16, Style.Unit.PX);
        getElement().getStyle().setPaddingTop(5, Style.Unit.PX);
        getElement().getStyle().setMarginBottom(-5, Style.Unit.PX);
    }
}
