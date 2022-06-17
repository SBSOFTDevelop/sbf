package ru.sbsoft.client.components.browser;

import ru.sbsoft.client.components.actions.Action;
import ru.sbsoft.client.components.browser.actions.GridOperationAction;
import ru.sbsoft.client.components.form.FormGridView;
import ru.sbsoft.client.components.grid.CustomGridToolBar;
import ru.sbsoft.client.components.grid.GridMenu;
import ru.sbsoft.client.components.operation.GridOperationMaker;
import ru.sbsoft.shared.interfaces.OperationType;
import ru.sbsoft.shared.model.FileFormat;
import ru.sbsoft.client.components.form.IParamFormFactory;
import ru.sbsoft.client.components.form.ParamHandlerCollector;
import ru.sbsoft.client.components.form.PrintParamFormFactory;
import ru.sbsoft.client.components.operation.PrintGridOperationAction;
import ru.sbsoft.client.components.operation.RunGridOperationAction;
import ru.sbsoft.client.components.operation.SimpleOperationMaker;
import ru.sbsoft.client.components.operation.TypedGridOperationAction;

/**
 *
 * @author Kiselev
 */
public class BrowserConfigHelper {

    private final Browser browser;
    private boolean doubleActionOnToolbar = false;

    public BrowserConfigHelper(Browser browser) {
        this.browser = browser;
        doubleActionOnToolbar = browser instanceof FormGridView;
    }

    public Browser getBrowser() {
        return browser;
    }

    public static GridOperationAction createReport(GridOperationMaker maker, TypedGridOperationAction.TYPE_USE_MODE useMode) {
        return new PrintGridOperationAction(maker, useMode);
    }

    public GridOperationAction createReport(OperationType type, IParamFormFactory paramsFormFactory, TypedGridOperationAction.TYPE_USE_MODE useMode) {
        return createReport(new SimpleOperationMaker(type, browser.getGrid(), paramsFormFactory, false), useMode);
    }

    public GridOperationAction createReport(OperationType type, TypedGridOperationAction.TYPE_USE_MODE useMode, FileFormat... formats) {
        return createReport(type, new BaseReportParamFormFactory(formats), useMode);
    }

    public static GridOperationAction createOperation(GridOperationMaker maker, TypedGridOperationAction.TYPE_USE_MODE useMode) {
        return new RunGridOperationAction(maker, useMode);
    }

    public GridOperationAction createOperation(OperationType type, IParamFormFactory paramsFormFactory, TypedGridOperationAction.TYPE_USE_MODE useMode) {
        return createOperation(new SimpleOperationMaker(type, browser.getGrid(), paramsFormFactory), useMode);
    }

    public GridOperationAction createOperation(OperationType type, IParamFormFactory paramsFormFactory, TypedGridOperationAction.TYPE_USE_MODE useMode, boolean reloadOnComplete) {
        return createOperation(new SimpleOperationMaker(type, browser.getGrid(), paramsFormFactory, reloadOnComplete), useMode);
    }

    public GridOperationAction createOperation(OperationType type, TypedGridOperationAction.TYPE_USE_MODE useMode) {
        return createOperation(type, null, useMode);
    }

    public GridOperationAction createOperation(OperationType type, TypedGridOperationAction.TYPE_USE_MODE useMode, boolean reloadOnComplete) {
        return createOperation(type, null, useMode, reloadOnComplete);
    }

    public void addReport(GridOperationMaker maker, TypedGridOperationAction.TYPE_USE_MODE useMode) {
        addAction(createReport(maker, useMode));
    }

    public void addReport(OperationType type, IParamFormFactory paramsFormFactory, TypedGridOperationAction.TYPE_USE_MODE useMode) {
        addAction(createReport(type, paramsFormFactory, useMode));
    }

    public void addReport(OperationType type, TypedGridOperationAction.TYPE_USE_MODE useMode, FileFormat... formats) {
        addAction(createReport(type, useMode, formats));
    }

    public void addOperation(GridOperationMaker maker, TypedGridOperationAction.TYPE_USE_MODE useMode) {
        addAction(new RunGridOperationAction(maker, useMode));
    }

