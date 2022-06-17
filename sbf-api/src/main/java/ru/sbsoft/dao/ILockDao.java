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

    void lock(String destinationType, BigDecimal recordId) throws LockTimeoutException;

    void deleteLock(String destinationType, BigDecimal recordId) throws LockTimeoutException;

    boolean isLocked(String destinationType, BigDecimal recordId);

    void createLockRecord(String destinationType, BigDecimal recordId);
}
