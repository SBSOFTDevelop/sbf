package ru.sbsoft.client.components.operation;

import ru.sbsoft.client.I18n;
import ru.sbsoft.shared.interfaces.OperationType;
import ru.sbsoft.shared.model.operation.OperationCommand;


/**
 * Базовый класс для операций имеющих тип в виде {@link OperationType} и не связанных с таблицей.
 * @see GridOperation
 */
public class TypedOperation extends AbstractOperation {

    private final OperationType type;

    public TypedOperation(final OperationType type) {
        this.type = type;
    }

    @Override
    protected OperationCommand createOperationCommand() {
        return new OperationCommand(type);
    }

    public static AbstractOperation create(OperationType type) {
        return create(type, null);
    }

    public static TypedOperation create(OperationType type, BaseOperationParamForm paramForm) {
        final TypedOperation operation = new TypedOperation(type);
        if (paramForm != null) {
            paramForm.setHeading(I18n.get(type.getTitle()));
            operation.setParamWindow(paramForm);
        }
        
        return operation;
    }
}
