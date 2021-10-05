package ru.sbsoft.client.utils;

import com.sencha.gxt.data.shared.ListStore;
import ru.sbsoft.client.components.form.ComboBox;

/**
 * @author balandin
 * @since Jun 8, 2015 7:22:34 PM
 */
public class ComboBoxes {

    public static Object checkValue(ComboBox cb, boolean fireEvents, boolean selectFirstIfNotExist) {
        Object v = cb.getValue();
        cb.setValue(null, false);

        applyFilters(cb);

        v = (v == null) ? null : findValue(cb, v);
        if (v == null && selectFirstIfNotExist && cb.getStore().size() > 0) {
            v = cb.getStore().get(0);
        }

        cb.setValue(v, fireEvents, false);
        cb.redraw(true);

        return v;
    }

    public static <T> T findValue(ComboBox cb, T value) {
        final ListStore store = cb.getStore();
        for (int i = 0, size = store.size(); i < size; i++) {
            if (value == store.get(i)) {
                return value;
            }
        }
        return null;
    }

    public static void applyFilters(ComboBox cb) {
        cb.getStore().setEnableFilters(false);
        cb.getStore().setEnableFilters(true);
    }
}
