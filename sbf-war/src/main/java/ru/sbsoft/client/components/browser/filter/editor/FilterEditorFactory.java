package ru.sbsoft.client.components.browser.filter.editor;

import java.util.HashMap;
import ru.sbsoft.shared.FilterEditorConfigBean;
import ru.sbsoft.shared.interfaces.FilterEditorType;

/**
 * Фабрика пользовательских редакторов
 * @author balandin
 * @since Nov 5, 2015
 */
public class FilterEditorFactory {

    private static final HashMap<FilterEditorType, FilterEditorBuilder> items = new HashMap<FilterEditorType, FilterEditorBuilder>();

    private FilterEditorFactory() {
    }

    public static void registr(FilterEditorType type, FilterEditorBuilder constructor) {
        final FilterEditorBuilder c = items.get(type);
        if (c != null) {
            throw new IllegalArgumentException("FilterEditorBuilder already registered " + type);
        }
        items.put(type, constructor);
    }

    public static FilterEditor create(FilterEditorType type, FilterEditorConfigBean config) {
        final FilterEditorBuilder c = items.get(type);
        if (c == null) {
            throw new IllegalArgumentException("FilterEditorBuilder not registered " + type);
        }
        return c.newInstance(config);
    }
}
