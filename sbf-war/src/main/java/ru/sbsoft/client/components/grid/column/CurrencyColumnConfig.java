package ru.sbsoft.client.components.grid.column;

import java.math.BigDecimal;
import ru.sbsoft.client.filter.editor.FilterAdapter;
import ru.sbsoft.client.filter.editor.CurrencyFilterAdapter;
import ru.sbsoft.shared.meta.IColumn;

/**
 * Параметры колонки таблицы с числовыми значениями типа {@link BigDecimal}, представляющими собой денежные суммы.
 */
public class CurrencyColumnConfig extends NumericColumnConfig<BigDecimal> {

    public static final String DEFAULT_FORMAT = "#,##0.00";

    public CurrencyColumnConfig(IColumn column, int index) {
        this(column, index, DEFAULT_FORMAT);
    }

    CurrencyColumnConfig(IColumn column, int index, String defaultFormat) {
        super(column, index, DEFAULT_FORMAT);
    }
    
    @Override
    public FilterAdapter createFilterAdapter() {
        return new CurrencyFilterAdapter(getColumn().getFilterConditions(), getFormat());
    }
}
