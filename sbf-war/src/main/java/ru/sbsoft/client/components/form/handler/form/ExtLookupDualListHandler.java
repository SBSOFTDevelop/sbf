package ru.sbsoft.client.components.form.handler.form;

import ru.sbsoft.svc.core.client.ValueProvider;
import java.util.List;
import ru.sbsoft.client.components.list.cell.CustomStyleCell;
import ru.sbsoft.sbf.app.model.IFormModel;
import ru.sbsoft.shared.model.LookupInfoModel;

/**
 *
 * @author sychugin
 */
public class ExtLookupDualListHandler<L extends LookupInfoModel, M extends IFormModel> extends ru.sbsoft.client.components.form.handler.ExtLookupDualListHandler<L, M, ExtLookupDualListHandler<L, M>> implements IFormFieldHandler<M> {

    private final ValueProvider<? super M, List<L>> valueProvider;

    public ExtLookupDualListHandler(String label, ValueProvider<? super M, List<L>> valueProvider, ValueProvider<M, List<L>> outValueProvider, CustomStyleCell cell, boolean withKeyInTitle) {

        super(null, label, outValueProvider, cell, withKeyInTitle);
        this.valueProvider = valueProvider;

    }

    public ExtLookupDualListHandler(String label, ValueProvider<? super M, List<L>> valueProvider, ValueProvider<M, List<L>> outValueProvider, CustomStyleCell cell) {

        this(label, valueProvider, outValueProvider, cell, false);
    }

    public ExtLookupDualListHandler(ValueProvider<? super M, List<L>> valueProvider, ValueProvider<M, List<L>> outValueProvider, CustomStyleCell cell) {
        this(null, valueProvider, outValueProvider, cell, true);
    }

    public ExtLookupDualListHandler(ValueProvider<? super M, List<L>> valueProvider, ValueProvider<M, List<L>> outValueProvider, CustomStyleCell cell, boolean withKeyInTitle) {

        this(null, valueProvider, outValueProvider, cell, withKeyInTitle);

    }

    @Override
    public void fromModel(M model) {

        setModel(model);
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
