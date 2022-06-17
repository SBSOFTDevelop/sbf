package ru.sbsoft.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author kiselev
 */
public class SqlExecutor {

    private static final Logger log = LoggerFactory.getLogger(SqlExecutor.class);

    private final IJdbcWorkExecutor jdbcExecutor;
    private final String sql;
    private final List<?> param;
    private int updateCount = -1;

    public SqlExecutor(IJdbcWorkExecutor jdbcExecutor, String sql, List<?> param) {
        this.jdbcExecutor = jdbcExecutor;
        this.sql = sql;
        this.param = param;
    }
    
    public SqlExecutor(IJdbcWorkExecutor jdbcExecutor, String sql, Object... param) {
        this(jdbcExecutor, sql, (List<?>)(param != null && param.length > 0 ? Arrays.asList(param) : null));
    }

    public SqlExecutor(final Connection conn, String sql, Object... param) {
        this(new ConnectionWrapper(conn), sql, param);
    }

    public SqlExecutor exec() throws SQLException {
        exec(null);
        return this;
    }

    public <R> R exec(RsConverter<R> f) throws SQLException {
        return jdbcExecutor.<R>executeJdbcWork(conn -> {
            return exec(conn, f);
        });
    }

    public <R, L extends List<R>> L collect(RsConverter<R> f, final L list) throws SQLException {
        return exec(rs -> {
            while (rs.next()) {
                R r = f.apply(rs);
                if (r != null) {
                    list.add(r);
                }
            }
            return list;
        });
    }

    public <R> List<R> collect(RsConverter<R> f) throws SQLException {
        return collect(f, new ArrayList<R>());
    }

    public int getUpdateCount() {
        return updateCount;
    }

    private <R> R exec(Connection conn, RsConverter<R> f) throws SQLException {
        try (PreparedStatement stat = prepareStatement(conn, sql, param)) {
            if (f != null) {
                try (ResultSet rs = stat.executeQuery()) {
                    return f.apply(rs);
                }
            } else {
                stat.execute();
                updateCount = stat.getUpdateCount();
                return null;
            }
        }
    }

    private static PreparedStatement prepareStatement(Connection conn, String sql, List<?> param) throws SQLException {
        PreparedStatement stat = null;
        try {
            stat = conn.prepareStatement(sql);
            int paramLength = param != null ? param.size() : 0;
            if (paramLength != stat.getParameterMetaData().getParameterCount()) {
                throw new SQLException("Parameter sizes do not match!");
            }
            if (param != null) {
                for (int i = 0; i < paramLength; i++) {
                    if (param.get(i) == null) {
                        stat.setNull(i + 1, Types.NULL);
                    } else {
                        stat.setObject(i + 1, param.get(i));
                    }
                }
            }
        } catch (SQLException | RuntimeException ex) {
            if (stat != null) {
                try {
                    stat.close();
                } catch (Exception ex2) {
                    log.error("Can't close db statement", ex2);
                }
            }
            throw ex;
        }
        return stat;
    }

    public interface RsConverter<R> {

        R apply(ResultSet t) throws SQLException;
    }

    private static class ConnectionWrapper implements IJdbcWorkExecutor {

        private final Connection conn;

        public ConnectionWrapper(Connection conn) {
            this.conn = conn;
        }

        @Override
        public <T> T executeJdbcWork(IJdbcWork<T> work) throws SQLException {
            return work.execute(conn);
        }
    }
}
