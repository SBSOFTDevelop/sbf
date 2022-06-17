package ru.sbsoft.client.components.grid;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.dom.client.*;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Event;
import ru.sbsoft.svc.core.client.SVCLogConfiguration;
import ru.sbsoft.svc.core.client.Style;
import ru.sbsoft.svc.core.client.dom.XElement;
import ru.sbsoft.svc.data.shared.ListStore;
import ru.sbsoft.svc.widget.core.client.event.*;
import ru.sbsoft.svc.widget.core.client.grid.Grid;
import ru.sbsoft.svc.widget.core.client.grid.LiveGridView;
import ru.sbsoft.svc.widget.core.client.selection.CellSelection;
import ru.sbsoft.svc.widget.core.client.selection.CellSelectionChangedEvent;
import ru.sbsoft.shared.model.MarkModel;

import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

public class SBMultiCellSelectionModel<M extends MarkModel> extends SBCellSelectionModel<M> {


    private static final Logger logger = Logger.getLogger(SBMultiCellSelectionModel.class.getName());

    private final Callback callback;

    private boolean startSelection = false;
    private Handler handler = new Handler();

    private int idSelSession = 0;
    private ListStore<M> loaderStore;
    private Set<CellSelection<M>> selections;
    private boolean saveSel = false;


    private void logGWT(String log) {
        if (SVCLogConfiguration.loggingIsEnabled()) {
            logger.finest(log);

        }
    }

    public void bindGrid(Grid<M> grid) {
        super.bindGrid(grid);

        if (handlerRegistration == null) return;
        loaderStore = ((SBLiveGridView<M>) grid.getView()).getCacheStore();
        handlerRegistration.removeHandler();
        handlerRegistration.add(grid.addViewReadyHandler(this.handler));
        handlerRegistration.add(grid.addCellMouseDownHandler(this.handler));
        handlerRegistration.add(grid.addDomHandler(this.handler, MouseUpEvent.getType()));
        handlerRegistration.add(grid.addDomHandler(this.handler, MouseDownEvent.getType()));

        handlerRegistration.add(grid.addHandler(this.handler, FrozenGridMouseEvent.getType()));

        ensureHandlers().addHandler(BeforeStartEditEvent.getType(), beforeStartEditEvent -> {
            if (selections != null && !selections.isEmpty()) saveSel = true;
        });

        handlerRegistration.add(grid.addDomHandler(this.handler, MouseOverEvent.getType()));

        handlerRegistration.add(((LiveGridView<M>) grid.getView()).addLiveGridViewUpdateHandler(this.handler));
    }


//    @Override
//    public List<M> getSelectedItems() {
//
//        return (selections != null) ? selections.stream().map(CellSelection::getModel).collect(Collectors.toList()) :
//                new ArrayList<>();
//
//    }



//>>>>>>> 10873a546026147b9af85f02d57b29ae50f25895
    @Override
    public HandlerRegistration addCellSelectionChangedHandler(CellSelectionChangedEvent.CellSelectionChangedHandler<M> handler) {
        return ensureHandlers().addHandler(CellSelectionChangedEvent.getType(), handler);
    }


    private void restoreSel(int cnt) {

        if (selections == null) {
            return;
        }

        for (int r = 0; r < cnt; r++) {

            M m = listStore.get(r);
            if (m != null) {

                for (CellSelection<?> sel : selections) {

                    if (!sel.getModel().equals(m)) {
                        continue;
                    }

                    ((SBLiveGridView<?>) grid.getView()).onCellSelect(r, sel.getCell(), true);
                }

            }

        }

    }


    public Set<CellSelection<M>> getSelectionsForCell(Grid.GridCell cell) {
        if (selections == null || selections.isEmpty()) {
            return null;
        }
        boolean isPresent = selections.stream().anyMatch((t) ->
                t.getCell() == cell.getCol() && t.getRow() == cell.getRow());

        return isPresent ? selections : null;
    }



    public interface Callback {

        boolean isEditable(Grid.GridCell cell, boolean inCache);

    }

    public SBMultiCellSelectionModel(SBMultiCellSelectionModel.Callback isEditable) {
        selectionMode = Style.SelectionMode.MULTI;
        this.callback = isEditable;
    }

