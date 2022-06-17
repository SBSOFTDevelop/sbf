package ru.sbsoft.client.components.treebrowser;

import ru.sbsoft.client.I18n;
import ru.sbsoft.client.components.operation.AbstractOperation;
import ru.sbsoft.client.components.operation.BaseOperationParamForm;
import ru.sbsoft.shared.interfaces.OperationType;
import ru.sbsoft.shared.model.operation.OperationCommand;

/**
 *
 * @author sokolov
 */
public class TreeGridOperation extends AbstractOperation {

    private final AbstractTreeGrid grid;
    private final OperationType type;
    //
    private boolean reloadOnComplete = true;

    public TreeGridOperation(AbstractTreeGrid grid, OperationType type) {
        this.grid = grid;
        this.type = type;
    }
    
    @Override
    protected OperationCommand createOperationCommand() {
        return grid.createGridOperationCommand(type);
    }

    public static TreeGridOperation create(AbstractTreeGrid grid, OperationType type, BaseOperationParamForm paramForm) {
        paramForm.setHeading(I18n.get(type.getTitle()));
        final TreeGridOperation operation = new TreeGridOperation(grid, type);
        operation.setParamWindow(paramForm);
        return operation;
    }

    @Override
    public void onCompleted() {
        
    }

    @Override
    public void onClose(Long operationId) {

        if (reloadOnComplete) {
            grid.refresh(true);
        }
    }

    public boolean isReloadOnComplete() {
        return reloadOnComplete;
    }

    public TreeGridOperation setReloadOnComplete(boolean reloadOnComplete) {
        this.reloadOnComplete = reloadOnComplete;
        return this;
    }    
    
}
