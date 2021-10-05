package ru.sbsoft.client.components.browser.actions;

import ru.sbsoft.client.I18n;
import ru.sbsoft.client.components.grid.BaseGrid;
import ru.sbsoft.client.consts.SBFResources;
import ru.sbsoft.shared.api.i18n.consts.SBFBrowserStr;

/**
 * Инвертирует все отметки н записях в таблице.
 * @see GridMarkAction
 * @author balandin
 * @since Oct 3, 2013 3:36:22 PM
 */
public class GridMarkInverseAllAction extends GridAction {

    public GridMarkInverseAllAction(BaseGrid grid) {
        super(grid);
        setCaption(I18n.get(SBFBrowserStr.menuCookInvertor));
        setToolTip(I18n.get(SBFBrowserStr.hintCookInvertor));
        setIcon16(SBFResources.BROWSER_ICONS.PinInvertor16());
        setIcon24(SBFResources.BROWSER_ICONS.PinInvertor());
    }

	@Override
	public boolean checkEnabled() {
		return getGrid().isMarksAllowed();
	}

    @Override
    protected void onExecute() {
        getGrid().invertAllMarks();
    }
}
