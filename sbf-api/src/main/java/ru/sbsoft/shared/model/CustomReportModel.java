package ru.sbsoft.shared.model;

import java.util.List;
import ru.sbsoft.shared.model.enums.GridTypeEnum;
import ru.sbsoft.shared.param.StringParamInfo;

/**
 * Класс модели "Пользовательский отчет"
 * @author sokolov
 */
public class CustomReportModel extends TehnologyModel {
    
    private String gridEnum;
    private GridTypeEnum gridType;  
    private String reportName;
    private String reportPath;
    private Boolean includeIdRow;
    private String headerSQL;
    private List<StringParamInfo> filters;
    private List<CustomReportParamModel> params;
    private List<CustomReportFilterInfo> filterInfos;

    public String getGridEnum() {
        return gridEnum;
    }

    public void setGridEnum(String gridEnum) {
        this.gridEnum = gridEnum;
    }

    public GridTypeEnum getGridType() {
        return gridType;
    }

    public void setGridType(GridTypeEnum gridType) {
        this.gridType = gridType;
    }

    public String getReportName() {
        return reportName;
    }

    public void setReportName(String reportName) {
        this.reportName = reportName;
    }

    public String getReportPath() {
        return reportPath;
    }

    public void setReportPath(String reportPath) {
        this.reportPath = reportPath;
    }

    public Boolean isIncludeIdRow() {
        return includeIdRow;
    }

    public void setIncludeIdRow(Boolean includeIdRow) {
        this.includeIdRow = includeIdRow;
    }

    public String getHeaderSQL() {
        return headerSQL;
    }

    public void setHeaderSQL(String headerSQL) {
        this.headerSQL = headerSQL;
    }

    public List<StringParamInfo> getFilters() {
        return filters;
    }

    public void setFilters(List<StringParamInfo> filters) {
        this.filters = filters;
    }

    public List<CustomReportParamModel> getParams() {
        return params;
    }

    public void setParams(List<CustomReportParamModel> params) {
        this.params = params;
    }

    public List<CustomReportFilterInfo> getFilterInfos() {
        return filterInfos;
    }

    public void setFilterInfos(List<CustomReportFilterInfo> filterInfos) {
        this.filterInfos = filterInfos;
    }
    
}
