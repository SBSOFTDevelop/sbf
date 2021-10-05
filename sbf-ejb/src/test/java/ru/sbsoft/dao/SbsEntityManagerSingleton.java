package ru.sbsoft.dao;

import java.util.Date;
import java.util.Locale;
import java.util.Properties;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.spi.PersistenceUnitTransactionType;
import org.eclipse.persistence.config.PersistenceUnitProperties;
import org.eclipse.persistence.config.TargetServer;

/**
 *
 * @author panarin
 */
public class SbsEntityManagerSingleton {

    private static EntityManagerFactory EMF;
    private static EntityManager EM;

    public static EntityManager getEM() {
        if (null == EM) {
            Locale.setDefault(Locale.US);

            checkEntityManagerFactory();
            Date beginTime = new Date();
            EM = EMF.createEntityManager();
            Date endTime = new Date();
            System.out.println(String.format("Create connect = %d msec", endTime.getTime() - beginTime.getTime()));
        }
        return EM;
    }

    private static void checkEntityManagerFactory() {
        if (null == EMF) {
            Date beginTime = new Date();
            EMF = Persistence.createEntityManagerFactory("TEST", createConnectionProperties());
            Date endTime = new Date();
            System.out.println(String.format("Create factory = %d msec", endTime.getTime() - beginTime.getTime()));
        }
    }

    public static void closeFactory() {
        if (null != EMF) {
            EMF.close();
        }
    }

    public static Properties createConnectionProperties() {
        final Properties props = new Properties();

        //props.put("eclipselink.logging.level.sql", "FINE");
        props.put("eclipselink.logging.level.sql", "INFO");
        props.put(PersistenceUnitProperties.TRANSACTION_TYPE, PersistenceUnitTransactionType.RESOURCE_LOCAL.name());
        props.put(PersistenceUnitProperties.TARGET_SERVER, TargetServer.None);

        props.put(PersistenceUnitProperties.DDL_GENERATION, PersistenceUnitProperties.NONE);
        props.put(PersistenceUnitProperties.DDL_GENERATION_MODE, PersistenceUnitProperties.DDL_DATABASE_GENERATION);

        props.put(PersistenceUnitProperties.JDBC_DRIVER, DbTestConfig.getDriver());
        props.put(PersistenceUnitProperties.JDBC_URL, DbTestConfig.getUrl());
        if (DbTestConfig.getLogin() != null && DbTestConfig.getLogin().trim().length() > 0) {
            props.put(PersistenceUnitProperties.JDBC_USER, DbTestConfig.getLogin());
            String passw = DbTestConfig.getPassw();
            if (passw != null && !(passw = passw.trim()).isEmpty()) {
                props.put(PersistenceUnitProperties.JDBC_PASSWORD, passw);
                //props.put(PersistenceUnitProperties.JDBC_PASSWORD, "indigo");
            }
        }

        props.put(PersistenceUnitProperties.LOGGING_LEVEL, "SEVERE");
        props.put(PersistenceUnitProperties.LOGGING_TIMESTAMP, "false");
        props.put(PersistenceUnitProperties.LOGGING_THREAD, "false");
        props.put(PersistenceUnitProperties.LOGGING_SESSION, "false");
        props.put(PersistenceUnitProperties.LOGGING_PARAMETERS, "true");

        return props;
    }

}
