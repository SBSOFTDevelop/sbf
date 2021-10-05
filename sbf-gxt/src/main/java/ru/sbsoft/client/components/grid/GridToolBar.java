package ru.sbsoft.client.components.grid;

import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.toolbar.SeparatorToolItem;
import ru.sbsoft.client.I18n;
import ru.sbsoft.client.components.actions.Action;
import ru.sbsoft.client.components.actions.ActionMenu;
import ru.sbsoft.client.components.actions.VoidAction;
import ru.sbsoft.client.components.browser.Browser;
import ru.sbsoft.client.consts.SBFResources;
import ru.sbsoft.shared.api.i18n.consts.SBFBrowserStr;

/**
 * Основная панель инструментов таблицы. Используется в {@link Browser}
 */
public class GridToolBar extends CustomGridToolBar {

    private TextButton reportButton;

    public GridToolBar(final Browser browser, boolean smallIcons) {
        super(browser, smallIcons);

        super.addButton(browser.getCloseAction());        
        super.addSeparator();
        super.addButton(getGrid().getRefreshAction());
        super.addButton(getGrid().getGridRloadAction());
        //super.addButton(browser.getShowFilterAction());
        super.addButton(browser.getShowFilterAction2());
        super.addButton(browser.getQuickAggregatesAction());
        super.addSeparator();
        super.addButton(getGrid().getMarkCheckAction());
        super.addButton(getGrid().getUpdateAction());
        super.addActionsButton(getGrid().getInsertAction(), getGrid().getCloneAction());
        super.addButton(getGrid().getDeleteAction());
        super.addSeparator();
        super.addButton(getGrid().getMoveFirstAction());
        super.addButton(getGrid().getMoveLastAction());
        super.addButton(getGrid().getMoveNextMarkAction());
        super.addButton(getGrid().getMovePrevMarkAction());
    }

    public void updateReportActions(Action... a) {
        if (reportButton != null) {
            reportButton.setEnabled(false);
            ActionMenu menu = (ActionMenu) reportButton.getMenu();
            menu.clear();
        }
        if (null == a || a.length < 1) {
            return;
        }
        if (null == reportButton) {
            Action reportAction = new VoidAction(I18n.get(SBFBrowserStr.menuReports), SBFResources.TREEMENU_ICONS.print16(), SBFResources.TREEMENU_ICONS.print24());
            ActionMenu menu = new ActionMenu(browser.getActionManager(), super.isSmallIcons());
            if (getWidgetCount() > 2) {
                int index = 2;
                if (!(getWidget(1) instanceof SeparatorToolItem)) {
                    index = 1;  
                } 
                reportButton = addSplitButton(reportAction, index);
                insert(new SeparatorToolItem(), index + 1);
            } else {
                addSeparator();
                reportButton = addSplitButton(reportAction);
            }
            reportButton.setMenu(menu);
            forceLayout();
        }
        reportButton.setEnabled(true);
        ActionMenu menu = (ActionMenu) reportButton.getMenu();
        for (int i = 0; i < a.length; i++) {
            menu.addAction(a[i]);
        }
    }
    
}
