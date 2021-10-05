package ru.sbsoft.client.components.browser.filter;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.logical.shared.BeforeSelectionEvent;
import com.google.gwt.event.logical.shared.BeforeSelectionHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.sencha.gxt.core.client.IdentityValueProvider;
import com.sencha.gxt.core.client.Style;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.SortDir;
import com.sencha.gxt.data.shared.Store;
import com.sencha.gxt.data.shared.TreeStore;
import com.sencha.gxt.widget.core.client.event.CellDoubleClickEvent;
import com.sencha.gxt.widget.core.client.tree.Tree;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import ru.sbsoft.client.I18n;
import ru.sbsoft.client.components.CommonServiceContainer;
import ru.sbsoft.client.components.CommonServiceWindow;
import ru.sbsoft.client.components.actions.AbstractAction;
import ru.sbsoft.client.components.actions.ActionMenu;
import ru.sbsoft.client.consts.SBFConst;
import ru.sbsoft.client.consts.SBFResources;
import ru.sbsoft.client.schedule.SchedulerChainCommand;
import ru.sbsoft.client.schedule.SchedulerChainManager;
import ru.sbsoft.client.schedule.SyncSchedulerChainCommand;
import ru.sbsoft.client.utils.ClientUtils;
import ru.sbsoft.client.utils.DefaultAsyncCallback;
import ru.sbsoft.shared.GridContext;
import ru.sbsoft.shared.api.i18n.consts.SBFBrowserStr;
import ru.sbsoft.shared.meta.filter.IStoredFilterList;
import ru.sbsoft.shared.meta.filter.StoredFilterPath;
import ru.sbsoft.shared.meta.filter.StoredFilterType;

/**
 * Диалог загрузки сохраненного фильтра. Отображает сохраненные фильтры,
 * доступные текущему пользователю и позволяет выбрать один из них. Пользователю
 * доступны три вида фильтров: собственные, назначенные на одну из его групп и
 * глобальные.
 *
 * @author Kiselev
 */
public class StoredFilterSelector extends CommonServiceWindow {

    private boolean opening = false;
    private AsyncCallback<StoredFilterPath> resultHandler = null;
    private final TreeStore<BaseDto> store;
    private final Tree<BaseDto, BaseDto> tree;
    private GridContext gridContext = null;
    private final List<FilterDeleteListener> deleteListeners = new ArrayList<FilterDeleteListener>();
    private final List<BaseDto> delItems = new ArrayList<BaseDto>();

    public StoredFilterSelector() {
        super(new CommonServiceContainer());
        setPixelSize(300, 400);
        setHeaderIcon(SBFResources.BROWSER_ICONS.FilterSaved16());
        setHeaderText(SBFBrowserStr.menuFilterSaved);

        store = new TreeStore<BaseDto>(new KeyProvider());
        store.addSortInfo(new Store.StoreSortInfo<BaseDto>(new Comparator<BaseDto>() {
            @Override
            public int compare(BaseDto o1, BaseDto o2) {
                if (o1 == o2) {
                    return 0;
                } else if (o1 == null) {
                    return 1;
                } else if (o2 == null) {
                    return -1;
                } else {
                    boolean f1 = o1.isFilter();
                    boolean f2 = o2.isFilter();
                    if(f1 ^ f2){
                        return f1 ? 1 : -1;
                    }else{
                        return f1 ? o1.toString().compareToIgnoreCase(o2.toString()) : 0;
                    }
                }
            }
        }, SortDir.ASC));
        tree = new SelectorTree(store, new IdentityValueProvider<BaseDto>());
        tree.getSelectionModel().setSelectionMode(Style.SelectionMode.SINGLE);
        tree.getSelectionModel().addBeforeSelectionHandler(new BeforeSelectionHandler<BaseDto>() {
            @Override
            public void onBeforeSelection(BeforeSelectionEvent<BaseDto> event) {
                if (event.getItem() instanceof SectionDto) {
                    event.cancel();
                }
            }
        });
        tree.getSelectionModel().addSelectionHandler(new SelectionHandler<BaseDto>() {
            @Override
            public void onSelection(SelectionEvent<BaseDto> event) {
                actionManager.updateState();
            }
        });
        functionalContainer.add(tree);

        DelAction delAction = new DelAction();
        toolBar.addButton(delAction);
        ActionMenu contextMenu = new ActionMenu(actionManager);
        contextMenu.addAction(delAction);
        tree.setContextMenu(contextMenu);
        CellDoubleClickEvent.CellDoubleClickHandler handler = new CellDoubleClickEvent.CellDoubleClickHandler() {
            @Override
            public void onCellClick(CellDoubleClickEvent event) {
                ClientUtils.alertWarning(I18n.get(SBFBrowserStr.msgDoubleClick));
            }
        };
        tree.addHandler(handler, CellDoubleClickEvent.getType());
    }

