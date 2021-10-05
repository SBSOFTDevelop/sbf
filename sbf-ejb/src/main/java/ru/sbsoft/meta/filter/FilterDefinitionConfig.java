package ru.sbsoft.meta.filter;

import ru.sbsoft.shared.meta.filter.FilterTemplateConfig;

/**
 *
 * @author Kiselev
 */
public interface FilterDefinitionConfig<SELF extends FilterDefinitionConfig<?>> extends FilterTemplateConfig<SELF> {

    SELF hide();

    SELF nosql();
}
