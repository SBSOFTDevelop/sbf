package ru.sbsoft.operation;

/**
 * @author balandin
 * @since Mar 13, 2013 7:50:59 PM
 */
public interface ProgressCallback {

    /**
     * 
     * Уведомляет раннер о проделанной работе
     * Возвращаемое булевское значение служит индикатором прерывания операции 
     * 
     * @param done - количество обработанных чегото там
     */
    void work(int done) throws InterruptedException;
}