    public void addOperation(OperationType type, IParamFormFactory paramsFormFactory, TypedGridOperationAction.TYPE_USE_MODE useMode) {
        addAction(createOperation(type, paramsFormFactory, useMode));
    }

    public void addOperation(OperationType type, IParamFormFactory paramsFormFactory, TypedGridOperationAction.TYPE_USE_MODE useMode, boolean reloadOnComplete) {
        addAction(createOperation(type, paramsFormFactory, useMode, reloadOnComplete));
    }

    public void addOperation(OperationType type, TypedGridOperationAction.TYPE_USE_MODE useMode) {
        addAction(createOperation(type, useMode));
    }

    public void addOperation(OperationType type, TypedGridOperationAction.TYPE_USE_MODE useMode, boolean reloadOnComplete) {
        addAction(createOperation(type, useMode, reloadOnComplete));
    }

    public BrowserConfigHelper setDoubleActionOnToolbar(boolean b) {
        this.doubleActionOnToolbar = b;
        return this;
    }

    public void addMarkButtons() {
        CustomGridToolBar gridToolBar = browser.getGridToolBar();
        if (gridToolBar != null) {
            gridToolBar.addSeparator();
            gridToolBar.addButton(browser.getGrid().getMarkClearAction());
            gridToolBar.addButton(browser.getGrid().getMarkInvertAllAction());
        }
    }

    public void addToolbarSeparator() {
        CustomGridToolBar gridToolBar = browser.getGridToolBar();
        if (gridToolBar != null) {
            gridToolBar.addSeparator();
        }
    }

    public void addAction(Action a) {
        GridMenu gridMenu = browser.getGridMenu();
        if (gridMenu != null) {
            if (a instanceof PrintGridOperationAction) {
                gridMenu.getReportMenu().addAction(a);
            } else {
                gridMenu.getOperationsMenu().addAction(a);
            }
        }
        CustomGridToolBar gridToolBar = browser.getGridToolBar();
        if (gridToolBar != null && doubleActionOnToolbar) {
            gridToolBar.addButton(a);
        }
    }

    public void addActionsButton(int beforeIndex, Action... a) {
        if (a != null && a.length > 1) {
            CustomGridToolBar gridToolBar = browser.getGridToolBar();
            if (gridToolBar != null) {
                Action mainAct = a[0];
                Action[] addActs = new Action[a.length - 1];
                System.arraycopy(a, 1, addActs, 0, a.length - 1);
                gridToolBar.addActionsButton(beforeIndex, mainAct, addActs);
//                TextButton btn = gridToolBar.addSplitButton(a[0], beforeIndex);
//                ActionMenu menu = new ActionMenu(browser.getActionManager(), gridToolBar.isSmallIcons());
//                //for (int i = a[0] instanceof VoidAction ? 1 : 0; i < a.length; i++) {
//                for (int i = 1; i < a.length; i++) {
//                    menu.addAction(a[i]);
//                }
//                btn.setMenu(menu);
            }
        }
    }

    public void addActionsButton(Action... a) {
        addActionsButton(browser.getGridToolBar().getWidgetCount(), a);
    }

    /*    
    public void addReportActions(int beforeIndex, Action... a) {
        GridMenu gridMenu = browser.getGridMenu();
        if (gridMenu != null) {
            for (int i = 0; i < a.length; i++) {
                gridMenu.getReportMenu().addAction(a[i]);
            }
        }
        CustomGridToolBar gridToolBar = browser.getGridToolBar();
        if (gridToolBar != null && doubleActionOnToolbar) {
            Action[] fa = new Action[a.length + 1];
            fa[0] = new VoidAction(I18n.get(SBFBrowserStr.menuReports), SBFResources.TREEMENU_ICONS.print16(), SBFResources.TREEMENU_ICONS.print24());
            for (int i = 0; i < a.length; i++) {
                fa[i+1] = a[i];
            }
            addActionsButton(beforeIndex, fa);
        }
        
    }
     */
    private static class BaseReportParamFormFactory extends PrintParamFormFactory {

        public BaseReportParamFormFactory(FileFormat... formats) {
            super(formats);
        }

        @Override
        protected void fillHandlers(ParamHandlerCollector h, ParamForm f) {
            //no handlers for simple
        }

    }
}
