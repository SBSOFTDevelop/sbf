package ru.sbsoft.client.components.grid.column;

import com.google.gwt.i18n.client.DateTimeFormat.PredefinedFormat;
import ru.sbsoft.client.components.field.DateConsts;
import ru.sbsoft.client.filter.editor.FilterAdapter;
import ru.sbsoft.client.filter.editor.TemporalFilterAdapter;
import ru.sbsoft.shared.FilterTypeEnum;
import ru.sbsoft.shared.meta.Column;

/**
 * Параметры колонки таблицы со временными значениями типа дата.
 */
public class DateColumnConfig extends TemporalColumnConfig {

    public DateColumnConfig(Column column, int index) {
        super(column, index, PredefinedFormat.DATE_SHORT);
    }
    
    @Override
    public FilterAdapter createFilterAdapter() {
        return new TemporalFilterAdapter(FilterTypeEnum.DATE, getColumn().getFilterConditions(), DateConsts.DATE, getFormat());
    }
}
