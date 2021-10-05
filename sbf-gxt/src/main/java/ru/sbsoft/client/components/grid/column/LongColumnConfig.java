package ru.sbsoft.client.components.grid.column;

import ru.sbsoft.client.filter.editor.FilterAdapter;
import ru.sbsoft.client.filter.editor.IntegerFilterAdapter;
import ru.sbsoft.shared.meta.Column;

/**
 * Параметры колонки таблицы с числовыми значениями типа {@link Long}.
 */
public class LongColumnConfig extends NumericColumnConfig<Long> {

    public static String DEFAULT_FORMAT = "#,##0";

    public LongColumnConfig(Column column, int index) {
        super(column, index, DEFAULT_FORMAT);
    }

    @Override
    public FilterAdapter createFilterAdapter() {
        return new IntegerFilterAdapter(getColumn().getFilterConditions(), getFormat());
    }
}
