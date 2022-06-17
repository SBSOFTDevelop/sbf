package ru.sbsoft.system.dao.common;

import ru.sbsoft.common.CronExpression;
import ru.sbsoft.common.DBType;
import ru.sbsoft.common.ServerConfig;
import ru.sbsoft.dao.IApplicationDao;
import ru.sbsoft.dao.IJdbcWorkExecutor;
import ru.sbsoft.dao.IMultiOperationManagerDao;
import ru.sbsoft.dao.JdbcWorkExecutor;
import ru.sbsoft.dao.operations.IMultiOperationDao;
import ru.sbsoft.dao.operations.ISchedulerDao;
import ru.sbsoft.shared.interfaces.OperationType;
import ru.sbsoft.shared.model.enums.SchedulerStatus;
import ru.sbsoft.shared.model.operation.IllegalOperationStatusException;
import ru.sbsoft.shared.model.operation.OperationException;
import ru.sbsoft.shared.model.operation.SchedulerContext;
import ru.sbsoft.system.common.MultiOperationEntity;
import ru.sbsoft.system.common.SchedulerEntity;

import javax.annotation.PostConstruct;
import javax.annotation.security.PermitAll;
import javax.ejb.*;
import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.PersistenceContext;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.*;

@Stateless
@Remote(ISchedulerDao.class)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
@PermitAll
public class SchedulerDaoBean implements ISchedulerDao {

    //@EJB
    //private ISchedulerDao thisBean;
    //@EJB
    // private IUtilEJB utilEJB;
    @EJB
    private IApplicationDao applicationDao;
    @EJB
    private IMultiOperationManagerDao manager;
    @EJB
    private IMultiOperationDao operationDao;
    @PersistenceContext(unitName = ru.sbsoft.common.jdbc.Const.DEFAULT_PERSISTENCE_CTX)
    private EntityManager em;

    private static final MultiOperationLog LOG = new MultiOperationLog(SchedulerDaoBean.class);

    private IJdbcWorkExecutor jdbcExecutor;

    //TODO 'sysdate' or 'last_run' date?
    private static final String MS_SQL = "select RECORD_ID,coalesce(iif(NEXT_SCHEDULE >=ENABLE_FROM,NEXT_SCHEDULE,ENABLE_FROM),NEXT_SCHEDULE)";
    private static final String O_SQL = "select RECORD_ID,nvl(greatest(NEXT_SCHEDULE, ENABLE_FROM),NEXT_SCHEDULE)";
    private static final String LIST_OPERATOINS_WITH_SCHEDULE_SQL = "  from SR_SCHEDULER where "
            + "  STATUS = '" + SchedulerStatus.READY + "' "
            + "  and ENABLED = true "
            + "  and ( ENABLE_FROM is null or ENABLE_FROM <= ? ) "
            + "  and ( ENABLE_TO is null or ENABLE_TO >= ? ) "
            + "  and ( NEXT_SCHEDULE is null or NEXT_SCHEDULE <= ? ) "
            + "  and ( COUNTER is null or COUNTER > 0) "
            + "  and APP_CODE = ? ";

    @PostConstruct
    private void init() {
        jdbcExecutor = new JdbcWorkExecutor(em);
    }

    //   @TransactionAttribute(TransactionAttributeType.REQUIRED)
    @Override
    public List<Long> listSchedulersToExecuteJdbc() throws OperationException {
        final String appCode = ServerConfig.getServerPrefix() + applicationDao.getAppCode();
        try {
            final String resSQL = (DBType.getCurrentType() == DBType.DBTYPE_MSSQL ? MS_SQL : O_SQL) + LIST_OPERATOINS_WITH_SCHEDULE_SQL;
            LOG.logSql(resSQL, appCode);

            List<ScheduleInfo> scheInfs = jdbcExecutor.executeJdbcWork((conn) -> {
                try (final PreparedStatement preparedStatement = conn.prepareStatement(resSQL)) {
                    final java.sql.Timestamp now = new java.sql.Timestamp(new Date().getTime());
                    preparedStatement.setTimestamp(1, now);
                    preparedStatement.setTimestamp(2, now);
                    preparedStatement.setTimestamp(3, now);
                    preparedStatement.setString(4, appCode);
                    final ResultSet resultSet = preparedStatement.executeQuery();
                    final List<ScheduleInfo> scheInfs0 = new ArrayList<>();
                    while (resultSet.next()) {
                        scheInfs0.add(new ScheduleInfo(resultSet.getLong(1), resultSet.getTimestamp(2)));
                    }
                    return scheInfs0;
                }
            });
            final List<Long> result = new ArrayList<>();
            for (ScheduleInfo inf : scheInfs) {
                final Long id = inf.getId();
                final Date nextSchedule = inf.getScheduleDate();
                if (nextSchedule == null) {
                    final Date newNextSchedule = getNextScheduleForScheduler(id);
                    //thisBean.setNextSchedule(id, newNextSchedule);
                    setNextSchedule(id, newNextSchedule);
                    if (!newNextSchedule.after(new Date())) {
                        result.add(id);
                    }
                } else {
                    result.add(id);
                }

            }
            return result;
        } catch (SQLException ex) {
            LOG.getLogger().warn("", ex);
            return Collections.EMPTY_LIST;
        }
    }

    private static class ScheduleInfo {

        private final Long id;
        private final Date scheduleDate;

        public ScheduleInfo(Long id, Date scheduleDate) {
            this.id = id;
            this.scheduleDate = scheduleDate;
        }

        public Long getId() {
            return id;
        }

        public Date getScheduleDate() {
            return scheduleDate;
        }
    }

