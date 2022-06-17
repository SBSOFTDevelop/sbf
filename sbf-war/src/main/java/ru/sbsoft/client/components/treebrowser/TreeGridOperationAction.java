package ru.sbsoft.client.components.treebrowser;

import ru.sbsoft.client.components.operation.IOperationMaker;

/**
 *
 * @author sokolov
 */
public class TreeGridOperationAction extends TreeGridAction {

    private final IOperationMaker operationMaker;

    public TreeGridOperationAction(AbstractTreeGrid grid, IOperationMaker operationMaker) {
        super(grid);
        this.operationMaker = operationMaker;
    }

    @Override
    protected void onExecute() {
        operationMaker.createOperation().startOperation();
    }

}
