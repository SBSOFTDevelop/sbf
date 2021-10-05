package ru.sbsoft.client.components.form.handler;

import com.google.gwt.i18n.client.NumberFormat;
import com.sencha.gxt.widget.core.client.form.IntegerSpinnerField;
import ru.sbsoft.shared.FilterInfo;
import ru.sbsoft.shared.filter.IntegerFilterInfo;
import ru.sbsoft.shared.param.IntegerParamInfo;
import ru.sbsoft.shared.param.ParamInfo;

/**
 *
 * @author sychugin
 * @param <SelfType>
 */
public class IntSpinnerHandler<SelfType extends IntSpinnerHandler<SelfType>> extends ValFieldBaseHandler<IntegerSpinnerField, Integer, SelfType> {

    public IntSpinnerHandler(String name, String label) {
        super(name, label);
    }

    @Override
    protected IntegerSpinnerField createField() {
        return new IntegerSpinnerField();
    }

    @Override
    protected FilterInfo createFilter() {
        return new IntegerFilterInfo(null, getVal());
    }

    @Override
    protected ParamInfo createParamInfo() {
        return new IntegerParamInfo(null, getVal());
    }

    public SelfType setRange(Integer minVal, Integer maxVal) {
        getField().setMinValue(minVal);
        getField().setMaxValue(maxVal);
        return (SelfType) this;
    }

    public SelfType setIncrement(Integer inc) {
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
