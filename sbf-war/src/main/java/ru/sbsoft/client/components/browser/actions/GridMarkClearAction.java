package ru.sbsoft.client.components.browser.actions;

import ru.sbsoft.client.I18n;
import ru.sbsoft.client.components.grid.BaseGrid;
import ru.sbsoft.client.consts.SBFResources;
import ru.sbsoft.shared.api.i18n.consts.SBFBrowserStr;

/**
 * Удаляет все отметки н записях в таблице.
 * @see GridMarkAction
 * @author balandin
 * @since Oct 3, 2013 3:34:44 PM
 */
public class GridMarkClearAction extends GridAction {

	public GridMarkClearAction(BaseGrid grid) {
		super(grid);
		setCaption(I18n.get(SBFBrowserStr.menuCookClear));
		setToolTip(I18n.get(SBFBrowserStr.hintCookClear));
		setIcon16(SBFResources.BROWSER_ICONS.PinClear16());
		setIcon24(SBFResources.BROWSER_ICONS.PinClear());
	}

	@Override
	public boolean checkEnabled() {
		return getGrid().hasMarkedRecords();
	}

	@Override
	protected void onExecute() {
		getGrid().deleteAllMarks();
	}
}
