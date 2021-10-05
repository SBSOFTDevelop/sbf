package ru.sbsoft.sbf.app.form;

/**
 *
 * @author Fedor Resnyanskiy
 */
public interface IValidatable {

    /**
     * Сбрасывает на элементе все ошибки валидации, если такие есть.
     */
    public void resetValidationErrors();

    /**
     * Запускает проверку значения на элементе.
     */
    public void validate();

    /**
     * Возвращает статус валидности элемента.
     *
     * @return true, если нет ошибок валидации, иначе false.
     */
    public boolean isValid();
}
