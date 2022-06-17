package ru.sbsoft.shared.meta.filter;

import java.util.Set;
import ru.sbsoft.shared.param.DTO;

/**
 *
 * @author Kiselev
 */
public interface IStoredFilterList extends DTO {

    Set<StoredFilterPath> get(StoredFilterType type);
    
    boolean isEmpty();

}
