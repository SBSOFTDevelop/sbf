package ru.sbsoft.client.components.form.handler;

import ru.sbsoft.client.components.IValSelectHandler;
import ru.sbsoft.client.components.ValSelectField;
import ru.sbsoft.shared.FilterInfo;
import ru.sbsoft.shared.param.ParamInfo;

/**
 *
 * @author sychugin
 * @param <V>
 * @param <SelfType>
 */
public abstract class ValSelectFieldHandler<V, SelfType extends ValSelectFieldHandler<V, SelfType>> extends BaseHandler<ValSelectField<V>, V, SelfType> {

    private final IValSelectHandler<V> h;

    public ValSelectFieldHandler(String label, IValSelectHandler<V> h) {
        super(null, label);
        this.h = h;
    }

    @Override
    protected FilterInfo createFilter() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected ParamInfo createParamInfo() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//        return new BigDecimalParamInfo(null ,getVal());
    }

    @Override
    protected ValSelectField<V> createField() {
        return new ValSelectField<>(h);
    }

    public SelfType setCleansable(boolean isCleansable) {
        getField().setCleansable(isCleansable);
        return (SelfType) this;
    }
}
