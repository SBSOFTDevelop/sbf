package ru.sbsoft.client.components.tree;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.rpc.AsyncCallback;
import ru.sbsoft.svc.core.client.Style;
import ru.sbsoft.svc.data.client.loader.RpcProxy;
import ru.sbsoft.svc.data.shared.SortDir;
import ru.sbsoft.svc.data.shared.Store;
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
import ru.sbsoft.svc.widget.core.client.ContentPanel;
import ru.sbsoft.svc.widget.core.client.button.IconButton;
import ru.sbsoft.svc.widget.core.client.button.ToolButton;
import ru.sbsoft.svc.widget.core.client.container.VerticalLayoutContainer;
import ru.sbsoft.svc.widget.core.client.tree.Tree;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import ru.sbsoft.client.I18n;
import ru.sbsoft.client.components.actions.AbstractAction;
import ru.sbsoft.client.components.actions.Action;
import ru.sbsoft.client.components.actions.ActionManager;
import ru.sbsoft.client.components.actions.event.KeyActionController;
import ru.sbsoft.client.components.actions.event.StdActKey;
import ru.sbsoft.client.components.grid.CustomToolBar;
import ru.sbsoft.client.consts.SBFResources;
import ru.sbsoft.client.model.TreeNodeProperties;
import ru.sbsoft.client.utils.VLC;
import ru.sbsoft.shared.FilterInfo;
import ru.sbsoft.shared.TreeNode;
import ru.sbsoft.shared.api.i18n.I18nResourceInfo;
import ru.sbsoft.shared.api.i18n.consts.SBFGeneralStr;

/**
 * Базовый класс дерева сущностей. Определяет все поведение за исключением
 * наполнения данными, которое выполняется потомками в
 * {@link #callService(java.math.BigDecimal, com.google.gwt.user.client.rpc.AsyncCallback)}.
 * Используется, в основном, при создании специального браузера с фильтром в
 * виде панели с указанным деревом.
 *
 * @see ru.sbsoft.client.components.browser.TreeBrowser
 */
public abstract class AbstractTree<K, N extends TreeNode<K>> extends VerticalLayoutContainer {

    private final static TreeNodeProperties PROPS = new TreeNodeProperties();
    private SbTree<N, String> tree;
    private TreeLoader<N> loader;
    protected final ActionManager actionManager = new ActionManager();
    protected final CustomToolBar editToolBar = new CustomToolBar(actionManager, true);
    private ContentPanel treeContainer = new ContentPanel();
    protected final KeyActionController keyActionController = new KeyActionController(this);
    private boolean openFirstTreeNodeOnFirstLoading = true;

    //private N config = null;
    protected AbstractTree() {
        editToolBar.getElement().getStyle().setProperty("borderBottom", "none");
        add(editToolBar, VLC.CONST);
        treeContainer.setAnimCollapse(false);
        treeContainer.setCollapsible(false);
        treeContainer.setHeaderVisible(false);
        add(treeContainer, VLC.FILL);
        initTools();
    }

//    protected ToolButton addToolBt(IconButton.IconConfig iconConf, I18nResourceInfo toolTipInfo) {
//        return addToolBt(iconConf, I18n.get(toolTipInfo));
//    }
//
//    protected ToolButton addToolBt(IconButton.IconConfig iconConf, String toolTip) {
//        ToolButton bt = new ToolButton(iconConf);
//        bt.setToolTip(toolTip);
//        addTool(bt);
//        return bt;
//    }
//
//    protected final void addToolBt(IconButton.IconConfig ic, final Action a) {
//        final ToolButton b = new ToolButton(ic);
//        b.setToolTip(Strings.coalesce(a.getToolTip(), a.getCaption()));
//        a.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
//            @Override
//            public void onValueChange(ValueChangeEvent<Boolean> event) {
//                b.setEnabled(event.getValue());
//            }
//        });
//        b.addSelectHandler(new SelectEvent.SelectHandler() {
//            @Override
//            public void onSelect(SelectEvent event) {
//                a.perform();
//            }
//        });
//        actionManager.registerAction(a);
//        addTool(b);
//    }
    protected final void addToolBt(IconButton.IconConfig ic, final Action a) {
        editToolBar.addButton(a);
    }

    protected final void addToolSep() {
        editToolBar.addSeparator();
    }

    //@Override
    protected void initTools() {
        //super.initTools();
        //getHeader().addTool(editToolBar);

        TreeAction refreshAct = new TreeAction(SBFGeneralStr.refresh, SBFResources.BROWSER_ICONS.Refresh16(), SBFResources.BROWSER_ICONS.Refresh()) {
            @Override
            protected void onExecute() {
                refresh(false);
            }
        };
        TreeAction expandAct = new TreeAction(SBFGeneralStr.expandAll, SBFResources.BROWSER_ICONS.ExpandAll16(), SBFResources.BROWSER_ICONS.ExpandAll()) {
            @Override
            protected void onExecute() {
                getTree().expandAll();
            }
        };
        TreeAction collapseAct = new TreeAction(SBFGeneralStr.collapseAll, SBFResources.BROWSER_ICONS.CollapseAll16(), SBFResources.BROWSER_ICONS.CollapseAll()) {
            @Override
            protected void onExecute() {
                getTree().collapseAll();
            }
        };

        addToolBt(ToolButton.REFRESH, refreshAct);
        addToolSep();
        addToolBt(ToolButton.EXPAND, expandAct);
        addToolBt(ToolButton.COLLAPSE, collapseAct);

        keyActionController.addAction(StdActKey.REFRESH, refreshAct);
    }

