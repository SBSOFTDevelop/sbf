package ru.sbsoft.client.components.form;

import ru.sbsoft.sbf.app.model.IFormModel;

/**
 *
 * @author Kiselev
 * @param <M>
 */
public interface HasParentModelListener<M extends IFormModel> {

    ModelChangeListener<M> getParentModelListener();
}