    @Override
    protected void handleMouseDown(CellMouseDownEvent event) {
        XEvent e = (XEvent) event.getEvent().cast();


        if (e.getButton() == NativeEvent.BUTTON_LEFT && !this.isLocked()) {

            startSelection = true;
            if (!e.getCtrlOrMetaKey() && !e.getAltKey()) {

                deselectAll();
                saveSel = false;

            } else {
                logGWT(" handleMouseDown select cell");


                idSelSession++;
                saveSel = true;


            }
            selectCell(event.getRowIndex(), event.getCellIndex());

        }
    }

    public void selectCell(int row, int cell) {
        M m = this.listStore.get(row);
        if (m != null) {
            if (this.selection != null && !saveSel) {
                deselectAll();
            }

            this.selection = new CellSelection(m, row, cell);
            if (grid.isViewReady()) {
                //grid.getView().onCellSelect(row, cell);

                ((SBLiveGridView<?>) grid.getView()).onCellSelect(row, cell, true);

                grid.getView().focusCell(row, cell, true);
                fireEvent(new CellSelectionChangedEvent(this.selection));
            }
        }

    }


    @Override
    public void deselect(int index) {

        if (selections == null) {
            return;
        }

        selections.removeIf(t -> {
            if (((CellSelectionModel<M>) t).getId() == index) {
                clear((CellSelectionModel<M>) t);
                return true;
            }
            return false;

        });

    }


    public void deselectAll() {
        super.deselectAll();
        if (this.selections != null) {


            selections.forEach((t)
                    -> clear((CellSelectionModel<M>) t));

            selections.clear();
            idSelSession = 0;
            saveSel = false;
        }
        logGWT("deselect all");
    }

    private void clear(CellSelectionModel<M> m) {
        int r = listStore.indexOf(m.getModel());
        if (r >= 0) {
            ((SBLiveGridView<M>) grid.getView()).onCellSelect(r, m.getCell(), false);
        }

    }


    private void handleMouseOver(MouseOverEvent event, Grid.GridCell cell) {
        if (!startSelection || cell == null) {
            return;
        }


        logGWT("MouseOver row=" + cell.getRow() + ", col=" + cell.getCol());
        doSelectCell(cell);

    }


    private void handleMouseUp(MouseUpEvent event, Grid.GridCell cell) {
        logGWT("MouseUp");

        startSelection = false;
    }

    private void handleMouseHeaderDown(Event event, Grid.GridCell cell) {
        if (cell == null) return;

        logGWT("handleMouseHeaderDown" + cell.getCol());

        handleMouseDown(new CellMouseDownEvent(0, cell.getCol(), event));
        doSelectCell(new Grid.GridCell(((SBLiveGridView) grid.getView()).getTotalCount() - 1, cell.getCol()));

    }

    private void handleFrozenGridMouseEvent(FrozenGridMouseEvent event) {
        logGWT("handleFrozenGridMouseEvent");
        final Grid.GridCell lastRowCell = new Grid.GridCell(event.getRowIndex(), grid.getColumnModel().getColumnCount());
        if (event.getEvent() instanceof MouseDownEvent) {

            handleMouseDown(new CellMouseDownEvent(event.getRowIndex(), 0, event.getEvent().getNativeEvent().cast()));
            doSelectCell(lastRowCell);
        } else if (event.getEvent() instanceof MouseOverEvent) {


            handleMouseOver((MouseOverEvent) event.getEvent(), lastRowCell);

        } else if (event.getEvent() instanceof MouseUpEvent) {

            handleMouseUp((MouseUpEvent) event.getEvent(), lastRowCell);

        }

    }


    protected void doSelectCell(Grid.GridCell cell) {

        if (this.isLocked() || selection == null) return;
        if (this.fireSelectionChangeOnClick) {
            this.fireSelectionChange();
            this.fireSelectionChangeOnClick = false;
        }

        int rowIndex = cell.getRow();
        int colIndex = cell.getCol();


        deselect(idSelSession);
        int minRow = Math.min(rowIndex, selection.getRow());
        int minCol = Math.min(colIndex, selection.getCell());

        int maxRow = Math.max(rowIndex, selection.getRow());
        int maxCol = Math.max(colIndex, selection.getCell());


        boolean isCache = minRow < 0;
        minRow = Math.max(minRow, 0);

        for (int i = minRow; i <= maxRow; i++) {
            for (int j = minCol; j <= maxCol; j++) {
                markCell(i, j, isCache);
            }

        }


        //       grid.getView().focusCell(rowIndex, colIndex, false);
//
//        if (!this.focusCellCalled) {
//            this.grid.getView().focusCell(rowIndex, colIndex, false);
//        }


    }


