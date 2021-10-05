package ru.sbsoft.client.components.browser.actions;

import ru.sbsoft.client.I18n;
import ru.sbsoft.client.components.grid.BaseGrid;
import ru.sbsoft.client.consts.SBFResources;
import ru.sbsoft.shared.api.i18n.consts.SBFBrowserStr;

/**
 * Устанавливает выделение на первую строку таблицы.
 * @author balandin
 * @since Oct 3, 2013 4:40:37 PM
 */
public class GridMoveFirstAction extends GridMoveAction {

    public GridMoveFirstAction(BaseGrid grid) {
        super(grid);
        setCaption(I18n.get(SBFBrowserStr.menuNavFirst));
        setToolTip(I18n.get(SBFBrowserStr.hintNavFirst));
        setIcon16(SBFResources.BROWSER_ICONS.NavFirst16());
        setIcon24(SBFResources.BROWSER_ICONS.NavFirst());
    }

    @Override
    protected void onExecute() {
        getGrid().gotoFirstRecord();
    }
}
