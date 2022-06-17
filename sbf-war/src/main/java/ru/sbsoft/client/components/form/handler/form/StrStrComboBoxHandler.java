package ru.sbsoft.client.components.form.handler.form;

import ru.sbsoft.svc.core.client.ValueProvider;
import ru.sbsoft.sbf.app.model.IFormModel;

/**
 *
 * @author Kiselev
 * @param <M>
 */
public class StrStrComboBoxHandler<M extends IFormModel> extends ru.sbsoft.client.components.form.handler.StrStrComboBoxHandler<StrStrComboBoxHandler<M>> implements IFormFieldHandler<M> {

    private final ValueProvider<? super M, String> valueProvider;

    public StrStrComboBoxHandler(String label, ValueProvider<? super M, String> valueProvider) {
        super(null, label);
        this.valueProvider = valueProvider;
    }

    @Override
    public void fromModel(M model) {
        if (valueProvider != null) {
            setVal(valueProvider.getValue(model));
        }
    }

    @Override
    public void toModel(M model) {
        if (valueProvider != null) {
            valueProvider.setValue(model, getVal());
        }
    }
}
