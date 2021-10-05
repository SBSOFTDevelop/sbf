package ru.sbsoft.client.components.multiOperation;

import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import ru.sbsoft.client.consts.SBFConst;
import ru.sbsoft.shared.model.enums.MultiOperationStatus;
import ru.sbsoft.shared.model.operation.OperationInfo;
import ru.sbsoft.shared.model.operation.OperationsSelectFilter;
import ru.sbsoft.shared.services.IMultiOpearationServiceAsync;

/**
 * Клиентский монитор операций. Периодически опрашивет сервер на наличие новых
 * операций или на изменения в старых. При отсутствии изменений период опроса
 * увеличивается, пока не достигнет опеределенного лимита. Синглетон, для
 * доступа используйте {@link getInstance()}.
 *
 * @author Fedor Resnyanskiy, SBSOFT
 */
public class MultiOperationMonitor {

    private static final MultiOperationMonitor INSTANCE = new MultiOperationMonitor();
    private static final DateTimeFormat truncFormat = DateTimeFormat.getFormat(DateTimeFormat.PredefinedFormat.DATE_FULL);
    //
    private final Map<Long, OperationInfo> operations = new HashMap<Long, OperationInfo>();
    private final List<AbstractMultiOperationMonitorHandler> handlerList = new ArrayList<AbstractMultiOperationMonitorHandler>();
    //
    private final MonitorTimer monitorTimer = new MonitorTimer();
    private final AsyncCallback<List<OperationInfo>> updateCallback = new UpdateCallback(monitorTimer);

    private MultiOperationMonitor() {
        monitorTimer.scheduleRepeating(1000);
    }

    /**
     * Возвращает экземпляр монитора операций в этом приложении.
     *
     * @return экземпляр монитора операций в этом приложении.
     */
    public static MultiOperationMonitor getInstance() {
        return INSTANCE;
    }

    /**
     * Добавляет обработчик операций к монитору.
     *
     * @param handler добавляемый обработчик.
     * @return упраляющий обработчиком объект.
     */
    public MultiOperationMonitorHandlerHolder addHandler(AbstractMultiOperationMonitorHandler handler) {
        handlerList.add(handler);
        return new MultiOperationMonitorHandlerHolder(handler);
    }

    /**
     * Обновляет информацию об операциях с сервера.
     */
    public void update() {

        monitorTimer.update();

    }

    /**
     * Принудительно обновляет информацию с сервера. При этом сбрасывается
     * период опроса.
     */
    public void forceUpdate() {
       
        monitorTimer.reset();
        monitorTimer.update();
       
    }

    //<editor-fold defaultstate="collapsed" desc="Classes">
    /**
     * Обработчик операции по-умолчанию.
     */
    public static abstract class AbstractMultiOperationMonitorHandler {

        /**
         * При обновлении информации об операции с сервера. При этом информация
         * не обязательно должна была измениться с предыдущего обновления.
         *
         * @param operationList список операций, полученных с сервера.
         */
        public void onUpdate(List<OperationInfo> operationList) {
        }

        /**
         * С сервера получена информация об операции, которой раньше не было на
         * клиенте.
         *
         * @param operationInfo информация о новой операции.
         */
        public void onNew(OperationInfo operationInfo) {
        }

        /**
         * Информация об операции уже была загружена на клиент, но с тех пор
         * поменялась.
         *
         * @param operationInfo информация об измененной операции.
         */
        public void onChange(OperationInfo operationInfo) {
        }

        /**
         * Операция был удалена с сервера.
         *
         * @param operationInfo информация об удаленной операции.
         */
        public void onDelete(OperationInfo operationInfo) {
        }
    }

    public static abstract class SimpleConcreteOperationMonitor extends AbstractMultiOperationMonitorHandler {

        private final Long operationId;

        public SimpleConcreteOperationMonitor(Long operationId) {
            this.operationId = operationId;
        }

        public boolean isThisOperation(OperationInfo info) {
            return info.getId().equals(operationId);
        }

        @Override
        public final void onNew(OperationInfo operationInfo) {
            if (isThisOperation(operationInfo)) {
                onNewOperation(operationInfo);
            }
        }

        public void onNewOperation(OperationInfo operationInfo) {
        }

        @Override
        public final void onChange(OperationInfo operationInfo) {
            if (isThisOperation(operationInfo)) {
                onChangeOperation(operationInfo);
            }
        }

        public void onChangeOperation(OperationInfo operationInfo) {
        }

        @Override
        public final void onDelete(OperationInfo operationInfo) {
            if (isThisOperation(operationInfo)) {
                onDeleteOperation(operationInfo);
            }
        }

        public void onDeleteOperation(OperationInfo operationInfo) {
        }

