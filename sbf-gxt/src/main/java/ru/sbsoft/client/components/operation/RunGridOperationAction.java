package ru.sbsoft.client.components.operation;

import ru.sbsoft.client.components.grid.BaseGrid;
import ru.sbsoft.client.consts.SBFResources;

/**
 *
 * @author Sokoloff
 * @author kiselev
 */
public class RunGridOperationAction extends TypedGridOperationAction {

    public RunGridOperationAction(BaseGrid grid, GridOperationMaker operationMaker, TYPE_USE_MODE typeUseMode) {
        super(grid, operationMaker, typeUseMode);
        if (TYPE_USE_MODE.ON_SELECTED_RECORD == typeUseMode) {
            setIcon16(SBFResources.GENERAL_ICONS.RunCurrent16());
            setIcon24(SBFResources.GENERAL_ICONS.RunCurrent());
        } else {
            setIcon16(SBFResources.GENERAL_ICONS.Run16());
            setIcon24(SBFResources.GENERAL_ICONS.Run());
        }
    }
    
    public RunGridOperationAction(GridOperationMaker operationMaker, TYPE_USE_MODE typeUseMode) {
        this(operationMaker.getGrid(), operationMaker, typeUseMode);
    }

}
