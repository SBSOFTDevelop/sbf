package ru.sbsoft.client.components.grid.column;

import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import ru.sbsoft.client.components.field.YearMonthDayFormat;
import ru.sbsoft.client.components.grid.column.cell.CustomCell;
import ru.sbsoft.client.filter.editor.FilterAdapter;
import ru.sbsoft.client.filter.editor.YearMonthDayFilterAdapter;
import ru.sbsoft.sbf.app.model.YearMonthDay;
import ru.sbsoft.shared.meta.IColumn;

/**
 *
 * @author sychugin
 */
public class YearMonthDayColumnConfig extends CustomColumnConfig<YearMonthDay> {

    private final YearMonthDayFormat format;

    public YearMonthDayColumnConfig(IColumn column, int index) {
        super(new CustomValueProvider<YearMonthDay>(index), column);
        setCell(new CustomCell<>());
        setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        format = YearMonthDayFormat.DATE_SHORT;
    }

    @Override
    public FilterAdapter createFilterAdapter() {
        return new YearMonthDayFilterAdapter(getColumn().getFilterConditions(), format);
    }
}
