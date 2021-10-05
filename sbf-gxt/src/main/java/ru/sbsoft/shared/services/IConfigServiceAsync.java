package ru.sbsoft.shared.services;

import com.google.gwt.user.client.rpc.AsyncCallback;
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
 * @see IConfigService
 * @author balandin
 */
public interface IConfigServiceAsync extends ISBFServiceAsync {

    public void loadConfiguration(GridContext context, AsyncCallback<List<ColumnCfg>> callback);

    public void saveConfiguration(GridContext context, List<ColumnCfg> columns, AsyncCallback<Void> callback);

    public void loadFilter(GridContext context, StoredFilterPath filterPath, AsyncCallback<FiltersBean> callback);

    public void saveFilter(GridContext context, FiltersBean filters, StoredFilterPath filterPath, AsyncCallback<Void> callback);
        
    void deleteFilters(GridContext context, Collection<StoredFilterPath> filterPaths, AsyncCallback<Map<StoredFilterPath, Exception>> callback);
    
    void getStoredFilterList(GridContext context, AsyncCallback<IStoredFilterList> callback);
    
    void loadCurrentFilter(GridContext context, AsyncCallback<FilterBox> callback);

    void setCurrentFilter(GridContext context, StoredFilterPath filterPath, AsyncCallback<Void> callback);
}
