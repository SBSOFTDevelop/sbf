package ru.sbsoft.shared.model;

import ru.sbsoft.sbf.app.model.FormModel;
import ru.sbsoft.shared.consts.CustomReportParamType;

/**
 *
 * @author sokolov
 */
public class CustomReportParamModel extends FormModel {

    private String code;
    private CustomReportParamType paramType;
    private String name;
    private boolean isNew = false;
    
    public CustomReportParamModel() {
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public CustomReportParamType getParamType() {
        return paramType;
    }

    public void setParamType(CustomReportParamType paramType) {
        this.paramType = paramType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isNew() {
        return isNew;
    }

    public void setNew(boolean isNew) {
        this.isNew = isNew;
    }
    
    
}
