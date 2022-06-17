package ru.sbsoft.client.components.treebrowser;

import ru.sbsoft.client.components.actions.Action;
import ru.sbsoft.client.components.grid.CustomToolBar;

/**
 *
 * @author sokolov
 */
public class TreeGridToolBar extends CustomToolBar {

    private final ITreeBrowser browser;

    public TreeGridToolBar(ITreeBrowser browser, boolean smallIcons) {
        super(browser.getActionManager(), smallIcons);
        this.browser = browser;

        Action closeAction = browser.getCloseAction();
        if (closeAction != null) {
            super.addButton(closeAction);
            super.addSeparator();
        }
        super.addButton(browser.getGrid().getRefreshAction());
        super.addSeparator();
        super.addButton(browser.getGrid().getUpdateAction());
        super.addActionsButton(browser.getGrid().getInsertAction(), browser.getGrid().getCloneAction());
        super.addButton(browser.getGrid().getDeleteAction());
        super.addSeparator();
        super.addButton(browser.getGrid().getExpandAction());
        super.addButton(browser.getGrid().getCollapseAction());
    }

    public ITreeBrowser getBrowser() {
        return browser;
    }

}
