package ru.sbsoft.client.components.browser;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.ui.Widget;
import ru.sbsoft.svc.core.client.dom.XElement;
import ru.sbsoft.svc.widget.core.client.TabItemConfig;
import ru.sbsoft.svc.widget.core.client.TabPanel;
import ru.sbsoft.svc.widget.core.client.grid.Grid;
import ru.sbsoft.svc.widget.core.client.tree.Tree;
import java.util.HashMap;
import java.util.Map;
import ru.sbsoft.client.appmenu.MenuItemModelEx;
import ru.sbsoft.client.components.IActivateBrowser;
import ru.sbsoft.client.components.ManagedBrowser;
import ru.sbsoft.client.components.MultiBrowser;
import ru.sbsoft.client.components.exception.BrowserException;
import ru.sbsoft.client.components.grid.BaseGrid;
import ru.sbsoft.client.utils.CliUtil;
import ru.sbsoft.shared.model.MenuItemModel;

/**
 * Корневая панель браузеров приложения. Отображает в себе браузеры. Для каждого
 * раздела главного меню создается собственная корневая панель.
 *
 * @see ru.sbsoft.client.appmenu.App
 * @author balandin
 * @since Dec 25, 2012 1:34:39 PM
 */
public class BaseBrowserTabPanel extends TabPanel {

    private final Map<String, AbstractContainer> openBrowsers = new HashMap<String, AbstractContainer>();
    private final BaseBrowserManager browserManager;
    private final String applicationCode;

    public BaseBrowserTabPanel(BaseBrowserManager browserManager, String applicationCode) {
        super();

        this.browserManager = browserManager;
        this.applicationCode = applicationCode;

        setAnimScroll(true);
        setTabScroll(true);
        setBodyBorder(false);
        setBorders(true);
        setCloseContextMenu(true);

        XElement.as(getElement().getChild(0)).getStyle().setProperty("borderWidth", "0");

        final SelectionHandler<Widget> callback = browserManager.getSelectionHandler();
        if (callback != null) {
            addSelectionHandler(callback);
        }

        addSelectionHandler(new SelectionHandler<Widget>() {
            @Override
            public void onSelection(SelectionEvent<Widget> event) {
                if (event.getSelectedItem() instanceof AbstractContainer) {
                    ManagedBrowser managed = ((AbstractContainer)event.getSelectedItem()).managed;
                    if (managed instanceof IActivateBrowser) {
                        ((IActivateBrowser) managed).onActivate();
                    }
                }
                if (event.getSelectedItem() instanceof BaseBrowserContainer) {
                    BaseBrowserContainer c = (BaseBrowserContainer) event.getSelectedItem();
                    Widget w = c.getWidget();
                    if (w instanceof BaseBrowser) {
                        BaseGrid g = ((BaseBrowser) w).getGrid();
                        if (g != null) {
                            g.onBrowserSelected();
                        }
                    }
                }
            }
        });
    }

    public String getApplicationCode() {
        return applicationCode;
    }

    @Override
    public boolean remove(final Widget child) {
        final boolean isRemoved = super.remove(child);
        if (isRemoved) {
            AbstractContainer container = (AbstractContainer) child;
            openBrowsers.remove(container.getIdBrowser());
            if (openBrowsers.isEmpty()) {
                setBorders(true);
            }
        }
        return isRemoved;
    }

    public boolean remove(final BaseBrowserContainer container) {
        final boolean isRemoved = super.remove(container);
        if (isRemoved) {
            openBrowsers.remove(container.getIdBrowser());
        }
        return isRemoved;
    }

    public AbstractContainer openBrowser(final String idBrowser, final String titleBrowser) throws BrowserException {
        return openBrowser(idBrowser, titleBrowser, null);
    }

    public AbstractContainer openBrowser(final MenuItemModel menuItem) throws BrowserException {
        return openBrowser(menuItem.getBrowserID(), menuItem.getMenuName(), menuItem);
    }

    public AbstractContainer openBrowser(String idBrowser, String titleBrowser, final MenuItemModel menuItem) throws BrowserException {
        AbstractContainer container = openBrowsers.get(idBrowser);
        if (container == null) {

            if (openBrowsers.isEmpty()) {
                setBorders(false);
            }

            final ManagedBrowser managed = menuItem instanceof MenuItemModelEx
                    ? browserManager.createBrowser(((MenuItemModelEx) menuItem).getBrowserMaker())
                    : browserManager.getRegisteredBrowser(idBrowser, titleBrowser);
            if (managed instanceof BaseBrowser) {
                BaseBrowser browser = (BaseBrowser) managed;
                container = new BaseBrowserContainer(browserManager, menuItem, browser);
                browser.setContainer((BaseBrowserContainer) container);
                browser.getGrid().checkInitialized();
            } else {
                container = new WidgetContainer(browserManager, menuItem, managed);
            }
            if (!(managed instanceof MultiBrowser)) {
                openBrowsers.put(idBrowser, container);
            }
            add(container, new TabItemConfig(container.getShortName(), true));
        }
        setActiveWidget(container);
        Scheduler.get().scheduleDeferred(new BrowserFocuser(container));
        return container;
    }
    
    private static class BrowserFocuser implements Scheduler.ScheduledCommand {
        private final AbstractContainer container;

        public BrowserFocuser(AbstractContainer container) {
            this.container = container;
        }

        @Override
        public void execute() {
            final Tree tree;
            final Grid grid;
            if((tree = CliUtil.findChildAnyOf(container, Tree.class)) != null){
                tree.focus();
            }else if((grid = CliUtil.findChildAnyOf(container, Grid.class)) != null){
                grid.focus();
            }else{
                container.focus();
            }
        }
    }
}
