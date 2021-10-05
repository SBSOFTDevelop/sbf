package ru.sbsoft.shared.meta.filter;

import ru.sbsoft.shared.interfaces.GridType;
import ru.sbsoft.shared.param.DTO;

/**
 * @author balandin
 * @since Jun 1, 2015 1:10:11 PM
 */
public class Dictionary implements DTO {

    private GridType gridType;
    private String column;

    public Dictionary() {
    }

    public Dictionary(GridType gridType, String column) {
        this.gridType = gridType;
        this.column = column;
    }

    public GridType getGridType() {
        return gridType;
    }

    public void setGridType(GridType gridType) {
        this.gridType = gridType;
    }

    public String getColumn() {
        return column;
    }

    public void setColumn(String column) {
        this.column = column;
    }
}
