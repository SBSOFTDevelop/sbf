package ru.sbsoft.client.components.treebrowser;

import com.sencha.gxt.widget.core.client.button.TextButton;
import ru.sbsoft.client.components.actions.Action;
import ru.sbsoft.client.components.actions.ActionMenu;
import ru.sbsoft.client.components.actions.VoidAction;
import ru.sbsoft.client.components.form.IParamFormFactory;
import ru.sbsoft.client.components.form.ParamFormFactory;
import ru.sbsoft.client.components.form.ParamHandlerCollector;
import ru.sbsoft.client.components.form.PrintParamFormFactory;
import ru.sbsoft.client.components.operation.PrintGridOperationAction;
import ru.sbsoft.shared.interfaces.OperationType;
import ru.sbsoft.shared.model.FileFormat;

/**
 *
 * @author sokolov
 */
public class TreeBrowserConfigHelper {

    
    private final ITreeBrowser browser;
    private boolean doubleActionOnToolbar = false;

    public TreeBrowserConfigHelper(ITreeBrowser browser) {
        this.browser = browser;
    }

    public ITreeBrowser getBrowser() {
        return browser;
    }

    public static TreeGridOperationAction createReport(TreeGridOperationMaker maker, TreeTypedGridOperationAction.TYPE_USE_MODE useMode) {
        return new TreePrintGridOperationAction(maker, useMode);
    }

    public TreeGridOperationAction createReport(OperationType type, IParamFormFactory paramsFormFactory, TreeTypedGridOperationAction.TYPE_USE_MODE useMode) {
        return createReport(new TreeSimpleOperationMaker(type, browser.getGrid(), paramsFormFactory, false), useMode);
    }

    public TreeGridOperationAction createReport(OperationType type, TreeTypedGridOperationAction.TYPE_USE_MODE useMode, FileFormat... formats) {
        return createReport(type, new BaseReportParamFormFactory(formats), useMode);
    }

    public static TreeGridOperationAction createOperation(TreeGridOperationMaker maker, TreeTypedGridOperationAction.TYPE_USE_MODE useMode) {
        return new TreeRunGridOperationAction(maker, useMode);
    }

    public TreeGridOperationAction createOperation(OperationType type, IParamFormFactory paramsFormFactory, TreeTypedGridOperationAction.TYPE_USE_MODE useMode) {
        return createOperation(new TreeSimpleOperationMaker(type, browser.getGrid(), paramsFormFactory), useMode);
    }

    public TreeGridOperationAction createOperation(OperationType type, IParamFormFactory paramsFormFactory, TreeTypedGridOperationAction.TYPE_USE_MODE useMode, boolean reloadOnComplete) {
        return createOperation(new TreeSimpleOperationMaker(type, browser.getGrid(), paramsFormFactory, reloadOnComplete), useMode);
    }

    public TreeGridOperationAction createOperation(OperationType type, TreeTypedGridOperationAction.TYPE_USE_MODE useMode) {
        return createOperation(type, null, useMode);
    }

    public TreeGridOperationAction createOperation(OperationType type, TreeTypedGridOperationAction.TYPE_USE_MODE useMode, boolean reloadOnComplete) {
        return createOperation(type, null, useMode, reloadOnComplete);
    }

    public void addReport(TreeGridOperationMaker maker, TreeTypedGridOperationAction.TYPE_USE_MODE useMode) {
        addAction(createReport(maker, useMode));
    }

    public void addReport(OperationType type, IParamFormFactory paramsFormFactory, TreeTypedGridOperationAction.TYPE_USE_MODE useMode) {
        addAction(createReport(type, paramsFormFactory, useMode));
    }

    public void addReport(OperationType type, TreeTypedGridOperationAction.TYPE_USE_MODE useMode, FileFormat... formats) {
        addAction(createReport(type, useMode, formats));
    }

    public void addOperation(TreeGridOperationMaker maker, TreeTypedGridOperationAction.TYPE_USE_MODE useMode) {
        addAction(new TreeRunGridOperationAction(maker, useMode));
    }

    public void addOperation(OperationType type, IParamFormFactory paramsFormFactory, TreeTypedGridOperationAction.TYPE_USE_MODE useMode) {
        addAction(createOperation(type, paramsFormFactory, useMode));
    }

    public void addOperation(OperationType type, IParamFormFactory paramsFormFactory, TreeTypedGridOperationAction.TYPE_USE_MODE useMode, boolean reloadOnComplete) {
        addAction(createOperation(type, paramsFormFactory, useMode, reloadOnComplete));
    }

    public void addOperation(OperationType type, TreeTypedGridOperationAction.TYPE_USE_MODE useMode) {
        addAction(createOperation(type, useMode));
    }

    public void addOperation(OperationType type, TreeTypedGridOperationAction.TYPE_USE_MODE useMode, boolean reloadOnComplete) {
        addAction(createOperation(type, useMode, reloadOnComplete));
    }

    public TreeBrowserConfigHelper setDoubleActionOnToolbar(boolean b) {
        this.doubleActionOnToolbar = b;
        return this;
    }

/*    
    public void addMarkButtons() {
        TreeGridToolBar gridToolBar = browser.getGridToolBar();
        if (gridToolBar != null) {
            gridToolBar.addSeparator();
            gridToolBar.addButton(browser.getGrid().getMarkClearAction());
            gridToolBar.addButton(browser.getGrid().getMarkInvertAllAction());
        }
    }
*/
    public void addToolbarSeparator() {
        TreeGridToolBar gridToolBar = browser.getGridToolBar();
        if (gridToolBar != null) {
            gridToolBar.addSeparator();
        }
    }

    public void addAction(Action a) {
        TreeGridMenu gridMenu = browser.getGridMenu();
        if (gridMenu != null) {
            if (a instanceof PrintGridOperationAction) {
                gridMenu.getReportMenu().addAction(a);
            } else {
                gridMenu.getOperationsMenu().addAction(a);
            }
        }
        TreeGridToolBar gridToolBar = browser.getGridToolBar();
        if (gridToolBar != null && doubleActionOnToolbar) {
            gridToolBar.addButton(a);
        }
    }

    public void addActions(int beforeIndex, Action... a) {
        if (a != null && a.length > 1) {
            TreeGridToolBar gridToolBar = browser.getGridToolBar();
            if (gridToolBar != null && doubleActionOnToolbar) {
                TextButton btn = gridToolBar.addSplitButton(a[0], beforeIndex);
                ActionMenu menu = new ActionMenu(browser.getActionManager(), gridToolBar.isSmallIcons());
                for (int i = a[0] instanceof VoidAction ? 1 : 0; i < a.length; i++) {
                    menu.addAction(a[i]);
                }
                btn.setMenu(menu);
            }
        }
    }

    public void addActions(Action... a) {
//        GridMenu gridMenu = browser.getGridMenu();
//        if (gridMenu != null) {
//            if (a instanceof PrintGridOperationAction) {
//                gridMenu.getReportMenu().addAction(a);
//            } else {
//                gridMenu.getOperationsMenu().addAction(a);
//            }
//        }

        addActions(browser.getGridToolBar().getWidgetCount(), a);

    }

    private static class BaseReportParamFormFactory extends PrintParamFormFactory {

        public BaseReportParamFormFactory(FileFormat... formats) {
            super(formats);
        }

        @Override
        protected void fillHandlers(ParamHandlerCollector h, ParamFormFactory.ParamForm f) {
            //no handlers for simple
        }

    }
  
}
