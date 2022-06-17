package ru.sbsoft.client.components.browser.actions;

import ru.sbsoft.client.I18n;
import ru.sbsoft.client.components.grid.BaseGrid;
import ru.sbsoft.client.consts.SBFResources;
import ru.sbsoft.shared.api.i18n.consts.SBFBrowserStr;

/**
 * Перемещает выделение на первую строку, имеющую метку.
 * @author balandin
 * @since Oct 3, 2013 4:40:37 PM
 */
public class GridMoveFirstMarkAction extends GridMoveMarkAction {

    public GridMoveFirstMarkAction(BaseGrid grid) {
        super(grid);
        setCaption(I18n.get(SBFBrowserStr.menuNavFirstPin));
        setToolTip(I18n.get(SBFBrowserStr.hintNavFirstPin));
        setIcon16(SBFResources.BROWSER_ICONS.NavFirstPin16());
        setIcon24(SBFResources.BROWSER_ICONS.NavFirstPin());
    }

    @Override
    protected void onExecute() {
        getGrid().selectMarkedRecord(false, false);
    }
}
