package ru.sbsoft.client.components.form;

import ru.sbsoft.sbf.app.model.FormModel;

/**
 * Позволяет получить текущую модель формы.
 *
 * @author rfa
 * @param <MODEL> тип модели формы.
 */
public interface HasFormModel<MODEL extends FormModel> {

    MODEL getModel();
}
