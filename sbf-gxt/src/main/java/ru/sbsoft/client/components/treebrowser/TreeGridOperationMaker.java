package ru.sbsoft.client.components.treebrowser;

import ru.sbsoft.client.components.operation.OperationMaker;
import ru.sbsoft.shared.interfaces.OperationType;

/**
 *
 * @author sokolov
 */
public abstract class TreeGridOperationMaker extends OperationMaker {

    private final AbstractTreeGrid grid;

    public TreeGridOperationMaker(OperationType type, AbstractTreeGrid grid) {
        super(type);
        this.grid = grid;
    }

    public AbstractTreeGrid getGrid() {
        return grid;
    }

}
