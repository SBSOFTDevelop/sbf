package ru.sbsoft.client.components.treebrowser;

import ru.sbsoft.client.I18n;
import ru.sbsoft.client.consts.SBFResources;
import ru.sbsoft.shared.api.i18n.consts.SBFBrowserStr;

/**
 *
 * @author sokolov
 */
public class TreeGridRefreshAction extends TreeGridAction {

    public TreeGridRefreshAction(AbstractTreeGrid grid) {
        super(grid);

        super.setCaption(I18n.get(SBFBrowserStr.menuFileRefresh));
        super.setToolTip(I18n.get(SBFBrowserStr.hintFileRefresh));
        super.setIcon16(SBFResources.BROWSER_ICONS.Refresh16());
        super.setIcon24(SBFResources.BROWSER_ICONS.Refresh());
        
    }

    @Override
    protected void onExecute() {
        getGrid().refresh(true);
    }

}
