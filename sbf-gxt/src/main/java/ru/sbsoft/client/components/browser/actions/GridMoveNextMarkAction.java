package ru.sbsoft.client.components.browser.actions;

import ru.sbsoft.client.I18n;
import ru.sbsoft.client.components.grid.BaseGrid;
import ru.sbsoft.client.consts.SBFResources;
import ru.sbsoft.shared.api.i18n.consts.SBFBrowserStr;

/**
 * Перемещает выделение на следующую строку, имеющую метку.
 * @author balandin
 * @since Oct 3, 2013 4:41:07 PM
 */
public class GridMoveNextMarkAction extends GridMoveMarkAction {

    public GridMoveNextMarkAction(BaseGrid grid) {
        super(grid);
        setCaption(I18n.get(SBFBrowserStr.menuNavNextPin));
        setToolTip(I18n.get(SBFBrowserStr.hintNavNextPin));
        setIcon16(SBFResources.BROWSER_ICONS.NavNextPin16());
        setIcon24(SBFResources.BROWSER_ICONS.NavNextPin());
    }

    @Override
    protected void onExecute() {
        getGrid().selectMarkedRecord(true, true);
    }
}
