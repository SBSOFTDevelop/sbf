package ru.sbsoft.client.components.treebrowser;

import ru.sbsoft.client.I18n;
import ru.sbsoft.client.consts.SBFResources;
import ru.sbsoft.shared.api.i18n.consts.SBFBrowserStr;

/**
 *
 * @author sokolov
 */
public class TreeGridCloneAction extends TreeGridAction {
    private final EditableTreeGrid grid;

    public TreeGridCloneAction(EditableTreeGrid grid) {
        super(grid);
        this.grid = grid;
        super.setCaption(I18n.get(SBFBrowserStr.menuOperRowClone));
        super.setToolTip(I18n.get(SBFBrowserStr.hintOperRowClone));
        super.setIcon16(SBFResources.BROWSER_ICONS.Paste16());
        super.setIcon24(SBFResources.BROWSER_ICONS.Paste());
    }

    @Override
    public boolean checkEnabled() {
        return super.checkEnabled() && (!getGrid().isReadOnly()) && grid.getSelectedModel() != null;
    }

    @Override
    protected void onExecute() {
        boolean clonSave = grid.isCloneable();
        grid.setCloneable(true);
        getGrid().insert();
        grid.setCloneable(clonSave);
    }

}
