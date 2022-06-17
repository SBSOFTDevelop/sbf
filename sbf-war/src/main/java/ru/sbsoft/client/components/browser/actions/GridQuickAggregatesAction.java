package ru.sbsoft.client.components.browser.actions;

import ru.sbsoft.client.I18n;
import ru.sbsoft.client.components.grid.BaseGrid;
import ru.sbsoft.client.components.grid.aggregate.AggregateWindow;
import ru.sbsoft.client.consts.SBFResources;
import ru.sbsoft.shared.api.i18n.consts.SBFBrowserStr;

/**
 *
 * @author Kiselev
 */
public class GridQuickAggregatesAction extends GridAction {

    public GridQuickAggregatesAction(BaseGrid grid) {
        super(grid);
        setCaption(I18n.get(SBFBrowserStr.menuAggregates));
        setToolTip(I18n.get(SBFBrowserStr.hintAggregates));
        setIcon16(SBFResources.BROWSER_ICONS.Statistics16());
        setIcon24(SBFResources.BROWSER_ICONS.Statistics());
    }

    @Override
    protected void onExecute() {
        getGrid().showQuickAggregate();
    }

    @Override
    public boolean checkEnabled() {
        return super.checkEnabled() && getGrid().hasMarkedRecords() &&  AggregateWindow.isApplicable(getGrid());
    }

}
