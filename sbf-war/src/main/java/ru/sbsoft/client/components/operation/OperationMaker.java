package ru.sbsoft.client.components.operation;

import ru.sbsoft.shared.interfaces.OperationType;

/**
 * Базовый класс для фабрик операций.
 * В отличие от {@link IOperationMaker} использует в качестве идентификатора операции {@link OperationType},
 * который предоставляет расширенную информацию об операции.
 * @author balandin
 * @since May 24, 2013 9:06:41 PM
 */
public abstract class OperationMaker implements IOperationMaker {

    private final OperationType type;

    public OperationMaker(OperationType type) {
        this.type = type;
    }

    @Override
    public String getOperationCode() {
        return type.getCode();
    }

    public OperationType getType() {
        return type;
    }
}
