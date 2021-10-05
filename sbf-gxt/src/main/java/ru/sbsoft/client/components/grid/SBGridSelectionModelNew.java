package ru.sbsoft.client.components.grid;

import com.google.gwt.dom.client.NativeEvent;
import com.sencha.gxt.widget.core.client.grid.Grid;
import com.sencha.gxt.widget.core.client.grid.GridSelectionModel;

/**
 *
 * @author Kiselev
 */
public class SBGridSelectionModelNew<M> extends GridSelectionModel<M> {

    private SBGridSelectionBehavior<M> behavior = new SBGridSelectionBehavior<>();

    @Override
    public void bindGrid(Grid<M> grid) {
        super.bindGrid(grid);
        behavior.bindGrid(grid);
    }

    @Override
    protected void onKeyPress(NativeEvent ne) {
        if(!behavior.handleKeyPress(ne)){
            super.onKeyPress(ne);
        }
    }

    @Override
    protected void onKeyUp(NativeEvent e) {
        //handled by SBGridSelectionBehavior in onKeyPress
    }

    @Override
    protected void onKeyDown(NativeEvent ne) {
        //handled by SBGridSelectionBehavior in onKeyPress
    }
}
