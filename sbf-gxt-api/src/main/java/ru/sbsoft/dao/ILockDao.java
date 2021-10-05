package ru.sbsoft.dao;

import java.math.BigDecimal;
import javax.persistence.LockTimeoutException;

/**
 * Интерфейс для работы с блокировками (сущность LockEntity),реализуется классом
 * {@link ru.sbsoft.system.dao.common.LockDaoBean}.
 *
 * @author rfa
 */
public interface ILockDao {

    public void lock(String destinationType, BigDecimal recordId) throws LockTimeoutException;

    public void deleteLock(String destinationType, BigDecimal recordId) throws LockTimeoutException;

    public boolean isLocked(String destinationType, BigDecimal recordId);

    public void createLockRecord(String destinationType, BigDecimal recordId);
}
