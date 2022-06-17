package ru.sbsoft.shared.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 *
 * @author sokolov
 */
public class CustomReportInfo implements Serializable {

    private BigDecimal reportId;
    private String title;
    private String reportPath;
    private Boolean incudeIdRow;
    private String headerSQL;
    private List<CustomReportParamModel> params;

    public CustomReportInfo() {
    }

    public BigDecimal getReportId() {
        return reportId;
    }

    public void setReportId(BigDecimal reportId) {
        this.reportId = reportId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getReportPath() {
        return reportPath;
    }

    public void setReportPath(String reportPath) {
        this.reportPath = reportPath;
    }

    public Boolean getIncudeIdRow() {
        return incudeIdRow;
    }

    public void setIncudeIdRow(Boolean incudeIdRow) {
        this.incudeIdRow = incudeIdRow;
    }

    public String getHeaderSQL() {
        return headerSQL;
    }

    public void setHeaderSQL(String headerSQL) {
        this.headerSQL = headerSQL;
    }

    public List<CustomReportParamModel> getParams() {
        return params;
    }

    public void setParams(List<CustomReportParamModel> params) {
        this.params = params;
    }

}
