package ru.sbsoft.client.components.form;

import ru.sbsoft.sbf.app.model.FormModel;
import ru.sbsoft.shared.model.MarkModel;

/**
 *
 * @author Kiselev
 * @param <M>
 */
public interface IMultiFormFactory<M extends FormModel, R extends MarkModel> extends IFormFactory<M, R>, IOwnerGridAcceptor {

    String createFormKeyFromModel(final R selectedModel);
}
