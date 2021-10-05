package ru.sbsoft.client.components.form.handler.form;

import com.sencha.gxt.core.client.ValueProvider;
import ru.sbsoft.sbf.app.model.IFormModel;

/**
 *
 * @author sokolov
 * @param <M>
 */
public class RegExpMaskHandler<M extends IFormModel> extends ru.sbsoft.client.components.form.handler.RegExpMaskHandler<RegExpMaskHandler<M>> implements IFormFieldHandler<M> {
    
    private final ValueProvider<? super M, String> valueProvider;

    public RegExpMaskHandler(String label, ValueProvider<? super M, String> valueProvider) {
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
