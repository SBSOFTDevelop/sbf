package ru.sbsoft.shared.meta;

import ru.sbsoft.shared.interfaces.GridType;
import ru.sbsoft.shared.param.DTO;

/**
 *
 * @author Kiselev
 */
public class LookupGridInfo implements DTO {

    private GridType gridType;
    private LookupKeyType keyType;

    public LookupGridInfo() {
    }

    public LookupGridInfo(GridType gridType, LookupKeyType keyType) {
        this.gridType = gridType;
        this.keyType = keyType;
    }

    public GridType getGridType() {
        return gridType;
    }

    public LookupKeyType getKeyType() {
        return keyType;
    }

}
