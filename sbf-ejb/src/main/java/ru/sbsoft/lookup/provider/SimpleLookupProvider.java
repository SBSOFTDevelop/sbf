package ru.sbsoft.lookup.provider;

import ru.sbsoft.dao.entity.BaseEntity;
import ru.sbsoft.shared.model.LookupInfoModel;

/**
 *
 * @author Kiselev
 * @param <E>
 */
public abstract class SimpleLookupProvider<E extends BaseEntity> extends BaseLookupProvider<E, LookupInfoModel> {

    protected SimpleLookupProvider(Class<E> enClass) {
        super(enClass);
    }

    @Override
    public LookupInfoModel createModel() {
        return new LookupInfoModel();
    }
    
}
