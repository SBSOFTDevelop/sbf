package ru.sbsoft.client.components.grid.aggregate;

import com.google.gwt.user.client.ui.Widget;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import ru.sbsoft.client.I18n;
import ru.sbsoft.client.components.browser.filter.fields.HoldersList;
import ru.sbsoft.client.components.grid.GridUtils;
import ru.sbsoft.client.components.grid.SystemGrid;
import ru.sbsoft.client.components.grid.aggregate.operation.Avg;
import ru.sbsoft.client.components.grid.aggregate.operation.Max;
import ru.sbsoft.client.components.grid.aggregate.operation.Min;
import ru.sbsoft.client.components.grid.aggregate.operation.Sum;
import ru.sbsoft.client.components.grid.dlgbase.BaseUnitWindow;
import ru.sbsoft.client.components.grid.dlgbase.Group;
import ru.sbsoft.client.consts.SBFConst;
import ru.sbsoft.client.consts.SBFResources;
import ru.sbsoft.client.utils.ClientUtils;
import ru.sbsoft.client.utils.DefaultAsyncCallback;
import ru.sbsoft.model.PageFilterInfo;
import ru.sbsoft.shared.api.i18n.consts.SBFBrowserStr;
import ru.sbsoft.shared.meta.Row;
import ru.sbsoft.shared.meta.aggregate.IAggregateDef;
import ru.sbsoft.shared.services.FetchParams;

/**
 * Диалог вычисления агрегатных значений столбцов по выбранным (mаrked) записям
 * браузера данных. Позволяет вычислять {@link Max}, {@link Min}, {@link Sum} и
 * {@link Avg}
 *
 * @author Kiselev
 */
public class AggregateWindow extends BaseUnitWindow<Group> {

    private final static List<IAggregate> OPERATIONS = new ArrayList<IAggregate>();
    private final static AggregateHoldersListFactory HOLDERS_FACTORY;

    static {
        OPERATIONS.add(new Max());
        OPERATIONS.add(new Min());
        OPERATIONS.add(new Sum());
        OPERATIONS.add(new Avg());
        HOLDERS_FACTORY = new AggregateHoldersListFactory(OPERATIONS);
    }

    public static boolean isApplicable(SystemGrid grid) {
        return grid.isMarksAllowed() && HOLDERS_FACTORY.hasColumnMatch(grid);
    }

    private final SystemGrid<Row> grid;
    private final HoldersList holdersList;
    private final CountAggregateItem countUnit = new CountAggregateItem();
    private List<BigDecimal> selIds = null;

    public AggregateWindow(SystemGrid<Row> grid) {
        super(new Group());
        DEFAULT_WINDOW_WIDTH = 600;
        getHeader().setIcon(SBFResources.BROWSER_ICONS.Statistics16());
        getHeader().setText(I18n.get(SBFBrowserStr.menuAggregates));

        this.grid = grid;
        this.holdersList = HOLDERS_FACTORY.create(grid);
        addStandardActions(ADD_ITEM, DEL);
        rootGroup.addUnit(countUnit);
    }

    @Override
    protected void apply() {
        this.mask();
        List<IAggregateDef> queries = new ArrayList<IAggregateDef>();
        for (AggregateItem item : rootGroup.getUnits(AggregateItem.class, true)) {
            item.addQuery(queries);
        }
        PageFilterInfo fetchParams = grid.getFetchParams(new FetchParams());
        SBFConst.GRID_SERVICE.getAggregates(GridUtils.getContext(grid), fetchParams, selIds, queries, new DefaultAsyncCallback<Map<String, ?>>(this) {
            @Override
            public void onResult(Map<String, ?> result) {
                for (AggregateItem item : rootGroup.getUnits(AggregateItem.class, true)) {
                    item.readQueryResult(result);
                }
            }
        });
    }

    @Override
    public void show(final Widget parent) {
        selIds = grid.getMarkedRecords();
        if (selIds != null && !selIds.isEmpty()) {
            rootGroup.clearValue();
            countUnit.setCount(selIds.size());
            AggregateWindow.super.show(parent);
        } else {
            ClientUtils.alertWarning(I18n.get(SBFBrowserStr.msgAggregatesNothingSelected));
        }
    }

    @Override
    protected AggregateItem createItem() {
        return tuneItem(new AggregateItem(holdersList, OPERATIONS));
    }
}
