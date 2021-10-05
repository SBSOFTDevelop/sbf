package ru.sbsoft.client.components.browser.actions;

import ru.sbsoft.client.I18n;
import ru.sbsoft.client.components.grid.BaseGrid;
import ru.sbsoft.client.consts.SBFResources;
import ru.sbsoft.shared.api.i18n.consts.SBFBrowserStr;

/**
 * Устанавливает выделение на последнюю строку таблицы.
 * @author balandin
 * @since Oct 3, 2013 4:40:45 PM
 */
public class GridMoveLastAction extends GridMoveAction {

    public GridMoveLastAction(BaseGrid grid) {
        super(grid);
        setCaption(I18n.get(SBFBrowserStr.menuNavLast));
        setToolTip(I18n.get(SBFBrowserStr.hintNavLast));
        setIcon16(SBFResources.BROWSER_ICONS.NavLast16());
        setIcon24(SBFResources.BROWSER_ICONS.NavLast());
    }

    @Override
    protected void onExecute() {
        getGrid().gotoLastRecord();
    }
}
