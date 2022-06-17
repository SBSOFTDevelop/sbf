package ru.sbsoft.meta.context;

import ru.sbsoft.common.jdbc.QueryContext;

import java.util.HashMap;
import java.util.Map;

import ru.sbsoft.common.jdbc.QueryParam;

/**
 * Абстрактный базовый класс для всех *Context классов, представляющих методы
 * (интерфейса {@link ru.sbsoft.common.jdbc.QueryContext}) для работы с
 * параметрами (экземплярами классов, реализующих интерфейс
 * {@link ru.sbsoft.common.jdbc.QueryParam}) запросов.
 *
 * @author balandin
 * @since May 5, 2014 8:04:44 PM
 */
public abstract class AbstractQueryContext implements QueryContext {

    private QueryContext parent;
    private Map<String, QueryParam> params;

    @Override
    public AbstractQueryContext setParent(QueryContext parent) {
        this.parent = parent;
        return this;
    }

    @Override
    public QueryContext getParent() {
        return parent;
    }

    @Override
    public boolean containsKey(String name) {
        final QueryParam param = get(name);
        return param != null && param.getValue() != null;
    }

    @Override
    public final QueryParam get(String name) {
        QueryParam qp = getParam(name);
        if (qp != null) {


            return qp;
        }
        if (getParent() != null) {
            return getParent().get(name);
        }
        return null;
    }

    @Override
    public QueryParam put(String name, QueryParam param) {

        return getParams().put(name, param);
    }

    private Map<String, QueryParam> getParams() {
        if (params == null) {
            params = new HashMap<>();
        }
        return params;
    }

    protected QueryParam getParam(String name) {
        return params != null ? params.get(name) : null;

    }

}
