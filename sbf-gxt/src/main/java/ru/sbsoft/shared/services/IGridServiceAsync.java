package ru.sbsoft.shared.services;

import com.google.gwt.user.client.rpc.AsyncCallback;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import ru.sbsoft.model.PageFilterInfo;
import ru.sbsoft.shared.GridContext;
import ru.sbsoft.shared.meta.Columns;
import ru.sbsoft.shared.meta.aggregate.IAggregateDef;
import ru.sbsoft.shared.model.CustomReportInfo;
import ru.sbsoft.shared.model.LookupInfoModel;
import ru.sbsoft.shared.model.MarkModel;
import ru.sbsoft.shared.param.StringParamInfo;

/**
 * @see IGridService
 */
public interface IGridServiceAsync extends ISBFServiceAsync {

    void getMeta(GridContext context, AsyncCallback<Columns> callback);

    void getModelForBrowser(GridContext context, PageFilterInfo config, AsyncCallback<FetchResult<MarkModel>> callback);

    void getModelRow(GridContext context, PageFilterInfo config, BigDecimal recordUQ, AsyncCallback<MarkModel> callback);

    void getModelRows(GridContext context, PageFilterInfo config, List<BigDecimal> recordUQs, AsyncCallback<List<? extends MarkModel>> callback);

    void getOnlyIdsForBrowser(GridContext context, PageFilterInfo config, String idColumn, AsyncCallback<List<BigDecimal>> callback);
    
    void getAggregates(GridContext context, PageFilterInfo filterInfo, List<BigDecimal> ids, List<IAggregateDef> defs, AsyncCallback<Map<String, ?>> callback);
    
    void lookup(GridContext context, PageFilterInfo config, List<BigDecimal> recordsUQs, AsyncCallback<List<LookupInfoModel>> callback);
    
    void getCustomReports(GridContext context, List<StringParamInfo> filters, AsyncCallback<List<CustomReportInfo>> callback);

//    public void getFilterValues(GridContext context, AsyncCallback<List<FilterInfo>> callback);
//
//    public void saveFilterValues(GridContext context, List<FilterInfo> filters, AsyncCallback<Void> callback);
}
