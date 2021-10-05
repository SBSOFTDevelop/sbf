package ru.sbsoft.dao;

import javax.ejb.SessionContext;
import javax.persistence.EntityManager;

/**
 *
 * @author Kiselev
 */
public interface ITemplateContext {

    EntityManager getEntityManager();

    IJdbcWorkExecutor getJdbcWorkExecutor();

    SessionContext getSessionContext();
}
