package ru.sbsoft.dao.operations;

import java.util.Date;
import java.util.List;
import ru.sbsoft.shared.model.enums.SchedulerStatus;
import ru.sbsoft.shared.model.operation.IllegalOperationStatusException;
import ru.sbsoft.shared.model.operation.OperationException;

/**
 * Доступ к управлению планировщиком.
 *
 * @author Fedor Resnyanskiy, SBSOFT
 */
public interface ISchedulerDao {

    /**
     * Возвращает список всех планировщиков, доступных для исполнения.
     *
     * @return список всех планировщиков, доступных для исполнения.
     * @throws OperationException
     */
    List<Long> listSchedulersToExecuteJdbc() throws OperationException;

    /**
     * Изменяет статус планировщика. Для исключения коллизий помимо нового статуса указывается
     * старый.
     *
     * @param operationId идентификатор планировщика.
     * @param oldStatus предыдущий статус.
     * @param newStatus новый статус.
     * @throws OperationException
     * @throws IllegalOperationStatusException
     */
    void changeSchedulerStatus(Long operationId, SchedulerStatus oldStatus, SchedulerStatus newStatus) throws OperationException;

    /**
     * Возвращает текущий статус планировщика.
     *
     * @param operationId идентификатор планировщика.
     * @return текущий статус планировщика.
     * @throws OperationException
     */
    SchedulerStatus getSchedulerStatus(Long operationId) throws OperationException;

    /**
     * Создает новую операция, используя данные планировщика.
     *
     * @param schedulerId идентификатор планировщика.
     * @throws OperationException
     */
    void createOperationWithScheduler(Long schedulerId) throws OperationException;

    /**
     * Устанавливает дату очередного срабатывания для планировщика.
     *
     * @param schedulerId идентификатор планировщика.
     * @param nextSchedule дата очередного срабатывания планировщика.
     * @throws OperationException
     */
    //public void setNextSchedule(Long schedulerId, Date nextSchedule) throws OperationException;

    /**
     * Получает дату очередного срабатывания для планировщика.
     *
     * @param schedulerId идентификатор планировщика.
     * @return дату очередного срабатывания для планировщика.
     * @throws OperationException
     */
    Date getNextSchedule(Long schedulerId) throws OperationException;

    /**
     * Вычисляет возможность запуска планировщика.
     *
     * @param schedulerId идентификатор планировщика.
     * @return true, если планировщик можно запустить, иначе false.
     * @throws OperationException
     */
    boolean isEnabled(Long schedulerId) throws OperationException;

    /**
     * Добавляет в журнал планировщика новую связанную с ним задачу.
     *
     * @param schedulerId идентификатор планировщика.
     * @param operationId добавляемая в журнал операция.
     * @throws OperationException
     */
    //public void addScheduledOperation(Long schedulerId, Long operationId) throws OperationException;
}
