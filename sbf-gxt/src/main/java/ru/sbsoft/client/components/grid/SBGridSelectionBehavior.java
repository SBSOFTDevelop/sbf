package ru.sbsoft.client.components.grid;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.shared.HandlerRegistration;
import com.sencha.gxt.core.client.dom.XElement;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.widget.core.client.event.LiveGridViewUpdateEvent;
import com.sencha.gxt.widget.core.client.grid.CellSelectionModel;
import com.sencha.gxt.widget.core.client.grid.Grid;
import com.sencha.gxt.widget.core.client.grid.LiveGridView;
import com.sencha.gxt.widget.core.client.selection.SelectionChangedEvent;

/**
 *
 * @author Kiselev
 */
public class SBGridSelectionBehavior<M> {

    private Grid<M> grid;
    private ISelectionModelAdapter<M> modelAdapter;
    private ListStore<M> listStore = null;
    private HandlerRegistration onUpdateHandler = null;
    private HandlerRegistration selChangeHandler = null;
    private M lastSelectedModel = null;
    //private int lastAbsIndex = -1;

    public void bindGrid(Grid<M> grid) {
        if (onUpdateHandler != null) {
            onUpdateHandler.removeHandler();
            onUpdateHandler = null;
        }
        if (selChangeHandler != null) {
            selChangeHandler.removeHandler();
            selChangeHandler = null;
        }
        if (modelAdapter != null) {
            modelAdapter.free();
            modelAdapter = null;
        }
        saveLastSelection(null);
        listStore = null;
        this.grid = grid;
        if (grid != null) {
            modelAdapter = SelectionModelAdapterFactory.createSelectionAdapter(grid);
            saveLastSelection(grid.getSelectionModel().getSelectedItem());
            if (grid.getStore() instanceof ListStore) {
                listStore = (ListStore<M>) grid.getStore();
            }
            if (grid.getView() instanceof LiveGridView) {
                onUpdateHandler = ((LiveGridView<M>) grid.getView()).addLiveGridViewUpdateHandler(new LiveGridViewUpdateEvent.LiveGridViewUpdateHandler() {
                    @Override
                    public void onUpdate(LiveGridViewUpdateEvent event) {
                        if (lastSelectedModel != null && (getSelectedItem() == null || (grid.getSelectionModel() instanceof CellSelectionModel))) {
                            int selPos = listStore.indexOf(lastSelectedModel);
                            saveLastSelection(null);
                            if (selPos >= 0) {
                                smartSelect(selPos);
                            }
                        }
                    }
                });
            }
            selChangeHandler = modelAdapter.addSelectionChangedHandler(new SelectionChangedEvent.SelectionChangedHandler<M>() {
                @Override
                public void onSelectionChanged(SelectionChangedEvent<M> event) {
                    if (getSelectedItem() != null) {
                        saveLastSelection(getSelectedItem());
                    }
                }
            });
        }
    }

    private void saveLastSelection(M sel) {
//        if (sel != null) {
//            lastAbsIndex = getAbsIndex(grid.getStore().indexOf(sel));
//        } else {
//            lastAbsIndex = -1;
//        }
        lastSelectedModel = sel;
    }

    private M getSelectedItem() {
        return modelAdapter.getSelectedItem();
    }

