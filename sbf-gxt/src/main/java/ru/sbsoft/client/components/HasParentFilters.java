package ru.sbsoft.client.components;

import java.util.List;
import ru.sbsoft.shared.FilterInfo;

/**
 *
 * @author vk
 */
public interface HasParentFilters {

    <T extends FilterInfo> void setParentFilters(List<T> fs);

    List<FilterInfo> getParentFilters();
}