        @Override
        public final void onUpdate(List<OperationInfo> operationList) {
            for (OperationInfo operationInfo : operationList) {
                if (isThisOperation(operationInfo)) {
                    onUpdateOperation(operationInfo);
                }
            }
        }

        public void onUpdateOperation(OperationInfo operationInfo) {

        }

    }

    /**
     * Класс, управляющий созданным обработчиком операций.
     */
    public class MultiOperationMonitorHandlerHolder {

        private final AbstractMultiOperationMonitorHandler handler;

        public MultiOperationMonitorHandlerHolder(AbstractMultiOperationMonitorHandler handler) {
            this.handler = handler;
        }

        public void remove() {
            handlerList.remove(handler);
        }
    }

    /**
     * Планировщик обновления информации о состоянии операций. После запуска или
     * обнаружения изменений планировщик начинает активно опрашивать сервер на
     * наличие изменений в операциях. Если новых изменений не обнаружено, период
     * между опросами увеличивается впоть до установленного предела (maxPeriod).
     */
    private class MonitorTimer extends Timer {

        private long maxPeriod = 60000L;
        private long lastRun = 0L;
        private long period = 0L;
       

        public MonitorTimer() {

        }

        @Override
        public void run() {
            if (lastRun + period < new Date().getTime()) {
                update();
                lastRun = new Date().getTime();
            }
        }

        public void update() {
            IMultiOpearationServiceAsync multiOperationService = SBFConst.MULTI_OPERATION_SERVICE;

            OperationsSelectFilter filter = createFilter();
            multiOperationService.listCurrentUserOperations(filter, updateCallback);

            incrementPeriod();
        }

        public void reset() {
            period = 0L;
        }

        private void incrementPeriod() {
            if (period < 10000) {
                period += 1000;
            }
            if (period < maxPeriod) {
                period = new BigDecimal(period).multiply(new BigDecimal(1.5)).longValue();
            }
        }

    }
    //</editor-fold>

    /**
     * Формирует фильтр для выборки целевых операций для пользователя.
     *
     * @return
     */
    public static OperationsSelectFilter createFilter() {
        final OperationsSelectFilter filter = new OperationsSelectFilter();
        filter.setPeriodStart(truncDate(new Date(new Date().getTime())));
        filter.setShowHidden(false);
        filter.setModuleCode(GWT.getModuleName());
        return filter;
    }

    private static Date truncDate(Date date) {
        return truncFormat.parse(truncFormat.format(date));
    }

    /**
     * Оповещатель обработчиков об обновлении состояния операции.
     */
    private class UpdateCallback implements AsyncCallback<List<OperationInfo>> {

        private final MonitorTimer monitorTimer;

        private UpdateCallback(MonitorTimer monitorTimer) {
            this.monitorTimer = monitorTimer;
        }

        @Override
        public void onFailure(Throwable caught) {
            GWT.log("Cannot update operation info", caught);
        }

        @Override
        public void onSuccess(List<OperationInfo> result) {
            boolean runningExists = false;

            if ((result == null || result.isEmpty()) && operations.isEmpty()) {
                return;
            }

            for (AbstractMultiOperationMonitorHandler handler : handlerList) {
                handler.onUpdate(result);
            }

            Map<Long, OperationInfo> resultMap = new HashMap<Long, OperationInfo>();
            for (OperationInfo o : result) {
                if (!operations.containsKey(o.getId())) {
                    for (AbstractMultiOperationMonitorHandler handler : handlerList) {
                        handler.onNew(o);
                    }
                } else {
                    final OperationInfo operation = operations.get(o.getId());
                    if (operation.getStatus() != o.getStatus()) {
                        for (AbstractMultiOperationMonitorHandler handler : handlerList) {
                            handler.onChange(o);
                        }
                    }
                }
                operations.put(o.getId(), o);
                resultMap.put(o.getId(), o);

                if (o.getStatus() == MultiOperationStatus.STARTED || o.getStatus() == MultiOperationStatus.CANCEL) {
                    runningExists = true;
                } else if (o.getStatus() == MultiOperationStatus.READY_TO_START && MultiOperationLogWindow.isWindowOpened(o.getId())) {
                    runningExists = true;
                }
            }

            List<Long> toRemove = new ArrayList<Long>();
            for (Long operationId : operations.keySet()) {
                if (!resultMap.containsKey(operationId)) {
                    toRemove.add(operationId);
                }
            }
            for (Long removingOperationId : toRemove) {
                OperationInfo removed = operations.remove(removingOperationId);
                for (AbstractMultiOperationMonitorHandler handler : handlerList) {
                    handler.onDelete(removed);
                }
            }

            if (runningExists) {
                monitorTimer.reset();
            }
        }

    };

}