    public void refresh(boolean forceAll) {
        final N selectedModel = forceAll ? null : getTree().getSelectionModel().getSelectedItem();
        if (null == selectedModel) {
            getTree().getStore().clear();
        } else {
            getTree().getStore().removeChildren(selectedModel);
        }
        getLoader().load(selectedModel);
    }

    public HandlerRegistration addSelectionHandler(SelectionHandler<N> handler) {
        return getTree().getSelectionModel().addSelectionHandler(handler);
    }

    public TreeNodeProperties getNodeProps() {
        return PROPS;
    }

    public boolean isOpenFirstTreeNodeOnFirstLoading() {
        return openFirstTreeNodeOnFirstLoading;
    }

    public void setOpenFirstTreeNodeOnFirstLoading(boolean openFirstTreeNodeOnFirstLoading) {
        this.openFirstTreeNodeOnFirstLoading = openFirstTreeNodeOnFirstLoading;
    }

    private class TreeExpander {

        private final N config;

        public TreeExpander(N config) {
            this.config = config;
        }

        public void tryExpand() {
            if (tree != null && tree.isVisible()) {
                if (config == null) {
                    if (openFirstTreeNodeOnFirstLoading) {
                        expandFirstTreeNode(TreeExpand.EXPAND);
                    }
                    onFirstLoading(tree.getStore().getRootItems());
                } else if (null == getTree().getStore().getParent(config)) {
                    getTree().setExpanded(config, true, false);
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

    private class DataLoadHandler extends ChildTreeStoreBinding<N> {

        public DataLoadHandler(TreeStore<N> store) {
            super(store);
        }

        @Override
        public void onLoad(LoadEvent<N, List<N>> event) {
            super.onLoad(event);
            new TreeExpander(event.getLoadConfig()).tryExpand();
        }
    }

    public Tree<N, String> getTree() {
        if (null == tree) {
            final TreeStore<N> store = new TreeStore<N>(getNodeProps().key());
            DataLoadHandler dataLoadHandler = new DataLoadHandler(store);
            getLoader().addLoadHandler(dataLoadHandler);
            getLoader().setReuseLoadConfig(true);

            tree = new SbTree<N, String>(store, getNodeProps().title());
            tree.setLoader(getLoader());
            tree.getStyle().setNodeOpenIcon(SBFResources.TREEMENU_ICONS.FolderOpen16());
            tree.getStyle().setNodeCloseIcon(SBFResources.TREEMENU_ICONS.Folder16());
            tree.getStyle().setLeafIcon(SBFResources.TREEMENU_ICONS.Leaf16());

            tree.getSelectionModel().setSelectionMode(Style.SelectionMode.SINGLE);
            tree.getStore().addSortInfo(new Store.StoreSortInfo<N>(new Comparator<N>() {
                @Override
                public int compare(N o1, N o2) {
                    String s1 = o1 != null && o1.getTitle() != null ? o1.getTitle() : "";
                    String s2 = o2 != null && o2.getTitle() != null ? o2.getTitle() : "";
                    return s1.compareToIgnoreCase(s2);
                }
            }, SortDir.ASC));

            treeContainer.setWidget(tree);

            tree.getSelectionModel().addSelectionHandler(new SelectionHandler<N>() {
                @Override
                public void onSelection(SelectionEvent<N> event) {
                    actionManager.updateState();
                }

            });

            tree.getStore().addStoreHandlers(new StoreHandlers<N>() {
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
        return tree;
    }

    public TreeLoader<N> getLoader() {
        if (null == loader) {
            loader = new TreeLoader<N>(createProxy()) {
                @Override
                public boolean hasChildren(N parent) {
                    return !parent.isLeaf();
                }

//                @Override
//                protected N prepareLoadConfig(N config) {
//
//                    if (AbstractTree.this.config != null) {
//                        return super.prepareLoadConfig(AbstractTree.this.config);
//                    }
//
//                    return super.prepareLoadConfig(config);
//                }
            };
        }
        return loader;
    }

    private DataProxy<N, List<N>> createProxy() {
        return new RpcProxy<N, List<N>>() {
            @Override
            public void load(N loadConfig, AsyncCallback<List<N>> callback) {
                N rootNode = createRootNode();
                if (null == loadConfig && rootNode != null) {
                    final List<N> nodes = new ArrayList<>();
                    nodes.add(rootNode);
                    callback.onSuccess(nodes);
                } else {
                    loadTreeItems(loadConfig, callback);
                }
            }
        };
    }

    protected void expandNode(N m, TreeExpand e) {
        if (m != null) {
            Tree<N, ?> t = getTree();
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

    protected void expandFirstTreeNode(TreeExpand e) {
        final List<N> rootItems = tree.getStore().getRootItems();
        if (null != rootItems && !rootItems.isEmpty()) {
            expandNode(rootItems.get(0), e);
        }
    }

    protected void onFirstLoading(final List<N> rootItems) {
    }

    protected N createRootNode() {
        return null;
    }

    protected abstract void loadTreeItems(N parent, AsyncCallback<List<N>> callback);

    protected abstract class TreeAction extends AbstractAction {

        public TreeAction(I18nResourceInfo caption, ImageResource icon16, ImageResource icon24) {
            this(I18n.get(caption), icon16, icon24);
        }

        private TreeAction(String caption, ImageResource icon16, ImageResource icon24) {
            super(caption, caption, icon16, icon24);
        }

    }

    public void setFilter(List<FilterInfo> filters) {
        throw new UnsupportedOperationException("Filter is not supported by: " + getClass().getName());
    }
}
