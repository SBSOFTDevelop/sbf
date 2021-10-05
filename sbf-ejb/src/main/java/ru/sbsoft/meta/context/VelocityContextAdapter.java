package ru.sbsoft.meta.context;

import org.apache.velocity.context.Context;
import ru.sbsoft.common.jdbc.QueryParam;
import ru.sbsoft.common.jdbc.QueryContext;

/**
 * Класс представляет собой адаптер-переходник (классический патерн) между
 * Velocity {@link org.apache.velocity.context.Context} и
 * {@link ru.sbsoft.common.jdbc.QueryContext}.
 * <p>
 * Используется парсером построителя запросов
 * {@link ru.sbsoft.dao.AbstractBuilder} для передачи QueryContext-а Velocity
 * парсеру.
 * <pre>
 * public abstract class AbstractBuilder implements BrowserDaoBean<Row> {
 *
 * ...
 * private String parse(final QueryContext context, final String template) {
 * try {
 * return VelocityRender.parse(new VelocityContextAdapter(context), template);
 * } catch (Exception ex) {
 * throw new EJBException(ex);
 * }
 * }
 * }
 * </pre>
 *
 * @author balandin
 * @since May 7, 2014 4:50:33 PM
 */
public class VelocityContextAdapter implements Context {

    private final QueryContext context;

    public VelocityContextAdapter(QueryContext context) {
        this.context = context;
    }

    public Object put(String string, Object o) {
        throw new UnsupportedOperationException();
    }

    public Object get(String string) {
        final QueryParam param = context.get(string);
        if (param != null) {
            return param.getValue();
        }
        return null;
    }

    public boolean containsKey(Object o) {
        return context.containsKey((String) o);
    }

    public Object[] getKeys() {
        throw new UnsupportedOperationException();
    }

    public Object remove(Object o) {
        throw new UnsupportedOperationException();
    }
}
