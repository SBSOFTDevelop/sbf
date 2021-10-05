package ru.sbsoft.client.components.form.handler;

import java.util.Date;
import ru.sbsoft.client.components.field.DateConsts;
import ru.sbsoft.client.components.field.DateField;
import ru.sbsoft.shared.FilterInfo;
import ru.sbsoft.shared.FilterTypeEnum;
import ru.sbsoft.shared.filter.DateFilterInfo;
import ru.sbsoft.shared.param.DateParamInfo;
import ru.sbsoft.shared.param.ParamInfo;

/**
 *
 * @author Kiselev
 * @param <SelfType>
 */
public class DateHandler<SelfType extends DateHandler<SelfType>> extends ValFieldBaseHandler<DateField, Date, SelfType> {

    private final DateConsts format;
    private final String nullText;

    public DateHandler(String name, String label) {
        this(name, label, (String) null);
    }

    public DateHandler(String name, String label, String nullText) {
        this(name, label, null, nullText);
    }

    public DateHandler(String name, String label, DateConsts format) {
        this(name, label, format, null);
    }

    public DateHandler(String name, String label, DateConsts format, String nullText) {
        super(name, label);
        this.format = format != null ? format : DateConsts.DATE;
        this.nullText = nullText;
    }

    public SelfType setMax(Date maxValue) {
        getField().setMaxValue(maxValue);
        return (SelfType) this;
    }

    public SelfType setMin(Date minValue) {
        getField().setMinValue(minValue);
        return (SelfType) this;
    }

    @Override
    protected DateField createField() {
        return nullText != null ? new DateField(format, nullText) : new DateField(format);
    }

    @Override
    protected void setFilter(FilterInfo config) {
        getField().setValue((Date) config.getValue());
        getField().finishEditing();
    }

    @Override
    protected FilterInfo createFilter() {
        DateFilterInfo res = new DateFilterInfo();
        res.setType(FilterTypeEnum.DATE);
        res.setValue(getField().getCurrentValue());
        return res;
    }

    @Override
    protected ParamInfo createParamInfo() {
        return new DateParamInfo(null, getVal());
    }
}
