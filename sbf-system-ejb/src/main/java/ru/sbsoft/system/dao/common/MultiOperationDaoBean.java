package ru.sbsoft.system.dao.common;

import ru.sbsoft.common.ServerConfig;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.Remote;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.PersistenceContext;
import ru.sbsoft.common.jdbc.NamedParameterStatement;
import ru.sbsoft.dao.IApplicationDao;
import ru.sbsoft.shared.api.i18n.Ii18nDao;
import ru.sbsoft.dao.operations.IMultiOperationDao;
import ru.sbsoft.processor.OperationLogger;
import ru.sbsoft.session.SessionUtils;
import ru.sbsoft.shared.FilterInfo;
import ru.sbsoft.shared.Modifier;
import ru.sbsoft.shared.OperationObject;
import ru.sbsoft.shared.api.i18n.consts.SBFGeneralStr;
import ru.sbsoft.shared.interfaces.ObjectType;
import ru.sbsoft.shared.interfaces.OperationType;
import ru.sbsoft.shared.meta.Wrapper;
import ru.sbsoft.shared.model.operation.IllegalOperationStatusException;
import ru.sbsoft.shared.model.operation.NoSuchOperationCodeException;
import ru.sbsoft.shared.model.OperationEvent;
import ru.sbsoft.shared.model.OperationEventType;
import ru.sbsoft.shared.model.operation.OperationException;
import ru.sbsoft.shared.model.enums.MultiOperationStatus;
import static ru.sbsoft.shared.model.enums.MultiOperationStatus.*;
import ru.sbsoft.shared.model.operation.OperationInfo;
import ru.sbsoft.shared.model.operation.OperationsSelectFilter;
import ru.sbsoft.shared.param.DTO;
import ru.sbsoft.system.common.MultiOperationEntity;
import ru.sbsoft.system.common.MultiOperationLogEntity;
import ru.sbsoft.system.common.MultiOperationParameterEntity;
import ru.sbsoft.system.dao.common.helpers.OperationLogDaoHelper;
import ru.sbsoft.system.dao.common.json.WrapperAdapter;
import ru.sbsoft.system.dao.common.json.EnumJsonAdapter;
import ru.sbsoft.system.dao.common.json.FilterInfoAdapter;
import ru.sbsoft.dao.IJdbcWorkExecutor;
import ru.sbsoft.dao.JdbcWorkExecutor;
import ru.sbsoft.system.dao.common.json.ObjectTypeAdapter;

@Stateless
@Remote(IMultiOperationDao.class)
@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
@PermitAll
public class MultiOperationDaoBean implements IMultiOperationDao {

    private static final MultiOperationLog LOG = new MultiOperationLog(MultiOperationDaoBean.class);
    private static final Gson GSON = new GsonBuilder()
            .registerTypeAdapter(FilterInfo.class, new FilterInfoAdapter())
            .registerTypeAdapter(Wrapper.class, new WrapperAdapter())
            .registerTypeHierarchyAdapter(Modifier.class, new EnumJsonAdapter())
            .registerTypeHierarchyAdapter(ObjectType.class, new ObjectTypeAdapter())
            .create();

    @PersistenceContext(unitName = ru.sbsoft.common.jdbc.Const.DEFAULT_PERSISTENCE_CTX)
    private EntityManager em;

    @Resource
    private SessionContext sc;

    @EJB
    private IApplicationDao applicationDao;

    @EJB
    private Ii18nDao i18nDao;

    private IJdbcWorkExecutor jdbcExecutor;

    @Inject
    private JmsSender jmsSender;

    @PostConstruct
    private void init() {
        jdbcExecutor = new JdbcWorkExecutor(em);
    }

    public void setEm(final EntityManager entityManager) {
        this.em = entityManager;
    }

    public void setSessionContext(final SessionContext sessionContext) {
        this.sc = sessionContext;
    }

    public void setEntityManager(final EntityManager entityManager) {
        this.em = entityManager;
    }

    public void setApplicationDao(IApplicationDao applicationDao) {
        this.applicationDao = applicationDao;
    }

