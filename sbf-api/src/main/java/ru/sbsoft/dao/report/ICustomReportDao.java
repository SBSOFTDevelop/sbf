package ru.sbsoft.dao.report;

import java.math.BigDecimal;
import java.util.List;
import ru.sbsoft.shared.GridContext;
import ru.sbsoft.shared.model.CustomReportInfo;
import ru.sbsoft.shared.param.StringParamInfo;

/**
 * Сервис выдает список доступных отчетов в контексте грида
 * @author sokolov
 */
public interface ICustomReportDao {
    
    List<CustomReportInfo> getCustomReports(GridContext context, List<StringParamInfo> filters);
    
    CustomReportInfo getCustomReport(BigDecimal reportId);
    
}
