package ru.sbsoft.client.components.grid;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.EventTarget;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Widget;
import ru.sbsoft.svc.data.shared.ListStore;
import ru.sbsoft.svc.data.shared.ModelKeyProvider;
import ru.sbsoft.svc.data.shared.loader.LoadEvent;
import ru.sbsoft.svc.data.shared.loader.LoadHandler;
import ru.sbsoft.svc.widget.core.client.event.BodyScrollEvent;
import ru.sbsoft.svc.widget.core.client.grid.ColumnConfig;
import ru.sbsoft.svc.widget.core.client.grid.ColumnModel;
import ru.sbsoft.svc.widget.core.client.grid.LiveGridView;
import ru.sbsoft.svc.widget.core.client.menu.CheckMenuItem;
import ru.sbsoft.svc.widget.core.client.menu.Item;
import ru.sbsoft.svc.widget.core.client.menu.Menu;
import ru.sbsoft.svc.widget.core.client.menu.MenuItem;
import ru.sbsoft.client.I18n;
import ru.sbsoft.client.components.browser.actions.GridMultisortAction;
import ru.sbsoft.client.components.grid.column.CustomColumnConfig;
import ru.sbsoft.common.Strings;

/**
 * Предоставляет средства для управления отображением таблицы данных.
 *
 * @param <M> модель данных строки таблицы
 */
public class SBLiveGridView<M> extends LiveGridView<M> {

    //   private static java.util.logging.Logger logger = java.util.logging.Logger.getLogger(SBLiveGridView.class.getName());
    private HandlerRegistration loadHandler = null;
    private BaseGrid baseGrid;
    private boolean updateScheduled = false;


    private int deleted = 0;
    private int inserted = 0;

    private int lastLiveScrollTop = 0;
    private int lastLiveScrollDelta = 0;

    //@UiField MarkCellStyle res;
    public SBLiveGridView() {
        super();
        addLiveGridViewUpdateHandler(ev -> Scheduler.get().scheduleDeferred(this::updateScrollTop));

    }

    public void onCellSelect(int row, int col, boolean select) {
        if (!grid.isViewReady()) {
            return;
        }

        if (select) {
            onCellSelect(row, col);
        } else {
            onCellDeselect(row, col);
        }

    }

    public void onRowSelect(int row, boolean select) {
        if (!grid.isViewReady()) {
            return;
        }

        if (select) {
            onRowSelect(row);
        } else {
            onRowDeselect(row);
        }
    }

    public M findModelWithKey(String key) {
        return cacheStore.findModelWithKey(key);
    }

    @Override
    protected void initData(ListStore<M> ds, ColumnModel<M> cm) {
        if (loadHandler != null) {
            loadHandler.removeHandler();
        }
        super.initData(ds, cm);
        loadHandler = grid.getLoader().addLoadHandler(new LoadHandler() {
            @Override
            public void onLoad(LoadEvent event) {
                deleted = 0;
                inserted = 0;
            }
        });
    }

    public void deleteRowByKey(final String key) {
        if (key != null) {
            ModelKeyProvider<? super M> keyProvider = cacheStore.getKeyProvider();
            for (final M deletedModel : cacheStore.getAll()) {
                if (key.equals(keyProvider.getKey(deletedModel))) {
                    ds.remove(deletedModel);
                    if (cacheStore.remove(deletedModel)) {
                        deleted++;
                        totalCount--;
                        sheduleViewUpdate();
                    }
                    //must to work automatically -- grid.getView().updateVScroll(); 
                    break;
                }
            }
        }
    }

    public void updateRow(final M model) {
        cacheStore.update(model);
        ds.update(model);
    }

    public void insertRowAfter(final M newModel, final M afterModel) {
        addToStore(ds, newModel, afterModel);
        addToStore(cacheStore, newModel, afterModel);
        inserted++;
        totalCount++;
        sheduleViewUpdate();
    }

    private void sheduleViewUpdate() {
        if (!updateScheduled) {
            updateScheduled = true;
            Scheduler.get().scheduleDeferred(() -> {
                updateScheduled = false;
                updateRows(viewIndex, true);
            });
        }
    }

    private static <M> void addToStore(ListStore<M> store, final M newModel, final M arterModel) {
        int index = store.indexOf(arterModel);
        store.add(index + 1, newModel);
    }

    public void setBaseGrid(BaseGrid baseGrid) {
        this.baseGrid = baseGrid;
    }

    public ListStore<M> getCacheStore() {
        return cacheStore;
    }

    public int getCalculatedPageHeight() {
        return getCalculatedRowHeight() * getVisibleRowCount();
    }

    @Override
    protected int getLiveScrollerHeight() {
        return liveScroller != null ? super.getLiveScrollerHeight() : 0;
    }

    protected int getCalculatedRowHeight() {
        return super.getRowHeight() + borderWidth; // TODO: Use a height field or rename
        // borderWidth
    }

    public void reshow() {
        reshow(false);
    }
    
    private void reshow(boolean headerToo){
        boolean p = preventScrollToTopOnRefresh;
        preventScrollToTopOnRefresh = true;
        refresh(headerToo);
        preventScrollToTopOnRefresh = p;
    } 

    public int getLiveStoreOffset() {
        return super.liveStoreOffset;
    }

    public int getViewIndex() {
        return super.viewIndex;
    }

