package ru.sbsoft.client.components.grid.column;

import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import ru.sbsoft.client.components.grid.column.cell.RoundBDCell;
import ru.sbsoft.client.components.grid.column.cell.NumberCell;
import ru.sbsoft.common.Strings;
import ru.sbsoft.shared.meta.Column;

/**
 * Базовый класс параметров колонки таблицы с числовыми значениями (т.е.
 * унаследованных от типа {@link Number}).
 */
public abstract class NumericColumnConfig<T extends Number> extends CustomColumnConfig<T> {

    private final NumberFormat format;

    public NumericColumnConfig(Column column, int index, String defaultFormat) {
        super(new CustomValueProvider<T>(index), column);
        format = NumberFormat.getFormat(Strings.coalesce(column.getFormat(), defaultFormat));
        if (column.getUpdateInfo() != null && column.getExpCellFormat() == null) {
            setCell(new RoundBDCell(format, column.getUpdateInfo().getLenght()));
        } else {
            setCell(new NumberCell<T>(format));
        }
        setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
    }

    public NumberFormat getFormat() {
        return format;
    }
}
