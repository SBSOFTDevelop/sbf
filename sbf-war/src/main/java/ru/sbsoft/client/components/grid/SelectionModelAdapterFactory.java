package ru.sbsoft.client.components.grid;

import com.google.gwt.event.shared.HandlerRegistration;
import ru.sbsoft.svc.core.client.Style;
import ru.sbsoft.svc.widget.core.client.grid.CellSelectionModel;
import ru.sbsoft.svc.widget.core.client.grid.Grid;
import ru.sbsoft.svc.widget.core.client.grid.GridSelectionModel;
import ru.sbsoft.svc.widget.core.client.selection.CellSelection;
import ru.sbsoft.svc.widget.core.client.selection.CellSelectionChangedEvent;
import ru.sbsoft.svc.widget.core.client.selection.SelectionChangedEvent;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import ru.sbsoft.client.utils.CliUtil;

/**
 *
 * @author Kiselev
 */
public final class SelectionModelAdapterFactory {

    public static <M> ISelectionModelAdapter<M> createSelectionAdapter(Grid<M> grid) {
        GridSelectionModel<M> sm = grid.getSelectionModel();
        return sm instanceof CellSelectionModel ? new CellSelectionModelAdapter<>((CellSelectionModel) sm) : new GridSelectionModelAdapter<>(sm);
    }

    private static abstract class AbstractSelectionModelAdapter<SM extends GridSelectionModel<M>, M> implements ISelectionModelAdapter<M> {

        protected final SM model;

        public AbstractSelectionModelAdapter(SM model) {
            Objects.requireNonNull(model, "Model can't be null");
            this.model = model;
        }

        @Override
        public M getSelectedItem() {
            return model.getSelectedItem();
        }

        @Override
        public List<M> getSelectedItems() {
            return model.getSelectedItems();
        }

        @Override
        public Style.SelectionMode getSelectionMode() {
            return model.getSelectionMode();
        }
    }

    public static class GridSelectionModelAdapter<M> extends AbstractSelectionModelAdapter<GridSelectionModel<M>, M> {

        public GridSelectionModelAdapter(GridSelectionModel<M> model) {
            super(model);
            CliUtil.requireNotInstanceOf(model, CellSelectionModel.class, "Grid celection model");
        }

        @Override
        public void setSelection(M item) {
            model.select(item, false);
        }

        @Override
        public void setSelection(int index) {
            model.select(index, false);
        }

        @Override
        public HandlerRegistration addSelectionChangedHandler(SelectionChangedEvent.SelectionChangedHandler<M> handler) {
            return model.addSelectionChangedHandler(handler);
        }

        @Override
        public boolean tryInitSelection() {
            if (model.getSelectedItem() == null) {
                setSelection(0);
                return true;
            }
            return false;
        }

        @Override
        public void free() {
            //nothing to do
        }

        @Override
        public void selectCell(Grid.GridCell cell) {
            setSelection(cell.getRow());
            model.getGrid().getView().focusCell(cell.getRow(), cell.getCol(), true);
        }
    }

    private static class CellSelectionModelAdapter<M> extends AbstractSelectionModelAdapter<CellSelectionModel<M>, M> {

        private final Grid<M> grid;
        private int currCellNum = 0;
        private HandlerRegistration selChangeHandler;

        public CellSelectionModelAdapter(CellSelectionModel<M> model) {
            super(model);
            CliUtil.requireInstanceOf(model, CellSelectionModel.class, "Cell celection model");
            this.grid = model.getGrid();
            selChangeHandler = model.addCellSelectionChangedHandler(new CellSelectionChangedEvent.CellSelectionChangedHandler<M>() {
                @Override
                public void onCellSelectionChanged(CellSelectionChangedEvent<M> event) {
                    setCurrCellNum();
                }
            });
            setCurrCellNum();
        }

        private void setCurrCellNum() {
            CellSelection<M> s = model.getSelectCell();
            if (s != null) {
                currCellNum = s.getCell();
            }
        }

        @Override
        public void setSelection(M item) {
            int index = grid.getStore().indexOf(item);
            if (index >= 0) {
                setSelection(index);
            }
        }

        @Override
        public void setSelection(int index) {
            model.selectCell(index, currCellNum);
        }

        @Override
        public HandlerRegistration addSelectionChangedHandler(final SelectionChangedEvent.SelectionChangedHandler<M> handler) {
            return model.addCellSelectionChangedHandler(new CellSelectionChangedEvent.CellSelectionChangedHandler<M>() {
                @Override
                public void onCellSelectionChanged(CellSelectionChangedEvent<M> event) {
                    SelectionChangedEvent<M> selEv = new SelectionChangedEvent<>(Collections.singletonList(getSelectedItem()));
                    handler.onSelectionChanged(selEv);
                }
            });
        }

        @Override
        public boolean tryInitSelection() {
            if (model.getSelectCell() == null) {
                Grid.GridCell cell = grid.walkCells(0, 0, 1, new GridSelectionModel.SelectionModelCallback(model));
                if (cell != null) {
                    model.selectCell(cell.getRow(), cell.getCol());
                }else{
                    model.selectCell(0, 0);
                }
                return true;
            }
            return false;
        }

        @Override
        public void free() {
            if(selChangeHandler != null){
                selChangeHandler.removeHandler();
                selChangeHandler = null;
            }
        }

        @Override
        public void selectCell(Grid.GridCell cell) {
            model.selectCell(cell.getRow(), cell.getCol());
        }
    }
}
