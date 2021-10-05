package ru.sbsoft.client.components.browser.filter.lookup;

import java.util.HashMap;
import ru.sbsoft.client.components.form.LookupField;
import ru.sbsoft.shared.LookupFieldConfigBean;
import ru.sbsoft.shared.interfaces.LookupType;

/**
 * Фабрика лукап полей
 * @author balandin
 * @since Nov 3, 2015
 */
public class LookupFieldFactory {

    private static final HashMap<LookupType, LookupFieldBuilder> items = new HashMap<LookupType, LookupFieldBuilder>();

    private LookupFieldFactory() {
    }

    public static void registr(LookupType type, LookupFieldBuilder constructor) {
        final LookupFieldBuilder c = items.get(type);
        if (c != null) {
            throw new IllegalArgumentException("LookupFieldConstructor already registered " + type);
        }
        items.put(type, constructor);
    }
    
    public static LookupField create(LookupType type, LookupFieldConfigBean config) {
        final LookupFieldBuilder c = items.get(type);
        if (c == null) {
            throw new IllegalArgumentException("LookupFieldConstructor not registered " + type);
        }
        return c.newInstance(config);
    }
}
