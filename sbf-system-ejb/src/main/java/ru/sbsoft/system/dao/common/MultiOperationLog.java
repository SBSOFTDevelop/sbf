package ru.sbsoft.system.dao.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.sbsoft.common.SystemProperty;

/**
 *
 * @author Kiselev
 */
public final class MultiOperationLog {

    public static final SystemProperty OPERATIONS_ENGINE_LOGGING = new SystemProperty("ru.sbsoft.operations.engine.logging", "false");
    //public static final SystemProperty OPERATIONS_MONITOR_LOGGING = new SystemProperty("ru.sbsoft.operations.monitor.logging", "false");

    private final Logger logger;

    public MultiOperationLog(Class clazz) {
        logger = LoggerFactory.getLogger(clazz);
    }

    public Logger getLogger() {
        return logger;
    }

    private static String createLogString(String sql, Object... params) {
        StringBuilder buf = new StringBuilder(sql.length() + 50).append('\n').append(sql).append("\n\tbind => [");
        if (params != null && params.length > 0) {
            for (Object o : params) {
                buf.append(o).append(", ");
            }
        }
        return buf.append(']').toString();
    }

    public void logSql(String sql, Object... params) {
        boolean traceEnabled = logger.isTraceEnabled();
        if (traceEnabled || (OPERATIONS_ENGINE_LOGGING.getParameterBooleanValue() && logger.isInfoEnabled())) {
            String logStr = createLogString(sql, params);
            if (traceEnabled) {
                logger.trace(logStr);
            } else {
                logger.info(logStr);
            }
        }
    }
}
