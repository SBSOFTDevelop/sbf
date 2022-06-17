package ru.sbsoft.client.components.form.handler;

import ru.sbsoft.svc.widget.core.client.form.BigDecimalField;
import ru.sbsoft.svc.widget.core.client.form.validator.MaxNumberValidator;
import ru.sbsoft.svc.widget.core.client.form.validator.MinNumberValidator;
import java.math.BigDecimal;
import ru.sbsoft.client.I18n;
import ru.sbsoft.shared.FilterInfo;
import ru.sbsoft.shared.filter.BigDecimalFilterInfo;
import ru.sbsoft.shared.param.BigDecimalParamInfo;
import ru.sbsoft.shared.param.ParamInfo;
import ru.sbsoft.shared.interfaces.NamedItem;

/**
 *
 * @author Kiselev
 * @param <SelfType>
 */
public class BigDecimalHandler<SelfType extends BigDecimalHandler<SelfType>> extends NumberHandler<BigDecimalField, BigDecimal, SelfType> {

    public BigDecimalHandler(NamedItem paramItem) {
        this(paramItem.getCode(), I18n.get(paramItem.getItemName()));
    }

    public BigDecimalHandler(String name, String label) {
        super(name, label);
    }

    @Override
    protected BigDecimalField createField() {
        return new BigDecimalField();
    }

    @Override
    protected FilterInfo createFilter() {
        getField().setFormat(null);
        return new BigDecimalFilterInfo(null, getVal());
    }

    @Override
    protected ParamInfo createParamInfo() {
        return new BigDecimalParamInfo(null, getVal());
    }

    public SelfType setRange(Double minVal, Double maxVal) {
        setMin(minVal);
        return setMax(maxVal);
    }

    public SelfType setMin(Double minVal) {
        getField().addValidator(new MinNumberValidator<BigDecimal>(BigDecimal.valueOf(minVal)));
        return (SelfType) this;
    }

    public SelfType setMax(Double maxVal) {
        getField().addValidator(new MaxNumberValidator<BigDecimal>(BigDecimal.valueOf(maxVal)));
        return (SelfType) this;
    }
}
