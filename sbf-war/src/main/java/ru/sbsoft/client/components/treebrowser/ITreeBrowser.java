package ru.sbsoft.client.components.treebrowser;

import ru.sbsoft.client.components.actions.ActionManager;

/**
 *
 * @author sokolov
 */
public interface ITreeBrowser {
    
    AbstractTreeGrid getGrid();
    
    TreeGridToolBar getGridToolBar();
    
    TreeGridMenu getGridMenu();
    
    ActionManager getActionManager();
    
    TreeBrowserCloseAction getCloseAction();
    
    void onExit();
    
}
