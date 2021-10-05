package ru.sbsoft.system.dao.common;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.security.PermitAll;
import javax.ejb.*;
import javax.persistence.*;
import ru.sbsoft.dao.ILockDao;
import ru.sbsoft.system.common.LockEntity;

@Stateless
@Remote(ILockDao.class)
@PermitAll
public class LockDaoBean implements ILockDao {

    @PersistenceContext(unitName = ru.sbsoft.common.jdbc.Const.DEFAULT_PERSISTENCE_CTX)
    private EntityManager em;

    @EJB
    private ILockDao thisBean;

    private static final Map lockParamters = new HashMap();

    static {
        lockParamters.put("javax.persistence.lock.timeout", Integer.valueOf(0));
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void lock(String destinationType, BigDecimal operationId) throws LockTimeoutException, PessimisticLockException {
        final List<LockEntity> list = em.createNamedQuery(LockEntity.LOCK)
                .setParameter("dest", destinationType)
                .setParameter("id", operationId)
                .getResultList();

        if (list.isEmpty()) {
            thisBean.createLockRecord(destinationType, operationId);
            lock(destinationType, operationId);
        } else {
            em.refresh(list.get(0), LockModeType.PESSIMISTIC_READ, lockParamters);
        }
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void deleteLock(String destinationType, BigDecimal operationId) throws LockTimeoutException {
        em.createQuery("delete from LockEntity o where o.DESTINATION_TYPE = :type and o.ID = :id")
                .setParameter("type", destinationType)
                .setParameter("id", operationId)
                .executeUpdate();
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public void createLockRecord(String destinationType, BigDecimal recordId) {
        LockEntity lock = new LockEntity();
        lock.setDestinationType(destinationType);
        lock.setId(recordId);
        em.persist(lock);
        em.flush();
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public boolean isLocked(String destinationType, BigDecimal recordId) {
        try {
            lock(destinationType, recordId);
            return false;
        } catch (PessimisticLockException ex) {
            return true;
        }
    }
}
