package ru.sbsoft.client.components.form.handler;

import com.google.gwt.i18n.client.NumberFormat;
import ru.sbsoft.svc.widget.core.client.form.BigDecimalSpinnerField;
import java.math.BigDecimal;
import ru.sbsoft.shared.FilterInfo;
import ru.sbsoft.shared.filter.BigDecimalFilterInfo;
import ru.sbsoft.shared.param.BigDecimalParamInfo;
import ru.sbsoft.shared.param.ParamInfo;

/**
 *
 * @author sychugin
 */
public class BigDecimalSpinnerHandler<SelfType extends BigDecimalSpinnerHandler<SelfType>> extends ValFieldBaseHandler<BigDecimalSpinnerField, BigDecimal, SelfType> {

    public BigDecimalSpinnerHandler(String name, String label) {
        super(name, label);
    }

    @Override
    protected BigDecimalSpinnerField createField() {
        return new BigDecimalSpinnerField();
    }

    @Override
    protected FilterInfo createFilter() {
        return new BigDecimalFilterInfo(null, getVal());
    }

    @Override
    protected ParamInfo createParamInfo() {
        return new BigDecimalParamInfo(null, getVal());
    }

    public SelfType setRange(BigDecimal minVal, BigDecimal maxVal) {
        getField().setMinValue(minVal);
        getField().setMaxValue(maxVal);

        return (SelfType) this;
    }

    public SelfType setIncrement(BigDecimal inc) {
        getField().setIncrement(inc);

        return (SelfType) this;
    }

    public SelfType setFormat(String f) {
        getField().getPropertyEditor().setFormat(NumberFormat.getFormat(f));
        return (SelfType) this;
    }

    public SelfType setFormat(NumberFormat f) {
        getField().getPropertyEditor().setFormat(f);
        return (SelfType) this;
    }
}
