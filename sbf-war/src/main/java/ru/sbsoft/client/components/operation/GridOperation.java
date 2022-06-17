package ru.sbsoft.client.components.operation;

import ru.sbsoft.client.I18n;
import ru.sbsoft.client.components.grid.BaseGrid;
import ru.sbsoft.client.consts.SBFConst;
import ru.sbsoft.client.utils.DefaultAsyncCallback;
import ru.sbsoft.shared.interfaces.OperationType;
import ru.sbsoft.shared.meta.BigDecimalListWrapper;
import ru.sbsoft.shared.model.operation.OperationCommand;
import ru.sbsoft.shared.model.operation.OperationInfo;

/**
 * Операция вызываемая из таблицы и, как правило, связанная с ее данными.
 * Для формирования команды операции обращается к таблице {@link BaseGrid#createGridOperationCommand(ru.sbsoft.shared.interfaces.OperationType)}.
 * @author balandin
 * @since May 24, 2013 5:00:42 PM
 */
public class GridOperation extends AbstractOperation {

    private final BaseGrid grid;
    private final OperationType type;
    //
    private boolean reloadOnComplete = true;
    private boolean unmarkOnComplete = true;

    public GridOperation(BaseGrid grid, OperationType type) {
        this.grid = grid;
        this.type = type;
    }

    @Override
    protected OperationCommand createOperationCommand() {
        return grid.createGridOperationCommand(type);
    }

    public static GridOperation create(BaseGrid grid, OperationType type, BaseOperationParamForm paramForm) {
        paramForm.setHeading(I18n.get(type.getTitle()));
        final GridOperation operation = new GridOperation(grid, type);
        operation.setParamWindow(paramForm);
        return operation;
    }

    @Override
    public void onCompleted() {
        grid.refreshChildGrids();
    }

    @Override
    public void onClose(Long operationId) {
        if (unmarkOnComplete) {
            grid.deleteAllMarks();
        }

        SBFConst.MULTI_OPERATION_SERVICE.getOperationParameter(operationId, OperationInfo.FINISH_MARKS, new DefaultAsyncCallback<BigDecimalListWrapper>() {
            @Override
            public void onResult(BigDecimalListWrapper result) {
                if (result != null) {
                    grid.setMarkedRecords(result.getValue());
                }
            }
        });

        if (reloadOnComplete) {
            grid.reload();
        }
    }

    public boolean isReloadOnComplete() {
        return reloadOnComplete;
    }

    public GridOperation setReloadOnComplete(boolean reloadOnComplete) {
        this.reloadOnComplete = reloadOnComplete;
        return this;
    }

    public boolean isUnmarkOnComplete() {
        return unmarkOnComplete;
    }

    public GridOperation setUnmarkOnComplete(boolean unmarkOnComplete) {
        this.unmarkOnComplete = unmarkOnComplete;
        return this;
    }
}
