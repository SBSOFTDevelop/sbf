package ru.sbsoft.server.services;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.servlet.annotation.WebServlet;
import ru.sbsoft.dao.IConfigDao;
import ru.sbsoft.dao.IGridDao;
import ru.sbsoft.dao.report.ICustomReportDao;
import ru.sbsoft.shared.services.FetchResult;
import ru.sbsoft.model.PageFilterInfo;
import ru.sbsoft.shared.GridContext;
import ru.sbsoft.shared.PageList;
import ru.sbsoft.shared.exceptions.FilterRequireException;
import ru.sbsoft.shared.meta.IColumns;
import ru.sbsoft.shared.meta.aggregate.IAggregateDef;
import ru.sbsoft.shared.meta.filter.FilterBox;
import ru.sbsoft.shared.model.CustomReportInfo;
import ru.sbsoft.shared.model.LookupInfoModel;
import ru.sbsoft.shared.model.MarkModel;
import ru.sbsoft.shared.param.StringParamInfo;
import ru.sbsoft.shared.services.IGridService;
import ru.sbsoft.shared.services.ServiceConst;

@WebServlet(urlPatterns = {ServiceConst.GRID_SERVICE_LONG})
public class GridService extends SBFRemoteServiceServlet implements IGridService {

    @EJB
    private IGridDao gridDao;
    @EJB
    private IConfigDao configDao;
    @EJB
    private ICustomReportDao reportDao;

    public GridService() {
    }

    @Override
    public IColumns getMeta(GridContext context) {
        return gridDao.getMeta(context);
    }

    @Override
    public FetchResult getModelForBrowser(GridContext context, PageFilterInfo config) throws FilterRequireException {
        final FilterBox acualFilter = getCurrentFilter(context);
        final PageList<? extends MarkModel> data = gridDao.getDataForBrowser(context, config(config, acualFilter));
        final FetchResult<? extends MarkModel> fetchResult = toFetchResult(data, config, acualFilter);
        return fetchResult;
    }

    @Override
    public List<BigDecimal> getOnlyIdsForBrowser(GridContext context, PageFilterInfo config, String idColumn) {
        return gridDao.getOnlyIdsForBrowser(context, config(config, context), idColumn);
    }

    @Override
    public MarkModel getModelRow(GridContext context, PageFilterInfo config, BigDecimal recordUQ) {
        return gridDao.getRow(context, config(config, context), recordUQ);
    }

    @Override
    public List<? extends MarkModel> getModelRows(GridContext context, PageFilterInfo config, List<BigDecimal> recordUQs) {
        return gridDao.getRows(context, config(config, context), recordUQs);
    }

    @Override
    public Map<String, ?> getAggregates(GridContext context, PageFilterInfo filterInfo, List<BigDecimal> ids, List<IAggregateDef> defs){
        return gridDao.getAggregates(context, filterInfo, ids, defs);
    }
    
    @Override
    public List<LookupInfoModel> lookup(GridContext context, PageFilterInfo config, List<BigDecimal> recordsUQs) {
        return gridDao.lookup(context, config(config, context), recordsUQs);
    }
    
    @Override
    public List<CustomReportInfo> getCustomReports(GridContext context, List<StringParamInfo> filters) {
        return reportDao.getCustomReports(context, filters);
    }    

    private FilterBox getCurrentFilter(GridContext context){
        FilterBox f = configDao.getCurrentFilter(getAppPrefix(), context);
        if(f != null && f.getPath() != null && !f.getPath().isDefault() && (f.getFilter() == null || f.getFilter().isEmpty())){
            configDao.setCurrentFilter(getAppPrefix(), context, null);
            f = configDao.getCurrentFilter(getAppPrefix(), context);
        }
        return f;
    }
    
    private PageFilterInfo config(PageFilterInfo config, GridContext context) {
        return config(config, getCurrentFilter(context));
    }
    
    private PageFilterInfo config(PageFilterInfo config, FilterBox filter) {
        config.setFilters(filter != null ? filter.getFilter() : null);
        return config;
    }

    private <T extends MarkModel> FetchResult<T> toFetchResult(final PageList<T> list, PageFilterInfo config, FilterBox usedFilter) {
        final FetchResult<T> result = new FetchResult<T>();
        result.setData(list);
        result.setOffset(config.getOffset());
        result.setTotalLength(list.getTotalSize());
        result.setAggs(list.getAggs());
        result.setActualFilter(usedFilter);
        return result;
    }

}
