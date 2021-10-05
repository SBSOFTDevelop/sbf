package ru.sbsoft.client.components.browser.actions;

import ru.sbsoft.client.I18n;
import ru.sbsoft.client.components.grid.BaseGrid;
import ru.sbsoft.client.consts.SBFResources;
import ru.sbsoft.shared.api.i18n.consts.SBFBrowserStr;

/**
 * Ставит отметку на текущей записи. Доступно, если отметки явно разрешены для таблицы.
 * @see ru.sbsoft.client.components.grid.SystemGrid#setMarksAllowed(boolean) 
 * @author balandin
 * @since Oct 3, 2013 3:33:26 PM
 */
public class GridMarkAction extends GridAction {

    public GridMarkAction(BaseGrid grid) {
        super(grid);
        setCaption(I18n.get(SBFBrowserStr.menuCookCheck));
        setToolTip(I18n.get(SBFBrowserStr.hintCookCheck));
        setIcon16(SBFResources.BROWSER_ICONS.Pin16());
        setIcon24(SBFResources.BROWSER_ICONS.Pin());
    }

	@Override
	public boolean checkEnabled() {
		return isSingeleSelection() && getGrid().isMarksAllowed();
	}

    @Override
    protected void onExecute() {
        getGrid().invertMark(true);
    }
}
