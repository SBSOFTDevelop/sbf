package ru.sbsoft.client.components.browser.filter;

import ru.sbsoft.shared.FilterInfo;

/**
 *
 * @author Kiselev
 */
public interface IFilterInfoProvider {
    FilterInfo getFilterInfo(boolean system);
}
