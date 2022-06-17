package ru.sbsoft.client.components.browser.actions;

import ru.sbsoft.client.I18n;
import ru.sbsoft.client.components.grid.BaseGrid;
import ru.sbsoft.client.consts.SBFResources;
import ru.sbsoft.shared.api.i18n.consts.SBFBrowserStr;

/**
 *
 * @author sychugin
 */
public class GridCloneAction extends GridAction {

    public GridCloneAction(BaseGrid grid) {
        super(grid);
        init();
    }

    private void init() {
        setCaption(I18n.get(SBFBrowserStr.menuOperRowClone));
        setToolTip(I18n.get(SBFBrowserStr.hintOperRowClone));
        setIcon16(SBFResources.BROWSER_ICONS.Paste16());
        setIcon24(SBFResources.BROWSER_ICONS.Paste());

    }

    @Override
    public boolean checkEnabled() {
        return super.checkEnabled() && (!getGrid().isReadOnly(true)
                && getGrid().getGrid().getSelectionModel().getSelectedItem() != null);
    }

    @Override
    protected void onExecute() {
        boolean cloneable = getGrid().isClonable();
        getGrid().setClonable(true);
        getGrid().insert();
        getGrid().setClonable(cloneable);
    }
}
