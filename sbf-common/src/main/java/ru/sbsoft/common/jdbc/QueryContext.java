package ru.sbsoft.common.jdbc;

/**
 * @author balandin
 * @since May 6, 2014 2:08:28 PM
 */
public interface QueryContext {

    boolean containsKey(String name);

    QueryParam get(String name);

    QueryParam put(String name, QueryParam param);

    QueryContext setParent(QueryContext parent);

    QueryContext getParent();

}
