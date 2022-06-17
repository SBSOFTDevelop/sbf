package ru.sbsoft.client.components.operation;

import ru.sbsoft.client.components.grid.BaseGrid;
import ru.sbsoft.shared.CommonOperationEnum;
import ru.sbsoft.shared.model.operation.OperationCommand;

/**
 * @deprecated NO USAGES FOUND
 */
public class ExportOperation extends AbstractOperation {

    private final BaseGrid grid;

    public ExportOperation(final BaseGrid grid) {
        super();
        this.grid = grid;
    }

    @Override
    protected OperationCommand createOperationCommand() {
        return grid.createGridOperationCommand(CommonOperationEnum.EXPORT);
    }
}
