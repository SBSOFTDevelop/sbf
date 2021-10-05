package ru.sbsoft.system.dao.common.helpers;

import java.math.BigDecimal;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import ru.sbsoft.system.common.MultiOperationEntity;
import ru.sbsoft.system.common.MultiOperationLogEntity;

public class OperationLogDaoHelper {

    public static List<MultiOperationLogEntity> loadLogs(final EntityManager em, final Long operationId, final Long lastLogId) {

        final TypedQuery<MultiOperationLogEntity> query = em.createQuery("select o from MultiOperationLogEntity o where o.OPERATION = :operation and o.RECORD_ID > :startFrom ORDER BY o.RECORD_ID DESC", MultiOperationLogEntity.class);
        query.setParameter("operation", em.find(MultiOperationEntity.class, operationId));
        query.setParameter("startFrom", new BigDecimal(lastLogId == null ? 0L : lastLogId));
        return query.getResultList();
    }

}
