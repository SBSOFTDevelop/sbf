package ru.sbsoft.client.components.form.handler;

import java.util.Date;
import ru.sbsoft.client.I18n;
import ru.sbsoft.client.components.form.CurrendDateField;
import ru.sbsoft.shared.FilterInfo;
import ru.sbsoft.shared.filter.DateFilterInfo;
import ru.sbsoft.shared.filter.FilterConsts;
import ru.sbsoft.shared.param.DateParamInfo;
import ru.sbsoft.shared.param.ParamInfo;
import ru.sbsoft.shared.api.i18n.consts.SBFFormStr;

/**
 *
 * @author Kiselev
 * @param <SelfType>
 */
public class CurrentDateHandler<SelfType extends CurrentDateHandler<SelfType>> extends ValFieldBaseHandler<CurrendDateField, Date, SelfType> {

    public CurrentDateHandler() {
        super(FilterConsts.CURRENT_DATE_FIELD, I18n.get(SBFFormStr.labelCurrentDate));
    }

    @Override
    protected CurrendDateField createField() {
        CurrendDateField cdf = new CurrendDateField();
        cdf.setValue(new Date());
        return cdf;
    }

    @Override
    protected FilterInfo createFilter() {
        return DateFilterInfo.createCurrentDateFilter(getField().getCurrentValue());
    }

    @Override
    protected void setFilter(FilterInfo config) {
        getField().setValue((Date) config.getValue());
        getField().finishEditing();
    }

    @Override
    protected ParamInfo createParamInfo() {
        return new DateParamInfo(null, getVal());
    }
}
