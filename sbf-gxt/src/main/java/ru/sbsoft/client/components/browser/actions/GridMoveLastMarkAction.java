package ru.sbsoft.client.components.browser.actions;

import ru.sbsoft.client.I18n;
import ru.sbsoft.client.components.grid.BaseGrid;
import ru.sbsoft.client.consts.SBFResources;
import ru.sbsoft.shared.api.i18n.consts.SBFBrowserStr;

/**
 * Перемещает выделение на последнюю строку, имеющую метку.
 * @author balandin
 * @since Oct 3, 2013 4:40:45 PM
 */
public class GridMoveLastMarkAction extends GridMoveMarkAction {

    public GridMoveLastMarkAction(BaseGrid grid) {
        super(grid);
        setCaption(I18n.get(SBFBrowserStr.menuNavLastPin));
        setToolTip(I18n.get(SBFBrowserStr.hintNavLastPin));
        setIcon16(SBFResources.BROWSER_ICONS.NavLastPin16());
        setIcon24(SBFResources.BROWSER_ICONS.NavLastPin());
    }

    @Override
    protected void onExecute() {
        getGrid().selectMarkedRecord(true, false);
    }
}
