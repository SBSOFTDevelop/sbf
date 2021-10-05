package ru.sbsoft.client.components.grid;

import com.google.gwt.dom.client.Element;
import com.sencha.gxt.data.shared.loader.PagingLoader;
import com.sencha.gxt.widget.core.client.grid.Grid;
import com.sencha.gxt.widget.core.client.grid.LiveGridView;

/**
 *
 * @author Kiselev
 */
public final class GridHelper {

    private GridHelper() {
    }

    public static int getLoaderOffset(Grid<?> grid) {
        if (grid.getLoader() instanceof PagingLoader<?, ?>) {
            return ((PagingLoader<?, ?>) grid.getLoader()).getOffset();
        } else {
            return 0;
        }
    }

    public static int getTotalRowCount(Grid<?> grid) {
        if (grid.getLoader() instanceof PagingLoader<?, ?>) {
            return ((PagingLoader<?, ?>) grid.getLoader()).getTotalCount();
        } else if (grid.getView() instanceof LiveGridView) {
            int rh = getRowHeight(grid);
            int sh = getScrollHeight(grid);
            if (rh <= 0 || sh <= 0) {
                return 0;
            }
            return (int) Math.round(((double) sh) / rh);

        } else {
            return grid.getStore() != null ? grid.getStore().size() : 0;
        }
    }

    public static int getScrollHeight(Grid<?> grid) {
        return grid.getView().getScroller().getScrollHeight();
    }

    public static int getVisibleHeight(Grid<?> grid) {
        return grid.getView().getScroller().getHeight(true);
    }

    public static int getRowHeight(Grid<?> grid) {
        Element row0 = grid.getView().getRow(0);
        if (row0 == null) {
            return 0;
        }
        return row0.getOffsetHeight();
    }

    public static int getTopVisibleRowIndex(Grid<?> grid) {
        int rowHeight = getRowHeight(grid);
        if (rowHeight > 0) {
            return (int) Math.ceil((double) grid.getView().getScroller().getScrollTop() / rowHeight);
        } else {
            return -1;
        }
    }

    public static int getBottomVisibleRowIndex(Grid<?> grid) {
        return getBottomVisibleRowIndex(grid, getTotalRowCount(grid));
    }

    public static int getBottomVisibleRowIndex(Grid<?> grid, int totalRowCount) {
        int rowHeight = getRowHeight(grid);
        if (rowHeight > 0) {
            return Math.min((int) Math.floor((double) (grid.getView().getScroller().getScrollTop() + getVisibleHeight(grid)) / rowHeight), totalRowCount);
        } else {
            return -1;
        }
    }

    public static int getVisibleRowCount(Grid<?> grid) {
        int topIndex = getTopVisibleRowIndex(grid);
        int bottIndex = getBottomVisibleRowIndex(grid);
        return topIndex >= 0 && bottIndex >= 0 ? bottIndex - topIndex + 1 : 0;
    }

//    public static <M> boolean isRowVisible(Grid<M> grid, M rowModel) {
//        XElement gridEl = grid.getElement();
//        Element rowEl = grid.getView().getRow(rowModel);
//        int gridTop = grid.getView().getHeader().getElement().getAbsoluteBottom();
//        int gridBot = gridEl.getAbsoluteBottom();
//        int rowTop = rowEl.getAbsoluteTop();
//        int rowBot = rowEl.getAbsoluteBottom();
//        return rowTop >= gridTop && rowBot <= gridBot;
//    }
}
