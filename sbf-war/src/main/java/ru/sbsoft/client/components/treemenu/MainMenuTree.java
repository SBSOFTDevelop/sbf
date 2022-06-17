package ru.sbsoft.client.components.treemenu;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import ru.sbsoft.svc.cell.core.client.form.ComboBoxCell;
import ru.sbsoft.svc.data.shared.LabelProvider;
import ru.sbsoft.svc.data.shared.ListStore;
import ru.sbsoft.svc.data.shared.ModelKeyProvider;
import ru.sbsoft.svc.data.shared.TreeStore;
import ru.sbsoft.svc.widget.core.client.ContentPanel;
import ru.sbsoft.svc.widget.core.client.button.SplitButton;
import ru.sbsoft.svc.widget.core.client.button.TextButton;
import ru.sbsoft.svc.widget.core.client.container.AccordionLayoutContainer;
import ru.sbsoft.svc.widget.core.client.container.AccordionLayoutContainer.ExpandMode;
import ru.sbsoft.svc.widget.core.client.container.SimpleContainer;
import ru.sbsoft.svc.widget.core.client.container.VerticalLayoutContainer;
import ru.sbsoft.svc.widget.core.client.event.ExpandEvent;
import ru.sbsoft.svc.widget.core.client.event.SelectEvent;
import ru.sbsoft.svc.widget.core.client.event.SelectEvent.SelectHandler;
import ru.sbsoft.svc.widget.core.client.form.SimpleComboBox;
import ru.sbsoft.svc.widget.core.client.menu.Item;
import ru.sbsoft.svc.widget.core.client.menu.Menu;
import ru.sbsoft.svc.widget.core.client.menu.MenuItem;
import ru.sbsoft.svc.widget.core.client.toolbar.SeparatorToolItem;
import ru.sbsoft.svc.widget.core.client.toolbar.ToolBar;
import ru.sbsoft.svc.widget.core.client.tree.Tree;
import ru.sbsoft.client.I18n;
import ru.sbsoft.client.appmenu.IAppStruct;
import ru.sbsoft.client.appmenu.MenuItemModelEx;
import ru.sbsoft.client.components.IBrowserMaker;
import ru.sbsoft.client.components.browser.AbstractContainer;
import ru.sbsoft.client.components.browser.BaseBrowserManager;
import ru.sbsoft.client.components.browser.BaseBrowserTabPanel;
import ru.sbsoft.client.components.dialog.ChangePasswdDialog;
import ru.sbsoft.client.components.dialog.AppSettingsForm;
import ru.sbsoft.client.components.exception.BrowserException;
import ru.sbsoft.client.components.multiOperation.MultiOperationMonitorWidget;
import ru.sbsoft.client.consts.SBFConst;
import ru.sbsoft.client.consts.SBFResources;
import ru.sbsoft.client.utils.*;
import ru.sbsoft.shared.MenuTypeEnum;
import ru.sbsoft.shared.api.i18n.consts.SBFBrowserStr;
import ru.sbsoft.shared.api.i18n.consts.SBFGeneralStr;
import ru.sbsoft.shared.model.ApplicationMenuTreeModel;
import ru.sbsoft.shared.model.MenuItemModel;
import ru.sbsoft.shared.model.user.UserEnv;

import java.util.*;
import ru.sbsoft.client.SaveAppInfoHandler;

/**
 * Дерево главного меню приложения. Создается и инициализируется в
 * {@link ru.sbsoft.client.SBFEntryPoint}. Для задания структуры меню
 * рекомендуется переопределять метод
 * {@link ru.sbsoft.client.SBFEntryPoint#fillAppMenu(ru.sbsoft.client.appmenu.AppStruct)}.
 *
 * @author panarin
 */
public class MainMenuTree extends VerticalLayoutContainer {

    private final List<SaveAppInfoHandler> saveInfoHandlers = new ArrayList<>();
    private final AccordionLayoutContainer applicationContainer;
    private final IMenuExecutor itemExecutor;
    private final BaseBrowserManager browserManager;
    private AppSettingsForm appSettingsForm;
    //
    private ToolBar toolbar;
    //
    private final SimpleContainer bottomWidgetContainer = new SimpleContainer();

