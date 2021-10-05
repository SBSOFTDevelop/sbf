package ru.sbsoft.meta.columns.consts;

import java.util.EnumMap;

/**
 * Класс представляет коллекцию свойств для установки дополнительных параметров колонок
 * <p> (<i>Сортировка, условие отбора, заголовок столбца</i>), которые используются построителем SQL запросов {@link ru.sbsoft.dao.AbstractTemplate}.
 * @author balandin
 * @since Feb 25, 2015 5:33:42 PM
 */
public class Properties {

    protected EnumMap<PropertiesEnum, Object> properties = new EnumMap<PropertiesEnum, Object>(PropertiesEnum.class);

    public Properties() {
    }

    public void put(PropertiesEnum key, Object value) {
        if (value == null) {
            properties.remove(key);
            return;
        }
        if (!key.getType().isAssignableFrom(value.getClass())) {
            throw new IllegalArgumentException();
        }
        properties.put(key, value);
    }

    public <T> T get(Class<T> type, PropertiesEnum key) {
        if (!key.getType().isAssignableFrom(type)) {
            throw new IllegalArgumentException();
        }
        return (T) properties.get(key);
    }

    public boolean contains(PropertiesEnum key) {
        return properties.containsKey(key);
    }
}
