package ru.sbsoft.lookup.provider;

import java.math.BigDecimal;
import javax.persistence.EntityManager;
import ru.sbsoft.dao.entity.BaseEntity;
import ru.sbsoft.shared.model.LookupInfoModel;
import ru.sbsoft.lookup.IEntityLookupProvider;

/**
 *
 * @author Kiselev
 * @param <E>
 * @param <M>
 */
public abstract class BaseLookupProvider<E extends BaseEntity, M extends LookupInfoModel> implements IEntityLookupProvider<E, M>{
    private final Class<E> enClass;
    
    protected BaseLookupProvider(Class<E> enClass) {
        this.enClass = enClass;
    }

    @Override
    public M createLookup(E e, EntityManager em) {
        if(e == null){
            return null;
        }
        M m = createModel();
        if(m.getID() == null && (e.getIdValue() instanceof BigDecimal)){
            m.setID((BigDecimal)e.getIdValue());
        }
        fillModel(m, e, em);
        return m;
    }

    public abstract void fillModel(M m, E e, EntityManager em);
    
    protected void set(M m, String semanticKey, String semanticName) {
        m.setSemanticKey(semanticKey);
        m.setSemanticName(semanticName);
    }

    @Override
    public Class<E> getId(){
        return enClass;
    }
    
}
