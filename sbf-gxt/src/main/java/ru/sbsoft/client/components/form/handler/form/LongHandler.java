package ru.sbsoft.client.components.form.handler.form;

import com.sencha.gxt.core.client.ValueProvider;
import ru.sbsoft.sbf.app.model.IFormModel;

/**
 *
 * @author Kiselev
 * @param <M>
 */
public class LongHandler<M extends IFormModel> extends ru.sbsoft.client.components.form.handler.LongHandler<LongHandler<M>> implements IFormFieldHandler<M> {
    private final ValueProvider<? super M, Long> valueProvider;

    public LongHandler(String label, ValueProvider<? super M, Long> valueProvider) {
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
