package ru.sbsoft.client.components.grid;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.widget.core.client.container.CardLayoutContainer;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import ru.sbsoft.client.I18n;
import ru.sbsoft.client.components.BaseWindow;
import ru.sbsoft.client.components.ElementUtils;
import ru.sbsoft.client.components.browser.Browser;
import ru.sbsoft.client.components.form.BaseForm;
import ru.sbsoft.client.components.lookup.LookupGridMenu;
import ru.sbsoft.model.PageFilterInfo;
import ru.sbsoft.shared.GridContext;
import ru.sbsoft.shared.api.i18n.consts.SBFBrowserStr;
import ru.sbsoft.shared.meta.Row;
import ru.sbsoft.shared.model.MarkModel;
import ru.sbsoft.shared.services.FetchParams;

/**
 * Вспомогательные функции для работы с таблицей.
 */
public class GridUtils {

    public static String VIEW_MODE = "v";

    public static boolean isActiveGrid(SystemGrid grid) {
        if (grid.isVisible(true)) {
            Widget widget = grid;
            while ((widget = widget.getParent()) != null) {
                if (widget.getParent() instanceof CardLayoutContainer) {
                    return ((CardLayoutContainer) widget.getParent()).getActiveWidget() == widget;
                }
            }
            return true;
        }

        return grid.getParent() instanceof LookupGridMenu;
    }

    public static boolean isReadOnly(SystemGrid grid) {
        while (grid != null) {
            if (grid.isReadOnly()) {
                return true;
            }
            final BaseWindow window = ElementUtils.findWindow((Widget) grid);
            if (window == null) {
                break;
            }
            if (window.getWindow().getData(VIEW_MODE) != null) {
                return true;
            }
            final BaseForm form = ElementUtils.findForm((Widget) grid);
            if (form == null) {
                break;
            }
            grid = form.getOwnerGrid();
        }
        return false;
    }

    public static Browser findParentBrowser(Widget widget) {
        Browser result = null;
        while (widget != null) {
            if (widget instanceof Browser) {
                result = (Browser) widget;
                widget = null;
            } else {
                widget = widget.getParent();
            }
        }
        return result;
    }

    public static GridContext getContext(SystemGrid g) {
        return g.getGridContext();
    }

    public static <R extends Row> void getMarkedRows(final SystemGrid<R> grid, final AsyncCallback<List<R>> callback) {
        List<BigDecimal> selIds = grid.getMarkedRecords();
        if (selIds == null || selIds.isEmpty()) {
            callback.onSuccess(Collections.<R>emptyList());
            return;
        }
        final List<R> rows = new ArrayList<R>();
        final Set<BigDecimal> ids = new HashSet<BigDecimal>(selIds);
        List<R> currentRows = grid.getGrid().getStore().getAll();
        if (currentRows != null && !currentRows.isEmpty()) {
            idToRow(currentRows, ids, grid.getKeyProvider(), rows);
        }
        if (!ids.isEmpty()) {
            PageFilterInfo fetchParams = grid.getFetchParams(new FetchParams());
            grid.getGridService().getModelRows(grid.getGridContext(), fetchParams, new ArrayList<BigDecimal>(ids), new AsyncCallback<List<? extends MarkModel>>() {
                @Override
                public void onFailure(Throwable caught) {
                    callback.onFailure(caught);
                }

                @Override
                public void onSuccess(List<? extends MarkModel> result) {
                    if (result != null && !result.isEmpty()) {
                        idToRow((List<R>) result, ids, grid.getKeyProvider(), rows);
                    }
                    if (!ids.isEmpty()) {
                        callback.onFailure(new IllegalStateException(I18n.get(SBFBrowserStr.msgRowsNotFound, ids.toString())));
                    } else {
                        callback.onSuccess(rows);
                    }
                }
            });
        } else {
            callback.onSuccess(rows);
        }
    }

    private static <R extends Row> List<R> idToRow(List<R> store, Set<BigDecimal> ids, ValueProvider<R, BigDecimal> keyProvider, List<R> appendTo) {
        List<R> result = appendTo != null ? appendTo : new ArrayList<R>();
        List<R> storeBuf = new LinkedList<R>(store);
        Iterator<BigDecimal> iter = ids.iterator();
        while (iter.hasNext()) {
            BigDecimal id = iter.next();
            Iterator<R> storeIter = storeBuf.iterator();
            while (storeIter.hasNext()) {
                R r = storeIter.next();
                if (id.equals(keyProvider.getValue(r))) {
                    result.add(r);
                    storeIter.remove();
                    iter.remove();
                    break;
                }
            }
        }
        return result;
    }
}
