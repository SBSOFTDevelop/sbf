package ru.sbsoft.client.components.browser.actions;

import ru.sbsoft.client.I18n;
import ru.sbsoft.client.components.grid.BaseGrid;
import ru.sbsoft.client.consts.SBFResources;
import ru.sbsoft.shared.api.i18n.consts.SBFBrowserStr;

/**
 * Запускает операцию создания новой записи в таблице.
 * @author balandin
 * @since Oct 3, 2013 4:21:05 PM
 */
public class GridInsertAction extends GridAction {

    public GridInsertAction(BaseGrid grid) {
        super(grid);
        super.setCaption(I18n.get(SBFBrowserStr.menuOperRowInsert));
        super.setToolTip(I18n.get(SBFBrowserStr.hintOperRowInsert));
        super.setIcon16(SBFResources.BROWSER_ICONS.RowInsert16());
        super.setIcon24(SBFResources.BROWSER_ICONS.RowInsert());
    }

    @Override
    public boolean checkEnabled() {
        return super.checkEnabled() && (!getGrid().isReadOnly(true));
    }

    @Override
    protected void onExecute() {
        getGrid().insert();
    }
}
