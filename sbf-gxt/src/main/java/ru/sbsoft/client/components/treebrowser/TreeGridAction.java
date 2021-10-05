package ru.sbsoft.client.components.treebrowser;

import ru.sbsoft.client.components.actions.AbstractAction;

/**
 *
 * @author sokolov
 */
public abstract class TreeGridAction extends AbstractAction {
    
    private final AbstractTreeGrid grid;

    public TreeGridAction(AbstractTreeGrid grid) {
        this.grid = grid;
    }

    public AbstractTreeGrid getGrid() {
        return grid;
    }
    
    protected boolean isSelection() {
        return grid.isTreeSelect();
    }

    @Override
    public boolean checkEnabled() {
        return super.checkEnabled() && grid.isInitialized();
    }
}
