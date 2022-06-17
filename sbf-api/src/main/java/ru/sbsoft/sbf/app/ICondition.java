package ru.sbsoft.sbf.app;

/**
 * Проверяющее условие.
 *
 * @author Fedor Resnyanskiy
 */
public interface ICondition {

    /**
     *
     * @return true, если условие выполнено, иначе false
     */
    boolean check();
}
