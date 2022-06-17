package ru.sbsoft.db.query;

import ru.sbsoft.common.jdbc.QueryParam;
import ru.sbsoft.common.jdbc.QueryParamImpl;
import ru.sbsoft.common.jdbc.QueryPapamHelper;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TemporalType;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.context.Context;
import ru.sbsoft.common.DBType;
import ru.sbsoft.common.SQLParameterProcessor;
import ru.sbsoft.common.Strings;
import ru.sbsoft.common.SQLParameterProcessor.ParameterListener;

public class VelosityTemplateQuery {

    private final EntityManager em;
    //private final IDatabaseInfoDao dbinfo;

    private String template;
    private final List<String> sorts = new ArrayList<String>();
    //
    private final Context velocityContext = new VelocityContext();
    private final Map<String, QueryParam> params = new HashMap<String, QueryParam>();
    private final List<String> nativeParamKeys = new ArrayList<String>();
    //
    private int offset = 0;
    private int limit = 0;
    //
    private int paramIndex = 0;

    private boolean isMS_SQL() {
        return DBType.getCurrentType() == DBType.DBTYPE_MSSQL;
    }

    
    public VelosityTemplateQuery(final String template, final EntityManager entityManager) {
        super();
        this.template = template;
        this.em = entityManager;
        velocityContext.put("concat", isMS_SQL() ? "+" : "||");
    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }

    public void setLimit(int limit) {
        if (limit < 0) {
            throw new IllegalArgumentException();
        }
        this.limit = limit;
    }

    public void setOffset(int offset) {
        if (offset < 0) {
            throw new IllegalArgumentException();
        }
        this.offset = offset;
    }

    private String getNextParamName() {
        paramIndex++;
        return "_" + paramIndex;
    }

    public void addSort(final String sort) {
        if (Strings.isEmpty(sort)) {
            throw new IllegalArgumentException();
        }
        sorts.add(sort);
    }

    public VelosityTemplateQuery addParam(final String name, Object value) {
        return internalAddParam(normalize(name), value, null);
    }

    public VelosityTemplateQuery addParam(final String name, Date value, TemporalType tt) {
        return internalAddParam(normalize(name), value, tt);
    }

    public VelosityTemplateQuery addParamList$(final String name, List value) {
        if (value == null || value.isEmpty()) {
            return this;
        }
        List<String> tmp = new ArrayList<String>(value.size());
        for (Object elementValue : value) {
            String elementName = getNextParamName();
            params.put(elementName, new QueryParamImpl(elementValue));
            tmp.add(":" + elementName);
        }
        velocityContext.put(normalize(name), Strings.join(tmp.toArray(), ", "));
        return this;
    }

    private String normalize(String name) {
        return name == null ? null : name.replace('.', '_');
    }

    private VelosityTemplateQuery internalAddParam(String name, Object value, TemporalType temporalType) {
        if (value != null) {
            velocityContext.put(name, value);
            params.put(name, new QueryParamImpl(value, temporalType));
        }
        return this;
    }

    public <T> Query createQuery(Class<T> type) {
        return setParmas(em.createQuery(getQueryString(true), type));
    }

    private Query setParmas(final Query query) {
        for (final String key : params.keySet()) {
            QueryPapamHelper.setQueryParameter(query, params.get(key), key);
        }

        return query;
    }

    public <T> Query createNativeQuery(Class<T> type) {
        // em.unwrap(type)
        return setNativeParams(em.createNativeQuery(getNativeQueryString(true), type));
    }

    public Query createNativeQuery() {

        return setNativeParams(em.createNativeQuery(getNativeQueryString(true)));
    }

    private Query setNativeParams(final Query query) {
        int index = 1;
        for (String key : nativeParamKeys) {
            QueryPapamHelper.setQueryParameter(query, params.get(key), index++);
        }
        return query;
    }

    public String logJDBCParams() throws SQLException {
        if (nativeParamKeys.isEmpty()) {
            return Strings.EMPTY;
        }
        ArrayList<Object> tmp = new ArrayList<Object>();
        for (String key : nativeParamKeys) {
            tmp.add(params.get(key));
        }
        return "\nbind [" + Strings.join(tmp.toArray(), ", ") + "]\n";
    }

    public void setJDBCParams(PreparedStatement statement) throws SQLException {
        int index = 1;
        for (String key : nativeParamKeys) {
            QueryPapamHelper.setJdbcParameter(statement, params.get(key), index++);
        }
    }

    public String getNativeQueryString(final boolean withSort) {
        return getNativeQueryString(withSort, "*");
    }

    private String getNativeQueryString(final boolean withSort, final String column) {
        StringBuilder sb = new StringBuilder(getQueryString(withSort));
        if (offset > 0 || limit > 0) {
            sb = new StringBuilder(frame(sb, column));
        }
        nativeParamKeys.clear();

        return SQLParameterProcessor.process(sb.toString(), new ParameterListener() {
            @Override
            public Object getParameterValue(String key) {
                return params.get(key).getValue();
            }

            @Override
            public void onListElement(QueryParam param) {
                final String elementName = getNextParamName();
                params.put(elementName, param);
                nativeParamKeys.add(elementName);
            }

            @Override
            public void onElement(String key) {
                nativeParamKeys.add(key);
            }
        }).toString();
    }

    private String frame(final CharSequence template, final String column) {
        final StringBuilder s = new StringBuilder();
        s.append("SELECT yyy.* FROM (SELECT xxx.").append(column).append(", ROWNUM rnum FROM (");
        s.append(template).append(") xxx) yyy WHERE (1=1)");
        if (offset > 0) {
            s.append(" AND rnum > :rowOffset");
            addParam("rowOffset", offset);
        }
        if (limit > 0) {
            s.append(" AND rnum <= :rowLimit");
            addParam("rowLimit", limit + offset);
        }
        return s.toString();
    }

    public Query createNativeCountQuery() {
        StringBuilder s = new StringBuilder();
        s.append("SELECT COUNT(*) FROM (\n");
        s.append(getNativeQueryString(false));
        s.append("\n)");
        return setNativeParams(em.createNativeQuery(s.toString()));
    }

    public Query createIDsQuery(final String column, final boolean withSort) {
        final StringBuilder s = new StringBuilder();
        s.append("SELECT ").append(column).append(" from( \n");
        s.append(getNativeQueryString(withSort, column));
        s.append(") \n");
        return setNativeParams(em.createNativeQuery(s.toString()));
    }

    public String getQueryString(boolean withSort) {
        String s = template;
        if (withSort && !sorts.isEmpty()) {
            final StringBuilder tmp = new StringBuilder(template);
            tmp.append(" ORDER BY ");
            tmp.append(Strings.join(sorts.toArray(), ", "));
            tmp.append("\n");
            s = tmp.toString();
        }
        try {
            return VelocityRender.parse(velocityContext, s);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public List<String> getNativeParamKeys() {
        return nativeParamKeys;
    }
}
