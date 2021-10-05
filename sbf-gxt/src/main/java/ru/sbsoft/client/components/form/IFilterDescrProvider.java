package ru.sbsoft.client.components.form;

import java.util.List;
import ru.sbsoft.shared.FilterInfo;

/**
 *
 * @author Kiselev
 */
public interface IFilterDescrProvider {

    String getFilterDescr(List<FilterInfo> filters);
}