    public boolean handleKeyPress(NativeEvent ne) {
        if (!isValid(ne, grid)) {
            return false;
        }
        if (modelAdapter.tryInitSelection()) {
            return true;
        } else {
            int lastSelectedIndex = listStore.indexOf(lastSelectedModel);
            //int lastAbsIndex = getAbsIndex(lastSelectedIndex);
            int keyCode = ne.getKeyCode();
            boolean isPgUp = keyCode == KeyCodes.KEY_PAGEUP;
            boolean isPgDown = keyCode == KeyCodes.KEY_PAGEDOWN;
            boolean isHome = keyCode == KeyCodes.KEY_HOME && ne.getCtrlKey();
            boolean isEnd = keyCode == KeyCodes.KEY_END && ne.getCtrlKey();
            Scroller scroller = new Scroller();
            if (isHome || isEnd || isPgUp || isPgDown) {
                stopEvent(ne);
                if (isPgUp || isPgDown) {
                    int pageSize = getVisibleRowCount(grid);
                    if (scroller.setMove(isPgDown ? pageSize : -pageSize)) {
                        scroller.doMove(lastSelectedIndex);
                    } else if (isPgDown && lastSelectedIndex != listStore.size() - 1) {
                        smartSelect(listStore.size() - 1);
                    } else if (isPgUp && lastSelectedIndex != 0) {
                        smartSelect(0);
                    }
                } else if (isHome || isEnd) {
                    int selIndex = isHome ? 0 : Integer.MAX_VALUE;
                    if (scroller.setMove(isHome ? Integer.MIN_VALUE : Integer.MAX_VALUE)) {
                        scroller.doMove(selIndex);
                    } else {
                        smartSelect(selIndex);
                    }
                }
                return true;
            }
            boolean isUp = keyCode == KeyCodes.KEY_UP;
            boolean isDown = keyCode == KeyCodes.KEY_DOWN;
            if (isUp || isDown) {
                stopEvent(ne);
                selectNext(isDown);
                return true;
            }
        }
        return false;
    }

    public static interface SelectionChangeResult {

        void onSelectionChangeFinish(boolean isSelectionChanged);
    }

    private void selectNext(final boolean forward) {
        selectNext(forward, null);
    }

    public void selectNext(final boolean forward, SelectionChangeResult selectionChangeResult) {
        Scroller scroller = new Scroller();
        final M selMod = modelAdapter.getSelectedItem();
        final int currSelIndex = listStore.indexOf(selMod);
        final int visRowCount = getVisibleRowCount(grid);
        IndexProvider indexProvider = () -> currSelIndex + (forward ? 1 : - 1);
        if ((forward && currSelIndex < visRowCount) || (!forward && currSelIndex > 0)) {
            smartSelect(indexProvider.getIndex(), selectionChangeResult);
        } else if (scroller.setMove((visRowCount - 1) * (forward ? 1 : -1))) {
            scroller.doMove(indexProvider, selectionChangeResult);
        } else {
            if (selectionChangeResult != null) {
                selectionChangeResult.onSelectionChangeFinish(false);
            }
        }
    }

    public static interface CellResult {

        void onResult(Grid.GridCell cell);
    }

    public void selectNextCell(int fromRow, int fromCell, final int step, final Grid.Callback checkCallback) {
        selectNextCell(fromRow, fromCell, step, checkCallback, null);
    }

    public void selectNextCell(int fromRow, int fromCell, final int step, final Grid.Callback checkCallback, CellResult resultCallback) {
        Grid.GridCell nextCell = walkRowCells(grid, fromRow, fromCell, step, checkCallback);
        if (nextCell != null) {
            modelAdapter.selectCell(nextCell);
            if (resultCallback != null) {
                resultCallback.onResult(nextCell);
            }
        } else {
            selectNext(step > 0, new SBGridSelectionBehavior.SelectionChangeResult() {
                @Override
                public void onSelectionChangeFinish(boolean isSelectionChanged) {
                    if (isSelectionChanged) {
                        M sel = grid.getSelectionModel().getSelectedItem();
                        if (sel != null) {
                            int rowIndex = grid.getStore().indexOf(sel);
                            if (rowIndex >= 0) {
                                int clen = grid.getColumnModel().getColumnCount();
                                selectNextCell(rowIndex, step > 0 ? 0 : clen - 1, step, checkCallback, resultCallback);
                                return;
                            }
                        }
                    }
                    if (resultCallback != null) {
                        resultCallback.onResult(null);
                    }
                }
            });
        }
    }

