package ru.sbsoft.client.components.grid.aggregate.result;

import com.google.gwt.i18n.client.NumberFormat;

/**
 *
 * @author Kiselev
 */
public class NumberHandler implements IResultHandler<Number> {
    private final NumberFormat fmt;

    public NumberHandler(NumberFormat fmt) {
        this.fmt = fmt;
    }

    @Override
    public String formatResultVal(Number o) {
        return fmt != null ? fmt.format(o) : String.valueOf(o);
    }
}
