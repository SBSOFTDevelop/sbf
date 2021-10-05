package ru.sbsoft.meta.lookup;

import java.math.BigDecimal;
import javax.persistence.EntityManager;
import ru.sbsoft.shared.meta.Row;
import ru.sbsoft.shared.model.LookupInfoModel;

/**
 * @author balandin
 * @since Feb 26, 2015 1:12:19 PM
 */
public class EntityLookupValueProvider extends LookupValueProvider {

    private Class<? extends ILookupEntity> entityClass;

    public EntityLookupValueProvider(String codeColumn, String nameColumn, Class<? extends ILookupEntity> entityClass) {
        this(codeColumn, nameColumn, null, entityClass);
    }

    public EntityLookupValueProvider(String codeColumn, String nameColumn, String semanticKeyColumn, Class<? extends ILookupEntity> entityClass) {
        super(codeColumn, nameColumn, semanticKeyColumn);
        this.entityClass = entityClass;
    }

    public LookupInfoModel createLookupModel(Row row, EntityManager entityManager) {
        final BigDecimal key = row.getPrimaryKeyValue();
        final ILookupEntity entity = entityManager.find(entityClass, key);
        if (entity == null) {
            throw new IllegalStateException("entity not found");
        }
        final LookupInfoModel model = entity.toLookupModel();
        if (model == null) {
            throw new IllegalStateException("model is null");
        }
        return model;
    }
}
