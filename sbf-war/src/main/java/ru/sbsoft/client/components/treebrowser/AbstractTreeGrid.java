package ru.sbsoft.client.components.treebrowser;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import ru.sbsoft.svc.data.client.loader.RpcProxy;
import ru.sbsoft.svc.data.shared.TreeStore;
import ru.sbsoft.svc.data.shared.event.StoreAddEvent;
import ru.sbsoft.svc.data.shared.event.StoreClearEvent;
import ru.sbsoft.svc.data.shared.event.StoreDataChangeEvent;
import ru.sbsoft.svc.data.shared.event.StoreFilterEvent;
import ru.sbsoft.svc.data.shared.event.StoreHandlers;
import ru.sbsoft.svc.data.shared.event.StoreRecordChangeEvent;
import ru.sbsoft.svc.data.shared.event.StoreRemoveEvent;
import ru.sbsoft.svc.data.shared.event.StoreSortEvent;
import ru.sbsoft.svc.data.shared.event.StoreUpdateEvent;
import ru.sbsoft.svc.data.shared.loader.ChildTreeStoreBinding;
import ru.sbsoft.svc.data.shared.loader.DataProxy;
import ru.sbsoft.svc.data.shared.loader.LoadEvent;
import ru.sbsoft.svc.data.shared.loader.TreeLoader;
import ru.sbsoft.svc.widget.core.client.container.SimpleContainer;
import ru.sbsoft.svc.widget.core.client.grid.ColumnConfig;
import ru.sbsoft.svc.widget.core.client.grid.ColumnModel;
import ru.sbsoft.svc.widget.core.client.treegrid.TreeGrid;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import ru.sbsoft.client.components.HasParentFilters;
import ru.sbsoft.client.components.InitManager;
import ru.sbsoft.client.components.Initializable;
import ru.sbsoft.client.components.actions.Action;
import ru.sbsoft.client.components.actions.ActionManager;
import static ru.sbsoft.client.components.grid.GridConsts.AUTO_EXPAND_MAX_VALUE;
import static ru.sbsoft.client.components.grid.GridConsts.AUTO_EXPAND_MIN_VALUE;
import ru.sbsoft.client.components.tree.TreeExpand;
import ru.sbsoft.client.consts.SBFResources;
import ru.sbsoft.client.model.TreeNodeProperties;
import ru.sbsoft.client.utils.ClientUtils;
import ru.sbsoft.client.utils.DefaultAsyncCallback;
import ru.sbsoft.client.utils.RoleCheker;
import ru.sbsoft.model.PageFilterInfo;
import ru.sbsoft.shared.FilterInfo;
import ru.sbsoft.shared.GridContext;
import ru.sbsoft.shared.TreeNode;
import ru.sbsoft.shared.interfaces.NamedGridType;
import ru.sbsoft.shared.interfaces.OperationType;
import ru.sbsoft.shared.model.GridParamsBean;
import ru.sbsoft.shared.model.MarkModel;
import ru.sbsoft.shared.model.operation.OperationCommand;

/**
 *
 * @author sychugin
 * @param <K>
 * @param <N>
 */
public abstract class AbstractTreeGrid<K, N extends TreeNode<K>> extends SimpleContainer implements HasParentFilters {

    private final NamedGridType gridType;
    private final GridContext gridContext;
    private TreeLoader<N> loader;
    private TreeGrid<N> treeGrid;
    protected ActionManager actionManager;
    //
    private boolean flagReadOnly = false;
    private boolean initialized = false;
    //
    private final Action refreshAction;
    private final Action updateAction;
    private final Action insertAction;
    private final Action deleteAction;
    private final Action expandAction;
    private final Action collapseAction;
    protected final List<FilterInfo> parentFilters = new ArrayList<FilterInfo>();

    private final static TreeNodeProperties PROPS = new TreeNodeProperties();

    private final InitManager initManager = new InitManager();

    public AbstractTreeGrid(NamedGridType gridType) {
        super();
        this.gridType = gridType;
        this.gridContext = new GridContext(gridType, gridType.getCode());
        this.refreshAction = new TreeGridRefreshAction(this);
        this.updateAction = new TreeGridUpdateAction(this);
        this.insertAction = new TreeGridInsertAction(this);
        this.deleteAction = new TreeGridDeleteAction(this);
        this.expandAction = new TreeGridExpandAction(this);
        this.collapseAction = new TreeGridCollapseAction(this);
    }

