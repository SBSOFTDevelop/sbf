package ru.sbsoft.client.components.form;

import ru.sbsoft.client.components.form.BaseForm;
import ru.sbsoft.sbf.app.model.FormModel;

/**
 *
 * @author Kiselev
 * @param <M>
 */
public interface IParentFormAware<M extends FormModel> {

    void setParentForm(BaseForm<M> f);
}
