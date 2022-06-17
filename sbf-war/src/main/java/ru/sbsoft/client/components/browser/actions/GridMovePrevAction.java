package ru.sbsoft.client.components.browser.actions;

import ru.sbsoft.client.I18n;
import ru.sbsoft.client.components.grid.BaseGrid;
import ru.sbsoft.client.consts.SBFResources;
import ru.sbsoft.shared.api.i18n.consts.SBFBrowserStr;

/**
 * Устанавливает выделение на предыдущую строку таблицы.
 * @author balandin
 * @since Oct 3, 2013 4:40:55 PM
 */
public class GridMovePrevAction extends GridMoveAction {

    public GridMovePrevAction(BaseGrid grid) {
        super(grid);
        setCaption(I18n.get(SBFBrowserStr.menuNavPrev));
        setToolTip(I18n.get(SBFBrowserStr.hintNavPrev));
        setIcon16(SBFResources.BROWSER_ICONS.NavPrev16());
        setIcon24(SBFResources.BROWSER_ICONS.NavPrev());
    }

    @Override
    protected void onExecute() {
        getGrid().gotoPrevRecord();
    }
}
