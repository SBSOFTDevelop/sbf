package ru.sbsoft.client.components.treebrowser;

import ru.sbsoft.client.I18n;
import ru.sbsoft.client.consts.SBFResources;
import ru.sbsoft.client.utils.ClientUtils;
import ru.sbsoft.shared.api.i18n.consts.SBFBrowserStr;

/**
 *
 * @author sokolov
 */
public class TreeGridDeleteAction extends TreeGridAction {

    public TreeGridDeleteAction(AbstractTreeGrid grid) {
        super(grid);
        setCaption(I18n.get(SBFBrowserStr.menuOperRowDelete));
        setToolTip(I18n.get(SBFBrowserStr.hintOperRowDelete));
        setIcon16(SBFResources.BROWSER_ICONS.RowDelete16());
        setIcon24(SBFResources.BROWSER_ICONS.RowDelete());
    }

    @Override
    public boolean checkEnabled() {
        return super.checkEnabled() && getGrid().isLeafSelect();
    }

    @Override
    protected void onExecute() {
        ClientUtils.confirm(getGrid(), I18n.get(SBFBrowserStr.msgDeleteCurrent) + "?", () -> {
            getGrid().delete();
        });
    }

}
