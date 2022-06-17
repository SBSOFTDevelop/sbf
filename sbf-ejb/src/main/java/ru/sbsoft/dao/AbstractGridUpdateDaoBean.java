package ru.sbsoft.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.SessionContext;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.sbsoft.common.jdbc.NamedParameterStatement;
import ru.sbsoft.sbf.app.model.YearMonthDay;
import ru.sbsoft.server.utils.YearMonthDayConverter;
import ru.sbsoft.session.SessionUtils;
import ru.sbsoft.shared.exceptions.ApplicationException;
import ru.sbsoft.shared.meta.Wrapper;
import ru.sbsoft.shared.util.IdName;

public abstract class AbstractGridUpdateDaoBean implements IGridUpdateDao {

    protected static final Logger LOGGER = LoggerFactory.getLogger(AbstractGridUpdateDaoBean.class);
    protected final ITableColumnNames DEFAULT_TABLE_COLUMN_NAMES = new TableColumnNames("RECORD_ID", "UPDATE_DATE", "UPDATE_USER");
    @Resource
    private SessionContext sessionContext;
    @PersistenceContext(unitName = ru.sbsoft.common.jdbc.Const.DEFAULT_PERSISTENCE_CTX)
    private EntityManager em;

    private IJdbcWorkExecutor jdbcWorkExecutor;

    @PostConstruct
    private void init() {
        jdbcWorkExecutor = new JdbcWorkExecutor(em);
    }

    public SessionContext getSessionContext() {
        return sessionContext;
    }

    public void setSessionContext(SessionContext sessionContext) {
        this.sessionContext = sessionContext;
    }

    public EntityManager getEm() {
        return em;
    }

    public void setJdbcWorkExecutor(IJdbcWorkExecutor jdbcWorkExecutor) {
        this.jdbcWorkExecutor = jdbcWorkExecutor;
    }

    @Override
    public List<BigDecimal> updateRows(String table, Map<BigDecimal, Map<String, Wrapper>> rows) {
        if (!canWrite(table)) {
            throw new ApplicationException("Нет прав на редактирование таблицы");
        }
        final List<BigDecimal> res = new ArrayList<>();
        Map<Set<String>, Map<BigDecimal, Map<String, Wrapper>>> rowGroups = new HashMap<>();
        rows.forEach((rowId, row) -> {
            Map<BigDecimal, Map<String, Wrapper>> groupRow = rowGroups.computeIfAbsent(row.keySet(), groupId -> new HashMap<BigDecimal, Map<String, Wrapper>>());
            groupRow.put(rowId, row);
        });
        try {
            for (Map.Entry<Set<String>, Map<BigDecimal, Map<String, Wrapper>>> group : rowGroups.entrySet()) {
                final String sql = createQuery(table, new ArrayList<>(group.getKey()));
                jdbcWorkExecutor.executeJdbcWork((conn) -> {
                    NamedParameterStatement statement = prepareStatement(conn, sql);
                    try {
                        for (Map.Entry<BigDecimal, Map<String, Wrapper>> row : group.getValue().entrySet()) {
                            Map<String, Wrapper> colVals = row.getValue();
                            List<String> columns = new ArrayList<>(colVals.size());
                            List<Wrapper> values = new ArrayList<>(colVals.size());
                            colVals.forEach((colName, val) -> {
                                columns.add(colName);
                                values.add(val);
                            });
                            res.add(row.getKey());
                            setParams(statement, table, row.getKey(), columns, values);
                            statement.addBatch();
                            statement.getStatement().clearParameters();
                        }
                        statement.executeBatch();
                    } finally {
                        try {
                            statement.close();
                        } catch (SQLException ex) {
                            LOGGER.error("Ошибка закрытия запроса", ex);
                        }
                    }
                    return null;
                });
            }
            return res;
        } catch (SQLException ex) {
            LOGGER.error("Ошибка запроса", ex);
            throw new ApplicationException("Ошибка запроса", ex);
        }
    }

