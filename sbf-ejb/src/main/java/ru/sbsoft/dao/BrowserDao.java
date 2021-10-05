package ru.sbsoft.dao;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import ru.sbsoft.meta.ColumnsInfo;
import ru.sbsoft.model.PageFilterInfo;
import ru.sbsoft.shared.PageList;
import ru.sbsoft.shared.exceptions.FilterRequireException;
import ru.sbsoft.shared.meta.Columns;
import ru.sbsoft.shared.model.LookupInfoModel;
import ru.sbsoft.shared.model.MarkModel;

/**
 * Интерфейс <code>BrowserDaoBean</code> объявляет методы DAO-слоя для Grid. 
 * 
 * @author balandin
 * @since Jul 2, 2013 11:22:16 AM
 */
public interface BrowserDao<M extends MarkModel> {

    Columns getMeta();

    ColumnsInfo getColumnsInfo();

    PageList<M> getDataForBrowser(PageFilterInfo pageFilterInfo) throws FilterRequireException;

    void processResultSet(String username, boolean isAdmin, PageFilterInfo pageFilterInfo, List<BigDecimal> ids, ResultSetHandler resultSetHandler);

    List<BigDecimal> getOnlyIdsForBrowser(PageFilterInfo filterInfo, String idColumn);

    MarkModel getRow(PageFilterInfo filterInfo, BigDecimal recordUQ);

    List<? extends MarkModel> getRows(PageFilterInfo filterInfo, List<BigDecimal> recordUQs);
    
    List<LookupInfoModel> lookup(PageFilterInfo filterInfo, List<BigDecimal> recordsUQs);

}
