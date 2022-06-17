package ru.sbsoft.client.components.form.handler;

import java.util.Date;
import ru.sbsoft.client.I18n;
import ru.sbsoft.client.components.form.AbstractYearMonthField;
import ru.sbsoft.shared.FilterInfo;
import ru.sbsoft.shared.filter.DateFilterInfo;
import ru.sbsoft.shared.param.DateParamInfo;
import ru.sbsoft.shared.param.ParamInfo;
import ru.sbsoft.shared.model.IYearMonth;
import ru.sbsoft.shared.interfaces.NamedItem;

/**
 *
 * @author Kiselev
 * @param <V>
 * @param <F>
 * @param <SelfType>
 */
public abstract class AbstractYearMonthHandler<V extends IYearMonth, F extends AbstractYearMonthField<V>, SelfType extends AbstractYearMonthHandler<V, F, SelfType>> extends BaseHandler<F, V, SelfType> {

    public AbstractYearMonthHandler(NamedItem paramItem) {
        this(paramItem.getCode(), I18n.get(paramItem.getItemName()));
    }

    public AbstractYearMonthHandler(String name, String label) {
        super(name, label);
    }

    protected abstract Date toDate(V v);

    protected abstract V fromDate(Date d);

    @Override
    protected void setFilter(FilterInfo config) {
        if (config.getValue() instanceof Date) {
            setVal(fromDate((Date) config.getValue()));
        }
    }

    @Override
    protected FilterInfo createFilter() {
        V v = getVal();
        return v != null ? new DateFilterInfo(null, toDate(v)) : null;
    }

    @Override
    protected ParamInfo createParamInfo() {
        V v = getVal();
        return v != null ? new DateParamInfo(null, toDate(v)) : null;
    }

    public SelfType setMin(V ym) {
        getField().setMin(ym);
        return (SelfType) this;
    }

    public SelfType setMax(V ym) {
        getField().setMax(ym);
        return (SelfType) this;
    }

    public SelfType setRange(V ymMin, V ymMax) {
        getField().setRange(ymMin, ymMax);
        return (SelfType) this;
    }
}
