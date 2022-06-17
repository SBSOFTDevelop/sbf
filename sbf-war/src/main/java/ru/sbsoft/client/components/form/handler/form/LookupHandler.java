package ru.sbsoft.client.components.form.handler.form;

import ru.sbsoft.svc.core.client.ValueProvider;
import ru.sbsoft.client.components.ISelectBrowserMaker;
import ru.sbsoft.sbf.app.model.IFormModel;
import ru.sbsoft.shared.model.LookupInfoModel;
import ru.sbsoft.shared.interfaces.NamedGridType;

/**
 *
 * @author Kiselev
 * @param <M>
 */
public class LookupHandler<M extends IFormModel> extends ru.sbsoft.client.components.form.handler.CommonLookupHandler<LookupHandler<M>> implements IFormFieldHandler<M> {

    private final ValueProvider<? super M, LookupInfoModel> valueProvider;

    public LookupHandler(String label, ValueProvider<? super M, LookupInfoModel> valueProvider) {
        this(label, valueProvider, (ISelectBrowserMaker)null);
    }

    public LookupHandler(String label, ValueProvider<? super M, LookupInfoModel> valueProvider, NamedGridType browserType) {
        this(label, valueProvider, browserType, null);
    }
    
    public LookupHandler(String label, ValueProvider<? super M, LookupInfoModel> valueProvider, NamedGridType browserType, String context) {
        super(null, label);
        this.valueProvider = valueProvider;
        if(browserType != null){
            setBrowser(browserType, context);
        }
    }
    
    public LookupHandler(String label, ValueProvider<? super M, LookupInfoModel> valueProvider, ISelectBrowserMaker browserMaker) {
        super(null, label);
        this.valueProvider = valueProvider;
        if (browserMaker != null) {
            setBrowserMaker(browserMaker);
        }
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
