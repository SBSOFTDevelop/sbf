package ru.sbsoft.client.components.grid.column;

import ru.sbsoft.client.components.grid.column.cell.CustomCell;
import ru.sbsoft.client.components.grid.column.cell.WordWrapCell;
import ru.sbsoft.client.filter.editor.FilterAdapter;
import ru.sbsoft.client.filter.editor.StringFilterAdapter;
import ru.sbsoft.shared.meta.Column;

/**
 * Параметры колонки таблицы с текстовыми значениями {@link String}.
 */
public class StringColumnConfig extends CustomColumnConfig<String> {

    public StringColumnConfig(Column column, int index) {
        super(new CustomValueProvider<String>(index), column);

        if (column.getWordWrap() == null) {
            setCell(new CustomCell<String>());
        } else {
            setCell(new WordWrapCell(column.getWordWrap()));
        }
    }

    @Override
    public FilterAdapter createFilterAdapter() {
        return new StringFilterAdapter(getColumn().getFilterConditions());
    }
}
