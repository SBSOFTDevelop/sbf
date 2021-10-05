package ru.sbsoft.client.components.browser.actions;

import ru.sbsoft.client.I18n;
import ru.sbsoft.client.components.grid.BaseGrid;
import ru.sbsoft.client.components.operation.ExportOperationMaker;
import ru.sbsoft.client.consts.SBFResources;
import ru.sbsoft.shared.api.i18n.consts.SBFBrowserStr;

/**
 * Запускает операцию создания файла электронной таблицы по текущим данным таблицы.
 * @author balandin
 * @since Oct 3, 2013 3:39:30 PM
 */
public class GridExportAction extends GridOperationAction {

	public GridExportAction(BaseGrid grid, ExportOperationMaker operationMaker) {
		super(grid, operationMaker);
		setCaption(I18n.get(SBFBrowserStr.menuFileExportData));
		setToolTip(I18n.get(SBFBrowserStr.hintFileExportData));
		setIcon16(SBFResources.BROWSER_ICONS.Export16());
		setIcon24(SBFResources.BROWSER_ICONS.Export());
	}
}
