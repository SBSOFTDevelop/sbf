package ru.sbsoft.client.components.form;

import ru.sbsoft.sbf.app.model.IFormModel;

/**
 *
 * @author vk
 * @param <M>
 */
public interface IFormToModelHandler<M extends IFormModel> {

    void toModel(M model);
}
