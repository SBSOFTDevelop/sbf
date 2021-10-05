package ru.sbsoft.client.components.browser.actions;

import ru.sbsoft.client.I18n;
import ru.sbsoft.client.components.grid.BaseGrid;
import ru.sbsoft.client.consts.SBFResources;
import ru.sbsoft.shared.api.i18n.consts.SBFBrowserStr;

/**
 * Отображает форму фильтра таблицы.
 * @author balandin
 */
public class GridShowFilterAction extends GridAction {

	public GridShowFilterAction(BaseGrid grid) {
		super(grid);
		setCaption(I18n.get(SBFBrowserStr.menuFilterGetData));
		setToolTip(I18n.get(SBFBrowserStr.hintFilterGetData));
		setIcon16(SBFResources.BROWSER_ICONS.Filter16());
		setIcon24(SBFResources.BROWSER_ICONS.Filter());
	}

	@Override
	public boolean checkEnabled() {
		// return getGrid().getFormFilter() != null;
        return false;
	}

	@Override
	protected void onExecute() {
		// getGrid().changeFilter();
	}
}