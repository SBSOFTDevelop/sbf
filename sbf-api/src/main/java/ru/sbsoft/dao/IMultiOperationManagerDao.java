package ru.sbsoft.dao;

import ru.sbsoft.shared.interfaces.OperationType;
import ru.sbsoft.shared.model.operation.NoSuchOperationCodeException;
import ru.sbsoft.shared.model.operation.OperationException;

/**
 * Компонент для серверного запуска операций.
 *
 * @author Fedor Resnyanskiy, SBSOFT
 */
public interface IMultiOperationManagerDao {

    /**
     * Возвращает тип операции по заданному типу.
     *
     * @param operationCode код операции для получения типа.
     * @return тип операции по заданному типу
     * @throws OperationException
     * @throws NoSuchOperationCodeException
     */
    OperationType getOperationTypeForCode(String operationCode) throws OperationException, NoSuchOperationCodeException;

    /**
     * Запускает поиск и запуск операций.
     */
    
    
    
    
    //public void startOperationIteration();

    /**
     * Запускет операцию из внешнего бина -например из MDB бина.
     *
     * @param operationId идентификатор операции для запуска.
     * @param operationCode код операции для запуска.
     */

    void executeOperation(Long operationId, String operationCode);


    /**
     * Запускет операцию.
     *
     * @param operationId идентификатор операции для запуска.
     */
    //public void executeOperation(Long operationId);

    /**
     * Создает глобальную блокировку БД и запускает операцию на выполнение.
     * Блокировка БД нужна для возможности остлеживать системные ошибки при
     * выполнении операций. Если операция не завершена, а блокировки нет -
     * произошла системная ошибка.
     *
     * @param operationId операция для блокировки и выполнения.
     */
    //public void lockAndStartOperation(Long operationId);

    /**
     * Удаляет запись ранее созданной операции из базы. Сама блокировка исчезает
     * в момент окончания транзакции опецрации.
     *
     * @param operationId операция для удаления блокировки.
     */
   // public void unlockOperation(Long operationId);

}
