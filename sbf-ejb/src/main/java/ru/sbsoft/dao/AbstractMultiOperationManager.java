package ru.sbsoft.dao;

import ru.sbsoft.shared.model.operation.SchedulerContext;
import ru.sbsoft.shared.model.operation.NoSuchOperationCodeException;
import ru.sbsoft.shared.model.operation.OperationInfo;
import ru.sbsoft.shared.model.operation.OperationException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import javax.annotation.Resource;
import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.sbsoft.dao.operations.IMultiOperationDao;
import ru.sbsoft.operation.ExceptionHelper;
import ru.sbsoft.processor.IOperationProcessor;
import ru.sbsoft.processor.ServerOperationContext;
import ru.sbsoft.processor.OperationLogger;
import ru.sbsoft.shared.OperationObject;
import ru.sbsoft.shared.api.i18n.Ii18nDao;
import ru.sbsoft.shared.exceptions.ApplicationException;
import ru.sbsoft.shared.exceptions.ThrowableWrapper;
import ru.sbsoft.shared.model.OperationEvent;
import ru.sbsoft.shared.model.OperationEventType;
import ru.sbsoft.shared.model.enums.MultiOperationStatus;
import static ru.sbsoft.shared.model.enums.MultiOperationStatus.*;

/**
 * Компонент для серверного запуска операций.
 *
 * @author Fedor Resnyanskiy, SBSOFT
 */
