package ru.sbsoft.client.components.operation.kladr;

import ru.sbsoft.client.components.operation.AbstractOperation;
import ru.sbsoft.shared.CommonOperationEnum;
import ru.sbsoft.shared.model.operation.OperationCommand;

/**
 * @author balandin
 * @since Mar 11, 2013 1:34:29 PM
 * @deprecated NO USAGES FOUND
 */
public class KLADRImportOperation extends AbstractOperation {

    private final CommonOperationEnum type;

    public KLADRImportOperation(CommonOperationEnum type) {
        this.type = type;
    }

    @Override
    protected OperationCommand createOperationCommand() {
        return new OperationCommand(type);
    }
}
