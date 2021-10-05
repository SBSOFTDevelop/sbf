package ru.sbsoft.dao;

import java.sql.Connection;
import java.sql.SQLException;

/**
 *
 * @author Kiselev
 */
public interface IJdbcWork<T> {
    T execute(Connection connection) throws SQLException;
}
