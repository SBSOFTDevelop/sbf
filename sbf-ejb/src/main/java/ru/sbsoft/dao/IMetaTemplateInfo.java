package ru.sbsoft.dao;

import java.util.List;
import ru.sbsoft.meta.ColumnsInfo;
import ru.sbsoft.meta.columns.consts.Properties;
import ru.sbsoft.shared.model.CustomReportFilterInfo;
import ru.sbsoft.shared.meta.filter.FilterDefinitions;
import ru.sbsoft.shared.meta.filter.FilterTemplate;
import ru.sbsoft.shared.model.SortingInfo;

/**
 * Предоставляет информацию о метаданных template.
 *
 * @author sokolov
 */
public interface IMetaTemplateInfo {

    ColumnsInfo getColumnsInfo();
    
    FilterDefinitions getFilterDefinitions();

    IFilterWrapper getFilterWrapper(String name);
    
    Properties getProperties();
    
    List<SortingInfo> getDefaultSort();
    
    FilterTemplate getFilterTemplate();
    
    default List<CustomReportFilterInfo> getReportFilters() {
        return null;
    }

}