    public void setJdbcExecutor(IJdbcWorkExecutor jdbcExecutor) {
        if (jdbcExecutor != null) {
            throw new EJBException("jdbcExecutor is managed by container");
        }
        this.jdbcExecutor = jdbcExecutor;
    }

    private OperationLogger createLogger(Long operationId) {
        return new OperationLogger(this, operationId);
    }

    @Override
    public Long createOperation(OperationType operationType, String locale, String currentModuleCode, boolean isNeedNotify) throws OperationException, NoSuchOperationCodeException {
        String username = SessionUtils.getCurrentUserName(sc);
        long id = createOperation(operationType, locale, currentModuleCode, username);
        MultiOperationEntity e = em.find(MultiOperationEntity.class, id);
        e.setNEED_NOTIFY(isNeedNotify);

        return id;
    }

    @Override
    public Long createOperation(OperationType operationType, String locale, String currentModuleCode) throws OperationException, NoSuchOperationCodeException {
        String username = SessionUtils.getCurrentUserName(sc);
        return createOperation(operationType, locale, currentModuleCode, username);
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public Long createSynchOperation(OperationType operationType, String currentModuleCode, String username) throws OperationException, NoSuchOperationCodeException {
        username = username == null ? SessionUtils.getCurrentUserName(sc) : username;
        String locale = "ru_RU";
        final MultiOperationEntity operation = new MultiOperationEntity();
        final Date now = new Date();
        operation.setOPERATION_CODE(operationType.getCode());
        operation.setTITLE(i18nDao.get(locale, operationType.getTitle()));
        operation.setSTATUS(MultiOperationStatus.DONE);
        operation.setCREATE_DATE(now);
        operation.setRUN_DATE(now);
        operation.setEND_DATE(now);
        operation.setCREATE_USER(username);
        operation.setPROGRESS(BigDecimal.valueOf(100));
        operation.setPROGRESS_COMMENT(i18nDao.get(locale, SBFGeneralStr.enumComplitedSecsess));
        operation.setVISIBLE(Boolean.TRUE);
        operation.setNOTIFIED(Boolean.FALSE);
        operation.setAPP_CODE(ServerConfig.getServerPrefix() + applicationDao.getAppCode());
        operation.setMODULE_CODE(currentModuleCode);
        operation.setNEED_NOTIFY(Boolean.FALSE);
        operation.setLOCALE(locale);
        try {
            em.persist(operation);
            em.flush();

            createLogger(operation.getRECORD_ID()).info(i18nDao.get(locale, SBFGeneralStr.msgOperCreate));
        } catch (Exception ex) {
            throw new OperationException("Cannot create new operation instance for code " + operationType, ex);
        }
        return operation.getRECORD_ID();

    }

    @Override
    public Long createOperation(OperationType operationType, String locale, String currentModuleCode, String username) throws OperationException, NoSuchOperationCodeException {
        final MultiOperationEntity operation = new MultiOperationEntity();

        operation.setOPERATION_CODE(operationType.getCode());
        operation.setTITLE(i18nDao.get(locale, operationType.getTitle()));
        operation.setSTATUS(MultiOperationStatus.CREATED);
        operation.setCREATE_DATE(new Date());
        operation.setCREATE_USER(username);
        operation.setPROGRESS(BigDecimal.ZERO);
        operation.setVISIBLE(Boolean.TRUE);
        operation.setNOTIFIED(Boolean.FALSE);

        operation.setAPP_CODE(ServerConfig.getServerPrefix() + applicationDao.getAppCode() + (operationType.isJms() ? ru.sbsoft.common.jdbc.Const.JMS_OPER_PREF : ""));
        operation.setMODULE_CODE(currentModuleCode);
        operation.setNEED_NOTIFY(Boolean.TRUE);
        operation.setLOCALE(locale);
        try {
            em.persist(operation);
            em.flush();

            createLogger(operation.getRECORD_ID()).info(i18nDao.get(locale, SBFGeneralStr.msgOperCreate));
        } catch (Exception ex) {
            throw new OperationException("Cannot create new operation instance for code " + operationType, ex);
        }
        return operation.getRECORD_ID();
    }

    private static final String CURRENT_USER_OPERATIONS_QUERY
            = "SELECT \n"
            + "   RECORD_ID,\n"
            + "   TITLE,\n"
            + "   STATUS,\n"
            + "   OPERATION_CODE,\n"
            + "   PROGRESS,\n"
            + "   CREATE_COMMENT,\n"
            + "   PROGRESS_COMMENT,\n"
            + "   NOTIFIED,\n"
            + "   NEED_NOTIFY,\n"
            + "   LOCALE\n"
            + " FROM\n"
            + "   SR_MULTI_OPERATION o\n"
            + "WHERE\n"
            + "   o.CREATE_USER = :username AND o.CREATE_DATE >= :periodStart AND o.MODULE_CODE = :moduleCode AND (o.APP_CODE = :appcode OR o.APP_CODE = :appcodejms) AND (1 = :showHidden OR o.VISIBLE = true)";

    @Override
    public List<OperationInfo> listCurrentUserOperationsJdbc(final OperationsSelectFilter filter) throws SQLException {
        final String username = SessionUtils.getCurrentUserName(sc);
        final String appCode = ServerConfig.getServerPrefix() + applicationDao.getAppCode();
        final java.sql.Date periodStart = toSql(filter.getPeriodStart());
        final String moduleCode = filter.getModuleCode();
        final int showHidden = filter.isShowHidden() ? 1 : 0;

        LOG.logSql(LIST_OPERATOINS_SQL, username, appCode, periodStart, moduleCode, showHidden);

        return jdbcExecutor.executeJdbcWork((conn) -> {
            try (NamedParameterStatement stat = new NamedParameterStatement(conn, CURRENT_USER_OPERATIONS_QUERY)) {
                stat.setString("username", username);
                stat.setString("appcode", appCode);
                stat.setString("appcodejms", appCode + ru.sbsoft.common.jdbc.Const.JMS_OPER_PREF);

                stat.setDate("periodStart", periodStart);
                stat.setString("moduleCode", moduleCode);
                stat.setInt("showHidden", showHidden);
                List<OperationInfo> result = new ArrayList<>();
                try (ResultSet rs = stat.executeQuery()) {
                    while (rs.next()) {
                        Long id = rs.getLong("RECORD_ID");
                        String title = rs.getString("TITLE");
                        MultiOperationStatus status = MultiOperationStatus.valueOf(rs.getString("STATUS"));
                        String code = rs.getString("OPERATION_CODE");
                        BigDecimal progress = rs.getBigDecimal("PROGRESS");
                        String createComment = rs.getString("CREATE_COMMENT");
                        String progressComment = rs.getString("PROGRESS_COMMENT");
                        boolean userNotified = rs.getBoolean("NOTIFIED");
                        boolean userNeedNotify = rs.getBoolean("NEED_NOTIFY");
                        String locale = rs.getString("LOCALE");
                        result.add(createOperationInfo(id, title, status, code, progress, createComment, progressComment, userNotified, userNeedNotify, locale));
                    }
                }
                return result;
            }
        });
    }

    private java.sql.Date toSql(Date d) {
        return d != null ? new java.sql.Date(d.getTime()) : null;
    }

//    @Override
//    public List<OperationInfo> listCurrentUserOperations(OperationsSelectFilter filter) throws OperationException {
//        List<OperationInfo> result = new ArrayList<>();
//        final String username = SessionUtils.getCurrentUserName(sc);
//
//        final List<MultiOperationEntity> operations = em.createQuery("select o from MultiOperationEntity o where o.CREATE_USER = :username and o.CREATE_DATE >= :periodStart and o.MODULE_CODE = :moduleCode and o.APP_CODE = :appcode and (1 = :showHidden or o.VISIBLE = true)", MultiOperationEntity.class)
//                .setParameter("username", username)
//                .setParameter("appcode", applicationDao.getAppCode())
//                .setParameter("periodStart", filter.getPeriodStart(), TemporalType.DATE)
//                .setParameter("moduleCode", filter.getModuleCode())
//                .setParameter("showHidden", filter.isShowHidden() ? 1 : 0)
//                .getResultList();
//
//        for (MultiOperationEntity operation : operations) {
//            result.add(createOperationInfo(operation));
//        }
//        return result;
//    }
//
    private OperationInfo createOperationInfo(MultiOperationEntity operation) {
        return createOperationInfo(operation.getRECORD_ID(), operation.getTITLE(), operation.getSTATUS(), operation.getOPERATION_CODE(), operation.getPROGRESS(), operation.getCREATE_COMMENT(),
                operation.getPROGRESS_COMMENT(), operation.getNOTIFIED(), operation.getNEED_NOTIFY(), operation.getLOCALE());
    }

    private OperationInfo createOperationInfo(Long id, String title, MultiOperationStatus status, String code, BigDecimal progress, String createComment, String progressComment, boolean userNotified, boolean userNeedNotify, String locale) {
        final OperationInfo operationInfo = new OperationInfo();
        operationInfo.setId(id);
        operationInfo.setTitle(title);
        operationInfo.setStatus(status);
        operationInfo.setCode(code);
        operationInfo.setProgress(progress);
        operationInfo.setCreateComment(createComment);
        operationInfo.setProcessComment(progressComment);
        operationInfo.setUserNotified(userNotified);
        operationInfo.setUserNeedNotify(userNeedNotify);
        operationInfo.setLocale(locale);
        return operationInfo;
    }

    /*@TransactionAttribute(TransactionAttributeType.REQUIRED)
    @Override
    public String getOperationCode(Long operationId) throws OperationException {
        try {
            return get(operationId).getOPERATION_CODE();
        } catch (Exception ex) {
            throw new OperationException("Cannot get operation code for operation #" + operationId, ex);
        }
    }
     */
//    @Override
//    public List<Long> listAllOpeartionsWithStatus(MultiOperationStatus status) throws OperationException {
//
//        try {
//            return em.createQuery("select o.RECORD_ID from MultiOperationEntity o where o.STATUS = :status and o.APP_CODE = :appcode", Long.class)
//                    .setParameter("status", status)
//                    .setParameter("appcode", applicationDao.getAppCode())
//                    .getResultList();
//        } catch (Exception ex) {
//            throw new OperationException("Cannot list operations with status " + status.name(), ex);
//        }
//    }
//
//    private static final String LIST_OPERATOINS_WITH_SCHEDULE_SQL = "select RECORD_ID from SR_MULTI_OPERATION where STATUS = '" + READY_TO_START.name() + "' and APP_CODE = ? and (SCHEDULE_DATE < sysdate or SCHEDULE_DATE is null)";
//
//    @Override
//    public List<Long> listOperationsToStartJdbc() throws OperationException {
//        final String appCode = applicationDao.getAppCode();
//
//        String logString = "\n" + LIST_OPERATOINS_WITH_SCHEDULE_SQL + "\n\tbind => [" + appCode + "]";
//
//        if (OPERATIONS_ENGINE_LOGGING.getParameterBooleanValue()) {
//            LOGGER.info(logString);
//        } else {
//            LOGGER.trace(logString);
//        }
//
//        try {
//            final List<Long> result;
//            try (PreparedStatement preparedStatement = connection.prepareStatement(LIST_OPERATOINS_WITH_SCHEDULE_SQL)) {
//                preparedStatement.setString(1, appCode);
//                final ResultSet resultSet = preparedStatement.executeQuery();
//                result = new ArrayList<>();
//                while (resultSet.next()) {
//                    final long id = resultSet.getLong(1);
//                    if (operationCanStart(id)) {
//                        result.add(id);
//                    }
//                }
//            }
//            return result;
//        } catch (SQLException ex) {
//            LOGGER.warn("", ex);
//            return Collections.EMPTY_LIST;
//        }
//    }
//
//    private boolean operationCanStart(long id) throws OperationException {
//        //final MultiOperationEntity operation = get(id);
//
//        return true;
//    }
//
    private static final String LIST_OPERATOINS_SQL = "select RECORD_ID from SR_MULTI_OPERATION where APP_CODE = ? and STATUS = ?";

    @Override
    public List<Long> listAllOpeartionsWithStatusJdbc(MultiOperationStatus status) throws OperationException {

        final String appCode = ServerConfig.getServerPrefix() + applicationDao.getAppCode();
        LOG.logSql(LIST_OPERATOINS_SQL, appCode, status.name());
        try {
            return jdbcExecutor.executeJdbcWork((conn) -> {
                try (PreparedStatement preparedStatement = conn.prepareStatement(LIST_OPERATOINS_SQL)) {
                    preparedStatement.setString(1, appCode);
                    preparedStatement.setString(2, status.name());
                    final ResultSet resultSet = preparedStatement.executeQuery();
                    final List<Long> result = new ArrayList<>();
                    while (resultSet.next()) {
                        result.add(resultSet.getLong(1));
                    }
                    return result;
                }
            });
        } catch (SQLException ex) {
            LOG.getLogger().warn("", ex);
            return Collections.EMPTY_LIST;
        }
    }

    @Override
    public void setOperationParameters(Long operationId, List<OperationObject> parameters) throws OperationException {
        final MultiOperationEntity operation = get(operationId);
        for (OperationObject parameter : parameters) {
            setParameterForOperation(parameter, operation);
        }
        em.flush();
    }

    @Override
    public void setOperationParameter(Long operationId, OperationObject parameter) throws OperationException {
        final MultiOperationEntity operation = get(operationId);
        setParameterForOperation(parameter, operation);
        em.flush();
    }

    private void setParameterForOperation(final OperationObject parameter, final MultiOperationEntity operation) {
        Object value = parameter.getDTO();

        try {
            List<MultiOperationParameterEntity> oldParam = em.createQuery("select o from MultiOperationParameterEntity o where o.PARAM_NAME = :name and o.OPERATION = :operation", MultiOperationParameterEntity.class)
                    .setParameter("name", parameter.getName())
                    .setParameter("operation", operation)
                    .getResultList();
            //
            final MultiOperationParameterEntity param;
            if (oldParam.size() >= 1) {
                param = oldParam.get(0);
            } else {
                param = new MultiOperationParameterEntity();
            }

            param.setOperation(operation);
            param.setPARAM_NAME(parameter.getName());
            param.setPARAM_CLASS(value == null ? null : value.getClass().getName());
            param.setPARAM_VALUE(value == null ? null : GSON.toJson(value));

            if (param.getIdValue() != null) {
                em.merge(param);
            } else {
                em.persist(param);
            }
        } catch (Exception ex) {
            throw new EJBException("Can't set operation parameters", ex);
        }
    }

    @Override
    public List<OperationObject> getOperationParameters(Long operationId) throws OperationException {
        final List<OperationObject> result = new ArrayList<>();
        final MultiOperationEntity operation = get(operationId);
        for (MultiOperationParameterEntity param : operation.getParameters()) {
            result.add(parameterEntityToModel(param));
        }
        return result;
    }

    @Override
    public OperationObject getOperationParameter(Long operationId, String name) throws OperationException {
        final MultiOperationEntity operation = get(operationId);
        final List<MultiOperationParameterEntity> param = em.createQuery("select o from MultiOperationParameterEntity o where o.OPERATION = :operation and o.PARAM_NAME = :name", MultiOperationParameterEntity.class)
                .setParameter("operation", operation)
                .setParameter("name", name)
                .getResultList();

        if (param.isEmpty()) {
            return null;
        }

        return parameterEntityToModel(param.get(0));
    }

    private OperationObject parameterEntityToModel(MultiOperationParameterEntity entity) throws OperationException {
        OperationObject parameter = new OperationObject();
        parameter.setName(entity.getPARAM_NAME());
        parameter.setDTO((DTO) fromJson(entity.getPARAM_CLASS(), entity.getPARAM_VALUE()));
        return parameter;
    }

    private static Object fromJson(String paramClass, String paramValue) throws OperationException {
        if (paramClass == null) {
            return null;
        }
        try {
            final Class<?> clazz = Class.forName(paramClass);
            return GSON.fromJson(paramValue, clazz);
        } catch (Exception ex) {
            throw new OperationException("Could not read from storage object operation", ex);
        }
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void changeOperationStatus_SameTransaction(Long operationId, MultiOperationStatus oldStatus, MultiOperationStatus newStatus) throws OperationException, IllegalOperationStatusException {
        changeOperationStatus(operationId, oldStatus, newStatus);
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void changeOperationStatus(Long operationId, MultiOperationStatus oldStatus, MultiOperationStatus newStatus) throws OperationException, IllegalOperationStatusException {
        try {
            MultiOperationEntity operation = get(operationId);
            /*
            if (isLock) {
                em.lock(operation, LockModeType.PESSIMISTIC_READ);
            }
             */
            if (operation.getSTATUS() != oldStatus) {
                throw new IllegalOperationStatusException(i18nDao.get(operation.getLOCALE(), SBFGeneralStr.msgOperStatusWait,
                        String.valueOf(operationId), operation.getSTATUS().caption(i18nDao, operation.getLOCALE()), oldStatus.name()));
            }
            operation.setSTATUS(newStatus);

            switch (newStatus) {
                case STARTED:
                    operation.setRUN_DATE(new Date());
                    break;

                case CANCEL:
                case DONE:
                case ERROR:
                    operation.setEND_DATE(new Date());
                    break;

            }

            em.merge(operation);
            /*
            if (isLock) {
                em.lock(operation, LockModeType.NONE);
            }
             */
            if (oldStatus == CANCEL && newStatus != CANCELED) {
                OperationEvent event = new OperationEvent();
                event.setType(OperationEventType.WARNING);
                event.setCreateDate(new Date());
                event.setMessage(i18nDao.get(operation.getLOCALE(), SBFGeneralStr.msgOperNotCanceledServer));
                writeLog(operationId, event);
            }

            em.flush();

            createLogger(operation.getRECORD_ID()).info(i18nDao.get(operation.getLOCALE(), SBFGeneralStr.msgOperChangeStatus,
                    newStatus.caption(i18nDao, operation.getLOCALE())));
        } catch (OperationException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new OperationException("Cannot change opertion #" + operationId + " status from " + oldStatus + " to " + newStatus, ex);
        }
    }

    @Override
    public MultiOperationStatus getOperationStatus(Long operationId) throws OperationException {
        return get(operationId).getSTATUS();
    }

    @Override
    public void writeLog(Long opertationId, OperationEvent event) throws OperationException {
        try {
            final MultiOperationLogEntity log = new MultiOperationLogEntity();
            log.setCREATE_DATE(event.getCreateDate() == null ? new Date() : event.getCreateDate());
            log.setCREATE_USER(SessionUtils.getCurrentUserName(sc));
            log.setMESSAGE(event.getMessage());
            log.setTRACE(event.getTrace());
            log.setTYPE_VALUE(event.getType());
            log.setOPERATION(get(opertationId));
            em.persist(log);
            em.flush();
        } catch (Exception ex) {
            throw new OperationException("Error add operation log", ex);
        }
    }

    @Override
    public void setOperationVisible(Long opertationId, boolean visible) throws OperationException {
        try {
            final MultiOperationEntity operation = get(opertationId);
            operation.setVISIBLE(visible);
            em.merge(operation);
            em.flush();
        } catch (Exception ex) {
            throw new OperationException("Error refresh operation", ex);
        }
    }

    @Override
    public OperationInfo getOperationInfo(Long operationId) throws OperationException {
        final MultiOperationEntity operation = get(operationId);
        return createOperationInfo(operation);
    }

    @Override
    public String getOperationRunUser(Long operationId) throws OperationException {
        final MultiOperationEntity operation = get(operationId);
        return operation.getCREATE_USER();
    }

    private MultiOperationEntity get(Long operationId) throws OperationException {
        final MultiOperationEntity operation = em.find(MultiOperationEntity.class, operationId);
        if (operation == null) {
            throw new OperationException("Operation #" + operationId + " not found");
        }
        return operation;
    }

    /*@Override
    public void startOperation(Long operationId) throws OperationException {
        final MultiOperationEntity operation = get(operationId);
        final MultiOperationStatus currentStatus = operation.getSTATUS();
        if (currentStatus != CREATED) {
            throw new OperationException(i18nDao.get(operation.getLOCALE(), SBFGeneralStr.msgOperErrorStartStatus, currentStatus.caption(i18nDao, operation.getLOCALE())));
        }
        changeOperationStatus(operationId, currentStatus, READY_TO_START);

        OperationEvent event = new OperationEvent();
        event.setType(OperationEventType.INFO);
        event.setCreateDate(new Date());
        event.setMessage(i18nDao.get(operation.getLOCALE(), SBFGeneralStr.msgOperInQueued));
        writeLog(operationId, event);
    }
     */
    @Override
    public void cancelOperation(Long operationId) throws OperationException {
        final MultiOperationEntity operation = get(operationId);
        final MultiOperationStatus currentStatus = operation.getSTATUS();
        if (currentStatus != CREATED && currentStatus != READY_TO_START && currentStatus != STARTED) {
            throw new OperationException(i18nDao.get(operation.getLOCALE(), SBFGeneralStr.msgOperNotCanceled, currentStatus.caption(i18nDao, operation.getLOCALE())));
        }
        if (currentStatus == STARTED) {
            //требуется обработка движком
            changeOperationStatus(operationId, currentStatus, MultiOperationStatus.CANCEL);
        } else {
            //не требуется обработка движком
            changeOperationStatus(operationId, currentStatus, MultiOperationStatus.CANCELED);
        }
    }

    @Override
    public void updateOperationProgress(Long operationId, BigDecimal progress, String comment) throws OperationException {
        final MultiOperationEntity operation = get(operationId);
        operation.setPROGRESS(progress);
        operation.setPROGRESS_COMMENT(comment);
        em.merge(operation);
        em.flush();
    }

    @Override
    public List<OperationEvent> getOperationLog(Long operationId, Long lastLog) {
        final List<MultiOperationLogEntity> entities = OperationLogDaoHelper.loadLogs(em, operationId, lastLog);
        final List<OperationEvent> events = new ArrayList<>(entities.size());
        for (final MultiOperationLogEntity entity : entities) {
            final OperationEvent event = new OperationEvent();
            event.setEventId(entity.getRECORD_ID());
            event.setMessage(entity.getMESSAGE());
            event.setType(entity.getTYPE_VALUE());
            event.setCreateDate(entity.getCREATE_DATE());
            event.setTrace(entity.getTRACE());
            events.add(event);
        }
        return events;
    }

    @Override
    public void setOperationNotify(Long operationId, Boolean notify) throws OperationException {
        final MultiOperationEntity operation = get(operationId);
        operation.setNEED_NOTIFY(notify);
        em.merge(operation);
        em.flush();
    }

    @Override
    public void setOperationNotified(Long operationId, Boolean notified) throws OperationException {
        final MultiOperationEntity operation = get(operationId);
        operation.setNOTIFIED(notified);
        em.merge(operation);
        em.flush();
    }

    @Override
    public void startOperation(Long operationId) throws OperationException {

        final MultiOperationEntity operation = get(operationId);
        final MultiOperationStatus currentStatus = operation.getSTATUS();
        if (currentStatus != CREATED) {
            throw new OperationException(i18nDao.get(operation.getLOCALE(), SBFGeneralStr.msgOperErrorStartStatus, currentStatus.caption(i18nDao, operation.getLOCALE())));
        }

        changeOperationStatus(operationId, currentStatus, READY_TO_START);

        OperationEvent event = new OperationEvent();
        event.setType(OperationEventType.INFO);
        event.setCreateDate(new Date());
        event.setMessage(i18nDao.get(operation.getLOCALE(), SBFGeneralStr.msgOperInQueued));
        writeLog(operationId, event);

        jmsSender.sendMessage(operationId, operation.getOPERATION_CODE());
    }

}
