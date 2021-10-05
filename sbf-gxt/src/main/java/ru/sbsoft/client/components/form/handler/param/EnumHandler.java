package ru.sbsoft.client.components.form.handler.param;

import ru.sbsoft.shared.interfaces.NamedItem;

/**
 *
 * @author Kiselev
 * @param <V>
 */
public class EnumHandler<V extends Enum<V> & NamedItem> extends ru.sbsoft.client.components.form.handler.EnumHandler<V, EnumHandler<V>> {

    public EnumHandler(NamedItem it) {
        super(it);
    }

    public EnumHandler(String label) {
        super(null, label);
        setParamGen(false);
    }
    
    public EnumHandler(String param, String label) {
        super(param, label);
    }

    public EnumHandler() {
        this((String) null);
    }

}
