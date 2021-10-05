package ru.sbsoft.client.components.treebrowser;

import ru.sbsoft.client.I18n;
import ru.sbsoft.client.consts.SBFResources;
import ru.sbsoft.shared.api.i18n.consts.SBFBrowserStr;

/**
 *
 * @author sokolov
 */
public class TreeGridUpdateAction extends TreeGridAction {

    private static final String TIP_EDIT = I18n.get(SBFBrowserStr.hintOperRowUpdate);
    private static final String TIP_VIEW = I18n.get(SBFBrowserStr.hintViewForm);

    public TreeGridUpdateAction(AbstractTreeGrid grid) {
        super(grid);
        setCaption(I18n.get(SBFBrowserStr.menuOperRowUpdate));
        setToolTip(TIP_EDIT);
        setIcon16(SBFResources.BROWSER_ICONS.RowUpdate16());
        setIcon24(SBFResources.BROWSER_ICONS.RowUpdate());
    }

    @Override
    public boolean checkEnabled() {
        return super.checkEnabled() && isSelection();
    }

    @Override
    public void checkState() {
        super.checkState();
        setToolTip(getGrid().isReadOnly() ? TIP_VIEW : TIP_EDIT);
    }

    @Override
    protected void onExecute() {
        getGrid().edit();
    }

}
