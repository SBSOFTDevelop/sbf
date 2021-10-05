package ru.sbsoft.shared.model.operation;

import ru.sbsoft.shared.interfaces.OperationType;
import ru.sbsoft.shared.param.DTO;

public class CreateOperationRequest implements DTO {

    private OperationType operationType;
    private String locale;
    private String moduleName;

    public OperationType getOperationType() {
        return operationType;
    }

    public void setOperationType(OperationType operationCode) {
        this.operationType = operationCode;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

}
