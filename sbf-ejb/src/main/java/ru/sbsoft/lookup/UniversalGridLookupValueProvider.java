package ru.sbsoft.lookup;

import java.math.BigDecimal;
import javax.persistence.EntityManager;
import ru.sbsoft.dao.entity.BaseEntity;
import ru.sbsoft.meta.lookup.EntityLookupValueProvider;
import ru.sbsoft.shared.meta.Row;
import ru.sbsoft.shared.model.LookupInfoModel;

/**
 *
 * @author Kiselev
 */
public class UniversalGridLookupValueProvider extends EntityLookupValueProvider {
    private Class<? extends BaseEntity> entityClass;

    public UniversalGridLookupValueProvider(String codeColumn, String nameColumn, Class<? extends BaseEntity> entityClass) {
        this(codeColumn, nameColumn, null, entityClass);
    }

    public UniversalGridLookupValueProvider(String codeColumn, String nameColumn, String semanticKeyColumn, Class<? extends BaseEntity> entityClass) {
        super(codeColumn, nameColumn, semanticKeyColumn, null);
        this.entityClass = entityClass;
    }
    
    @Override
    public LookupInfoModel createLookupModel(Row row, EntityManager entityManager) {
        final BigDecimal key = row.getPrimaryKeyValue();
        final BaseEntity entity = entityManager.find(entityClass, key);
        if (entity == null) {
            throw new IllegalStateException("entity not found");
        }
        final LookupInfoModel model = LookupHelper.toLookup(entityManager, entity);
        if (model == null) {
            throw new IllegalStateException("model is null");
        }
        return model;
    }
}
