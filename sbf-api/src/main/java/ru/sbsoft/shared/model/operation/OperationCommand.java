package ru.sbsoft.shared.model.operation;

import java.util.ArrayList;
import java.util.List;
import ru.sbsoft.shared.interfaces.OperationType;
import ru.sbsoft.shared.model.GridParamsBean;
import ru.sbsoft.shared.param.DTO;
import ru.sbsoft.shared.param.ParamInfo;

/**
 * Информация о команде запуска операции.
 * <p>
 * Это базовый класс для любых команд запуска операций.
 * </p>
 *
 * @author panarin
 */
public class OperationCommand implements DTO {

    private Long currentOperationId;
    private String moduleName;
    private OperationType operationType;
    private GridParamsBean gridContext;
    private String locale;
    private boolean needNotify;
    private ArrayList<ParamInfo> params = new ArrayList<ParamInfo>();
    //

    public OperationCommand() {
    }

    public boolean isNeedNotify() {
        return needNotify;
    }

    public void setNeedNotify(boolean needNotify) {
        this.needNotify = needNotify;
    }



    
    public OperationCommand(OperationType operationType) {
        this.operationType = operationType;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public String getCode() {
        return operationType == null ? null : operationType.getCode();
    }

    public List<ParamInfo> getParams() {
        return params;
    }

    public void putParams(List<ParamInfo> params) {
        this.params.clear();
        this.params.addAll(params);
    }

    /**
     *
     * @return идентификатор операции.
     */
    public Long getCurrentOperationId() {
        return currentOperationId;
    }

    /**
     * Устанавливает идентификатор операции.
     *
     * @param currentOperationId идентификатор операции.
     */
    public void setCurrentOperationId(Long currentOperationId) {
        this.currentOperationId = currentOperationId;
    }

    public GridParamsBean getGridContext() {
        return gridContext;
    }

    public void setGridContext(GridParamsBean gridContext) {
        this.gridContext = gridContext;
    }

    public OperationType getOperationType() {
        return operationType;
    }

    public void setOperationType(OperationType operationType) {
        this.operationType = operationType;
    }

    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

}
