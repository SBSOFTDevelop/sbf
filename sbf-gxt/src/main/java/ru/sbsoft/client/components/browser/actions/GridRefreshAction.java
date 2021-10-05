package ru.sbsoft.client.components.browser.actions;

import ru.sbsoft.client.I18n;
import ru.sbsoft.client.components.grid.BaseGrid;
import ru.sbsoft.client.consts.SBFResources;
import ru.sbsoft.client.utils.ClientUtils;
import ru.sbsoft.shared.meta.Column;
import ru.sbsoft.shared.meta.Row;
import ru.sbsoft.shared.model.MarkModel;
import ru.sbsoft.shared.api.i18n.consts.SBFBrowserStr;

/**
 * Действие пункта меню, запускающее операцию обновления таблицы.
 *
 * @author balandin
 */
public class GridRefreshAction extends GridAction {

    public GridRefreshAction(BaseGrid grid) {
        super(grid);
        super.setCaption(I18n.get(SBFBrowserStr.menuFileRefresh));
        super.setToolTip(I18n.get(SBFBrowserStr.hintFileRefresh));
        super.setIcon16(SBFResources.BROWSER_ICONS.Refresh16());
        super.setIcon24(SBFResources.BROWSER_ICONS.Refresh());
    }

    @Override
    protected void onExecute() {
        final boolean wasCtrlKey = getGrid().getActionManager().wasCtrlKey();
        final boolean wasAltKey = getGrid().getActionManager().wasAltKey();
        final MarkModel model = getGrid().getCurrentRecord();

        if (model != null && wasCtrlKey && wasAltKey && model instanceof Row) {

            StringBuilder s = new StringBuilder("<br><br><br><br>");

            final Row row = (Row) model;
            for (Column column : row.getColumns().getColumns()) {
                s.append(column.getAlias()).append(" / ");
                s.append(column.getType().name()).append(" / ");
                final Object value = row.getValue(column.getAlias());
                if (value == null) {
                    s.append("NULL");
                    continue;
                }
                s.append(value.getClass().getName()).append(" / ");
                s.append(value).append("<br>");
            }
            s.append("<br><br>");

            ClientUtils.message("Row content", s.toString());

        }

        if (wasCtrlKey && model != null) {
            getGrid().refreshRow(model.getRECORD_ID());
        } else {
            getGrid().reload();
        }
    }
}
