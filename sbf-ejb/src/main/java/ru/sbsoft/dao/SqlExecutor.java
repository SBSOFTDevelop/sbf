package ru.sbsoft.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
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
    private final Connection conn;
    private final String sql;
    private final Object[] param;
    private int updateCount = -1;

    public SqlExecutor(IJdbcWorkExecutor jdbcExecutor, String sql, Object... param) {
        this.jdbcExecutor = jdbcExecutor;
        this.conn = null;
        this.sql = sql;
        this.param = param;
    }

    public SqlExecutor(Connection conn, String sql, Object... param) {
        this.jdbcExecutor = null;
        this.conn = conn;
        this.sql = sql;
        this.param = param;
    }

    public SqlExecutor exec() throws SQLException {
        exec(null);
        return this;
    }

    public <R> R exec(RsConverter<R> f) throws SQLException {
        if (jdbcExecutor != null) {
            return jdbcExecutor.<R>executeJdbcWork(conn -> {
                return exec(conn, f);
            });
        } else {
            return exec(conn, f);
        }
    }
    
    public <R> List<R> collect(RsConverter<R> f) throws SQLException {
        return exec(rs -> {
            List<R> res = new ArrayList<>();
            while(rs.next()){
                R r = f.apply(rs);
                if(r != null){
                    res.add(r);
                }
            }
            return res;
        });
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

    private static PreparedStatement prepareStatement(Connection conn, String sql, Object... param) throws SQLException {
        PreparedStatement stat = null;
        try {
            stat = conn.prepareStatement(sql);
            int paramLength = param != null ? param.length : 0;
            if (paramLength != stat.getParameterMetaData().getParameterCount()) {
                throw new SQLException("Parameter sizes do not match!");
            }
            if (param != null) {
                for (int i = 0; i < param.length; i++) {
                    if (param[i] == null) {
                        stat.setNull(i + 1, Types.NULL);
                    } else {
                        stat.setObject(i + 1, param[i]);
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

        public R apply(ResultSet t) throws SQLException;
    }
}
