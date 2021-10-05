package ru.sbsoft.processor;

import java.util.List;
import ru.sbsoft.shared.OperationObject;
import ru.sbsoft.shared.interfaces.OperationType;
import ru.sbsoft.shared.model.operation.SchedulerContext;

/**
 * Контекст экземпляра операции.
 *
 * @author Fedor Resnyanskiy, SBSOFT
 */
public class ServerOperationContext {

    private Long currentOperationId;
    private OperationType operationType;
    private String runUser;
    private OperationLogger operationLogger;
    private List<OperationObject> parameters;
    private SchedulerContext schedulerContext;

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

    /**
     * Возвращает логгер операции.
     *
     * @return логгер операции.
     */
    public OperationLogger getOperationLogger() {
        return operationLogger;
    }

    /**
     * Устанавливает логгер операции.
     *
     * @param operationLogger логгер операции.
     */
    public void setOperationLogger(OperationLogger operationLogger) {
        this.operationLogger = operationLogger;
    }

    /**
     * Возвращает список параметров, переданных операции для исполнения.
     *
     * @return список параметров операции.
     */
    public List<OperationObject> getParameters() {
        return parameters;
    }

    /**
     * Устанавливает список параметров, переданных операции для исполнения.
     *
     * @param parameters список параметров операции.
     */
    public void setParameters(List<OperationObject> parameters) {
        this.parameters = parameters;
    }

    /**
     * Возвращает имя пользователя, который создал текущий экземпляр операции.
     *
     * @return имя пользователя, который создал текущий экземпляр операции.
     */
    public String getRunUser() {
        return runUser;
    }

    /**
     * Устанавливает имя пользователя, который создал текущий экземпляр операции.
     *
     * @param runUser имя пользователя, который создал текущий экземпляр операции.
     */
    public void setRunUser(String runUser) {
        this.runUser = runUser;
    }

    /**
     * Возвращает тип операции.
     *
     * @return тип операции.
     */
    public OperationType getOperationType() {
        return operationType;
    }

    /**
     * Устанавливает тип операции.
     *
     * @param operationType тип операции.
     */
    public void setOperationType(OperationType operationType) {
        this.operationType = operationType;
    }

    /**
     * Возвращает контекст планировщика, если операция запускалась через планировщик. Если для
     * запуска планировщик не использовался, возвращается null.
     *
     * @return контекст планировщика.
     */
    public SchedulerContext getSchedulerContext() {
        return schedulerContext;
    }

    /**
     * Устанавливает контекст планировщика.
     *
     * @param schedulerContext контекст планировщика.
     */
    public void setSchedulerContext(SchedulerContext schedulerContext) {
        this.schedulerContext = schedulerContext;
    }

}
