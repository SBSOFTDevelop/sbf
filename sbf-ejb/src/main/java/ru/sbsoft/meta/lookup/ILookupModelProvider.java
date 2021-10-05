package ru.sbsoft.meta.lookup;

import javax.persistence.EntityManager;
import ru.sbsoft.shared.model.LookupInfoModel;

/**
 *
 * @author Kiselev
 */
public interface ILookupModelProvider {

    LookupInfoModel toLookupModel(EntityManager m);
}
