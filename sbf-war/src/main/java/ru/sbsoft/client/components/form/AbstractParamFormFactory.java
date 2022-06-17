package ru.sbsoft.client.components.form;

import ru.sbsoft.client.I18n;
import ru.sbsoft.shared.interfaces.OperationType;
import ru.sbsoft.client.utils.CliUtil;

/**
 * Базовая функциональность для фабрик форм.
 * @author Kiselev
 */
public abstract class AbstractParamFormFactory implements IParamFormFactory {

    private String header = null;
    private OperationType operationType = null;

    protected AbstractParamFormFactory(OperationType operationType) {
        setOperationType(operationType);
    }

    protected AbstractParamFormFactory(String header) {
        setHeader(header);
    }

    public final String getHeader() {
        return header;
    }

    public final void setHeader(String header) {
        this.header = header;
    }

    public final void setOperationType(OperationType operationType) {
        setHeader(operationType != null ? I18n.get(operationType.getTitle()) : null);
        this.operationType = operationType;
    }

    public final OperationType getOperationType() {
        return operationType;
    }
    
    protected final String getCode() {
        return (operationType != null ? operationType.getCode() : CliUtil.getSimpleName(this)) + "Param";
    }

}
