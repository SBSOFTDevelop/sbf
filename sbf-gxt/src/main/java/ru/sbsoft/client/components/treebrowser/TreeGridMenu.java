package ru.sbsoft.client.components.treebrowser;

import ru.sbsoft.client.I18n;
import ru.sbsoft.client.components.actions.Action;
import ru.sbsoft.client.components.actions.ActionMenu;
import ru.sbsoft.client.components.actions.ActionMenuBar;
import ru.sbsoft.shared.api.i18n.consts.SBFBrowserStr;

/**
 *
 * @author sokolov
 */
public class TreeGridMenu extends ActionMenuBar {

    private final ITreeBrowser browser;
    private ActionMenu systemMenu;
    private ActionMenu operationsMenu;
    private ActionMenu reportMenu;

    public TreeGridMenu(ITreeBrowser browser) {
        super(browser.getActionManager());
        this.browser = browser;

        createMenu();
    }

    private AbstractTreeGrid getGrid() {
        return browser.getGrid();
    }

    protected void createMenu() {
        addMenuBar(I18n.get(SBFBrowserStr.menuFile), systemMenu = createFileMenu());
        addMenuBar(I18n.get(SBFBrowserStr.menuOperation), operationsMenu = createOperationsMenu());
        addMenuBar(I18n.get(SBFBrowserStr.menuNavigation), createNavigationMenu());
    }

    protected ActionMenu createFileMenu() {
        final ActionMenu menu = new ActionMenu(getActionManager());
        menu.addAction(browser.getGrid().getRefreshAction());
        Action closeAction = browser.getCloseAction(); 
        if (closeAction != null) {
            menu.addSeparator();
            menu.addAction(closeAction);
        }
        return menu;
    }

    protected ActionMenu createOperationsMenu() {
        ActionMenu menu = new ActionMenu(getActionManager());
        menu.addAction(getGrid().getUpdateAction());
        menu.addAction(getGrid().getInsertAction());
        menu.addAction(getGrid().getCloneAction());
        menu.addAction(getGrid().getDeleteAction());
        return menu;
    }
    
    protected ActionMenu createNavigationMenu() {
        ActionMenu menu = new ActionMenu(getActionManager());
        menu.addAction(getGrid().getExpandAction());
        menu.addAction(getGrid().getCollapseAction());
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
            insert(new MBI(I18n.get(SBFBrowserStr.menuReports), reportMenu = new ActionMenu(getActionManager())), 3);
        }
        return reportMenu;
    }

    public ActionMenu getReportMenuAsIs() {
        return reportMenu;
    }    

}