    private boolean isEditableRC(int row, int col, boolean inCache) {
        return callback == null || callback.isEditable(new Grid.GridCell(row, col), inCache);
    }


    private void markCell(int row, int col, boolean inCache) {

        if (!isSelectable(row, col) || !isEditableRC(row, col, inCache)) {
            return;
        }

        M m = inCache ? loaderStore.get(row) : listStore.get(row);

        if (m != null) {

            if (selections == null) {
                selections = new HashSet<>();
            }

            final CellSelectionModel<M> sel = new CellSelectionModel<>(m, row, col, idSelSession);
            if (grid.isViewReady()) {


                selections.add(sel);
                int r = listStore.indexOf(m);

                if (r >= 0) ((SBLiveGridView<?>) grid.getView()).onCellSelect(r, col, true);
                logGWT("select r=" + r + ", col=" + col);


            }
        }

    }


    private class Handler implements ViewReadyEvent.ViewReadyHandler, CellMouseDownEvent.CellMouseDownHandler,
            MouseOverHandler, MouseUpHandler, LiveGridViewUpdateEvent.LiveGridViewUpdateHandler, MouseDownHandler,
            FrozenGridMouseEvent.MouseEventHandler, BeforeStartEditEvent.HasBeforeStartEditHandlers<M> {
        private Handler() {
        }

        public void onCellMouseDown(CellMouseDownEvent event) {
            handleMouseDown(event);
        }

        public void onViewReady(ViewReadyEvent event) {
            if (selection != null) {
                CellSelection<M> sel = selection;
                selection = null;
                selectCell(sel.getRow(), sel.getCell());
            }

        }


        private Grid.GridCell findCell(NativeEvent e) {

            Element target = Element.as(e.getEventTarget());

            if (grid != null && grid.getView().isSelectableTarget(target) && grid.getView().getBody().isOrHasChild(target)) {
                int row = grid.getView().findRowIndex(target);
                int col = grid.getView().findCellIndex(target, (String) null);
                if (row != -1 && col != -1) {
                    return new Grid.GridCell(row, col);
                }
            }

            return null;
        }

        private Grid.GridCell getHeaderCell(NativeEvent event) {

            XElement target = event.getEventTarget().cast();
            for (int i = 0; i < grid.getColumnModel().getColumnCount(); i++) {

                if (grid.getView().getHeader().getHead(i).getElement().equals(target)) {

                    return new Grid.GridCell(-1, i);

                }

            }
            return null;
        }


        @Override
        public void onMouseOver(MouseOverEvent event) {
            handleMouseOver(event, findCell(event.getNativeEvent()));
        }


        @Override
        public void onMouseUp(MouseUpEvent event) {
            handleMouseUp(event, findCell(event.getNativeEvent()));
        }


        @Override
        public void onUpdate(LiveGridViewUpdateEvent liveGridViewUpdateEvent) {
            logGWT("updateRows10  getViewIndex(): " + liveGridViewUpdateEvent.getViewIndex() + " event.getLiveStoreOffset(): "
                    + liveGridViewUpdateEvent.getLiveStoreOffset() + " count: " + liveGridViewUpdateEvent.getRowCount());

            restoreSel(liveGridViewUpdateEvent.getRowCount());
        }

        @Override
        public void onMouseDown(MouseDownEvent event) {
            handleMouseHeaderDown(event.getNativeEvent().cast(), getHeaderCell(event.getNativeEvent()));
        }

        @Override
        public void onFrozenGridMouseEvent(FrozenGridMouseEvent event) {
            handleFrozenGridMouseEvent(event);
        }

        @Override
        public HandlerRegistration addBeforeStartEditHandler(BeforeStartEditEvent.BeforeStartEditHandler beforeStartEditHandler) {
            return null;
        }

        @Override
        public void fireEvent(GwtEvent<?> gwtEvent) {

        }
    }


    private static class CellSelectionModel<M extends MarkModel> extends CellSelection<M> {

        private final int id;

        public CellSelectionModel(M model, int row, int cell, int id) {
            super(model, row, cell);
            this.id = id;

        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof CellSelectionModel)) return false;
            CellSelectionModel<?> that = (CellSelectionModel<?>) o;


            if (getCell() != that.getCell()) return false;
            return getModel().equals(that.getModel());
        }

        public int getId() {
            return id;
        }

    }

}
