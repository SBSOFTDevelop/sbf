package ru.sbsoft.sbf.app.form;

/**
 *
 * @author Fedor Resnyanskiy
 */
public interface IValidatable {

    /**
     * Сбрасывает на элементе все ошибки валидации, если такие есть.
     */
    void resetValidationErrors();

    /**
     * Запускает проверку значения на элементе.
     */
    void validate();

    /**
     * Возвращает статус валидности элемента.
     *
     * @return true, если нет ошибок валидации, иначе false.
     */
    boolean isValid();
}
