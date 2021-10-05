package ru.sbsoft.dao;

import java.util.List;
import javax.ejb.SessionContext;
import ru.sbsoft.shared.GridContext;
import ru.sbsoft.shared.model.CustomReportFilterInfo;
import ru.sbsoft.shared.model.enums.GridTypeEnum;

/**
 * @author balandin
 * @since Jun 6, 2014 1:31:09 PM
 */
public interface GridSupport {

    DefaultTemplateBuilder getTemplateBuilder(GridContext context);

    void setSessionContext(SessionContext sessionContext);

    List<CustomReportFilterInfo> getReportFilters(String gridCode, GridTypeEnum gridType);
}
