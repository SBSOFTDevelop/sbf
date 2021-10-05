package ru.sbsoft.transaction;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 * Класс, представляющий методы пакетного добавления записей в таблицу СУБД.
 * @author balandin
 * @since Mar 15, 2013 2:23:42 PM
 */
public abstract class SQLBatch implements Serializable {

    private static int BATCH_SIZE = 100;
    private List<Map<String, Object>> records = new ArrayList<Map<String, Object>>(BATCH_SIZE);
    protected long ID = 0;
    protected int paramIndex = 0;
    private final HashMap<Integer, Query> queries = new HashMap<Integer, Query>();
    //
    private final EntityManager entityManager;
    private final String HEADER;
    private final String VALUES;

    public SQLBatch(EntityManager entityManager, String header, int num) {
        super();
        this.entityManager = entityManager;
        this.HEADER = header;
        this.VALUES = createListParameters(num);
    }

    public void addRecord(Map<String, Object> record) throws SQLException {
        records.add(record);
        if (records.size() == BATCH_SIZE) {
            flush();
        }
    }

    public void flush() throws SQLException {
        if (records.size() > 0) {

            final Query query = getQuery(records.size());
            for (Map<String, Object> record : records) {
                setParams(query, ++ID, record);
            }

            query.executeUpdate();
            entityManager.createNativeQuery("COMMIT").executeUpdate();

            records.clear();
            paramIndex = 0;
        }
    }

    private Query getQuery(int num) throws SQLException {
        Query query = queries.get(num);
        if (query == null) {
            queries.put(num, (query = entityManager.createNativeQuery(HEADER + "\n" + repl(VALUES, " UNION ALL\n", records.size()))));
        }
        return query;
    }

    public void execute(EntityManager entityManager) throws SQLException {
        final Query query = entityManager.createNativeQuery(HEADER + "\n" + repl(VALUES, " UNION ALL\n", records.size()));
        for (Map<String, Object> record : records) {
            setParams(query, ++ID, record);
        }
        query.executeUpdate();
    }

    public int nextParamIndex() {
        return ++paramIndex;
    }

    public long getTotalRecordsCount() {
        return ID;
    }

    private static String createListParameters(int count) {
        return "SELECT " + repl("?", ",", count) + " FROM DUAL";
    }

    private static String repl(String src, String delim, int num) {
        StringBuilder b = new StringBuilder();
        for (int i = 0; i < num; i++) {
            if (b.length() != 0) {
                b.append(delim);
            }
            b.append(src);
        }
        return b.toString();
    }

    protected abstract void setParams(Query query, long ID, Map<String, Object> record) throws SQLException;
}
