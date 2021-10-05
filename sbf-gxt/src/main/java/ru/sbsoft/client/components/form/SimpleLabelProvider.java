package ru.sbsoft.client.components.form;

import com.sencha.gxt.data.shared.LabelProvider;
import com.sencha.gxt.widget.core.client.form.ComboBox;

/**
 * Возвращает метку заданного обекта как {@code Object.toString()}.
 * Используется про создании потомков {@link ComboBox}.
 * @author balandin
 * @since Mar 29, 2013 2:56:15 PM
 */
public class SimpleLabelProvider<T> implements LabelProvider<T> {

    @Override
    public String getLabel(T item) {
        return item.toString();
    }
}
