package ru.sbsoft.client.components.form.handler;

import ru.sbsoft.svc.cell.core.client.form.SvcYearMonthDayConverter;
import ru.sbsoft.svc.cell.core.client.form.YearMonthDayField;
import java.util.Date;
import ru.sbsoft.client.I18n;
import ru.sbsoft.client.components.field.YearMonthDayMaskField;
import ru.sbsoft.sbf.app.model.YearMonthDay;
import ru.sbsoft.shared.FilterInfo;
import ru.sbsoft.shared.filter.DateFilterInfo;
import ru.sbsoft.shared.interfaces.NamedItem;
import ru.sbsoft.shared.param.DateParamInfo;
import ru.sbsoft.shared.param.ParamInfo;

/**
 *
 * @author Kiselev
 * @param <SelfType>
 */
public class YearMonthDayHandler<SelfType extends YearMonthDayHandler<SelfType>> extends BaseHandler<YearMonthDayField, YearMonthDay, SelfType> {

    public YearMonthDayHandler(NamedItem paramItem) {
        this(paramItem.getCode(), I18n.get(paramItem.getItemName()));
    }

    public YearMonthDayHandler(String name, String label) {
        super(name, label);
    }

    @Override
    protected YearMonthDayField createField() {
        return new YearMonthDayMaskField();
    }

    @Override
    protected void setFilter(FilterInfo config) {
        if (config.getValue() instanceof Date) {
            setVal(SvcYearMonthDayConverter.convert((Date) config.getValue()));
        }
    }

    @Override
    protected FilterInfo createFilter() {
        YearMonthDay v = getVal();
        return v != null ? new DateFilterInfo(null, SvcYearMonthDayConverter.convert(v)) : null;
    }

    @Override
    protected ParamInfo createParamInfo() {
        YearMonthDay v = getVal();
        return v != null ? new DateParamInfo(null, SvcYearMonthDayConverter.convert(v)) : null;
    }

    public SelfType setMin(YearMonthDay ym) {
        getField().setMinValue(SvcYearMonthDayConverter.convert(ym));
        return (SelfType) this;
    }

    public SelfType setMax(YearMonthDay ym) {
        getField().setMaxValue(SvcYearMonthDayConverter.convert(ym));
        return (SelfType) this;
    }
}
