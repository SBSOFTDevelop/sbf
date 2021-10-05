package ru.sbsoft.client.components;

import java.util.List;
import ru.sbsoft.shared.FilterInfo;

/**
 * Источник дополнительных фильтров данных для браузера {@code LookupField}.
 * Обычно используется для фильтрации выбора в {@code LookupField} в зависимости от контекста,
 * например, состояния других полей формы.
 * @see ru.sbsoft.client.components.form.LookupField#LookupField(boolean, ru.sbsoft.client.components.IFilterSource) 
 * @author balandin
 * @since Jan 25, 2013 6:34:06 PM
 */
public interface IFilterSource {
    
    public List<FilterInfo> getFilters();

}
