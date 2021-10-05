package ru.sbsoft.client.components.browser.actions;

import com.sencha.gxt.data.shared.loader.ListLoader;
import ru.sbsoft.client.I18n;
import ru.sbsoft.client.components.grid.BaseGrid;
import ru.sbsoft.client.consts.SBFResources;
import ru.sbsoft.shared.api.i18n.consts.SBFBrowserStr;

/**
 *
 * @author Kiselev
 */
public class GridMultisortAction extends GridAction {

    public GridMultisortAction(BaseGrid grid) {
        super(grid);
        setCaption(I18n.get(SBFBrowserStr.menuMultiSort));
        setToolTip(I18n.get(SBFBrowserStr.hintMultiSort));
        setIcon16(SBFResources.BROWSER_ICONS.multiSortIcon());
        setIcon24(SBFResources.BROWSER_ICONS.multiSortIcon()); //no 24 icon - use 16
    }

    @Override
    public boolean checkEnabled() {
        return super.checkEnabled() && isRemoteSort();
    }

    @Override
    protected void onExecute() {
        getGrid().showMultisort();
    }

    private boolean isRemoteSort() {
        ListLoader l = getGrid().getGrid().getLoader();
        return l != null && l.isRemoteSort();
    }
}
