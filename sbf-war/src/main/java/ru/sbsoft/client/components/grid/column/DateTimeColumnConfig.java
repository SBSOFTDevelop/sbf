package ru.sbsoft.client.components.grid.column;

import com.google.gwt.i18n.client.DateTimeFormat.PredefinedFormat;
import ru.sbsoft.client.components.field.DateConsts;
import ru.sbsoft.client.filter.editor.FilterAdapter;
import ru.sbsoft.client.filter.editor.TemporalFilterAdapter;
import ru.sbsoft.shared.FilterTypeEnum;
import ru.sbsoft.shared.meta.IColumn;

/**
 * @author balandin
 * @since Nov 18, 2015
 */
public class DateTimeColumnConfig extends TemporalColumnConfig {

    public DateTimeColumnConfig(IColumn column, int index) {
        super(column, index, PredefinedFormat.DATE_TIME_MEDIUM);
    }

    @Override
    public FilterAdapter createFilterAdapter() {
        // return new TemporalFilterAdapter(FilterTypeEnum.DATE, DateConsts.DATE_TIME, getFormat());
        // редакторы без учета времени
        return new TemporalFilterAdapter(FilterTypeEnum.DATE, getColumn().getFilterConditions(), DateConsts.DATE, getFormat());
    }
}
