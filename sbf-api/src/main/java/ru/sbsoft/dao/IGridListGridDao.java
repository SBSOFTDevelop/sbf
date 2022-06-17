package ru.sbsoft.dao;

import java.math.BigDecimal;
import java.util.List;
import ru.sbsoft.model.PageFilterInfo;
import ru.sbsoft.shared.GridContext;
import ru.sbsoft.shared.PageList;
import ru.sbsoft.shared.exceptions.FilterRequireException;
import ru.sbsoft.shared.meta.IColumns;
import ru.sbsoft.shared.model.MarkModel;

/**
 *
 * @author sokolov
 */
public interface IGridListGridDao {
    
    IColumns getMeta(GridContext context);
    
    PageList<? extends MarkModel> getDataForBrowser(GridContext context, PageFilterInfo pageFilterInfo) throws FilterRequireException;    
    
    List<BigDecimal> getOnlyIdsForBrowser(GridContext context, PageFilterInfo pageFilterInfo, String idColumn);

    MarkModel getRow(GridContext context, PageFilterInfo pageFilterInfo, BigDecimal recordUQ);

}
