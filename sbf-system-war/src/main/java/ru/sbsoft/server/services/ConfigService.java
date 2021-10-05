package ru.sbsoft.server.services;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.servlet.annotation.WebServlet;
import ru.sbsoft.dao.IConfigDao;
import ru.sbsoft.shared.FiltersBean;
import ru.sbsoft.shared.GridContext;
import ru.sbsoft.shared.meta.config.ColumnCfg;
import ru.sbsoft.shared.meta.filter.FilterBox;
import ru.sbsoft.shared.meta.filter.IStoredFilterList;
import ru.sbsoft.shared.meta.filter.StoredFilterPath;
import ru.sbsoft.shared.services.IConfigService;
import ru.sbsoft.shared.services.ServiceConst;

/**
 * @author balandin
 * @since May 21, 2015 2:04:15 PM
 */
@WebServlet(urlPatterns = {ServiceConst.CONFIG_SERVICE_LONG})
public class ConfigService extends SBFRemoteServiceServlet implements IConfigService {

    @EJB
    private IConfigDao configDao;

    public ConfigService() {
    }

    @Override
    public List<ColumnCfg> loadConfiguration(GridContext context) {
        return configDao.loadConfiguration(getAppPrefix(), context);
    }

    @Override
    public void saveConfiguration(GridContext context, List<ColumnCfg> columns) {
        configDao.saveConfiguration(getAppPrefix(), context, columns);
    }

    @Override
    public FiltersBean loadFilter(GridContext context, StoredFilterPath filterPath) {
        return configDao.getFilter(getAppPrefix(), context, filterPath);
    }

    @Override
    public void saveFilter(GridContext context, FiltersBean filters, StoredFilterPath filterPath) {
        configDao.saveFilter(getAppPrefix(), context, filters, filterPath);
    }
    
    @Override
    public IStoredFilterList getStoredFilterList(GridContext context) {
        return configDao.getStoredFilterList(getAppPrefix(), context);
    }

    @Override
    public Map<StoredFilterPath, Exception> deleteFilters(GridContext context, Collection<StoredFilterPath> filterPaths) {
        return configDao.deleteFilters(getAppPrefix(), context, filterPaths);
    }

    @Override
    public FilterBox loadCurrentFilter(GridContext context) {
        return configDao.getCurrentFilter(getAppPrefix(), context);
    }

    @Override
    public void setCurrentFilter(GridContext context, StoredFilterPath filterPath) {
        configDao.setCurrentFilter(getAppPrefix(), context, filterPath);
    }
}
