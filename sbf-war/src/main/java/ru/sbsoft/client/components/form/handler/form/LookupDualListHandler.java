package ru.sbsoft.client.components.form.handler.form;

import ru.sbsoft.svc.core.client.ValueProvider;
import java.util.List;
import ru.sbsoft.sbf.app.model.IFormModel;
import ru.sbsoft.shared.model.LookupInfoModel;

/**
 *
 * @author Kiselev
 * @param <M> outer model type
 */
public class LookupDualListHandler<M extends IFormModel> extends ru.sbsoft.client.components.form.handler.LookupDualListHandler<LookupDualListHandler<M>> implements IFormFieldHandler<M> {

    private final ValueProvider<? super M, List<LookupInfoModel>> valueProvider;

    public LookupDualListHandler(String label, ValueProvider<? super M, List<LookupInfoModel>> valueProvider,ValueProvider<? super IFormModel, List<LookupInfoModel>> allValuesProvider) {
        super(null, label, allValuesProvider);
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
