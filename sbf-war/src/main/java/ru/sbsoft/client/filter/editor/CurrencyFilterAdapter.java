package ru.sbsoft.client.filter.editor;

import com.google.gwt.i18n.client.NumberFormat;
import java.math.BigDecimal;
import java.util.List;
import ru.sbsoft.client.I18n;
import ru.sbsoft.client.components.browser.filter.FilterSetupException;
import ru.sbsoft.shared.Condition;
import ru.sbsoft.shared.FilterTypeEnum;
import ru.sbsoft.shared.api.i18n.consts.SBFExceptionStr;

public class CurrencyFilterAdapter extends NumberFilterAdapter {

    public CurrencyFilterAdapter(List<Condition> filterConditions, NumberFormat format) {
        super(FilterTypeEnum.NUMERIC, filterConditions, format);
    }

    @Override
    protected Object checkValue(Object value) throws FilterSetupException {
        if (value instanceof BigDecimal) {
            return value;
        }
        throw new FilterSetupException(I18n.get(SBFExceptionStr.typeMismatch), BigDecimal.class, value);
    }
}
