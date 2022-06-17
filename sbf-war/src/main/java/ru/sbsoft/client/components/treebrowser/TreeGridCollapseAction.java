package ru.sbsoft.client.components.treebrowser;

import ru.sbsoft.client.I18n;
import ru.sbsoft.client.consts.SBFResources;
import ru.sbsoft.shared.api.i18n.consts.SBFGeneralStr;

/**
 *
 * @author sokolov
 */
public class TreeGridCollapseAction extends TreeGridAction {

    public TreeGridCollapseAction(AbstractTreeGrid grid) {
        super(grid);
        setCaption(I18n.get(SBFGeneralStr.collapseAll));
        setToolTip(I18n.get(SBFGeneralStr.collapseAll));
        setIcon16(SBFResources.BROWSER_ICONS.CollapseAll16());
        setIcon24(SBFResources.BROWSER_ICONS.CollapseAll());
    }
    
    @Override
    protected void onExecute() {
        getGrid().collapseAll();
    }

}
