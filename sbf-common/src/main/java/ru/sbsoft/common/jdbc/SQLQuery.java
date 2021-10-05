package ru.sbsoft.common.jdbc;

import java.io.Closeable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.sbsoft.common.DB;
import ru.sbsoft.common.SQLParameterProcessor;
import ru.sbsoft.common.Strings;

/**
 * @author balandin
 * @since May 6, 2014 1:12:53 PM
 */
public class SQLQuery implements Closeable {

    private static final String CONTROL_PARAM = "sbsoft.jdbc.logging.level";
    private static final Logger LOGGER = LoggerFactory.getLogger(SQLQuery.class);
    //
    private final QueryContext context;
    //
    private PreparedStatement statement = null;
    private ResultSet resultSet = null;
    //
    private int paramIndex;
    private List<String> paramNames;

    static {
        final String s = System.getProperties().getProperty(CONTROL_PARAM);
        if (s != null) {
            java.util.logging.Logger.getLogger(SQLQuery.class.getName()).setLevel(Level.parse(s));
        }
    }

    public SQLQuery(QueryContext context) {
        this.context = context;
    }

    public List<String> getParamNames() {
        if (paramNames == null) {
            paramNames = new ArrayList<String>();
        }
        return paramNames;
    }

    private String getNextParamName() {
        return "_" + String.valueOf(++paramIndex);
    }

    public ResultSet query(Connection c, String sql) throws SQLException {
        resetState();
        String jdbcSql = parse(sql);
        LOGGER.debug("\n" + jdbcSql + logJDBCParams());
        statement = c.prepareStatement(jdbcSql);
        setParameters();
        return resultSet = statement.executeQuery();
    }

    private String parse(String sql) {
        return SQLParameterProcessor.process(sql, new SQLParameterProcessor.ParameterListener() {
            @Override
            public Object getParameterValue(String key) {
                return context.get(key).getValue();
            }

            @Override
            public void onListElement(QueryParam param) {
                String elementName = getNextParamName();
                context.put(elementName, param);
                getParamNames().add(elementName);
            }

            @Override
            public void onElement(String key) {
                getParamNames().add(key);
            }
        }).toString();
    }

    public String logJDBCParams() throws SQLException {
        if (getParamNames().isEmpty()) {
            return Strings.EMPTY;
        }
        ArrayList<Object> tmp = new ArrayList<Object>();
        for (String key : getParamNames()) {
            tmp.add(context.get(key));
        }
        return "\nbind [" + Strings.join(tmp.toArray(), ", ") + "]\n";
    }

    public void setParameters() throws SQLException {
        if (paramNames != null) {
            int index = 1;
            for (String key : paramNames) {
                QueryPapamHelper.setJdbcParameter(statement, context.get(key), index++);
            }
        }
    }

    private void resetState() {
        DB.close(resultSet);
        DB.close(statement);
        paramNames = null;
        paramIndex = 0;
    }

    @Override
    public void close() {
        resetState();
    }
}
