package ru.sbsoft.client.components.grid.column;

import ru.sbsoft.client.components.grid.column.cell.GridIconCell;
import ru.sbsoft.client.filter.editor.FilterAdapter;
import ru.sbsoft.shared.model.ImageBase64;

/**
 * Параметры колонки таблицы с иконками {@link ImageBase64}.
 */
public class IconColumnConfig extends CustomColumnConfig<Object> {

    private final CustomColumnConfig delegate;
    
    public IconColumnConfig(CustomColumnConfig delegate) {
        super(delegate.getValueProvider(), delegate.getColumn());
        setCell(new GridIconCell(delegate.getColumn().getIconMap()));
        
        
        this.delegate = delegate;
    }

    @Override
    public FilterAdapter createFilterAdapter() {
        return delegate.createFilterAdapter();
    }
}
