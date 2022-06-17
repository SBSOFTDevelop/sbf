package ru.sbsoft.client.components.treebrowser;

import ru.sbsoft.client.I18n;
import ru.sbsoft.client.consts.SBFResources;
import ru.sbsoft.shared.api.i18n.consts.SBFBrowserStr;

/**
 *
 * @author sokolov
 */
public class TreeGridInsertAction extends TreeGridAction {

    public TreeGridInsertAction(AbstractTreeGrid grid) {
        super(grid);
        super.setCaption(I18n.get(SBFBrowserStr.menuOperRowInsert));
        super.setToolTip(I18n.get(SBFBrowserStr.hintOperRowInsert));
        super.setIcon16(SBFResources.BROWSER_ICONS.RowInsert16());
        super.setIcon24(SBFResources.BROWSER_ICONS.RowInsert());

    }

    @Override
    public boolean checkEnabled() {
        return super.checkEnabled() && (!getGrid().isReadOnly());
    }

    @Override
    protected void onExecute() {
        getGrid().insert();
    }

}
