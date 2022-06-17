package ru.sbsoft.client.components.grid.menu;

import ru.sbsoft.svc.widget.core.client.menu.Item;
import ru.sbsoft.svc.widget.core.client.menu.SeparatorMenuItem;
import java.util.List;
import ru.sbsoft.client.components.actions.Action;
import ru.sbsoft.client.components.actions.ActionMenu;
import ru.sbsoft.client.components.browser.Browser;
import ru.sbsoft.client.components.browser.actions.GridMultisortAction;
import ru.sbsoft.client.components.grid.BaseGrid;
import ru.sbsoft.client.components.grid.GridMenu;

/**
 * Контекстное меню таблицы.
 *
 * @author balandin
 * @since Apr 17, 2014 12:20:10 PM
 */
public class GridContextMenu {

    private ActionMenu menu;
    private final Browser browser;
    private List<Action> extraActions;

    public GridContextMenu(Browser browser) {
        this.browser = browser;
    }

    public ActionMenu getMenu() {
        if (menu == null) {
            BaseGrid grid = browser.getGrid();
            menu = new ActionMenu(browser.getActionManager());

            final GridMenu gridMenu = browser.getGridMenu();
            if (gridMenu != null) {

                copy(gridMenu.getSystemMenu(), menu);
                copy(gridMenu.getOperationsMenu(), menu);
                copy(gridMenu.getReportMenuAsIs(), menu);

                if (grid.isMarksAllowed()) {
                    menu.addAction(grid.getMarkClearAction());
                    menu.addAction(grid.getMarkInvertAllAction());
                }

            } else {

                menu.addAction(grid.getSaveAction());
                menu.addAction(grid.getRefreshAction());
                menu.addAction(grid.getGridRloadAction());
                menu.addSeparator();
                menu.addAction(grid.getUpdateAction());
                menu.addAction(grid.getInsertAction());
                menu.addAction(grid.getCloneAction());
                menu.addAction(grid.getDeleteAction());
                menu.addSeparator();
                menu.addAction(browser.getShowFilterAction2());

            }

            if (grid.isRemoteSort() && grid.isMultisortEnabled()) {
                menu.addSeparator();
                menu.addAction(new GridMultisortAction(grid));
            }
            
            if (extraActions != null && !extraActions.isEmpty()) {
                menu.addSeparator();
                extraActions.forEach(action -> menu.addAction(action));
            }

            menu.addSeparator();
            menu.addAction(browser.getInfoAction());

            browser.getActionManager().updateState();
        }
        return menu;
    }
    
    public void addExtraActions(List<Action> extraActions) {
        if (null == this.extraActions) {
            this.extraActions = extraActions;
        } else {
            this.extraActions.addAll(extraActions);
        }
    }

    protected static void copy(ActionMenu src, ActionMenu toMenu) {
        if (src != null) {
            for (int i = 0; i < src.getWidgetCount(); i++) {
                final Item item = (Item) src.getWidget(i);
                if (item.getData(Action.LABEL) != null) {
                    toMenu.addAction((Action) item.getData(Action.LABEL));
                } else if (item instanceof SeparatorMenuItem) {
                    toMenu.addSeparator();
                }
            }
            toMenu.addSeparator();
        }
    }
}
