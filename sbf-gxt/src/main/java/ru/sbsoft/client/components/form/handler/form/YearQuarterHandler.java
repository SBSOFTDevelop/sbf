package ru.sbsoft.client.components.form.handler.form;

import com.sencha.gxt.core.client.ValueProvider;
import ru.sbsoft.sbf.app.model.IFormModel;
import ru.sbsoft.shared.model.YearQuarter;

/**
 *
 * @author Kiselev
 * @param <M>
 */
public class YearQuarterHandler<M extends IFormModel> extends ru.sbsoft.client.components.form.handler.YearQuarterHandler<YearQuarterHandler<M>> implements IFormFieldHandler<M> {

    private final ValueProvider<? super M, YearQuarter> valueProvider;

    public YearQuarterHandler(String label, ValueProvider<? super M, YearQuarter> valueProvider) {
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
