package ru.sbsoft.client.components.browser.actions;

import ru.sbsoft.client.I18n;
import ru.sbsoft.client.components.grid.BaseGrid;
import ru.sbsoft.client.consts.SBFResources;
import ru.sbsoft.shared.api.i18n.consts.SBFBrowserStr;

/**
 * Отображает в режиме редактирования (при наличии прав) форму с данными
 * выделенной записи таблицы.
 *
 * @author balandin
 * @since Oct 3, 2013 4:21:33 PM
 */
public class GridUpdateAction extends GridAction {

    private static final String TIP_EDIT = I18n.get(SBFBrowserStr.hintOperRowUpdate);
    private static final String TIP_VIEW = I18n.get(SBFBrowserStr.hintViewForm);

    private boolean gridReadonly = false;

    public GridUpdateAction(BaseGrid grid) {
        super(grid);
        setCaption(I18n.get(SBFBrowserStr.menuOperRowUpdate));
        setToolTip(TIP_EDIT);
        setIcon16(SBFResources.BROWSER_ICONS.RowUpdate16());
        setIcon24(SBFResources.BROWSER_ICONS.RowUpdate());
    }

    @Override
    public boolean checkEnabled() {
        return isSingeleSelection();
    }

    @Override
    public void checkState() {
        super.checkState();
        if (getGrid().isReadOnly(true) != gridReadonly) {
            gridReadonly = !gridReadonly;
            setToolTip(gridReadonly ? TIP_VIEW : TIP_EDIT);
        }
    }

    @Override
    protected void onExecute() {
        getGrid().edit();
    }
}
