package ru.sbsoft.client.components.treebrowser;

import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import ru.sbsoft.client.components.actions.ActionManager;
import ru.sbsoft.client.components.browser.WidgetContainer;
import ru.sbsoft.sbf.gxt.utils.VLC;

/**
 *
 * @author sokolov
 */
public abstract class TreeBrowser extends VerticalLayoutContainer implements ITreeBrowser {

    protected final ActionManager actionManager = new ActionManager();
    private final boolean smallIcons;

    protected String headerCaption;
    protected AbstractTreeGrid grid;
    protected TreeGridMenu gridMenu;
    protected TreeGridToolBar gridToolBar;
    protected TreeBrowserCloseAction closeAction;
    

    protected abstract AbstractTreeGrid createGrid();
    
    protected abstract void init();

    public TreeBrowser(boolean smallIcons) {
        this.smallIcons = smallIcons;
    }

    @Override
    protected void onAfterFirstAttach() {
        super.onAfterFirstAttach();
        init();
    }   
    
    protected void initBars() {
        add(getGridMenu());
        add(getGridToolBar(), VLC.CONST);
    }

    @Override
    public TreeGridMenu getGridMenu() {
        if (gridMenu == null) {
            gridMenu = new TreeGridMenu(this);
        }
        return gridMenu;
    }

    @Override
    public TreeGridToolBar getGridToolBar() {
        if (gridToolBar == null) {
            gridToolBar = new TreeGridToolBar(this, smallIcons);
        }
        return gridToolBar;
    }
    
    @Override
    public AbstractTreeGrid getGrid() {
        return grid;
    }

    @Override
    public void onExit() {
        if (getParent() != null && getParent() instanceof WidgetContainer) {
            WidgetContainer container = (WidgetContainer) getParent();
            container.exit();
        }
    }

    @Override
    public TreeBrowserCloseAction getCloseAction() {
        if (closeAction == null) {
            closeAction = new TreeBrowserCloseAction(this);
        }
        return closeAction;
    }
    
    @Override
    public ActionManager getActionManager() {
        return actionManager;
    }    

}