    private void setParams(final NamedParameterStatement statement, String table, BigDecimal primaryKey, List<String> columns, List<Wrapper> values) throws SQLException {
        for (int i = 0; i < columns.size(); i++) {
            Object val = i < values.size() ? values.get(i).getValue() : null;
            if(val instanceof IdName){
                val = ((IdName)val).getId();
            }
            if (null == val) {
                statement.setNull(columns.get(i), Types.NULL);
            } else {
                if (val instanceof YearMonthDay) {
                    statement.setDate(columns.get(i), new Date(YearMonthDayConverter.convert((YearMonthDay) val).getTime()));
                } else {
                    statement.setObject(columns.get(i), val);
                }
            }
        }
        ITableColumnNames names = getTableColumnNames(table);
        if (names.getUpdateDate() != null) {
            statement.setTimestamp(names.getUpdateDate(), new Timestamp(new java.util.Date().getTime()));
        }
        if (names.getUpdateUser() != null) {
            statement.setString(names.getUpdateUser(), getCurrentUserName());
        }
        statement.setBigDecimal(names.getKey(), primaryKey);
    }

    protected abstract boolean canWrite(String table);

    protected NamedParameterStatement prepareStatement(Connection conn, final String query) {
        try {
            return new NamedParameterStatement(conn, query);
        } catch (SQLException ex) {
            LOGGER.error("Не удалось создать запрос", ex);
            throw new ApplicationException("Не удалось создать запрос");
        }
    }

    private String createQuery(String table, List<String> cols) {
        ITableColumnNames names = getTableColumnNames(table);
        if (names == null || names.getKey() == null) {
            throw new IllegalStateException("Update query for " + table + " can not be created: key column is not provided");
        }
        final QueryBuilder sb = new QueryBuilder();
        sb.append("UPDATE ").append(table);
        sb.append(" SET ");

        for (int i = 0; i < cols.size(); i++) {
            if (i == 0) {

                sb.appendColumnParam(cols.get(i));

            } else {
                sb.appendNextColumnParam(cols.get(i));
            }

        }

        sb.appendNextColumnParam(names.getUpdateDate());

        sb.appendNextColumnParam(names.getUpdateUser());

        sb.append(" WHERE ").appendColumnParam(names.getKey());

        return sb.toString();
    }

    private String createQuery(String table, String column, String sIds) {
        ITableColumnNames names = getTableColumnNames(table);
        if (names == null || names.getKey() == null) {
            throw new IllegalStateException("Update query for " + table + " can not be created: key column is not provided");
        }
        final QueryBuilder sb = new QueryBuilder();
        sb.append("UPDATE ").append(table);
        sb.append(" SET ").appendColumnParam(column);
        sb.appendNextColumnParam(names.getUpdateDate());
        sb.appendNextColumnParam(names.getUpdateUser());
        sb.append(" WHERE ");
        if (sIds != null) {
            sb.append(names.getKey()).append(sIds);
        } else {
            sb.appendColumnParam(names.getKey());
        }
        return sb.toString();
    }

    protected ITableColumnNames getTableColumnNames(String table) {
        return DEFAULT_TABLE_COLUMN_NAMES;
    }

    protected String getCurrentUserName() {
        return SessionUtils.getCurrentUserName(getSessionContext());
    }

    public static class QueryBuilder {

        private final StringBuilder buf = new StringBuilder();

        public QueryBuilder append(String s) {
            buf.append(s);
            return this;
        }

        public QueryBuilder appendNextColumnParam(String columnName) {
            if (isValidName(columnName)) {
                buf.append(", ");
                appendColumnParam(columnName);
            }
            return this;
        }

        public QueryBuilder appenAndColumnParam(String columnName) {
            if (isValidName(columnName)) {
                buf.append(" and ");
                appendColumnParam(columnName);
            }
            return this;
        }

        public QueryBuilder appendColumnParam(String columnName) {
            if (isValidName(columnName)) {
                buf.append(columnName).append(" = :").append(columnName);
            }
            return this;
        }

        private boolean isValidName(String columnName) {
            return columnName != null && !columnName.trim().isEmpty();
        }

        @Override
        public String toString() {
            return buf.toString();
        }
    }

    protected interface ITableColumnNames {

        String getKey();

        String getUpdateDate();

        String getUpdateUser();
    }

    protected static class TableColumnNames implements ITableColumnNames {

        private final String key;
        private final String updateDate;
        private final String updateUser;

        public TableColumnNames(String key, String updateDate, String updateUser) {
            this.key = key;
            this.updateDate = updateDate;
            this.updateUser = updateUser;
        }

        public String getKey() {
            return key;
        }

        public String getUpdateDate() {
            return updateDate;
        }

        public String getUpdateUser() {
            return updateUser;
        }
    }
}
