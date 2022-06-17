package ru.sbsoft.dao;

import ru.sbsoft.model.PageFilterInfo;
import ru.sbsoft.shared.GridContext;
import ru.sbsoft.shared.PageList;
import ru.sbsoft.shared.exceptions.FilterRequireException;
import ru.sbsoft.shared.meta.IColumns;
import ru.sbsoft.shared.meta.aggregate.IAggregateDef;
import ru.sbsoft.shared.model.LookupInfoModel;
import ru.sbsoft.shared.model.MarkModel;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface IGridDao {

    IColumns getMeta(GridContext context);

    PageList<? extends MarkModel> getDataForBrowser(GridContext context, PageFilterInfo pageFilterInfo) throws FilterRequireException;

    List<BigDecimal> getOnlyIdsForBrowser(GridContext context, PageFilterInfo pageFilterInfo);
    
    List<BigDecimal> getOnlyIdsForBrowser(GridContext context, PageFilterInfo pageFilterInfo, String idColumn);

    MarkModel getRow(GridContext context, PageFilterInfo pageFilterInfo, BigDecimal recordUQ);

    List<? extends MarkModel> getRows(GridContext context, PageFilterInfo pageFilterInfo, List<BigDecimal> recordUQs);

    Map<String, ?> getAggregates(GridContext context, PageFilterInfo filterInfo, List<BigDecimal> ids, final List<IAggregateDef> defs);

    List<LookupInfoModel> lookup(GridContext context, PageFilterInfo config, List<BigDecimal> recordUQ);
    
    List<String> getGridTemplatesCode();
    
    List<String> getDynGridTemplatesCode();
    
}
