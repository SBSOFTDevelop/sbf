package ru.sbsoft.client.components.form;

import com.google.gwt.user.client.rpc.AsyncCallback;
import ru.sbsoft.shared.FormContext;
import ru.sbsoft.sbf.app.model.IFormModel;
import ru.sbsoft.shared.model.MarkModel;

/**
 *
 * @author Kiselev
 * @param <M>
 */
public interface IFormFactory<M extends IFormModel, R extends MarkModel> {

    void createEditForm(final R model, AsyncCallback<BaseForm<M>> callback);

    FormContext getFormContext(R model);
}
