package ru.sbsoft.client.components.browser.actions;

import ru.sbsoft.client.I18n;
import ru.sbsoft.client.components.grid.BaseGrid;
import ru.sbsoft.client.consts.SBFResources;
import ru.sbsoft.shared.api.i18n.consts.SBFBrowserStr;

/**
 * Отображает в режиме просмотра форму с данными выделенной записи таблицы.
 * @author balandin
 * @since Apr 17, 2014 3:37:48 PM
 */
public class GridShowAction extends GridAction {

	public GridShowAction(BaseGrid grid) {
		super(grid);
		setCaption(I18n.get(SBFBrowserStr.menuViewForm));
		setToolTip(I18n.get(SBFBrowserStr.hintViewForm));
		setIcon16(SBFResources.BROWSER_ICONS.QueryView16());
		setIcon24(SBFResources.BROWSER_ICONS.QueryView());
	}

	@Override
	public boolean checkEnabled() {
		return isSingeleSelection();
	}

	@Override
	protected void onExecute() {
		getGrid().viewForm(false);
	}
}
