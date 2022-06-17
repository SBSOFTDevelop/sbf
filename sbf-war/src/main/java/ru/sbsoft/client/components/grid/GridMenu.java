package ru.sbsoft.client.components.grid;

import com.google.gwt.event.shared.HandlerRegistration;
import ru.sbsoft.svc.widget.core.client.event.BeforeShowEvent;
import ru.sbsoft.svc.widget.core.client.menu.MenuBarItem;
import ru.sbsoft.client.I18n;
import ru.sbsoft.client.components.actions.Action;
import ru.sbsoft.client.components.actions.ActionMenu;
import ru.sbsoft.client.components.actions.ActionMenuBar;
import ru.sbsoft.client.components.browser.Browser;
import ru.sbsoft.shared.api.i18n.consts.SBFBrowserStr;

/**
 * Строка меню таблицы.
 * @author panarin
 */
public class GridMenu extends ActionMenuBar {

    private final Browser browser;
    private HandlerRegistration handlerRegistration;
    private ActionMenu systemMenu;
    private ActionMenu operationsMenu;
    private ActionMenu reportMenu;
    private MenuBarItem reportMenuItem;

    public GridMenu(final Browser browser) {
        super(browser.getActionManager());
        this.browser = browser;
        createMenu();
    }

    private BaseGrid getGrid() {
        return browser.getGrid();
    }

    protected void createMenu() {
        addMenuBar(I18n.get(SBFBrowserStr.menuFile), systemMenu = createFileMenu());
        addMenuBar(I18n.get(SBFBrowserStr.menuCookies), createMarkMenu());
        addMenuBar(I18n.get(SBFBrowserStr.menuOperation), operationsMenu = createOperationsMenu());
        addMenuBar(I18n.get(SBFBrowserStr.menuNavigation), createNavigationMenu());
    }

    protected ActionMenu createFileMenu() {
        final ActionMenu menu = new ActionMenu(getActionManager());
        menu.addAction(getGrid().getSaveAction());
        menu.addAction(getGrid().getRefreshAction());
        menu.addAction(getGrid().getGridRloadAction());
        // menu.addAction(browser.getShowFilterAction());
        menu.addAction(browser.getShowFilterAction2());
        menu.addAction(browser.getQuickAggregatesAction());
        menu.addAction(browser.getGrid().getExportAction());

        handlerRegistration = menu.addBeforeShowHandler(new BeforeShowEvent.BeforeShowHandler() {
            @Override
            public void onBeforeShow(BeforeShowEvent event) {
                if (handlerRegistration != null) {
                    handlerRegistration.removeHandler();
                    handlerRegistration = null;
                }
                final Action action = browser.getCloseAction();
                if (action != null) {
                    menu.addSeparator();
                    menu.addAction(action);
                }
            }
        });

        return menu;
    }

    protected ActionMenu createMarkMenu() {
        if (getGrid().isMarksAllowed()) {
            ActionMenu menu = new ActionMenu(getActionManager());
            menu.addAction(getGrid().getMarkCheckAction());
            menu.addAction(getGrid().getMarkClearAction());
            menu.addAction(getGrid().getMarkInvertAllAction());
            return menu;
        }
        return null;
    }

    protected ActionMenu createOperationsMenu() {
        ActionMenu menu = new ActionMenu(getActionManager());
        menu.addAction(getGrid().getUpdateAction());
        menu.addAction(getGrid().getInsertAction());
        menu.addAction(getGrid().getCloneAction());
        menu.addAction(getGrid().getDeleteAction());
        if (getGrid().getShowAction() != null) {
            menu.addSeparator();
            menu.addAction(getGrid().getShowAction());
        }
        return menu;
    }

    protected ActionMenu createNavigationMenu() {
        ActionMenu menu = new ActionMenu(getActionManager());
        menu.addAction(getGrid().getMoveFirstAction());
        menu.addAction(getGrid().getMoveLastAction());
        menu.addAction(getGrid().getMovePrevAction());
        menu.addAction(getGrid().getMoveNextAction());
        if (getGrid().isMarksAllowed()) {
            menu.addSeparator();
            menu.addAction(getGrid().getMoveFirstMarkAction());
            menu.addAction(getGrid().getMoveLastMarkAction());
            menu.addAction(getGrid().getMovePrevMarkAction());
            menu.addAction(getGrid().getMoveNextMarkAction());
        }
        return menu;
    }

    public ActionMenu getSystemMenu() {
        return systemMenu;
    }

    public ActionMenu getOperationsMenu() {
        return operationsMenu;
    }

    public ActionMenu getReportMenu() {
        if (reportMenu == null) {
            insert(reportMenuItem = new MBI(I18n.get(SBFBrowserStr.menuReports), reportMenu = new ActionMenu(getActionManager())), 3);
        }
        return reportMenu;
    }
    
    public boolean activateReportMenu(){
        if (reportMenu != null && reportMenuItem != null && reportMenu.isEnabled() && reportMenu.getWidgetCount() > 0) {
            setActiveItem(reportMenuItem, true);
            return true;
        }
        return false;
    }

    public ActionMenu getReportMenuAsIs() {
        return reportMenu;
    }
    
    public void updateReportActions(Action... a) {
        if (reportMenu != null) {
            reportMenu.clear();
            reportMenu.setEnabled(false);
        }
        if (null == a || a.length < 1) {
            return;
        }
        getReportMenu().setEnabled(true);
        for (Action action : a) {
            reportMenu.addAction(action);
        }
    }
}
