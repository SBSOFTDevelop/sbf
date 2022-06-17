package ru.sbsoft.dao;

import java.sql.Connection;
import java.sql.SQLException;
import javax.persistence.EntityManager;

public class JdbcWorkExecutor implements IJdbcWorkExecutor {

    private EntityManager em;

    public JdbcWorkExecutor(EntityManager em) {
        this.em = em;
    }

    @Override
    public <T> T executeJdbcWork(IJdbcWork<T> work) throws SQLException {
        Connection conn = em.unwrap(Connection.class);

        if (conn == null) {
            if (!em.isJoinedToTransaction()) {
                throw new SQLException(getClass().getName() + ":" + "[EntityManager] transaction required");
            }
            throw new SQLException(getClass().getName() + ": connection is null");
        }
        return work.execute(conn);
//        ==== HIBERNATE ====
//        Session session = em.unwrap(Session.class);
//        try {
//            return session.doReturningWork(new ReturningWork<T>() {
//                @Override
//                public T execute(Connection connection) throws SQLException {
//                    return work.execute(connection);
//                }
//            });
//        } catch (HibernateException ex) {
//            if (ex.getCause() instanceof SQLException) {
//                throw (SQLException) ex.getCause();
//            } else if (ex.getCause() instanceof EJBException) {
//                throw (EJBException) ex.getCause();
//            } else {
//                throw ex;
//            }
//        }
//      ======================
    }
}