@PermitAll
public abstract class AbstractMultiOperationManager implements IMultiOperationManagerDao {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractMultiOperationManager.class);
    // private static final String OPERATION_LOCK_STRING = "OPERATION";
    /**
     * Минимальная задержка между запусками новых операций.
     */
    // private static final SystemProperty START_OPERATION_DELAY = new SystemProperty("ru.sbsoft.operations.startdelay", "1");
    /**
     * Минимальная задержка между проверками целостности операций.
     */
    //  private static final SystemProperty REPAIR_OPERATION_DELAY = new SystemProperty("ru.sbsoft.operations.repairdelay", "300");

    private static final Map<Long, IOperationProcessor> runningProcessors = new ConcurrentHashMap<>();
    //private static Long lastStartRun;
    //  private static Long lastRepairRun;

    @EJB
    private IMultiOperationDao operationDao;

    @EJB
    private Ii18nDao i18nDao;

    // @EJB
    // private IMultiOperationManagerDao thisBean;
    //  @EJB
    //  private ILockDao lockDao;
    @EJB
    protected IUtilEJB utilEJB;

    @Resource
    private javax.ejb.SessionContext sc;

    /**
     * Производит поиск подготовленных к запуску операций и запускает их, если
     * необходимо.
     */
    //<editor-fold defaultstate="collapsed" desc="startOperation">
    /**
     * Метод для срабатывания планировщика. Его назначение - быть точкой входа и
     * перехватывать исключения, иначе планировщик удалится.
     */
    // @Schedule(hour = "*", minute = "*", second = "*/5", persistent = false)
    /*
    public void scheduler() {
        LOGGER.trace("startOperationScheduler invoked");
        try {


            utilEJB.runNotInTransaction(() -> startOperationIteration());

        } catch (Exception ex) {
            LOGGER.error("startOperationIteration execution error", ex);
        }
    }
     */
    /**
     * Получает все новые операции и запускает каждую из них в асинхронном
     * режиме.
     */
    //@Override
    /*
    private void startOperationIteration() {
        if (!operationsCanStart()) {
            return;
        }

        final List<Long> operationsToStart = listReadyToStartOperations();
        LOGGER.trace("got " + operationsToStart.size() + " to execute");
        for (final Long operationId : operationsToStart) {
            //thisBean.lockAndStartOperation(operationId);
            utilEJB.runInTransactionAsync(() -> lockAndStartOperation(operationId));
        }
    }

    private boolean operationsCanStart() {
        long now = new Date().getTime();
        if (lastStartRun == null || lastStartRun <= now - START_OPERATION_DELAY.getParameterLongValue() * 1000) {
            lastStartRun = now;
            return true;
        }
        return false;
    }
     */
    /**
     * Асинхронный старт операции. Для того, чтобы можно было найти
     * незавершенные операции после системной ошибки, создается блокировка на
     * уровне БД, снимаемая по завершению текущей транзации.
     *
     * @param operationId
     */
    //  @Override
    //  @Asynchronous
    //   @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    /*
    private void lockAndStartOperation(Long operationId) {
        try {
            lockDao.lock(OPERATION_LOCK_STRING, new BigDecimal(operationId));
        } catch (PessimisticLockException ex) {
            //уже выполняется
            return;
        }
        //в новой транзакции, чтобы не сбросилась блокировка при исключениях БД.
        utilEJB.runInNewTransaction(() -> executeOperation(operationId));
        unlock(operationId);
    }

    private void unlock(final Long operationId) {
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                //thisBean.unlockOperation(operationId);
                utilEJB.runInTransactionAsync(() -> unlockOperation(operationId));
            }
        }, 5000);
    }
     */
    //@Override
    // @Asynchronous
    // @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    /*
    public void unlockOperation(Long operationId) {
        lockDao.deleteLock(OPERATION_LOCK_STRING, new BigDecimal(operationId));
    }
     */
    protected abstract void execute(IOperationProcessor processor, String operationCode) throws OperationException;

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @Override
    public void executeOperation(Long operationId, String operationCode) {
        utilEJB.runInNewTransaction(() -> _beginOper(operationId, operationCode));
        boolean handled = true;
        final IOperationProcessor processor = runningProcessors.get(operationId);
        if (processor == null) {
            return;
        }

        try {
            execute(processor, operationCode);

//processor.execute();
        } catch (Exception ex) {
            handled = handleException(ex, operationId, operationCode, processor);

        }
        if (handled) {
            utilEJB.runInNewTransaction(() -> _endOper(operationId, operationCode));
        }

    }

    private void _beginOper(Long operationId, String operationCode) {
        LOGGER.info("begin executing operation #" + operationId);
        final IOperationProcessor processor;

        try {

            processor = getProcessorByType(operationCode);
        } catch (Exception ex) {
            try {
                operationDao.changeOperationStatus(operationId, READY_TO_START, ERROR);
                OperationEvent event = new OperationEvent();
                event.setType(OperationEventType.ERROR);
                event.setCreateDate(new Date());
                event.setMessage(ex.getMessage());
                operationDao.writeLog(operationId, event);
                throw ex;

            } catch (OperationException ex1) {
                LOGGER.error("Opeartion #" + operationId + " prepare error", ex);
                return;
            }

        }
        try {
            OperationLogger logger = new OperationLogger(operationDao, operationId);
            String runUser = operationDao.getOperationRunUser(operationId);
            final List<OperationObject> parameters = operationDao.getOperationParameters(operationId);
            ServerOperationContext context = new ServerOperationContext();
            context.setCurrentOperationId(operationId);
            context.setRunUser(runUser);
            context.setOperationLogger(logger);
            context.setParameters(parameters);
            context.setOperationType(getOperationTypeForCode(operationCode));
            context.setSchedulerContext(createSchedulerContext(parameters));

            //TODO add parameters to context
            //
            sc.getContextData().put("RUN_USER", runUser);
            processor.init(context);

            runningProcessors.put(operationId, processor);

            operationDao.changeOperationStatus(operationId, READY_TO_START, STARTED);

            // thisBean.executeOperation(operationId);
            // processor.execute();
        } catch (Exception ex) {
            handleException(ex, operationId, operationCode, processor);
        }

    }

    private void _endOper(Long operationId, String operationCode) {
        IOperationProcessor processor = runningProcessors.remove(operationId);

        if (processor == null) {
            return;
        }

        try {
            final MultiOperationStatus currentStatus = checkAndGetCurrentStatus(processor, operationId);
            operationDao.changeOperationStatus(operationId, currentStatus, processor.isCanceled() ? CANCELED : DONE);
        } catch (OperationException ex) {
            LOGGER.error("Cannot change operation #" + operationId + " (" + operationCode + ") status to " + DONE, ex);
        }

    }

    private boolean handleException(Exception ex, Long operationId, String operationCode, IOperationProcessor processor) {
        if ((ex instanceof OperationException) && (ex.getCause() instanceof InterruptedException)) {
            //Пользователь отменил операцию, без паники
            //Статус операции изменится на CANCELED чуть позже
            return true;

        } else {
            //Прочие ошибки
            LOGGER.error("Operation #" + operationId + " (" + operationCode + ") execution error", ex);
            try {

                OperationEvent event = new OperationEvent();
                event.setType(OperationEventType.ERROR);
                event.setCreateDate(new Date());
                event.setMessage(getExceptionMessage(operationId, ex));
                event.setTrace(new ThrowableWrapper(ex).generateTrace());
                operationDao.writeLog(operationId, event);
                //
                final MultiOperationStatus currentStatus = checkAndGetCurrentStatus(processor, operationId);
                operationDao.changeOperationStatus(operationId, currentStatus, ERROR);
            } catch (OperationException ex1) {
                LOGGER.error("Cannot make operation #" + operationId + " (" + operationCode + ") erorred", ex1);
            }
            return false;
        }

    }

    /**
     * Запуск самой операции в новой транзакции, чтобы внутренние исключения на
     * уровне БД не прервали внешнюю транзакцию, тем самым сняв блокировку.
     *
     * @param operationId
     */
    //@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    //@Override
    /*public void executeOperation(Long operationId) {
        LOGGER.info("executing operation #" + operationId);
        final String operationCode;
        final IOperationProcessor processor;
        try {
            operationCode = operationDao.getOperationCode(operationId);

            try {
                processor = getProcessorByType(operationCode);
            } catch (Exception ex) {
                operationDao.changeOperationStatus(operationId, READY_TO_START, ERROR);
                OperationEvent event = new OperationEvent();
                event.setType(OperationEventType.ERROR);
                event.setCreateDate(new Date());
                event.setMessage(ex.getMessage());
                operationDao.writeLog(operationId, event);
                throw ex;
            }

            operationDao.changeOperationStatus(operationId, READY_TO_START, STARTED);
        } catch (IllegalOperationStatusException ex) {
            LOGGER.info("operation #" + operationId + " already not ready to start; skipping...");
            return;
        } catch (Exception ex) {
            LOGGER.error("Opeartion #" + operationId + " prepare error", ex);
            return;
        }

        try {
            OperationLogger logger = new OperationLogger(operationDao, operationId);
            String runUser = operationDao.getOperationRunUser(operationId);
            final List<OperationObject> parameters = operationDao.getOperationParameters(operationId);
            ServerOperationContext context = new ServerOperationContext();
            context.setCurrentOperationId(operationId);
            context.setRunUser(runUser);
            context.setOperationLogger(logger);
            context.setParameters(parameters);
            context.setOperationType(getOperationTypeForCode(operationCode));
            context.setSchedulerContext(createSchedulerContext(parameters));

            //TODO add parameters to context
            //
            sc.getContextData().put("RUN_USER", runUser);
            processor.init(context);

            runningProcessors.put(operationId, processor);
            processor.execute();

        } catch (Exception ex) {
            if ((ex instanceof OperationException) && (ex.getCause() instanceof InterruptedException)) {
                //Пользователь отменил операцию, без паники
                //Статус операции изменится на CANCELED чуть позже
            } else {
                //Прочие ошибки
                LOGGER.error("Operation #" + operationId + " (" + operationCode + ") execution error", ex);
                try {
                    OperationEvent event = new OperationEvent();
                    event.setType(OperationEventType.ERROR);
                    event.setCreateDate(new Date());
                    event.setMessage(getExceptionMessage(operationId, ex));
                    event.setTrace(new ThrowableWrapper(ex).generateTrace());
                    operationDao.writeLog(operationId, event);
                    //
                    final MultiOperationStatus currentStatus = checkAndGetCurrentStatus(processor, operationId);
                    operationDao.changeOperationStatus(operationId, currentStatus, ERROR);
                } catch (OperationException ex1) {
                    LOGGER.error("Cannot make operation #" + operationId + " (" + operationCode + ") erorred", ex1);
                }
                return;
            }
        } finally {
            runningProcessors.remove(operationId);
        }

        try {
            final MultiOperationStatus currentStatus = checkAndGetCurrentStatus(processor, operationId);
            operationDao.changeOperationStatus(operationId, currentStatus, processor.isCanceled() ? CANCELED : DONE);
        } catch (OperationException ex) {
            LOGGER.error("Cannot change operation #" + operationId + " (" + operationCode + ") status to " + DONE, ex);
        }
    }
     */
    private String getExceptionMessage(Long operationId, Throwable ex) throws OperationException {
        if (null == ex.getCause()) {
            if (ex instanceof ApplicationException) {
                ApplicationException appException = (ApplicationException) ex;
                OperationInfo operationInfo = operationDao.getOperationInfo(operationId);
                return i18nDao.get(operationInfo.getLocale(), appException.getLocalizedString());
            } else {
                return ExceptionHelper.getExceptionMessage(ex);
            }
        } else {
            return getExceptionMessage(operationId, ex.getCause());
        }
    }

    private static SchedulerContext createSchedulerContext(List<OperationObject> operationParameters) {
        for (OperationObject parameter : operationParameters) {
            if (parameter.getName().equals(OperationInfo.SCHEDULER_CONTEXT)) {
                return (SchedulerContext) parameter.getDTO();
            }
        }
        return null;
    }

    private MultiOperationStatus checkAndGetCurrentStatus(IOperationProcessor processor, Long operationId) throws OperationException {
        final MultiOperationStatus currentStatus = operationDao.getOperationStatus(operationId);
        if (processor.isCanceled()) {
            if (currentStatus != CANCEL) {
                throw new OperationException("Operation must has status " + CANCEL + " after canceling; status is " + currentStatus + " instead");
            }
        } else if (currentStatus != CANCEL && currentStatus != STARTED) {
            throw new OperationException("Operation cannot has status " + currentStatus + " in the end of execution");
        }
        return currentStatus;
    }

    /*
    private List<Long> listReadyToStartOperations() {
        try {
            final List<Long> readyToStartOperations = operationDao.listAllOpeartionsWithStatusJdbc(READY_TO_START);
            return readyToStartOperations;
        } catch (Exception ex) {
            LOGGER.error("Cannot list ready to start operations", ex);
            return Collections.EMPTY_LIST;
        }
    }
     */
    //</editor-fold>
    /**
     * Сканирует операции на "поломанные", т.е. завершившиеся аварийно, с
     * неправильным статусом. Переводит их в статус ошибочных.
     */
    //<editor-fold defaultstate="collapsed" desc="repairOperation">
    //@Schedule(hour = "*", minute = "*", second = "1", persistent = false)
    /*
    public void repairOperationsScheduler() {
        if (!canRepair()) {
            return;
        }

        LOGGER.trace("invalidOperationsDetection started");
        repairOperationsWithStatus(STARTED);
        repairOperationsWithStatus(CANCEL);
    }

    private boolean canRepair() {
        long now = new Date().getTime();
        if (lastRepairRun == null || lastRepairRun <= now - REPAIR_OPERATION_DELAY.getParameterLongValue() * 1000) {
            lastRepairRun = now;
            return true;
        }
        return false;
    }

    public void repairOperationsWithStatus(MultiOperationStatus status) {
        try {
            final List<Long> operationsToRepair = operationDao.listAllOpeartionsWithStatusJdbc(status);
            for (Long operationId : operationsToRepair) {
                LOGGER.trace("Operation #" + operationId + " suspected of abort");
                //если операция выполняется, то блокировка должна быть активна
                if (!lockDao.isLocked(OPERATION_LOCK_STRING, new BigDecimal(operationId))) {
                    try {
                        operationDao.changeOperationStatus(operationId, status, ERROR);
                    } catch (IllegalOperationStatusException ex) {
                        //кто-то уже исправил статус
                        continue;
                    }

                    LOGGER.info("Operation #" + operationId + " aborted with server failure");
                    OperationEvent event = new OperationEvent();
                    event.setType(OperationEventType.ERROR);
                    event.setCreateDate(new Date());
                    event.setMessage("Operation aborted with server failure");
                    operationDao.writeLog(operationId, event);
                }
            }
        } catch (Exception ex) {
            LOGGER.error("invalidOperationsDetection error", ex);
        }
    }
     */
    //</editor-fold>
    /**
     * Возвращает обработчик для указанного типа операции.
     *
     * @param operationCode код операции, для которого производится поиск
     * обработчика.
     * @return новый обработчик для операции.
     * @throws OperationException
     * @throws NoSuchOperationCodeException
     */
    protected abstract IOperationProcessor getProcessorByType(String operationCode) throws OperationException, NoSuchOperationCodeException;

}
