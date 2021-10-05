package ru.sbsoft.client.components.form.action;

import ru.sbsoft.client.components.form.BaseValidatedForm;
import ru.sbsoft.sbf.app.model.IFormModel;

/**
 *
 * @author Kiselev
 * @param <M>
 */
public interface UpdateEditorsStateListener<M extends IFormModel> {

    void updateEditorsState(BaseValidatedForm<M> form);
}
