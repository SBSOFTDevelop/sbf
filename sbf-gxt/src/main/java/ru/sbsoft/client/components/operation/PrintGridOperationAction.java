package ru.sbsoft.client.components.operation;

import ru.sbsoft.client.I18n;
import ru.sbsoft.client.components.grid.BaseGrid;
import ru.sbsoft.client.consts.SBFResources;

/**
 *
 * @author Sokoloff
 * @author kiselev
 */
public class PrintGridOperationAction extends TypedGridOperationAction {

    public PrintGridOperationAction(BaseGrid grid, GridOperationMaker operationMaker, TYPE_USE_MODE typeUseMode) {
        super(grid, operationMaker, typeUseMode);
        setIcon16(SBFResources.TREEMENU_ICONS.print16());
        setIcon24(SBFResources.TREEMENU_ICONS.print24());
        setToolTip(I18n.get(operationMaker.getType().getTitle()));
    }
    
    public PrintGridOperationAction(GridOperationMaker operationMaker, TYPE_USE_MODE typeUseMode) {
        this(operationMaker.getGrid(), operationMaker, typeUseMode);
    }

}
