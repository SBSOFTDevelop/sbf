package ru.sbsoft.client.components.grid.column;

import com.google.gwt.i18n.client.DateTimeFormat.PredefinedFormat;
import ru.sbsoft.client.components.field.DateConsts;
import ru.sbsoft.client.filter.editor.FilterAdapter;
import ru.sbsoft.client.filter.editor.TemporalFilterAdapter;
import ru.sbsoft.shared.FilterTypeEnum;
import ru.sbsoft.shared.meta.IColumn;

/**
 * Параметры колонки таблицы со временными значениями типа 'точка во времени'
 * (т.е. с датой и временем).
 */
public class TimestampColumnConfig extends TemporalColumnConfig {

    public TimestampColumnConfig(IColumn column, int index) {
        super(column, index, PredefinedFormat.DATE_TIME_MEDIUM);
    }

    @Override
    public FilterAdapter createFilterAdapter() {
        return new TemporalFilterAdapter(FilterTypeEnum.TIMESTAMP, getColumn().getFilterConditions(), DateConsts.DATE_TIME, getFormat());
    }
}
