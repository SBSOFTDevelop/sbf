package ru.sbsoft.client.components.form.handler.form;

import com.sencha.gxt.core.client.ValueProvider;
import ru.sbsoft.sbf.app.model.IFormModel;

/**
 *
 * @author Kiselev
 * @param <M>
 */
public class IntHandler<M extends IFormModel> extends ru.sbsoft.client.components.form.handler.IntHandler<IntHandler<M>> implements IFormFieldHandler<M> {
    private final ValueProvider<? super M, Integer> valueProvider;

    public IntHandler(String label, ValueProvider<? super M, Integer> valueProvider) {
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
