package ru.sbsoft.system.dao.common.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import ru.sbsoft.common.Strings;

/**
 * хэлпер построитель JPA запросов
 *
 * @author balandin
 * @since Jul 31, 2015
 */
public class QueryBuilder {
    public static final String OPERAND_EQ = "=";
    public static final String OPERAND_NOT_EQ = "<>";

    private final EntityManager em;
    private final StringBuilder b = new StringBuilder();
    private final List args = new ArrayList();

    public QueryBuilder(EntityManager entityManager) {
        this.em = entityManager;
    }

    public static QueryBuilder create(EntityManager entityManager) {
        return new QueryBuilder(entityManager);
    }

    public QueryBuilder add(String str) {
        b.append(str);
        return this;
    }

    public QueryBuilder prepend(String str) {
        b.insert(0, str);
        return this;
    }

    public QueryBuilder add(Class aClass) {
        b.append(aClass.getSimpleName());
        return this;
    }

    public QueryBuilder eq(String param, Object value) {
        return arg(param, "=", value);
    }

    public QueryBuilder notEq(String param, Object value) {
        return arg(param, "<>", value);
    }

    public QueryBuilder like(String param, Object value) {
        return arg(param, "like", value);
    }

    public QueryBuilder in(String param, Collection<?> value) {
        return arg(param, "in", value);
    }

    public QueryBuilder back(int len) {
        if (b.length() > len) {
            b.setLength(b.length() - len);
        } else {
            b.setLength(0);
        }
        return this;
    }

    public QueryBuilder arg(String param, String operation, Object value) {
        if (value instanceof String) {
            value = Strings.clean((String) value, false);
        }
        b.append(' ').append(param);
        if (value == null) {
            if (operation != null && ("<>".equals(operation.trim()) || "!=".equals(operation.trim()) || "not".equalsIgnoreCase(operation.trim()))) {
                b.append(" is not null");
            } else {
                b.append(" is null");
            }
        } else {
            int n = args.indexOf(value);
            if (n == -1) {
                n = args.size();
                args.add(value);
            }
            b.append(' ').append(operation).append(' ');
            b.append(':').append(getArgName(n));
        }
        return this;
    }

    public Query query() {
        final Query q = em.createQuery(b.toString());
        for (int i = 0, size = args.size(); i < size; i++) {
            q.setParameter(getArgName(i), args.get(i));
        }
        return q;
    }

    private String getArgName(int argIndex) {
        return "arg" + argIndex;
    }
}
