package ru.sbsoft.dao;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import ru.sbsoft.shared.FiltersBean;
import ru.sbsoft.shared.GridContext;
import ru.sbsoft.shared.meta.config.ColumnCfg;
import ru.sbsoft.shared.meta.filter.FilterBox;
import ru.sbsoft.shared.meta.filter.IStoredFilterList;
import ru.sbsoft.shared.meta.filter.StoredFilterPath;

/**
 * Сохранение и восстановление конфигурации браузерной гриды
 *
 * @see ru.sbsoft.system.dao.common.GridConfig
 * @author balandin
 * @since May 21, 2015 2:08:55 PM
 */
public interface IConfigDao {

    List<ColumnCfg> loadConfiguration(String appPrefix, GridContext context);

    void saveConfiguration(String appPrefix, GridContext context, List<ColumnCfg> columns);

    FiltersBean getFilter(String appPrefix, GridContext context, StoredFilterPath filterPath);

    void saveFilter(String appPrefix, GridContext context, FiltersBean filters, StoredFilterPath filterPath);
    
    Map<StoredFilterPath, Exception> deleteFilters(String appPrefix, GridContext context, Collection<StoredFilterPath> filterPaths);
    
    IStoredFilterList getStoredFilterList(String appPrefix, GridContext context);
    
    FilterBox getCurrentFilter(String appPrefix, GridContext context);
    
    void setCurrentFilter(String appPrefix, GridContext context, StoredFilterPath filterPath);
    
}