    private DataProxy<N, List<N>> createProxy() {
        return new RpcProxy<N, List<N>>() {
            @Override
            public void load(N loadConfig, final AsyncCallback<List<N>> callback) {
                AsyncCallback<List<N>> resCall = new AsyncCallback<List<N>>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        callback.onFailure(caught);
                        ClientUtils.alertException(caught, null);
                    }

                    @Override
                    public void onSuccess(List<N> result) {
                        callback.onSuccess(result);
                        initManager.doInit(new DefaultAsyncCallback<Void>() {
                            @Override
                            public void onResult(Void result) {
                                initialized = true;
                                actionManager.updateState();
                            }
                        });
                    }
                };
                N rootNode = createRootNode();
                if (null == loadConfig && rootNode != null) {
                    final List<N> nodes = new ArrayList<N>();
                    nodes.add(createRootNode());
                    resCall.onSuccess(nodes);
                } else {
                    loadTreeItems(loadConfig, parentFilters, resCall);
                }
            }
        };
    }

    protected void addInitializable(Initializable ini) {
        initManager.addInitializable(ini);
    }

    public boolean isTreeSelect() {
        return !isEmpty(getNodeSelections());
    }

    public boolean isLeafSelect() {
        List<N> list = getNodeSelections();
        return list != null && list.size() == 1 && list.get(0).isLeaf();
    }

    public boolean isEmpty(Collection c) {
        return c == null || c.isEmpty();
    }

    public ActionManager getActionManager() {
        return actionManager;
    }

    public void setActionManager(ActionManager actionManager) {
        this.actionManager = actionManager;
    }

    public void update() {
        refresh(true);
        actionManager.updateState();
    }

    public void refresh(boolean forceAll) {
        final N selectedModel = forceAll ? null : getTreeGrid().getSelectionModel().getSelectedItem();
        refreshChildren(selectedModel);
    }

    public void refreshChildren(N node) {
        if (null == node) {
            getTreeGrid().getTreeStore().clear();
        } else {
            getTreeGrid().getTreeStore().removeChildren(node);
        }

        getLoader().load(node);
    }

    protected N createRootNode() {
        return null;
    }

    public TreeNodeProperties getNodeProps() {
        return PROPS;
    }

    public boolean isInitialized() {
        return initialized;
    }

    public OperationCommand createGridOperationCommand(OperationType type) {
        final MarkModel selectedItem = getSelectedModel();
        final BigDecimal selectedRecord = selectedItem == null ? null : selectedItem.getRECORD_ID();
        final PageFilterInfo fetchParams = new PageFilterInfo();
        fetchParams.setParentFilters(getParentFilters());
        fetchParams.setOffset(0);
        fetchParams.setLimit(0);

        final OperationCommand cmd = new OperationCommand(type);
        cmd.setGridContext(
                new GridParamsBean(
                        getGridContext(),
                        fetchParams,
                        Collections.emptyList(),
                        selectedRecord
                )
        );
        return cmd;
    }

    protected abstract void addColumns(List<ColumnConfig<N, ?>> listColumns);

    protected abstract String getTreeFieldTitle();

    protected abstract int getTreeFieldWidth();

    protected void onRecordChanged(StoreRecordChangeEvent<N> event) {
    }

    public TreeGrid<N> getTreeGrid() {
        if (null == treeGrid) {
            final TreeStore<N> store = new TreeStore<N>(getNodeProps().key());
            store.addStoreRecordChangeHandler(new StoreRecordChangeEvent.StoreRecordChangeHandler<N>() {
                @Override
                public void onRecordChange(StoreRecordChangeEvent<N> event) {
                    onRecordChanged(event);
                }
            });

            getLoader().addLoadHandler(new ChildTreeStoreBinding<N>(store) {
                @Override
                public void onLoad(LoadEvent<N, List<N>> event) {
                    super.onLoad(event);
                    new TreeExpander(event.getLoadConfig()).tryExpand();
                }

            });
            getLoader().setReuseLoadConfig(true);
            ColumnConfig<N, String> cc1 = new ColumnConfig<>(getNodeProps().title(), getTreeFieldWidth(), getTreeFieldTitle());
            List<ColumnConfig<N, ?>> l = new ArrayList<>();
            l.add(cc1);
            addColumns(l);
            ColumnModel<N> cm = new ColumnModel<>(l);

            treeGrid = new TreeGrid(store, cm, cc1);
            treeGrid.setTreeLoader(getLoader());
            treeGrid.getStyle().setLeafIcon(SBFResources.TREEMENU_ICONS.Leaf16());
            treeGrid.getView().setForceFit(false);
            treeGrid.getView().setTrackMouseOver(false);
            treeGrid.getView().setAutoExpandMax(AUTO_EXPAND_MAX_VALUE);
            treeGrid.getView().setAutoExpandMin(AUTO_EXPAND_MIN_VALUE);
            treeGrid.getView().setAutoExpandColumn(cc1);

            configTreeGrid(treeGrid, l);

            setWidget(treeGrid);

            treeGrid.getSelectionModel().addSelectionHandler(new SelectionHandler<N>() {
                @Override
                public void onSelection(SelectionEvent<N> event) {
                    actionManager.updateState();
                }

            });

            treeGrid.getTreeStore().addStoreHandlers(new StoreHandlers<N>() {
                @Override
                public void onAdd(StoreAddEvent<N> event) {
                    actionManager.updateState();
                }

                @Override
                public void onRemove(StoreRemoveEvent<N> event) {
                    actionManager.updateState();
                }

                @Override
                public void onFilter(StoreFilterEvent<N> event) {
                    actionManager.updateState();
                }

                @Override
                public void onClear(StoreClearEvent<N> event) {
                    actionManager.updateState();
                }

                @Override
                public void onUpdate(StoreUpdateEvent<N> event) {
                    actionManager.updateState();
                }

                @Override
                public void onDataChange(StoreDataChangeEvent<N> event) {
                    actionManager.updateState();
                }

                @Override
                public void onRecordChange(StoreRecordChangeEvent<N> event) {
                    actionManager.updateState();
                }

                @Override
                public void onSort(StoreSortEvent<N> event) {
                    actionManager.updateState();
                }
            });

        }
        return treeGrid;
    }

    public TreeLoader<N> getLoader() {
        if (null == loader) {
            loader = new TreeLoader<N>(createProxy()) {
                @Override
                public boolean hasChildren(N parent) {
                    return !parent.isLeaf();
                }
            };
        }
        return loader;
    }

    protected final List<N> getNodeSelections() {
        return getTreeGrid().getSelectionModel().getSelectedItems();
    }

    protected void configTreeGrid(TreeGrid<N> treeGrid, List<ColumnConfig<N, ?>> l) {

    }

    protected void expandNode(N m, TreeExpand e) {
        if (m != null) {
            TreeGrid<N> t = getTreeGrid();
            switch (e) {
                case COLLAPSE:
                    t.setExpanded(m, false);
                    break;
                case EXPAND_DEEP:
                    t.setExpanded(m, true, true);
                    break;
                default:
                    t.setExpanded(m, true, false);
            }
        }
    }

    protected void expandTree(TreeExpand e) {
        final List<N> rootItems = treeGrid.getTreeStore().getRootItems();
        if (null != rootItems && rootItems.size() > 0) {
            expandNode(rootItems.get(0), e);
        }
    }

    protected void onFirstLoading(final List<N> rootItems) {
        expandTree(TreeExpand.EXPAND);
    }

    protected MarkModel getSelectedModel() {
        final N selectedModel = getTreeGrid().getSelectionModel().getSelectedItem();
        if (selectedModel == null) {
            return null;
        }
        return itemToMarkModel(selectedModel);
    }

    protected final GridContext getGridContext() {
        return gridContext;
    }

    @Override
    public <T extends FilterInfo> void setParentFilters(final List<T> vals) {
        if (!ClientUtils.equals(parentFilters, vals)) {
            parentFilters.clear();
            if (vals != null && vals.size() > 0) {
                parentFilters.addAll(vals);
            }
            if (isInitialized()) {
                update();
            }
        }
    }

    @Override
    public List<FilterInfo> getParentFilters() {
        return parentFilters;
    }

    public Action getRefreshAction() {
        return refreshAction;
    }

    public Action getUpdateAction() {
        return updateAction;
    }

    public Action getInsertAction() {
        return insertAction;
    }

    public Action getDeleteAction() {
        return deleteAction;
    }

    public Action getExpandAction() {
        return expandAction;
    }

    public Action getCollapseAction() {
        return collapseAction;
    }

    public Action getCloneAction() {
        return null;
    }

    public void setReadOnly(boolean readOnly) {
        flagReadOnly = readOnly;
    }

    public boolean isReadOnly() {
        return flagReadOnly || RoleCheker.getInstance().isTableReadOnly(getTableName());
    }

    public void edit() {

    }

    public void insert() {

    }

    public void delete() {

    }

    public void expandAll() {
        getTreeGrid().expandAll();
    }

    public void collapseAll() {
        getTreeGrid().collapseAll();
    }

    protected String getTableName() {
        return gridType.getSecurityId();
    }

    private class TreeExpander {

        private final N config;

        public TreeExpander(N config) {
            this.config = config;
        }

        public void tryExpand() {
            if (treeGrid != null && treeGrid.isVisible()) {
                if (config == null) {
                    onFirstLoading(treeGrid.getTreeStore().getRootItems());
                } else if (null == getTreeGrid().getTreeStore().getParent(config)) {
                    getTreeGrid().setExpanded(config, true, false);

                }
                actionManager.updateState();
            } else {
                Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
                    @Override
                    public void execute() {
                        tryExpand();
                    }
                });
            }
        }
    }

    protected abstract MarkModel itemToMarkModel(N item);

    protected abstract void loadTreeItems(N parent, List<FilterInfo> parentFilters, AsyncCallback<List<N>> callback);

    protected abstract void loadTreeItem(K itemKey, AsyncCallback<N> callback);

}
