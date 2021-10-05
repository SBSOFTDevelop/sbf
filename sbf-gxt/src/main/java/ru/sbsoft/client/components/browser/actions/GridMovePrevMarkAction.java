package ru.sbsoft.client.components.browser.actions;

import ru.sbsoft.client.I18n;
import ru.sbsoft.client.components.grid.BaseGrid;
import ru.sbsoft.client.consts.SBFResources;
import ru.sbsoft.shared.api.i18n.consts.SBFBrowserStr;

/**
 * Перемещает выделение на предыдущую строку, имеющую метку.
 * @author balandin
 * @since Oct 3, 2013 4:40:55 PM
 */
public class GridMovePrevMarkAction extends GridMoveMarkAction {

    public GridMovePrevMarkAction(BaseGrid grid) {
        super(grid);
        setCaption(I18n.get(SBFBrowserStr.menuNavPrevPin));
        setToolTip(I18n.get(SBFBrowserStr.hintNavPrevPin));
        setIcon16(SBFResources.BROWSER_ICONS.NavPrevPin16());
        setIcon24(SBFResources.BROWSER_ICONS.NavPrevPin());
    }

    @Override
    protected void onExecute() {
        getGrid().selectMarkedRecord(false, true);
    }
}
