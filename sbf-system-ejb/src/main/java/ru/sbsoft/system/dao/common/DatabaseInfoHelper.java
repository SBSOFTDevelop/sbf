package ru.sbsoft.system.dao.common;

import javax.persistence.EntityManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.sbsoft.dao.JdbcWorkExecutor;
import ru.sbsoft.shared.exceptions.ApplicationException;

public class DatabaseInfoHelper {

    private static final Logger LOGGER = LoggerFactory.getLogger(DatabaseInfoHelper.class);
    private static String dbname = null;

    //@AccessTimeout(value = 60000)
    public static String getDatabaseName(EntityManager em) {
        if (dbname == null) {
            try {
                dbname = new JdbcWorkExecutor(em).executeJdbcWork((conn) -> {
                    String dbn = conn.getMetaData().getDatabaseProductName();
                    LOGGER.info("DBNAME:" + dbn);
                    return dbn;
                });
            } catch (Exception ex) {
                LOGGER.error("Cannot get database name", ex);
                throw new ApplicationException("Cannot get database name: " + ex.getMessage());

            }
        }
        return dbname;
    }
}
