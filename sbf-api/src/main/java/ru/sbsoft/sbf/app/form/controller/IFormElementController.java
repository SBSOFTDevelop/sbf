package ru.sbsoft.sbf.app.form.controller;

import ru.sbsoft.sbf.app.form.*;

/**
 * Описывает основное поведение элемента формы в части работы с данными.
 *
 * @author Fedor Resnyanskiy
 * @param <MODEL> класс модели
 */
public interface IFormElementController<MODEL> extends
        IWritable<MODEL>,
        IReadable<MODEL>,
        IReadOnly,
        IValidatable,
        IHasFormElement {
}
