package ru.sbsoft.server.services;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.servlet.annotation.WebServlet;
import ru.sbsoft.dao.IConfigDao;
import ru.sbsoft.dao.IGridListGridDao;
import ru.sbsoft.model.PageFilterInfo;
import ru.sbsoft.shared.GridContext;
import ru.sbsoft.shared.PageList;
import ru.sbsoft.shared.exceptions.FilterRequireException;
import ru.sbsoft.shared.meta.Columns;
import ru.sbsoft.shared.meta.aggregate.IAggregateDef;
import ru.sbsoft.shared.meta.filter.FilterBox;
import ru.sbsoft.shared.model.CustomReportInfo;
import ru.sbsoft.shared.model.LookupInfoModel;
import ru.sbsoft.shared.model.MarkModel;
import ru.sbsoft.shared.param.StringParamInfo;
import ru.sbsoft.shared.services.FetchResult;
import ru.sbsoft.shared.services.IGridListGridService;
import ru.sbsoft.shared.services.ServiceConst;

/**
 *
 * @author sokolov
 */
@WebServlet(urlPatterns = {ServiceConst.GRIDLIST_SERVICE_LONG})
public class GridListGridService extends SBFRemoteServiceServlet implements IGridListGridService {
    
    @EJB
    private IGridListGridDao gridListDao;
    
    @EJB
    private IConfigDao configDao;

    @Override
    public Columns getMeta(GridContext context) {
        return gridListDao.getMeta(context);
    }

    @Override
    public FetchResult getModelForBrowser(GridContext context, PageFilterInfo config) throws FilterRequireException {
        final FilterBox acualFilter = getCurrentFilter(context);
        final PageList<? extends MarkModel> data = gridListDao.getDataForBrowser(context, config(config, acualFilter));
        final FetchResult<? extends MarkModel> fetchResult = toFetchResult(data, config, acualFilter);
        return fetchResult;
    }

    @Override
    public List<BigDecimal> getOnlyIdsForBrowser(GridContext context, PageFilterInfo config, String idColumn) {
        return gridListDao.getOnlyIdsForBrowser(context, config, idColumn);
    }

    @Override
    public MarkModel getModelRow(GridContext context, PageFilterInfo config, BigDecimal recordUQ) {
        return gridListDao.getRow(context, config, recordUQ);
    }

    @Override
    public List<? extends MarkModel> getModelRows(GridContext context, PageFilterInfo config, List<BigDecimal> recordUQs) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Map<String, ?> getAggregates(GridContext context, PageFilterInfo filterInfo, List<BigDecimal> ids, List<IAggregateDef> defs) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<LookupInfoModel> lookup(GridContext context, PageFilterInfo config, List<BigDecimal> recordUQ) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<CustomReportInfo> getCustomReports(GridContext context, List<StringParamInfo> filters) {
        return null;
    }
    
    private FilterBox getCurrentFilter(GridContext context){
        FilterBox f = configDao.getCurrentFilter(getAppPrefix(), context);
        if(f != null && f.getPath() != null && !f.getPath().isDefault() && (f.getFilter() == null || f.getFilter().isEmpty())){
            configDao.setCurrentFilter(getAppPrefix(), context, null);
            f = configDao.getCurrentFilter(getAppPrefix(), context);
        }
        return f;
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