    public int getTotalCount() {
        return super.totalCount;
    }

    @Override
    protected Menu createContextMenu(final int colIndex) {
        Menu contextMenu = super.createContextMenu(colIndex);
        int lastItem = contextMenu.getWidgetCount() - 1;
        if (lastItem >= 0) {
            Widget widget = contextMenu.getWidget(lastItem);
            if (widget instanceof MenuItem) {
                MenuItem columns = (MenuItem) widget;
                Menu columnMenu = columns.getSubMenu();
                if (columnMenu != null) {
                    int cols = columnMenu.getWidgetCount();
                    for (int i = 0; i < cols; i++) {
                        widget = columnMenu.getWidget(i);
                        if (widget instanceof CheckMenuItem) {
                            ((CheckMenuItem) widget).setWidth(300);
                            updateTitleColumn(cm.getColumn(i + 1), (CheckMenuItem) widget);
                        }
                    }
                }
            }
        }
        if (baseGrid != null && baseGrid.isMultisortEnabled()) {
            final GridMultisortAction multisortAction = new GridMultisortAction(baseGrid);
            MenuItem complexSortitem = new MenuItem() {
                @Override
                public boolean isVisible() {
                    return multisortAction.isEnabled();
                }

                @Override
                public boolean isEnabled() {
                    return multisortAction.isEnabled();
                }
            };
            complexSortitem.setText(multisortAction.getCaption());
            complexSortitem.setToolTip(multisortAction.getToolTip());
            complexSortitem.setIcon(multisortAction.getIcon16());
            complexSortitem.addSelectionHandler(new SelectionHandler<Item>() {
                @Override
                public void onSelection(SelectionEvent<Item> event) {
                    multisortAction.perform();
                }
            });
            contextMenu.add(complexSortitem);
        }
        return contextMenu;
    }

    /**
     * Добавляет в заголовок checkMenu наименование группы столбца последнего
     * уровня, если группа имеется.
     *
     * @param columnConfig
     * @param checkMenu
     */
    private void updateTitleColumn(ColumnConfig columnConfig, CheckMenuItem checkMenu) {
        if (!(columnConfig instanceof CustomColumnConfig)) {
            return;
        }
        CustomColumnConfig customColumnConfig = (CustomColumnConfig) columnConfig;
        if (null == customColumnConfig.getColumn().getGroup() || Strings.isEmpty(I18n.get(customColumnConfig.getColumn().getGroup().getTitle()))) {
            return;
        }
        checkMenu.setText(I18n.get(customColumnConfig.getColumn().getGroup().getTitle()) + "\\" + I18n.get(customColumnConfig.getColumn().getCaption()));
    }

    /**
     * Вертикальный скролл позиционируется некорректно, если происходит
     * инициализация скрытого (на закладке) грида. Надо корректировать
     * принудительно после отрисовки
     */
    public void positionLiveScroller() {
        liveScroller.setTop(headerElem.getOffsetHeight());
    }

    @Override
    public void renderFooter() {
        super.renderFooter();
    }

    @Override
    protected void handleComponentEvent(Event event) {
        super.handleComponentEvent(event);
        EventTarget t = event.getEventTarget();
        if (event.getTypeInt() == Event.ONSCROLL && Element.is(t)) {
            Element target = Element.as(t);
            if (liveScroller.isOrHasChild(target)) {
                int liveScrollTop = liveScroller.getScrollTop();
                int liveScrollTopMax = liveScroller.getScrollHeight() - getLiveScrollerHeight();
                //int scrollTopMax = scroller.getScrollHeight() - scroller.getClientHeight();
                int delta;
                if (liveScrollTop <= 0) {
                    delta = -1;
                } else if (liveScrollTop >= liveScrollTopMax - 1) {
                    delta = 1;
                } else {
                    delta = liveScrollTop - lastLiveScrollTop;
                }
                //   if (delta != 0) {
                lastLiveScrollDelta = delta;
                updateScrollTop();
                //  }
                lastLiveScrollTop = liveScrollTop;
            }
        }
    }
    
    public void resetScrollDelta(){
        lastLiveScrollTop = 0;
        lastLiveScrollDelta = 0;
    }

    private void updateScrollTop() {
        int scrollTop = lastLiveScrollDelta <= 0 ? 0 : scroller.getScrollHeight() - scroller.getClientHeight() /*maxTop*/;
        scroller.setScrollTop(scrollTop);
        grid.fireEvent(new BodyScrollEvent(scroller.getScrollLeft(), scrollTop));
    }

    @Override
    public int getScrollAdjust() {
        return super.getScrollAdjust();
    }

    public boolean isAutoFit(){

        return baseGrid != null && baseGrid.isAutoFit();

    }


    public boolean updateAutoFit() {
        if (!isAutoFit()) return true;

        ColumnModel cmm = grid.getColumnModel();

        int tw = cmm.getTotalWidth();
        int aw = grid.getElement().getWidth(true) - getScrollAdjust();
        if (aw <= 0) {
            aw = grid.getElement().getComputedWidth();
        }

        if (aw < 20) { // not initialized, so don't screw up the
            // default widths
            return false;
        }

        setForceFit(tw <= aw);
        lastViewWidth = -1;
        layout(true);
        reshow(true);
        return true;
    }

}
