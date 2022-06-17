package ru.sbsoft.client.components.grid;

import ru.sbsoft.svc.core.client.ValueProvider;
import ru.sbsoft.svc.widget.core.client.grid.ColumnConfig;
import java.math.BigDecimal;
import java.util.Set;
import ru.sbsoft.client.components.actions.Action;
import ru.sbsoft.client.components.browser.actions.GridExportAction;
import ru.sbsoft.client.components.operation.ExportOperationMaker;
import ru.sbsoft.common.Strings;
import ru.sbsoft.shared.CommonOperationEnum;
import ru.sbsoft.shared.FormContext;
import ru.sbsoft.shared.GridContext;
import ru.sbsoft.shared.interfaces.GridType;
import ru.sbsoft.shared.interfaces.NamedGridType;
import ru.sbsoft.shared.interfaces.OperationType;
import ru.sbsoft.shared.meta.IColumn;
import ru.sbsoft.shared.meta.Row;
import ru.sbsoft.shared.model.operation.OperationCommand;

/**
 * Базовый класс всех таблиц в приложении.
 *
 * @author balandin
 * @since Apr 23, 2014 11:19:04 AM
 */
public class ContextGrid extends BaseGrid<Row> {

    private GridExportAction exportAction;
    private final GridContext gridContext;

    public ContextGrid(GridType gridType) {
        this(gridType, null);
    }

    public ContextGrid(GridType gridType, String context) {
        this(new GridContext(gridType, context));
    }

    protected ContextGrid(GridContext gc) {
        gc.setParentFilters(parentFilters);
        this.gridContext = gc;
    }

    @Override
    protected final GridContext getGridContext() {
        return gridContext;
    }

    @Override
    protected String getTableName() {
        return gridContext.getGridType().getCode();
    }
    
    @Override
    protected String getSecurityName() {
        if (gridContext.getGridType() instanceof NamedGridType) {
            return ((NamedGridType)gridContext.getGridType()).getSecurityId();
        }
        return gridContext.getGridType().getCode();
    }

    @Override
    protected FormContext getFormContext(Row model) {
        return null;
    }

    @Override
    public Action getExportAction() {
        if (exportAction == null) {
            exportAction = new GridExportAction(this, new ExportOperationMaker(this));
        }
        return exportAction;
    }

    @Override
    public OperationCommand createGridOperationCommand(OperationType type) {
        final OperationCommand command = super.createGridOperationCommand(type);
        if (type == CommonOperationEnum.EXPORT) {
            final String keyColumnAlias = getMetaInfo().getPrimaryKey().getAlias();
            final String markColumnAlias = getKeyProvider().getPath();
            if (!Strings.equals(keyColumnAlias, markColumnAlias)) {
                command.getGridContext().getPageFilterInfo().setColumnName(markColumnAlias);
            }
        }
        return command;
    }

    @Override
    protected ValueProvider<Row, BigDecimal> createKeyProvider() {
        if (getMetaInfo() != null) {
            final IColumn temporalKeyColumn = getMetaInfo().getTemporalKey();
            if (temporalKeyColumn != null) {
                final ColumnConfig columnConfig = (ColumnConfig) temporalKeyColumn.getData(IColumn.COLUMN_CONFIG_PREFIX);
                return columnConfig.getValueProvider();
            }
        }
        return super.createKeyProvider();
    }

    public <E extends Enum<?>> boolean isMode(E... modes) {
        Set modifiers = getGridContext().getModifiers().getItems();
        if (modifiers != null) {
            for (E mode : modes) {
                if (modifiers.contains(mode)) {
                    return true;
                }
            }
        }
        return false;
    }
}
