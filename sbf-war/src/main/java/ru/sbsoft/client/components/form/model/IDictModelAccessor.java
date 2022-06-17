package ru.sbsoft.client.components.form.model;

import ru.sbsoft.svc.core.client.ValueProvider;
import ru.sbsoft.sbf.app.model.IDictModel;

/**
 *
 * @author Kiselev
 * @param <M>
 */
public interface IDictModelAccessor<M extends IDictModel> extends IFormModelAccessor<M> {

    ValueProvider<IDictModel, String> code();

    ValueProvider<IDictModel, String> name();
}
