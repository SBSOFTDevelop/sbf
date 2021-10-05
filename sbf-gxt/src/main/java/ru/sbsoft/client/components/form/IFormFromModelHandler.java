package ru.sbsoft.client.components.form;

import ru.sbsoft.sbf.app.model.IFormModel;

/**
 *
 * @author vk
 * @param <M>
 */
public interface IFormFromModelHandler<M extends IFormModel> {

    void fromModel(M model);
}
