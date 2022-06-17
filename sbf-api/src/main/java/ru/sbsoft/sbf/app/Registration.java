package ru.sbsoft.sbf.app;

/**
 * Используется для возможности удалить привязку внешнего агента на listener или другой объект, переданный на использование.
 *
 * @author Fedor Resnyanskiy
 */
public interface Registration {

    void remove();
}