    public MainMenuTree(final BaseBrowserManager browserManager, final IMenuExecutor itemExecutor, String monitorTitle) {
        super();

        this.browserManager = browserManager;
        this.itemExecutor = itemExecutor;
        this.bottomWidgetContainer.setHeight("2em");
        setBottomWidget(new MultiOperationMonitorWidget(monitorTitle));
        add(getToolBar(), VLC.CONST);

        applicationContainer = new AccordionLayoutContainer() {
            @Override
            protected void onExpand(final ExpandEvent event) {
                super.onExpand(event);
                if (event.getSource() instanceof ApplicationContentPanel) {
                    final ApplicationContentPanel appPanel = (ApplicationContentPanel) event.getSource();
                    if (appPanel != null) {
                        final String appCode = appPanel.getApplication().getAPPLICATION_CODE();
                        browserManager.selectApplicationPanel(appCode, false);
                    }
                }
            }
        };
        applicationContainer.setExpandMode(ExpandMode.SINGLE_FILL);

        // --------------------------------------------------------------------
        browserManager.setSelectionHandler(new SelectionHandler<Widget>() {
            @Override
            public void onSelection(final SelectionEvent<Widget> event) {
                AbstractContainer container = (AbstractContainer) event.getSelectedItem();
                MenuItemModel menuItem = container.getMenuItem();
                if (menuItem != null && menuItem.getParentID() != null) { //проверяем, что menuItem принадлежит дереву меню (реализация вызова не из главного меню)
                    Tree<MenuItemModel, MenuItemModel> tree = getCurrentTree();
                    if (tree != null) {
                        TreeStore<MenuItemModel> store = tree.getStore();
                        MenuItemModel currItem = store.findModel(menuItem);
                        String browserId = getBrowserId(menuItem);
                        if (currItem == null && browserId != null) {
                            for (MenuItemModel m : store.getAll()) {
                                if (browserId.equals(getBrowserId(m))) {
                                    currItem = m;
                                    break;
                                }
                            }
                        }
                        if (currItem != null) {
                            tree.getSelectionModel().select(false, currItem);
                        } else {
                            tree.getSelectionModel().deselectAll();
                        }
                    }
                }
            }
        });
    }

    private static String getBrowserId(MenuItemModel m) {
        return MenuTypeEnum.Browser.equals(m.getMenuType()) && ((!(m instanceof MenuItemModelEx)) || ((MenuItemModelEx) m).getBrowserMaker() != null) ? m.getBrowserID() : null;
    }

    public ToolBar getToolBar() {
        if (toolbar == null) {
            toolbar = new ToolBar();
            toolbar.getElement().getStyle().setProperty("borderBottom", "none");
//            toolbar.add(new SeparatorToolItem());
            toolbar.add(createExitButton());
            toolbar.add(new SeparatorToolItem());
            toolbar.add(createTreeControlButton());
            toolbar.add(createReloadButton());
            if (RoleCheker.getInstance().isAdmin()) {
                toolbar.add(createSettingsButton());
            }
        }
        return toolbar;
    }

    public void addToolBarWidget(Widget widget) {
        ToolBar tb = getToolBar();
        tb.add(widget);
    }
    
    public void addSaveInfoHandler(SaveAppInfoHandler handler) {
        saveInfoHandlers.add(handler);
    }    
    
    public void focusMenuTree(){
        applicationContainer.focus();
    }

    private static class ExButton extends SplitButton {

        private boolean wasCtrl = false;

        @Override
        public void onBrowserEvent(Event event) {
            super.onBrowserEvent(event);
            wasCtrl = event.getCtrlKey();
        }

        public boolean wasCtrl() {
            try {
                return wasCtrl;
            } finally {
                wasCtrl = false;
            }
        }
    }

    private SimpleComboBox<IBrowserMaker> cbMakers;
    private HashMap<IBrowserMaker, Integer> cbMakersIndexes;

    private MenuItem createChangePWDItem() {

        MenuItem m = new MenuItem();
        m.setIcon(SBFResources.GENERAL_ICONS.UserID16());
        m.setText(I18n.get(SBFGeneralStr.labelChangePassword));
        m.setEnabled(RoleCheker.getInstance().getPasswordPolicy().isCanChange());
        m.addSelectionHandler((SelectionEvent<Item> event) -> {
            new ChangePasswdDialog(RoleCheker.getInstance().getPasswordPolicy()).show();
        });

        return m;

    }

