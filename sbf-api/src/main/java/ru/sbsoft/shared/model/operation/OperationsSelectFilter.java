package ru.sbsoft.shared.model.operation;

import java.util.Date;
import ru.sbsoft.shared.param.DTO;

public class OperationsSelectFilter implements DTO {

    private Date periodStart;
    private Date periodEnd;
    private boolean showHidden;
    private String moduleCode;

    public OperationsSelectFilter() {
    }

    public Date getPeriodStart() {
        return periodStart;
    }

    public void setPeriodStart(Date periodStart) {
        this.periodStart = periodStart;
    }

    public Date getPeriodEnd() {
        return periodEnd;
    }

    public void setPeriodEnd(Date periodEnd) {
        this.periodEnd = periodEnd;
    }

    public boolean isShowHidden() {
        return showHidden;
    }

    public void setShowHidden(boolean showHidden) {
        this.showHidden = showHidden;
    }

    public String getModuleCode() {
        return moduleCode;
    }

    public void setModuleCode(String moduleCode) {
        this.moduleCode = moduleCode;
    }

}
