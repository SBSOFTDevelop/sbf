package ru.sbsoft.processor;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import org.slf4j.LoggerFactory;
import ru.sbsoft.dao.operations.IMultiOperationDao;
import ru.sbsoft.shared.exceptions.ThrowableWrapper;
import ru.sbsoft.shared.model.OperationEvent;
import ru.sbsoft.shared.model.OperationEventType;
import ru.sbsoft.shared.model.operation.OperationException;

public class OperationLogger {

    private final static org.slf4j.Logger LOGGER = LoggerFactory.getLogger(OperationLogger.class);

    private final IMultiOperationDao operationDao;
    private Long operationId;

    public OperationLogger(IMultiOperationDao operationDao) {
        this.operationDao = operationDao;
    }

    public OperationLogger(IMultiOperationDao operationDao, Long operationId) {
        this.operationDao = operationDao;
        this.operationId = operationId;
    }

    public void setOperationId(Long operationId) {
        this.operationId = operationId;
    }

    public void error(String error) {
        error(error, (String) null);
    }

    public void error(String error, String trace) {
        log(error, trace, OperationEventType.ERROR);
    }

    public void error(String error, Throwable ex) {
        error(error, new ThrowableWrapper(ex).generateTrace());
    }

    public void warn(String message) {
        log(message, null, OperationEventType.WARNING);
    }

    public void warn(String message, Throwable ex) {
        log(message, new ThrowableWrapper(ex).generateTrace(), OperationEventType.WARNING);
    }

    public void info(String info) {
        info(info, null);
    }

    public void info(String info, String trace) {
        log(info, trace, OperationEventType.INFO);
    }

    public void log(String message, String trace, OperationEventType type) {
        final OperationEvent event = new OperationEvent();
        event.setMessage(message);
        event.setTrace(trace);
        event.setCreateDate(new Date());
        event.setType(type);
        try {
            operationDao.writeLog(operationId, event);
        } catch (OperationException ex) {
            LOGGER.error("Cannot save log message", ex);
        }
    }

    /**
     Выводит записи лога операции в переданный логгер в режиме "INFO".
    
     @param logger логгер для вывода лога операции.
     @return Список выведенных записей лога операции.
     */
    public List<OperationEvent> printToLog(org.slf4j.Logger logger) {
        try {
            final List<OperationEvent> events = operationDao.getOperationLog(operationId, 0L);
            for (final OperationEvent event : events) {
                logger.info(event.toString());
            }
            return events;
        } catch (OperationException ex) {
            logger.error("cannot print operation log", ex);
        }
        return Collections.EMPTY_LIST;
    }

    /**
     Выводит записи лога операции в переданный логгер в режиме "INFO".
    
     @param logger логгер для вывода лога операции.
     @return Список выведенных записей лога операции.
     */
    public List<OperationEvent> printToLog(java.util.logging.Logger logger) {
        try {
            final List<OperationEvent> events = operationDao.getOperationLog(operationId, 0L);
            for (final OperationEvent event : events) {
                logger.info(event.toString());
            }
            return events;
        } catch (OperationException ex) {
            logger.log(Level.SEVERE, "cannot print operation log", ex);
        }
        return Collections.EMPTY_LIST;
    }

}
