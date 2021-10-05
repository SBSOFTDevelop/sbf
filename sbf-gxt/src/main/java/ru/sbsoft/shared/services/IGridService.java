package ru.sbsoft.shared.services;

import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import ru.sbsoft.model.PageFilterInfo;
import ru.sbsoft.shared.GridContext;
import ru.sbsoft.shared.exceptions.FilterRequireException;
import ru.sbsoft.shared.meta.Columns;
import ru.sbsoft.shared.meta.aggregate.IAggregateDef;
import ru.sbsoft.shared.model.CustomReportInfo;
import ru.sbsoft.shared.model.LookupInfoModel;
import ru.sbsoft.shared.model.MarkModel;
import ru.sbsoft.shared.param.StringParamInfo;

/**
 * Сервис для создания и отображения таблицы данных на клиенте.
 * Вся необходимая информация передается с шаблона таблицы на сервере.
 * @see ru.sbsoft.dao.AbstractTemplate и его наследников
 */
@RemoteServiceRelativePath(ServiceConst.GRID_SERVICE_SHORT)
public interface IGridService extends SBFRemoteService {

    Columns getMeta(GridContext context);

    FetchResult<MarkModel> getModelForBrowser(GridContext context, PageFilterInfo config) throws FilterRequireException;

    List<BigDecimal> getOnlyIdsForBrowser(GridContext context, PageFilterInfo config, String idColumn);

    MarkModel getModelRow(GridContext context, PageFilterInfo config, BigDecimal recordUQ);

    List<? extends MarkModel> getModelRows(GridContext context, PageFilterInfo config, List<BigDecimal> recordUQs);

    Map<String, ?> getAggregates(GridContext context, PageFilterInfo filterInfo, List<BigDecimal> ids, List<IAggregateDef> defs);
    
    List<LookupInfoModel> lookup(GridContext context, PageFilterInfo config, List<BigDecimal> recordUQ);
    
    List<CustomReportInfo> getCustomReports(GridContext context, List<StringParamInfo> filters);

//    public List<FilterInfo> getFilterValues(GridContext context);
//
//    public void saveFilterValues(GridContext context, List<FilterInfo> filters);
}
