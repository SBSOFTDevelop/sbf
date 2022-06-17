package ru.sbsoft.shared.meta;

import java.io.Serializable;
import java.util.List;
import ru.sbsoft.shared.FilterInfo;
import ru.sbsoft.shared.interfaces.ObjectType;

/**
 *
 * @author vk
 */
public interface IValueSelectorConfig<T extends ObjectType> extends Serializable {

    T getBrowserType();

    List<FilterInfo> getFilters();

    boolean isShowCode();
}
