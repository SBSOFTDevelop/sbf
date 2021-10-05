package ru.sbsoft.client.components.form;

import ru.sbsoft.sbf.app.model.IFormModel;

/**
 *
 * @author vk
 * @param <M>
 */
public interface IFormDataHandler<M extends IFormModel> extends IFormFromModelHandler<M>, IFormToModelHandler<M> {
}
