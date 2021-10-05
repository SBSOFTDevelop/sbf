package ru.sbsoft.system.dao.common;

import java.util.Date;
import java.util.List;
import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.Schedule;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.sbsoft.common.ServerConfig;
import ru.sbsoft.dao.IUtilEJB;
import ru.sbsoft.dao.operations.ISchedulerDao;
import ru.sbsoft.shared.model.enums.SchedulerStatus;
import ru.sbsoft.shared.model.operation.OperationException;

/**
 * Компонент для запуска планировщика.
 *
 * @author Fedor Resnyanskiy, SBSOFT
 */
@Stateless
@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
@PermitAll
public class SchedulerManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(SchedulerManager.class);
    private static final String SCHEDULER_LOCK_STRING = "SCHEDULER";
    //private static final SystemProperty START_SCHEDULER_DELAY = new SystemProperty("ru.sbsoft.schedulers.startdelay", "1");

    private static Long lastSchedulerRun;

    @EJB
    private ISchedulerDao schedulerDao;

    @EJB
    private IUtilEJB utilEJB;

   // @EJB
   // private ILockDao lockDao;

    @PersistenceContext(unitName = ru.sbsoft.common.jdbc.Const.DEFAULT_PERSISTENCE_CTX)
    private EntityManager em;

    //<editor-fold defaultstate="collapsed" desc="scheduleOperation">
    /**
     * Метод для срабатывания планировщика. Его назначение - быть точкой входа и
     * перехватывать исключения, иначе планировщик удалится.
     */
    @Schedule(hour = "*", minute = "*", second = "*/5", persistent = false)
    public void scheduler() {
        LOGGER.trace("schedulerScheduler invoked");
        try {
            schedulerIteration();
        } catch (Exception ex) {
            LOGGER.error("schedulerScheduler execution error", ex);
        }
    }

    /**
     * Получает все планировщики для обработки и запускает их в отдельных
     * потоках.
     *
     * @throws OperationException
     */
    private void schedulerIteration() throws OperationException {
        if (!schedulerCanStart()) {
            return;
        }

        final List<Long> schedulersToExecute = listSchedulers();

        for (final Long scheduler : schedulersToExecute) {
            utilEJB.runInTransactionAsync(() -> executeScheduler(scheduler));
        }
    }

    private boolean schedulerCanStart() {
        long now = new Date().getTime();
        if (lastSchedulerRun == null || lastSchedulerRun <= now - ServerConfig.getStartSchedulerDelay()) {
            lastSchedulerRun = now;
            return true;
        }
        return false;
    }

    //   @Asynchronous
//    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    private void executeScheduler(Long schedulerId) {

        try {
            schedulerDao.changeSchedulerStatus(schedulerId, SchedulerStatus.READY, SchedulerStatus.PROCESS);
            try {
                boolean actualState = false;
                while (!actualState && isScheduleActive(schedulerId)) {
                    schedulerDao.createOperationWithScheduler(schedulerId);
                }
                schedulerDao.changeSchedulerStatus(schedulerId, SchedulerStatus.PROCESS, SchedulerStatus.READY);
            } catch (Exception ex) {
                LOGGER.error("Scheduler executing error", ex);
                schedulerDao.changeSchedulerStatus(schedulerId, SchedulerStatus.PROCESS, SchedulerStatus.ERROR);
            }

        } catch (OperationException ex) {
            LOGGER.error("Scheduler executing error", ex);
        }

    }

    private boolean isScheduleActive(Long schedulerId) throws OperationException {
        return schedulerDao.isEnabled(schedulerId);
    }

    private List<Long> listSchedulers() throws OperationException {
        return schedulerDao.listSchedulersToExecuteJdbc();
    }

    //</editor-fold>
}
