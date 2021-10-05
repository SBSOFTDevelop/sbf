package ru.sbsoft.dao;

import java.sql.SQLException;

public interface IJdbcWorkExecutor {

    <T> T executeJdbcWork(IJdbcWork<T> work) throws  SQLException;
}