    private TextButton createExitButton() {
        final ExButton b = new ExButton();
        final Menu menu = new Menu();
        b.setIcon(SBFResources.GENERAL_ICONS.Exit());

        b.setToolTip(I18n.get(SBFBrowserStr.hintFileExit));
        b.setData("svc-menutext", I18n.get(SBFBrowserStr.menuFileExit));

        b.addSelectHandler(new SelectHandler() {
            @Override
            public void onSelect(final SelectEvent event) {
                if (b.wasCtrl()) {
                    initBrowsersCombo();
                    return;
                }

                SBFConst.SECURUTY_SERVICE.logout(new DefaultAsyncCallback<Void>() {
                    @Override
                    public void onResult(final Void result) {
                        ClientUtils.reload();
                    }
                });
            }
        });

        final MenuItem m = new MenuItem();
        m.setIcon(SBFResources.GENERAL_ICONS.Exit16());
        m.setText(I18n.get(SBFBrowserStr.hintFileExit));
        m.addSelectionHandler(new SelectionHandler<Item>() {
            @Override
            public void onSelection(SelectionEvent<Item> event) {
                b.fireEvent(new SelectEvent());
            }
        });

        menu.add(m);
        menu.add(createChangePWDItem());

        b.setMenu(menu);

        return b;
    }

    private void initBrowsersCombo() {

        final NumberFormat INDEX_FORMAT = NumberFormat.getFormat("[000] ");

        cbMakers = new SimpleComboBox<IBrowserMaker>(
                new ComboBoxCell<IBrowserMaker>(
                        new ListStore<IBrowserMaker>(new ModelKeyProvider<IBrowserMaker>() {

                            @Override
                            public String getKey(IBrowserMaker item) {
                                return item.getIdBrowser();
                            }
                        }),
                        new LabelProvider<IBrowserMaker>() {

                    @Override
                    public String getLabel(IBrowserMaker item) {
                        Integer index = cbMakersIndexes == null ? null : cbMakersIndexes.get(item);
                        return (index == null ? "" : INDEX_FORMAT.format(index)) + item.getIdBrowser() + " / " + item.getClass().getName();
                    }
                }
                ) {
            @Override
            protected boolean itemMatchesQuery(IBrowserMaker item, String query) {
                return getLabelProvider().getLabel(item).toLowerCase().contains(query.toLowerCase());
            }
        }
        );
        cbMakers.setTriggerAction(ComboBoxCell.TriggerAction.ALL);
        cbMakers.setTypeAhead(true);
        cbMakers.setMinListWidth(900);

        new ComboBoxValueChangeHandler<IBrowserMaker>() {
            @Override
            public void onValueChange(ValueChangeEvent<IBrowserMaker> event) {
                BaseBrowserTabPanel p = browserManager.selectApplicationPanel("TEST", true);
                IBrowserMaker value = event.getValue();
                if (value != null) {
                    if (value.getIdBrowser() != null) {
                        try {
                            p.openBrowser(value.getIdBrowser(), value.getTitleBrowser());
                        } catch (BrowserException ex) {
                            ClientUtils.alertException(ex);
                        }
                    }
                    cbMakers.setValue(null, false, true);
                }
            }
        }.addHandler(cbMakers);

        final Map<String, IBrowserMaker> registers = browserManager.getRegistrBrowsers();
        final String[] ids = registers.keySet().toArray(new String[]{});
        Arrays.<String>sort(ids, new Comparator<String>() {

            @Override
            public int compare(String o1, String o2) {
                return o1.compareToIgnoreCase(o2);
            }
        });

        int index = 0;
        cbMakersIndexes = new HashMap<IBrowserMaker, Integer>();

        // final HashMap<String, Columns> map = new HashMap<String, Columns>();
        for (final String id : ids) {
            IBrowserMaker maker = registers.get(id);
            cbMakers.add(maker);
            cbMakersIndexes.put(maker, ++index);
        }
        insert(cbMakers, 2, VLC.CONST);
        forceLayout();
    }

    private TextButton createTreeControlButton() {
        final TextButton b = new TextButton();
        b.setIcon(SBFResources.BROWSER_ICONS.Sort());
        b.setToolTip(I18n.get(SBFGeneralStr.hintTreeFunctions));
        b.setData("svc-menutext", I18n.get(SBFGeneralStr.menuTreeFunctions));

        final Menu menu = new Menu();
        menu.add(createExpandAllMenu());
        menu.add(createCollapseAllMenu());

        b.setMenu(menu);
        return b;
    }