    public void addDeleteListener(FilterDeleteListener l) {
        if (!deleteListeners.contains(l)) {
            deleteListeners.add(l);
        }
    }

    private void fireFilterDeleted(StoredFilterPath path) {
        for (FilterDeleteListener l : deleteListeners) {
            l.onFilterDelete(path);
        }
    }

    public void show(GridContext context, AsyncCallback<StoredFilterPath> resultHandler) {
        if (!opening && !isVisible()) {
            opening = true;
            this.resultHandler = resultHandler;
            this.gridContext = context;
            delItems.clear();
            SBFConst.CONFIG_SERVICE.getStoredFilterList(context, new DefaultAsyncCallback<IStoredFilterList>() {
                @Override
                public void onResult(IStoredFilterList result) {
                    if (result != null && !result.isEmpty()) {
                        fillTree(result);
                        StoredFilterSelector.super.show();
                        actionManager.updateState();
                        Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
                            @Override
                            public void execute() {
                                tree.expandAll();
                            }
                        });
                    } else {
                        ClientUtils.alertWarning(I18n.get(SBFBrowserStr.msgFilerNoStored));
                    }
                    opening = false;
                }
            });
        }
    }

    @Override
    protected void apply() {
        apply(getSelectedFilter());
    }

    private void apply(final StoredFilterPath f) {
        List<SchedulerChainCommand> cmd = new ArrayList<SchedulerChainCommand>();
        if (!delItems.isEmpty()) {
            final List<StoredFilterPath> delPaths = new ArrayList<StoredFilterPath>();
            for (BaseDto item : delItems) {
                final StoredFilterPath path = item != null ? item.getFilter() : null;
                if (path != null && !path.isDefault()) {
                    delPaths.add(path);
                }
            }
            if (!delPaths.isEmpty()) {
                cmd.add(new SchedulerChainCommand() {
                    @Override
                    public void execute() {
                        SBFConst.CONFIG_SERVICE.deleteFilters(gridContext, delPaths, new DefaultAsyncCallback<Map<StoredFilterPath, Exception>>() {
                            @Override
                            public void onResult(Map<StoredFilterPath, Exception> result) {
                                for (StoredFilterPath p : delPaths) {
                                    Exception err = result != null ? result.get(p) : null;
                                    if (err == null) {
                                        fireFilterDeleted(p);
                                    } else {
                                        ClientUtils.alertException(err);
                                    }
                                }
                                getChainManager().next();
                            }
                        });
                    }
                });
            }
        }
        if (resultHandler != null && f != null) {
            cmd.add(new SyncSchedulerChainCommand() {
                @Override
                protected void onCommand() {
                    resultHandler.onSuccess(f);
                }
            });
        }
        cmd.add(new SyncSchedulerChainCommand() {
            @Override
            protected void onCommand() {
                hide();
            }
        });
        new SchedulerChainManager(cmd).next();
    }

    @Override
    protected boolean isApplyEnabled() {
        return resultHandler != null && (getSelectedFilter() != null || !delItems.isEmpty());
    }

    private BaseDto getSelection() {
        return tree.getSelectionModel().getSelectedItem();
    }

    private StoredFilterPath getSelectedFilter() {
        BaseDto base = getSelection();
        if (base != null) {
            return base.getFilter();
        }
        return null;
    }

    private void fillTree(IStoredFilterList filterList) {
        store.clear();
        for (StoredFilterType type : new StoredFilterType[]{StoredFilterType.GLOBAL, StoredFilterType.GROUP, StoredFilterType.PERSONAL}) {
            Set<StoredFilterPath> part = filterList.get(type);
            if (part != null && !part.isEmpty()) {
                SectionDto sec = new SectionDto(type);
                store.add(sec);
                for (StoredFilterPath f : part) {
                    store.add(sec, new FilterDto(sec, f));
                }
            }
        }
        store.applySort(false);
    }

    private class KeyProvider implements ModelKeyProvider<BaseDto> {

        @Override
        public String getKey(BaseDto b) {
            return b.getKey();
        }
    }

    private static interface BaseDto {

        String getKey();

        StoredFilterPath getFilter();

        StoredFilterType getType();

        boolean isFilter();
    }

    private static class SectionDto implements BaseDto {

        private final StoredFilterType type;

        public SectionDto(StoredFilterType type) {
            this.type = type;
        }

        @Override
        public String getKey() {
            return type.getCode();
        }

        @Override
        public String toString() {
            return I18n.get(type.getItemName());
        }

        @Override
        public StoredFilterPath getFilter() {
            return null;
        }

        @Override
        public StoredFilterType getType() {
            return type;
        }

        @Override
        public boolean isFilter() {
            return false;
        }
    }

    private static class FilterDto implements BaseDto {

        private final StoredFilterPath filter;
        private String key = null;
        private final SectionDto section;

        public FilterDto(SectionDto section, StoredFilterPath filter) {
            this.filter = filter;
            this.section = section;
        }

        @Override
        public StoredFilterPath getFilter() {
            return filter;
        }

        @Override
        public String getKey() {
            if (key == null) {
                key = filter.toString();
            }
            return key;
        }

        @Override
        public String toString() {
            return String.valueOf(filter.getFilterName());
        }

        @Override
        public StoredFilterType getType() {
            return section.getType();
        }

        @Override
        public boolean isFilter() {
            return true;
        }
    }

    protected class DelAction extends AbstractAction {

        public DelAction() {
            setCaption(SBFBrowserStr.menuFilterDelete);
            setToolTip(SBFBrowserStr.hintFilterDelete);
            setIcon16(SBFResources.GENERAL_ICONS.Delete16());
            setIcon24(SBFResources.GENERAL_ICONS.Delete24());
        }

        @Override
        public boolean checkEnabled() {
            BaseDto dto = getSelection();
            return dto != null && dto.getType() != StoredFilterType.GLOBAL && getSelectedFilter() != null;
        }

        @Override
        protected void onExecute() {
            if (checkEnabled()) {
                ClientUtils.confirm(StoredFilterSelector.this, SBFBrowserStr.msgDeleteSelectedItem, new Command() {
                    @Override
                    public void execute() {
                        BaseDto delDto = getSelection();
                        if (delDto != null) {
                            store.remove(getSelection());
                            delItems.add(delDto);
                            actionManager.updateState();
                        }
                    }
                });
            }
        }
    }

    private class SelectorTree extends Tree<BaseDto, BaseDto> {

        public SelectorTree(TreeStore<BaseDto> store, ValueProvider<? super BaseDto, BaseDto> valueProvider) {
            super(store, valueProvider);
        }

        @Override
        protected void onDoubleClick(Event event) {
            super.onDoubleClick(event);
            TreeNode<BaseDto> node = findNode(event.getEventTarget().<Element>cast());
            if (node != null) {
                BaseDto dto = node.getModel();
                if (dto != null) {
                    apply(dto.getFilter());
                }
            }
        }

    }

    public static interface FilterDeleteListener {

        void onFilterDelete(StoredFilterPath path);
    }

}
