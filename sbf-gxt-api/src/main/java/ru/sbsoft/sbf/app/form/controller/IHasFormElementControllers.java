package ru.sbsoft.sbf.app.form.controller;

/**
 *
 * @author Fedor Resnyanskiy
 */
public interface IHasFormElementControllers<MODEL> {
    
    /**
     * Добавляет зависимый элемент формы. Этот элемент будет оповещаться об
     * изменениях модели, а также информация об изменении будет получаться из
     * него.
     *
     * @param formElement элемент формы.
     */
    void addFormElementController(IFormElementController<MODEL> formElement);

    /**
     * Удаляет зависимый элемент формы.
     *
     * @param formElement
     */
    void removeFormElementController(IFormElementController<MODEL> formElement);
}
