package ru.sbsoft.client.components.field;

import com.google.gwt.i18n.client.NumberFormat;
import ru.sbsoft.svc.cell.core.client.form.NumberInputCell;
import ru.sbsoft.svc.widget.core.client.form.NumberField;
import java.math.BigDecimal;

/**
 * Компонент предназначен для работы с действительными числами без указания формата.
 * @author sokolov
 */
public class FreeNumberField extends NumberField<BigDecimal> {

    public FreeNumberField() {
        super(new FreeNumberProperyEditor());
        setFormat(NumberFormat.getDecimalFormat());
    }

    public FreeNumberField(NumberInputCell<BigDecimal> cell) {
        super(cell, new FreeNumberProperyEditor());
        setFormat(NumberFormat.getDecimalFormat());
    }

}
