package ru.sbsoft.client.components.field;

import com.google.gwt.i18n.client.NumberFormat;
import com.sencha.gxt.widget.core.client.form.NumberPropertyEditor;
import java.math.BigDecimal;
import java.text.ParseException;

/**
 * Переопределен метод parse для корректного парсинга действительных чисел свободного формата. 
 * @author sokolov
 */
public class FreeNumberProperyEditor extends NumberPropertyEditor.BigDecimalPropertyEditor {

    @Override
    public BigDecimal parse(CharSequence text) throws ParseException {
        String value = text.toString();
        try {
            Double d = NumberFormat.getDecimalFormat().parse(value);
            return (BigDecimal) returnTypedValue(d);
        } catch (Exception ex) {
            throw new ParseException(ex.getMessage(), 0);
        }
    }

}
