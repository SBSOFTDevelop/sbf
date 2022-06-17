package ru.sbsoft.client.components.grid;

import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.dom.client.KeyCodes;
import ru.sbsoft.svc.widget.core.client.grid.CellSelectionModel;
import ru.sbsoft.svc.widget.core.client.grid.Grid;
import ru.sbsoft.svc.widget.core.client.selection.CellSelection;

/**
 *
 * @author Kiselev
 */
public class SBCellSelectionModel<M> extends CellSelectionModel<M> {

    private SBGridSelectionBehavior<M> behavior = new SBGridSelectionBehavior<>();
    private SelectionModelCallback callback = new SelectionModelCallback(this);

    @Override
    public void bindGrid(Grid<M> grid) {
        super.bindGrid(grid);
        behavior.bindGrid(grid);
    }

    @Override
    protected void onKeyPress(NativeEvent ne) {
        if (!behavior.handleKeyPress(ne)) {
            int keyCode = ne.getKeyCode();
            boolean isRight = keyCode == KeyCodes.KEY_RIGHT;
            boolean isLeft = keyCode == KeyCodes.KEY_LEFT;
            if (isRight || isLeft) {
                ne.preventDefault();
                ne.stopPropagation();
                CellSelection<M> currSel = getSelectCell();
                int step = isRight ? 1 : -1;
                behavior.selectNextCell(currSel.getRow(), currSel.getCell() + step, step, callback);
            } else {
                super.onKeyPress(ne);
            }
        }
    }

}
