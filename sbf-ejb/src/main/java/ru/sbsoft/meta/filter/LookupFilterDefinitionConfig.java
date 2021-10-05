package ru.sbsoft.meta.filter;

import ru.sbsoft.shared.Modifier;

/**
 *
 * @author Kiselev
 */
public interface LookupFilterDefinitionConfig extends FilterDefinitionConfig<LookupFilterDefinitionConfig> {

    LookupFilterDefinitionConfig mod(Modifier modifier);
}
