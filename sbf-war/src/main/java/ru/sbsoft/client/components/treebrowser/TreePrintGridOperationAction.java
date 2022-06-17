package ru.sbsoft.client.components.treebrowser;

import ru.sbsoft.client.I18n;
import ru.sbsoft.client.consts.SBFResources;

/**
 *
 * @author sokolov
 */
public class TreePrintGridOperationAction extends TreeTypedGridOperationAction {
    
        public TreePrintGridOperationAction(AbstractTreeGrid grid, TreeGridOperationMaker operationMaker, TreeTypedGridOperationAction.TYPE_USE_MODE typeUseMode) {
        super(grid, operationMaker, typeUseMode);
        setIcon16(SBFResources.TREEMENU_ICONS.print16());
        setIcon24(SBFResources.TREEMENU_ICONS.print24());
        setToolTip(I18n.get(operationMaker.getType().getTitle()));
    }
    
    public TreePrintGridOperationAction(TreeGridOperationMaker operationMaker, TreeTypedGridOperationAction.TYPE_USE_MODE typeUseMode) {
        this(operationMaker.getGrid(), operationMaker, typeUseMode);
    }

}
