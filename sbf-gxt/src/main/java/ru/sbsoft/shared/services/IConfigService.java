package ru.sbsoft.shared.services;

import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
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
 * Сервис сохранения/получения конфигурации колонок таблицы.
 *
 * @author balandin
 * @since May 21, 2015 2:04:53 PM
 */
@RemoteServiceRelativePath(ServiceConst.CONFIG_SERVICE_SHORT)
public interface IConfigService extends SBFRemoteService {

    public List<ColumnCfg> loadConfiguration(GridContext context);

    public void saveConfiguration(GridContext context, List<ColumnCfg> columns);

    public FiltersBean loadFilter(GridContext context, StoredFilterPath filterPath);

    public void saveFilter(GridContext context, FiltersBean filters, StoredFilterPath filterPath);

    Map<StoredFilterPath, Exception> deleteFilters(GridContext context, Collection<StoredFilterPath> filterPaths);

    IStoredFilterList getStoredFilterList(GridContext context);

    FilterBox loadCurrentFilter(GridContext context);

    void setCurrentFilter(GridContext context, StoredFilterPath filterPath);
}
