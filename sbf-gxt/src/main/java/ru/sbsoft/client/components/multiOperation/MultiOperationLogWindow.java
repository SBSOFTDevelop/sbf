package ru.sbsoft.client.components.multiOperation;

import java.util.HashMap;
import java.util.Map;
import ru.sbsoft.client.components.multiOperation.MultiOperationMonitor.SimpleConcreteOperationMonitor;
import ru.sbsoft.client.components.operation.AbstractOperation;
import ru.sbsoft.client.components.operation.BaseOperationProgressForm;
import ru.sbsoft.client.consts.SBFConst;
import ru.sbsoft.client.utils.DefaultAsyncCallback;
import ru.sbsoft.shared.consts.SBFGridEnum;
import ru.sbsoft.shared.filter.LongFilterInfo;
import ru.sbsoft.shared.model.operation.OperationCommand;
import ru.sbsoft.shared.model.OperationEventType;
import ru.sbsoft.shared.model.enums.MultiOperationStatus;
import ru.sbsoft.shared.model.operation.OperationInfo;

/**
 * Журнал выполнения операции.
 *
 * @author Fedor Resnyanskiy, SBSOFT
 */
public final class MultiOperationLogWindow extends BaseOperationProgressForm {

    private static Map<Long, MultiOperationLogWindow> windows = new HashMap<Long, MultiOperationLogWindow>();
    private final Long operationId;

    /**
     * Конструктор журнала выполнения операции.
     *
     * @param operationId идентификатор операции.
     * @param operation все сложно; нужна, чтобы можно было закрыть окна
     * операции. Может быть null.
     */
    private MultiOperationLogWindow(final Long operationId, AbstractOperation operation) {
        super(operation != null ? operation : new MultiOperation(), "");
        this.operationId = operationId;
        setModal(false);
        setClosable(true);
        MultiOperationMonitor.getInstance().addHandler(new LogUpdateHandler(operationId));
        addHideHandler(new WindowHideHandler() {
            @Override
            public void onHide(WindowHideEvent event) {
                windows.remove(operationId);

            }
        });
    }

    @Override
    public long getOperationId() {
        return operationId;
    }

    /**
     * Возвращает окно журнала операции.
     *
     * @param operationId идентификатор операции для отображения журнала.
     * @param operation
     * @return
     */
    public static MultiOperationLogWindow forOperation(Long operationId, AbstractOperation operation) {
        if (!windows.containsKey(operationId)) {
            windows.put(operationId, createNewOperationLogWindow(operationId, operation));
        }
        return windows.get(operationId);
    }

    /**
     * Возвращает окно журнала операции.
     *
     * @param operationId идентификатор операции для отображения журнала.
     * @return
     */
    public static MultiOperationLogWindow forOperation(Long operationId) {
        return forOperation(operationId, null);
    }

    /**
     * Возвращает признак, открыто ли окно с журналом операции.
     *
     * @param operationId идентификатор операции для проверки.
     * @return признак, открыто ли окно с журналом операции.
     */
    public static boolean isWindowOpened(Long operationId) {
        return windows.containsKey(operationId);
    }

    private static MultiOperationLogWindow createNewOperationLogWindow(Long operationId, AbstractOperation operation) {
        final MultiOperationLogWindow window = new MultiOperationLogWindow(operationId, operation);
        //получить заголовок
        SBFConst.MULTI_OPERATION_SERVICE.getOperationInfo(operationId, new DefaultAsyncCallback<OperationInfo>() {
            @Override
            public void onResult(OperationInfo info) {
                window.setHeading(info.getTitle());
            }
        });
        return window;
    }

    @Override
    protected SBFGridEnum getLogGridType() {
        return SBFGridEnum.SR_MULTI_OPERATION_LOG;
    }

    @Override
    protected void onAfterFirstAttach() {
        super.onAfterFirstAttach();
        loggingBrowser.setHeaderVisible(false);
        loggingGrid.setParentFilter(new LongFilterInfo("OPERATION_RECORD_ID", operationId));
    }

    @Override
    protected boolean hasMenu() {
        return false;
    }

    public void show(final MultiOperationStatus status) {
        show();
    }

    public void show(boolean isUpdate) {
        if (isUpdate) {
            show();
        } else {
            super.show();
            loggingGrid.tryReload();
        }
    }

    @Override
    public void show() {
        super.show();

        MultiOperationMonitor mm = MultiOperationMonitor.getInstance();
        mm.forceUpdate();

    }

    private static class MultiOperation extends AbstractOperation {

//        private final Long operationId;
//
//        public MultiOperation(Long operationId) {
//            this.operationId = operationId;
//        }
        @Override
        protected OperationCommand createOperationCommand() {
            return new OperationCommand();
        }

        @Override
        public void onCompleted() {
            super.onCompleted();
        }

        @Override
        public void onClose(Long operationId) {
            super.onClose(operationId);
        }

    }

    private class LogUpdateHandler extends SimpleConcreteOperationMonitor {

        public LogUpdateHandler(Long operationId) {
            super(operationId);
        }

        @Override
        public void onUpdateOperation(OperationInfo operationInfo) {
            loggingGrid.tryReload();
            MultiOperationStatus status = operationInfo.getStatus();

            if (status == MultiOperationStatus.DONE) {
                resultEventType = OperationEventType.FINISH_OK;
            }

        }
    }

}
