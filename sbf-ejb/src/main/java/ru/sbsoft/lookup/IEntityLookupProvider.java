package ru.sbsoft.lookup;

import javax.persistence.EntityManager;
import ru.sbsoft.dao.entity.BaseEntity;
import ru.sbsoft.shared.interfaces.IdItem;
import ru.sbsoft.shared.model.LookupInfoModel;

/**
 *
 * @author Kiselev
 * @param <E>
 * @param <M>
 */
public interface IEntityLookupProvider<E extends BaseEntity, M extends LookupInfoModel> extends IdItem<Class<E>> {

    M createLookup(E e, EntityManager em);

    M createModel();
}
