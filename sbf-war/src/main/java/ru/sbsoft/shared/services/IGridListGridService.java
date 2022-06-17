package ru.sbsoft.shared.services;

import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import ru.sbsoft.model.PageFilterInfo;
import ru.sbsoft.shared.GridContext;
import ru.sbsoft.shared.exceptions.FilterRequireException;
import ru.sbsoft.shared.meta.IColumns;
import ru.sbsoft.shared.meta.aggregate.IAggregateDef;
import ru.sbsoft.shared.model.CustomReportInfo;
import ru.sbsoft.shared.model.LookupInfoModel;
import ru.sbsoft.shared.model.MarkModel;
import ru.sbsoft.shared.param.StringParamInfo;

/**
 * Сервис отображения списка гридов в приложении 
 * @author sokolov
 */
@RemoteServiceRelativePath(ServiceConst.GRIDLIST_SERVICE_SHORT)
public interface IGridListGridService extends SBFRemoteService {
    
    IColumns getMeta(GridContext context);

    FetchResult<MarkModel> getModelForBrowser(GridContext context, PageFilterInfo config) throws FilterRequireException;

    List<BigDecimal> getOnlyIdsForBrowser(GridContext context, PageFilterInfo config, String idColumn);

    MarkModel getModelRow(GridContext context, PageFilterInfo config, BigDecimal recordUQ);

    List<? extends MarkModel> getModelRows(GridContext context, PageFilterInfo config, List<BigDecimal> recordUQs);

    Map<String, ?> getAggregates(GridContext context, PageFilterInfo filterInfo, List<BigDecimal> ids, List<IAggregateDef> defs);
    
    List<LookupInfoModel> lookup(GridContext context, PageFilterInfo config, List<BigDecimal> recordUQ);
    
    List<CustomReportInfo> getCustomReports(GridContext context, List<StringParamInfo> filters);

}
