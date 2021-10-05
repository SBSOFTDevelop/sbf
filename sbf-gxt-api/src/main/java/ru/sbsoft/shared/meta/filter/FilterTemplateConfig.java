package ru.sbsoft.shared.meta.filter;

import ru.sbsoft.shared.Condition;

/**
 *
 * @author Kiselev
 */
public interface FilterTemplateConfig<SELF extends FilterTemplateConfig<?>> {

    SELF strict();

    SELF req();
    
    SELF op(Condition condition);
}
