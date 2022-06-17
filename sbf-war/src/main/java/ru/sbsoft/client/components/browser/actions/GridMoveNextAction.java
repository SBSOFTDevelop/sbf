package ru.sbsoft.client.components.browser.actions;

import ru.sbsoft.client.I18n;
import ru.sbsoft.client.components.grid.BaseGrid;
import ru.sbsoft.client.consts.SBFResources;
import ru.sbsoft.shared.api.i18n.consts.SBFBrowserStr;

/**
 * Устанавливает выделение на следующую строку таблицы.
 * @author balandin
 * @since Oct 3, 2013 4:41:07 PM
 */
public class GridMoveNextAction extends GridMoveAction {

    public GridMoveNextAction(BaseGrid grid) {
        super(grid);
        setCaption(I18n.get(SBFBrowserStr.menuNavNext));
        setToolTip(I18n.get(SBFBrowserStr.hintNavNext));
        setIcon16(SBFResources.BROWSER_ICONS.NavNext16());
        setIcon24(SBFResources.BROWSER_ICONS.NavNext());
    }

    @Override
    protected void onExecute() {
        getGrid().gotoNextRecord();
    }
}