    public static <M> Grid.GridCell walkRowCells(Grid<M> grid, int row, int fromCol, int step, Grid.Callback callback) {
        int clen = grid.getColumnModel().getColumnCount();
        if (step == 0) {
            step = clen;
        }
        for (int col = fromCol; (step < 0 && col >= 0) || (step > 0 && col < clen); col += step) {
            Grid.GridCell cell = new Grid.GridCell(row, col);
            if (callback.isSelectable(cell)) {
                return cell;
            }
        }
        return null;
    }

    private class Scroller<M> {

        private final XElement scroll;
        private final int rowHeight;
        private final int lastTop;
        private int currentTop;
        private int nextTop;

        public Scroller() {
            scroll = grid.getView().getScroller();
            rowHeight = GridHelper.getRowHeight(grid);
            lastTop = GridHelper.getScrollHeight(grid) - GridHelper.getVisibleHeight(grid);
            currentTop = scroll.getScrollTop();
            nextTop = currentTop;
        }

        public boolean setMove(int rows) {
            currentTop = scroll.getScrollTop();
            final long topStep = rowHeight * rows;
            long nextTopL = currentTop + topStep;
            if (nextTopL < 0) {
                nextTop = 0;
            } else if (nextTopL > lastTop) {
                nextTop = lastTop;
            } else {
                nextTop = (int) nextTopL;
            }
            return hasMove();
        }

        public boolean hasMove() {
            return nextTop != currentTop;
        }

        public void doMove(int selectIndex) {
            doMove(() -> selectIndex, null);
        }

        public void doMove(IndexProvider selectIndexProvider, SelectionChangeResult selectionChangeResult) {
            if (hasMove()) {
                if (currentTop != scroll.getScrollTop()) {
                    throw new IllegalStateException("Scroll is moved from " + currentTop + " to " + scroll.getScrollTop());
                }
                final LiveGridViewOnUpdateSelector ss;
                if (selectIndexProvider != null) {
                    ss = new LiveGridViewOnUpdateSelector(selectIndexProvider);
                    ss.setSelectionChangeResult(selectionChangeResult);
                } else {
                    ss = null;
                }
                scroll.setScrollTop(nextTop);
                if (ss != null) {
                    ss.done();
                }
                currentTop = scroll.getScrollTop();
                nextTop = currentTop;
            }
        }
    }

    private void smartSelect(final int index) {
        smartSelect(index, null);
    }

    private void smartSelect(final int index, SelectionChangeResult changeResult) {
        boolean selChanged = false;
        if (listStore != null && listStore.size() > 0) {
            int pos = index;
            if (pos < 0) {
                pos = 0;
            } else if (pos > listStore.size() - 1) {
                pos = listStore.size() - 1;
            }
            M currSel = modelAdapter.getSelectedItem();
            int currSelIndex = listStore.indexOf(currSel);
            if (pos != currSelIndex) {
                selChanged = true;
            }
            modelAdapter.setSelection(pos);
            grid.getView().ensureVisible(pos, 0, false);
            grid.getView().focusRow(pos);
        }
        if (changeResult != null) {
            changeResult.onSelectionChangeFinish(selChanged);
        }
    }

    private interface IndexProvider {

        int getIndex();
    }

    private class LiveGridViewOnUpdateSelector {

        private HandlerRegistration reg = null;
        private boolean immediate = false;
        private final IndexProvider indexProvider;
        private SelectionChangeResult selectionChangeResult;

        public LiveGridViewOnUpdateSelector(IndexProvider indexProvider) {
            this.indexProvider = indexProvider;
            if (grid.getView() instanceof LiveGridView) {
                saveLastSelection(null);
                reg = ((LiveGridView<M>) grid.getView()).addLiveGridViewUpdateHandler(new LiveGridViewUpdateEvent.LiveGridViewUpdateHandler() {
                    @Override
                    public void onUpdate(LiveGridViewUpdateEvent event) {
                        if (reg != null) {
                            reg.removeHandler();
                        }
                        doSelect();
                    }
                });
            } else {
                immediate = true;
            }
        }

