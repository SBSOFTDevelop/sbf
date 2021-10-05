package ru.sbsoft.client.components.grid;

import com.google.gwt.user.client.Event;
import com.sencha.gxt.widget.core.client.event.CellMouseDownEvent;
import com.sencha.gxt.widget.core.client.event.XEvent;
import com.sencha.gxt.widget.core.client.grid.Grid;
import com.sencha.gxt.widget.core.client.selection.CellSelection;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author sychugin
 */
public class SBMultiCellSelectionModel<M> extends SBCellSelectionModel<M> {

    private final Callback callback;

    public interface Callback {

        boolean isEditable(Grid.GridCell cell);

    }

    private List<CellSelection<M>> selections;

    public SBMultiCellSelectionModel(Callback isEditable) {
        this.callback = isEditable;
    }

    @Override
    protected void handleMouseDown(CellMouseDownEvent event) {

        XEvent e = event.getEvent().<XEvent>cast();
        if (e.getButton() == Event.BUTTON_LEFT && !isLocked() && e.getAltKey()) {
            deselectAll();
            markCell(event.getRowIndex(), event.getCellIndex());

        } else {
            // clearSelections();

            super.handleMouseDown(event);
        }
    }

    public List<CellSelection<M>> getSelectionsForCell(Grid.GridCell cell) {
        if (selections == null || selections.isEmpty()) {
            return null;
        }
        boolean isPresent = selections.stream().filter((t) -> {
            return t.getCell() == cell.getCol() && t.getRow() == cell.getRow();
        }).findAny().isPresent();
        return isPresent ? selections : null;
    }

    public void clearSelections() {

        if (selections == null || selections.isEmpty()) {
            return;
        }

        selections.forEach((t) -> {

            ((SBLiveGridView) grid.getView()).markCell(t.getRow(), t.getCell(), false);

        });

        selections.clear();

    }

    private boolean isEditableRC(int row, int col) {

        return callback != null ? callback.isEditable(new Grid.GridCell(row, col)) : true;

    }

    private static class MarkModel<M> extends CellSelection<M> {

        public MarkModel(M model, int row, int cell) {
            super(model, row, cell);
        }

        @Override
        public boolean equals(Object obj) {
            if (super.equals(obj)) {
                return true;
            }
            if (((CellSelection) obj).getCell() != getCell()) {
                return false;
            }

            return (((CellSelection) obj).getRow() == getRow());
        }
    }

    private void markCell(int row, int cell) {

        if (!isEditableRC(row, cell)) {
            return;
        }

        M m = listStore.get(row);
        if (m != null) {

            if (selections == null) {
                selections = new ArrayList<>();
            }

            final CellSelection<M> sel = new MarkModel<M>(m, row, cell);
            if (grid.isViewReady()) {
                if (selections.indexOf(sel) >= 0) {
                    ((SBLiveGridView) grid.getView()).markCell(row, cell, false);
                    selections.remove(sel);

                    return;
                }
                selections.add(sel);
                ((SBLiveGridView) grid.getView()).markCell(row, cell);

            }
        }

    }

}
