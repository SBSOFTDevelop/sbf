package ru.sbsoft.client.components.grid.column.cell;

import com.google.gwt.i18n.client.NumberFormat;

/**
 * Ячейка таблицы для отображения значений {@link Number}
 *
 * @author balandin
 * @since Oct 23, 2014 2:45:24 PM
 */
public class NumberCell<T extends Number> extends CustomCell<T> {

    private final NumberFormat format;

    public NumberCell(NumberFormat format) {
        this.format = format;
    }

    @Override
    public String format(T value) {
        return format.format(value);
    }

    @Override
    protected String format(T value, String formatString) {
        NumberFormat fmt = NumberFormat.getFormat(formatString);
        return fmt.format(value);
    }

}
