package ru.sbsoft.client.components.treebrowser;

import ru.sbsoft.client.consts.SBFResources;

/**
 *
 * @author sokolov
 */
public class TreeRunGridOperationAction extends TreeTypedGridOperationAction {

    public TreeRunGridOperationAction(AbstractTreeGrid grid, TreeGridOperationMaker operationMaker, TreeTypedGridOperationAction.TYPE_USE_MODE typeUseMode) {
        super(grid, operationMaker, typeUseMode);
        if (TreeTypedGridOperationAction.TYPE_USE_MODE.ON_SELECTED_RECORD == typeUseMode) {
            setIcon16(SBFResources.GENERAL_ICONS.RunCurrent16());
            setIcon24(SBFResources.GENERAL_ICONS.RunCurrent());
        } else {
            setIcon16(SBFResources.GENERAL_ICONS.Run16());
            setIcon24(SBFResources.GENERAL_ICONS.Run());
        }
    }

    public TreeRunGridOperationAction(TreeGridOperationMaker operationMaker, TreeTypedGridOperationAction.TYPE_USE_MODE typeUseMode) {
        this(operationMaker.getGrid(), operationMaker, typeUseMode);
    }

}
