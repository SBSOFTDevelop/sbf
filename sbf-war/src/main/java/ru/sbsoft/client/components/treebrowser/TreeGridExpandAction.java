package ru.sbsoft.client.components.treebrowser;

import ru.sbsoft.client.I18n;
import ru.sbsoft.client.consts.SBFResources;
import ru.sbsoft.shared.api.i18n.consts.SBFGeneralStr;

/**
 *
 * @author sokolov
 */
public class TreeGridExpandAction extends TreeGridAction {

    public TreeGridExpandAction(AbstractTreeGrid grid) {
        super(grid);
        setCaption(I18n.get(SBFGeneralStr.expandAll));
        setToolTip(I18n.get(SBFGeneralStr.expandAll));
        setIcon16(SBFResources.BROWSER_ICONS.ExpandAll16());
        setIcon24(SBFResources.BROWSER_ICONS.ExpandAll());
    }

    @Override
    protected void onExecute() {
        getGrid().expandAll();
    }

}
