package ru.sbsoft.client.components.operation;

import java.math.BigDecimal;
import java.util.List;
import ru.sbsoft.client.components.grid.BaseGrid;

/**
 * @author balandin
 * @since Mar 20, 2013 1:46:21 PM
 * @deprecated NO USAGES FOUND - FOUND 1 STUB USAGE IN INSCASE
 */
public interface IGridOperationContext {

    void setGrid(BaseGrid grid);

    void setRecordIDs(List<BigDecimal> records);
}
