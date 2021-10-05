package ru.sbsoft.client.components.form;

import ru.sbsoft.sbf.app.model.IFormModel;

/**
 *
 * @author Kiselev
 * @param <M>
 */
public interface ModelChangeListener<M extends IFormModel> {
    void modelChanged(M m);
}
