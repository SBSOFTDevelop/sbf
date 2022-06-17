package ru.sbsoft.client.components.form.handler;

import com.google.gwt.event.logical.shared.ValueChangeHandler;
import java.util.List;
import ru.sbsoft.client.components.IFilterSource;
import ru.sbsoft.client.components.ISelectBrowserMaker;
import ru.sbsoft.client.components.browser.BaseBrowserFactory;
import ru.sbsoft.client.components.browser.SelectBrowserFactory;
import ru.sbsoft.client.components.form.FormMakerAdapter;
import ru.sbsoft.client.components.form.IFormFactory;
import ru.sbsoft.client.components.form.LookupField;
import ru.sbsoft.shared.FilterInfo;
import ru.sbsoft.shared.model.LookupInfoModel;
import ru.sbsoft.shared.param.LookUpParamInfo;
import ru.sbsoft.shared.param.ParamInfo;
import ru.sbsoft.shared.interfaces.NamedGridType;

/**
 *
 * @author Kiselev
 * @param <M>
 * @param <SelfType>
 */
public class BaseLookupHandler<M extends LookupInfoModel, SelfType extends BaseLookupHandler<M, SelfType>> extends BaseHandler<LookupFieldEx<M>, M, SelfType> {

    private ISelectBrowserMaker browserMaker = null;
    private IFormFactory formFactory = null;

    public BaseLookupHandler(String name, String label) {
        super(name, label);
    }

    public SelfType setFieldsReadonly(boolean b) {
        getField().setFieldsReadonly(b);
        return (SelfType) this;
    }

    public SelfType addValueChangeHandler(ValueChangeHandler<M> h) {
        getField().addValueChangeHandler(h);
        return (SelfType) this;
    }

    @Override
    protected LookupFieldEx<M> createField() {
        LookupFieldEx<M> f = new LookupFieldEx<M>();
        if (formFactory != null) {
            f.setFormMaker(new FormMakerAdapter(formFactory));
        }
        if (browserMaker != null) {
            f.setSelectBrowserMaker(browserMaker);
        }
        return f;
    }

    public final SelfType setEmptyText(String key, String name) {
        getField().getFieldKey().setEmptyText(key);
        getField().getFieldName().setEmptyText(name);
        return (SelfType) this;
    }

    public final boolean isCaseSensitive() {
        return getField().isCaseSensitive();
    }

    public final SelfType setCaseSensitive(boolean caseSensitive) {
        getField().setCaseSensitive(caseSensitive);
        return (SelfType) this;
    }

    protected final SelfType setBrowser(NamedGridType browserType, String context) {
        if (browserType != null) {
            this.browserMaker = new SelectBrowserFactory(browserType);
            setContext(context);
        }
        return (SelfType) this;
    }

    protected final SelfType setBrowserMaker(ISelectBrowserMaker browserMaker) {
        this.browserMaker = browserMaker;
        if (formFactory != null && getFactory(false) != null) {
            getFactory().setForm(formFactory);
        }
        return (SelfType) this;
    }

    public final SelfType setBrowserFilter(IFilterSource f) {
        getField().setFilterSource(f);
        return (SelfType) this;
    }

    public SelfType disableDropList() {
        LookupField<M> f = getField();
        f.disableKeySearch();
        f.disableNameSearch();
        return (SelfType) this;
    }
    
    public SelfType setKeyFieldVisible(boolean visible) {
        LookupField<M> f = getField();
        f.setKeyFieldVisible(visible);
        return (SelfType) this;
    }

    public boolean isContextSupported() {
        return browserMaker instanceof BaseBrowserFactory;
    }

    public SelfType setMultiselect(boolean b) {
        getFactory().setMultiselect(b);
        return (SelfType) this;
    }

    private BaseBrowserFactory getFactory() {
        return getFactory(true);
    }

    private BaseBrowserFactory getFactory(boolean required) {
        if (browserMaker instanceof BaseBrowserFactory) {
            return (BaseBrowserFactory) browserMaker;
        } else {
            if (required) {
                throw new UnsupportedOperationException("Operation is supported only for BaseBrowserFactory");
            } else {
                return null;
            }
        }
    }

    public SelfType setContext(String context) {
        if (context != null && !(context = context.trim()).isEmpty()) {
            BaseBrowserFactory bf = getFactory();
            bf.setContext(bf.getGridType().getCode() + context);
        }
        return (SelfType) this;
    }

    public SelfType setForm(IFormFactory formFactory) {
        this.formFactory = formFactory;
        BaseBrowserFactory bro = getFactory(false);
        if (bro != null) {
            bro.setForm(formFactory);
        }
        if (formFactory != null) {
            getField().setFormMaker(new FormMakerAdapter(formFactory));
        } else {
            getField().setFormMaker(null);
        }
        return (SelfType) this;
    }

    public String getContext() {
        if (browserMaker instanceof BaseBrowserFactory) {
            BaseBrowserFactory bf = (BaseBrowserFactory) browserMaker;
            return bf.getContext();
        } else {
            throw new UnsupportedOperationException("Context is supported only for BaseBrowserFactory");
        }
    }

    @Override
    public SelfType setVal(M val) {
        getField().setValue(val, true);
        return (SelfType) this;
    }

    @Override
    public M getVal() {
        return getField().getValue();
    }

    @Override
    protected String getHumanValRaw() {
        M val = getField().getValue();
        return val != null ? val.getSemanticKey() : "";
    }

    @Override
    public boolean isEmpty() {
        return getField().isEmpty();
    }

    @Override
    protected void setFilter(FilterInfo config) {
        getField().setValues((List<M>) config.getValue());
    }

    @Override
    protected FilterInfo createFilter() {
        return getField().createLookUpFilter(null);
    }

    @Override
    protected ParamInfo createParamInfo() {
        LookUpParamInfo res = new LookUpParamInfo();
        FilterInfo flt = createFilter();
        res.setValue((List<LookupInfoModel>) flt.getValue());
        return res;
    }

    protected final ISelectBrowserMaker getBrowserMaker() {
        return browserMaker;
    }
}
