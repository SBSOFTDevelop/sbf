package ru.sbsoft.meta.filter;

import ru.sbsoft.shared.interfaces.NamedItem;
import ru.sbsoft.shared.interfaces.NumeratedItem;
import ru.sbsoft.shared.meta.filter.FilterTemplateConfig;

/**
 * Конфигуратор типизированных фильтров.
 * @author Kiselev
 * @see ru.sbsoft.dao.AbstractTemplate
 */
public interface FilterColumnConfig extends FilterTemplateConfig<FilterColumnConfig> {

    FilterColumnConfig vals(NamedItem... values);
    
    FilterColumnConfig vals(NumeratedItem... values);

    FilterColumnConfig descr(String s);
    
    FilterColumnConfig setFormat(String format);

}
