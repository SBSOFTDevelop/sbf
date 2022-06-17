package ru.sbsoft.processor;

import ru.sbsoft.shared.model.operation.OperationException;

/**
 * Интерфейс для описания класса запуска операции.
 *
 * @author Fedor Resnyanskiy, SBSOFT
 */
public interface IOperationProcessor {

    /**
     * Запускает выполнение операции.
     *
     * @throws OperationException если во время выполнения произошла ошибка.
     */
    void execute() throws OperationException;

    /**
     * Текущее состояние остановки операции. Операция может и не прерваться,
     * если обработчик не проверяет статус отмены или все итерации операции уже
     * окончены.
     *
     * @return true, если операция остановлена, иначе false.
     * @throws OperationException
     */
    boolean isCanceled() throws OperationException;

    /**
     * Инициализарует операцию контекстом текущего экземпляра.
     *
     * @param operationContext контекст экземпляра операции.
     * @throws OperationException
     */
    void init(ServerOperationContext operationContext) throws OperationException;
}