        public LiveGridViewOnUpdateSelector(int index) {
            this(() -> index);
        }

        public void setSelectionChangeResult(SelectionChangeResult selectionChangeResult) {
            this.selectionChangeResult = selectionChangeResult;
        }

        public void done() {
            if (immediate) {
                doSelect();
            }
        }

        private void doSelect() {
            smartSelect(indexProvider.getIndex(), selectionChangeResult);
        }
    }

//    private int getTotalRowCount() {
//        if (grid.getLoader() instanceof PagingLoader<?, ?>) {
//            return ((PagingLoader<?, ?>) grid.getLoader()).getTotalCount();
//        } else {
//            int rh = getRowHeight();
//            int totalHeight = getScrollHeight();
//            return (int) ((totalHeight < 1 || rh < 1) ? 0 : Math.round((double) totalHeight / rh));
//        }
//    }
//
    private static <M> int getVisibleRowCount(Grid<M> grid) {
        int rh = GridHelper.getRowHeight(grid);
        int visibleHeight = GridHelper.getVisibleHeight(grid);
        return (int) ((visibleHeight < 1 || rh < 1) ? 0 : Math.floor((double) visibleHeight / rh));
    }

    public static void stopEvent(NativeEvent ne) {
        ne.preventDefault();
        ne.stopPropagation();
    }

    public static <M> boolean isValid(NativeEvent ne, Grid<M> grid) {
        if (Element.is(ne.getEventTarget()) && !grid.getView().isSelectableTarget(Element.as(ne.getEventTarget()))) {
            return false;
        }
        ListStore<M> listStore = grid.getStore();
        if (listStore == null || listStore.size() <= 0) {
            return false;
        }
        return true;
    }

// ============== See GridHelper =================
//    private int getTopVisibleRowIndex() {
//        return (int) Math.ceil((double) grid.getView().getScroller().getScrollTop() / getRowHeight(grid));
//    }
//
//    private int getBottomVisibleRowIndex() {
//        return (int) Math.floor((double) (grid.getView().getScroller().getScrollTop() + getVisibleHeight(grid)) / getRowHeight(grid));
//    }
//
//    private static <M> int getScrollHeight(Grid<M> grid) {
//        return grid.getView().getScroller().getScrollHeight();
//    }
//
//    private static <M> int getVisibleHeight(Grid<M> grid) {
//        return grid.getView().getScroller().getHeight(true);
//    }
//
//    private static <M> int getRowHeight(Grid<M> grid) {
//        Element row0 = grid.getView().getRow(0);
//        if (row0 == null) {
//            return -1;
//        }
//        return row0.getOffsetHeight();
//    }
//
//    private int getTotalRowCount() {
//        if (grid.getLoader() instanceof PagingLoader<?, ?>) {
//            return ((PagingLoader<?, ?>) grid.getLoader()).getTotalCount();
//        } else {
//            return grid.getStore().size();
//        }
//    }
//
//    private int getLoaderOffset() {
//        if (grid.getLoader() instanceof PagingLoader<?, ?>) {
//            return ((PagingLoader<?, ?>) grid.getLoader()).getOffset();
//        } else {
//            return 0;
//        }
//    }
//  ===========================================================================
    
    private M getSelection() {
        M item = grid.getSelectionModel().getSelectedItem();
        return item != null ? item : lastSelectedModel;
    }

//*********** Wrong conversion ******************
//    private int getAbsIndex() {
//        M item = getSelection();
//        int index = grid.getStore().indexOf(item);
//        return index >= 0 ? getAbsIndex(index) : lastAbsIndex;
//    }
//
//    private int getAbsIndex(int relIndex) {
//        return Math.min(Math.max(getLoaderOffset() + relIndex, 0), getTotalRowCount() - 1);
//    }
//
//    private int getRelIndex(int absIndex) {
//        return Math.max(Math.min(absIndex, getTotalRowCount() - 1) - getLoaderOffset(), 0);
//    }
//***************************************************
}
