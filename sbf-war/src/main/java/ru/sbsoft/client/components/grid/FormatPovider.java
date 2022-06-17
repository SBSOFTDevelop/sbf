package ru.sbsoft.client.components.grid;

import ru.sbsoft.client.components.grid.column.CustomColumnConfig;
import ru.sbsoft.shared.grid.format.IExpCellFormat;
import ru.sbsoft.shared.meta.Row;

/**
 *
 * @author sychugin
 */
public class FormatPovider {

    private final SystemGrid.IRowByIndexProvider grid;
    

    public FormatPovider(SystemGrid.IRowByIndexProvider grid) {
        this.grid = grid;
        
    }

    public String getFormat(CustomColumnConfig config, int index) {
        Row row = grid.getRow(index);
        IExpCellFormat format = config.getColumn().getExpCellFormat();
        return format == null ? "0" : config.getColumn().getExpCellFormat().getFormat(row);
    }

}