    public Date getNextScheduleForScheduler(long schedulerId) throws OperationException {
        final SchedulerEntity scheduler = get(schedulerId);
        try {
            CronExpression cron = new CronExpression(scheduler.getCRON_EXPRESSION());
            cron.setTimeZone(TimeZone.getDefault());

            final Date lastSchedule = scheduler.getNEXT_SCHEDULE();
            Date cronFrom;
            if (lastSchedule != null && !scheduler.isIGNORE_BACK()) {
                cronFrom = lastSchedule;
            } else {
                cronFrom = new Date();
            }

            return cron.getNextValidTimeAfter(cronFrom);
        } catch (ParseException ex) {
            throw new OperationException("Cannot parse cron expression '" + scheduler.getCRON_EXPRESSION() + "'", ex);
        }
    }

    //@Override
    private void setNextSchedule(Long schedulerId, Date nextSchedule) throws OperationException {
        final SchedulerEntity scheduler = get(schedulerId);
        scheduler.setNEXT_SCHEDULE(nextSchedule);
        em.merge(scheduler);
        em.flush();
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    @Override
    public void changeSchedulerStatus(Long schedulerId, SchedulerStatus oldStatus, SchedulerStatus newStatus) throws OperationException {
        try {
            SchedulerEntity scheduler = get(schedulerId);
            em.lock(scheduler, LockModeType.PESSIMISTIC_READ);
            if (scheduler.getSTATUS() != oldStatus) {
                throw new IllegalOperationStatusException("Scheduler # " + schedulerId + " has status " + scheduler.getSTATUS() + "; expected " + oldStatus);
            }
            scheduler.setSTATUS(newStatus);

            em.merge(scheduler);
            em.lock(scheduler, LockModeType.NONE);

            em.flush();

            LOG.getLogger().info("Scheduler status #" + schedulerId + " changed to '" + newStatus + "'");
        } catch (OperationException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new OperationException("Cannot change scheduler #" + schedulerId + " status from " + oldStatus + " to " + newStatus, ex);
        }
    }

    @Override
    public SchedulerStatus getSchedulerStatus(Long schedulerId) throws OperationException {
        return get(schedulerId).getSTATUS();
    }

    //  @Override
    private void addScheduledOperation(Long schedulerId, Long operationId) throws OperationException {
        final SchedulerEntity scheduler = get(schedulerId);
        scheduler.getOperations().add(em.find(MultiOperationEntity.class, operationId));
        em.merge(scheduler);
        em.flush();
    }

    @Override
    public void createOperationWithScheduler(Long schedulerId) throws OperationException {
        final SchedulerEntity scheduler = get(schedulerId);
        //
        final OperationType operationType = manager.getOperationTypeForCode(scheduler.getOPERATION_CODE());
        final Long operationId = operationDao.createOperation(operationType, "", scheduler.getMODULE_CODE(), scheduler.getUSERNAME());
        operationDao.setOperationNotify(operationId, scheduler.isNOTIFY());
        addScheduledOperation(schedulerId, operationId);
        //if (operationType.isJms()) {
        operationDao.startOperation(operationId);
        incrementScheduler(schedulerId);
        //  return;
        //}

        /*  
        operationDao.setOperationParameter(operationId, new OperationObject(OperationInfo.SCHEDULER_CONTEXT, createSchedulerContext(scheduler)));
        operationDao.changeOperationStatus_SameTransaction(operationId, MultiOperationStatus.CREATED, MultiOperationStatus.READY_TO_START);

        incrementScheduler(schedulerId);
         */
    }

    private void incrementScheduler(Long schedulerId) throws OperationException {
        final Date newNextSchedule = getNextScheduleForScheduler(schedulerId);
        final Date now = new Date();
        final SchedulerEntity scheduler = get(schedulerId);
        scheduler.setCOUNTER(scheduler.getCOUNTER() == null ? null : (scheduler.getCOUNTER() - 1));
        scheduler.setPREV_SCHEDULE(scheduler.getNEXT_SCHEDULE());
        scheduler.setNEXT_SCHEDULE(newNextSchedule);
        scheduler.setLAST_RUN(now);
        em.merge(scheduler);
        em.flush();
    }

    @Override
    public boolean isEnabled(Long schedulerId) throws OperationException {
        final SchedulerEntity scheduler = get(schedulerId);
        final Date now = new Date();
        return scheduler.isENABLED()
                && (scheduler.getCOUNTER() == null || scheduler.getCOUNTER() > 0)
                && (scheduler.getENABLE_FROM() == null || !scheduler.getENABLE_FROM().after(now))
                && (scheduler.getENABLE_TO() == null || !scheduler.getENABLE_TO().before(now))
                && scheduler.getNEXT_SCHEDULE() != null
                && !scheduler.getNEXT_SCHEDULE().after(now);
    }

    @Override
    public Date getNextSchedule(Long schedulerId) throws OperationException {
        return get(schedulerId).getNEXT_SCHEDULE();
    }

    private static SchedulerContext createSchedulerContext(SchedulerEntity schedulerEntity) {
        SchedulerContext context = new SchedulerContext();
        context.setSchedulerId(schedulerEntity.getRECORD_ID().longValue());
        context.setStartDate(new Date());
        context.setNextSchedule(schedulerEntity.getNEXT_SCHEDULE());
        context.setPrevSchedule(schedulerEntity.getPREV_SCHEDULE());
        context.setLastRun(schedulerEntity.getLAST_RUN());
        return context;
    }

    private SchedulerEntity get(Long schedulerId) throws OperationException {
        final SchedulerEntity operation = em.find(SchedulerEntity.class, new BigDecimal(schedulerId));
        if (operation == null) {
            throw new OperationException("Operation #" + schedulerId + " not found");
        }
        return operation;
    }

}
