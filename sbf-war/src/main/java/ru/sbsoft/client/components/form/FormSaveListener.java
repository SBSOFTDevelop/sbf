package ru.sbsoft.client.components.form;

import ru.sbsoft.sbf.app.model.IFormModel;

/**
 *
 * @author Kiselev
 * @param <M>
 */
public interface FormSaveListener<M extends IFormModel> {

    void onSave(BaseValidatedForm<M> form);
}
