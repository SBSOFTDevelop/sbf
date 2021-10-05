package ru.sbsoft.client.components.browser.actions;

import ru.sbsoft.client.I18n;
import ru.sbsoft.client.components.grid.BaseGrid;
import ru.sbsoft.client.consts.SBFResources;
import ru.sbsoft.shared.api.i18n.consts.SBFBrowserStr;

/**
 * Действие пункта меню, запускающее операцию обновления таблицы.
 *
 * @author balandin
 */
public class GridReloadAction extends GridAction {

    public GridReloadAction(BaseGrid grid) {
        super(grid);
        super.setCaption(I18n.get(SBFBrowserStr.menuGridRecreate));
        super.setToolTip(I18n.get(SBFBrowserStr.menuGridRecreate));
        super.setIcon16(SBFResources.BROWSER_ICONS.Reload16());
        super.setIcon24(SBFResources.BROWSER_ICONS.Reload());
    }

    @Override
    protected void onExecute() {
        getGrid().reloadGrid();
    }
}
