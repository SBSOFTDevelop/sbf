package ru.sbsoft.client.components.treemenu;

import com.google.gwt.cell.client.Cell.Context;
import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;
import ru.sbsoft.svc.cell.core.client.SimpleSafeHtmlCell;
import ru.sbsoft.svc.core.client.dom.XElement;
import ru.sbsoft.svc.data.client.loader.RpcProxy;
import ru.sbsoft.svc.data.shared.IconProvider;
import ru.sbsoft.svc.data.shared.TreeStore;
import ru.sbsoft.svc.data.shared.loader.ChildTreeStoreBinding;
import ru.sbsoft.svc.data.shared.loader.TreeLoader;
import ru.sbsoft.svc.widget.core.client.ContentPanel;
import ru.sbsoft.svc.widget.core.client.container.AccordionLayoutContainer.AccordionLayoutAppearance;
import ru.sbsoft.svc.widget.core.client.tips.QuickTip;
import ru.sbsoft.svc.widget.core.client.tree.Tree;
import java.util.List;
import ru.sbsoft.client.I18n;
import ru.sbsoft.client.appmenu.IAppStruct;
import ru.sbsoft.client.components.exception.BrowserException;
import ru.sbsoft.client.consts.SBFConst;
import ru.sbsoft.client.consts.SBFResources;
import ru.sbsoft.client.utils.ClientUtils;
import ru.sbsoft.common.Strings;
import ru.sbsoft.shared.model.ApplicationMenuTreeModel;
import ru.sbsoft.shared.model.MenuItemModel;
import ru.sbsoft.shared.api.i18n.consts.SBFGeneralStr;

/**
 * Представляет собой раздел главного меню и содержит соответствующее поддерево
 * элементов. Создается и добавляется в элемент типа "аккордеон" при
 * инициализации {@link MainMenuTree}.
 *
 * @see ApplicationMenuTreeModel
 * @author balandin
 * @since Dec 25, 2012 12:30:45 PM
 */
class ApplicationContentPanel extends ContentPanel {

    private final static MenuItemProperties M_ITEM_PROP = new MenuItemProperties();
    private final static IconProvider<MenuItemModel> ICON_PROVIDER = createIconProvider();
    private final static AccordionLayoutAppearance APPEARANCE = GWT.create(AccordionLayoutAppearance.class);
    //
    private final MainMenuTree mainMenuTree;
    private final ApplicationMenuTreeModel application;
    private final IAppStruct appStruct;

    public ApplicationContentPanel(MainMenuTree mainMenuTree, final ApplicationMenuTreeModel application) {
        this(mainMenuTree, application, null);
    }

    public ApplicationContentPanel(MainMenuTree mainMenuTree, final ApplicationMenuTreeModel application, IAppStruct appStruct) {
        super(APPEARANCE);

        this.appStruct = appStruct;
        this.mainMenuTree = mainMenuTree;
        this.application = application;

        super.setAnimCollapse(false);
        super.setHeading(application.getAPPLICATION_TITLE());

        XElement.as(super.getElement().getChild(0).getChild(0)).getStyle().setProperty("borderWidth", "0 0 1px");
        XElement.as(super.getElement().getChild(1).getChild(0)).getStyle().setProperty("borderWidth", "0");

        super.setWidget(createAppTree(application.getRootMenuList()));
    }

    public ApplicationMenuTreeModel getApplication() {
        return application;
    }
    
    public void reloadMenu(){
        Widget w = getWidget();
        if(w instanceof Tree){
            Tree<MenuItemModel, MenuItemModel> t = (Tree)w;
            t.getStore().clear();
            t.getStore().add(application.getRootMenuList());
        }
    }

    private static IconProvider<MenuItemModel> createIconProvider() {
        return model -> {
            switch (model.getMenuType()) {
                case Report:
                    return SBFResources.TREEMENU_ICONS.print16();
                case Operation:
                    return SBFResources.TREEMENU_ICONS.ItemGreen16();
                case OperationSingle:
                    return SBFResources.TREEMENU_ICONS.ItemBlue16();
                default:
                    return null;
            }
        };
    }

    private Tree<MenuItemModel, MenuItemModel> createAppTree(final List<MenuItemModel> roots) {
        final TreeStore<MenuItemModel> treeStore = new TreeStore<MenuItemModel>(M_ITEM_PROP.key()) {
            @Override
            public boolean hasChildren(MenuItemModel parent) {
                return MenuItemModel.isFolder(parent);
            }
        };
        treeStore.add(roots);
        final Tree<MenuItemModel, MenuItemModel> appTree = new Tree<>(treeStore, M_ITEM_PROP.menuObject());
        appTree.setIconProvider(ICON_PROVIDER);
        appTree.setCell(createTreeCell());
        appTree.getStyle().setNodeOpenIcon(SBFResources.TREEMENU_ICONS.FolderOpen16());
        appTree.getStyle().setNodeCloseIcon(SBFResources.TREEMENU_ICONS.Folder16());
        appTree.getStyle().setLeafIcon(SBFResources.TREEMENU_ICONS.Leaf16());
        appTree.setLoader(createLoader(treeStore));

        final QuickTip quickTip = new QuickTip(appTree);
        quickTip.setMaxWidth(500);

        return appTree;
    }

    private TreeLoader<MenuItemModel> createLoader(final TreeStore<MenuItemModel> treeStore) {
        final RpcProxy<MenuItemModel, List< MenuItemModel>> proxy = new RpcProxy<MenuItemModel, List< MenuItemModel>>() {
            @Override
            public void load(MenuItemModel loadConfig, final AsyncCallback<List<MenuItemModel>> callback) {
                mask(I18n.get(SBFGeneralStr.loadData));
                AsyncCallback<List<MenuItemModel>> cb = new AsyncCallback<List<MenuItemModel>>() {
                    @Override
                    public void onFailure(final Throwable caught) {
                        unmask();
                        callback.onFailure(caught);
                    }

                    @Override
                    public void onSuccess(final List<MenuItemModel> result) {
                        unmask();
                        callback.onSuccess(result);
                    }
                };
                if (appStruct != null) {
                    appStruct.loadChildren(loadConfig, cb);
                } else {
                    SBFConst.DB_STRUCT_SERVICE.getChildsMenuItem(loadConfig, cb);
                }
            }
        };

        final TreeLoader<MenuItemModel> loader = new TreeLoader<MenuItemModel>(proxy) {
            @Override
            public boolean hasChildren(MenuItemModel parent) {
                return MenuItemModel.isFolder(parent);
            }
        };
        loader.addLoadHandler(new ChildTreeStoreBinding<>(treeStore));
        loader.addLoadExceptionHandler(event -> ClientUtils.alertException(event.getException()));

        return loader;
    }

    private SimpleSafeHtmlCell<MenuItemModel> createTreeCell() {
        return new SimpleSafeHtmlCell<MenuItemModel>(MenuItemSafeHtmlRenderer.getInstance(), "mouseover", "click") {
            @Override
            public void onBrowserEvent(final Context context, final Element parent, final MenuItemModel value,
                    final NativeEvent event, final ValueUpdater<MenuItemModel> valueUpdater) {

                super.onBrowserEvent(context, parent, value, event, valueUpdater);

                if ("mouseover".equals(event.getType())) {
                    parent.setAttribute("qtip", Strings.coalesce(value.getDescription(), value.getMenuName()));
                } else if ("click".equals(event.getType())) {
                    try {
                        mainMenuTree.executeItem(application, value);
                    } catch (final BrowserException error) {
                        ClientUtils.showError(error.getMessage());
                    }
                }

            }
        };
    }
}