    private MenuItem createExpandAllMenu() {
        final MenuItem m = new MenuItem();
        m.setIcon(SBFResources.BROWSER_ICONS.ExpandAll16());
        m.setText(I18n.get(SBFGeneralStr.expandAll));
        m.addSelectionHandler(new SelectionHandler<Item>() {

            @Override
            public void onSelection(SelectionEvent<Item> event) {
                Tree<MenuItemModel, MenuItemModel> tree = getCurrentTree();
                if (tree != null) {
                    tree.expandAll();
                }
            }
        });
        return m;
    }

    private MenuItem createCollapseAllMenu() {
        final MenuItem m = new MenuItem();
        m.setIcon(SBFResources.BROWSER_ICONS.CollapseAll16());
        m.setText(I18n.get(SBFGeneralStr.collapseAll));
        m.addSelectionHandler(new SelectionHandler<Item>() {

            @Override
            public void onSelection(SelectionEvent<Item> event) {
                Tree<MenuItemModel, MenuItemModel> tree = getCurrentTree();
                if (tree != null) {
                    tree.collapseAll();
                }
            }
        });
        return m;
    }

    private TextButton createReloadButton() {
        final TextButton b = new TextButton();
        b.setIcon(SBFResources.BROWSER_ICONS.Refresh());
        b.setToolTip(I18n.get(SBFGeneralStr.hintReloadMenu));
        b.setData("svc-menutext", I18n.get(SBFGeneralStr.menuReloadMenu));
        b.addSelectHandler(ev -> reload());
        return b;
    }
    
    private TextButton createSettingsButton() {
        final TextButton b = new TextButton();
        b.setIcon(SBFResources.BROWSER_ICONS.Preference());
        b.setToolTip(I18n.get(SBFGeneralStr.hintSettingsMenu));
        b.setData("svc-menutext", I18n.get(SBFGeneralStr.menuSettingsMenu));
        b.addSelectHandler(ev -> showSettings());
        return b;        
    }

    private Tree<MenuItemModel, MenuItemModel> getCurrentTree() {
        if (null == applicationContainer.getActiveWidget()) {
            return null;
        } else {
            final ContentPanel cp = (ContentPanel) applicationContainer.getActiveWidget();
            return (Tree<MenuItemModel, MenuItemModel>) cp.getWidget();
        }
    }

    private void reload() {
        this.mask();
        RoleCheker.reload(new DefaultAsyncCallback<UserEnv>(this) {
            @Override
            public void onResult(UserEnv result) {
                for (int i = 0; i < applicationContainer.getWidgetCount(); i++) {
                    Widget w = applicationContainer.getWidget(i);
                    if (w instanceof ApplicationContentPanel) {
                        ApplicationContentPanel p = (ApplicationContentPanel) w;
                        p.reloadMenu();
                    }
                }
            }
        });
    }
    
    private void showSettings() {
        if (null == appSettingsForm) {
            appSettingsForm = new AppSettingsForm();
            appSettingsForm.addSaveInfoHandler(m -> saveInfoHandlers.forEach(h -> h.onSave(m)));
        }
        appSettingsForm.show();
    }

    public void init(List<ApplicationMenuTreeModel> applicationList) {
        init(applicationList, null);
    }

    public void init(final List<ApplicationMenuTreeModel> applicationList, IAppStruct appStruct) {
        applicationContainer.clear();
        if (null == applicationList || applicationList.isEmpty()) {
            return;
        }
        for (final ApplicationMenuTreeModel appMmodel : applicationList) {
            applicationContainer.add(new ApplicationContentPanel(this, appMmodel, appStruct));
        }
        if (applicationContainer.getWidgetCount() > 0) {
            Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {

                @Override
                public void execute() {
                    ApplicationContentPanel p = (ApplicationContentPanel) applicationContainer.getWidget(0);
                    applicationContainer.setActiveWidget(p);
                    browserManager.selectApplicationPanel(p.getApplication().getAPPLICATION_CODE(), false);
                }
            });
        }
        add(applicationContainer, VLC.FILL);
        add(bottomWidgetContainer, VLC.CONST);
        forceLayout();
    }

    public void executeItem(final ApplicationMenuTreeModel application, final MenuItemModel value) throws BrowserException {
        itemExecutor.executeItem(application.getAPPLICATION_CODE(), value);
    }

    public void setBottomWidget(IsWidget widget) {
        bottomWidgetContainer.add(widget);
    }
}
