package ru.sbsoft.client.filter.editor;

import com.google.gwt.i18n.client.NumberFormat;
import java.math.BigDecimal;
import java.util.List;
import ru.sbsoft.client.I18n;
import ru.sbsoft.client.components.browser.filter.FilterSetupException;
import ru.sbsoft.shared.Condition;
import ru.sbsoft.shared.FilterTypeEnum;
import ru.sbsoft.shared.api.i18n.consts.SBFExceptionStr;

public class IntegerFilterAdapter extends NumberFilterAdapter {

    public IntegerFilterAdapter(List<Condition> filterConditions, NumberFormat format) {
        super(FilterTypeEnum.LONG, filterConditions, format);
    }

    @Override
    protected Object checkValue(Object value) throws FilterSetupException {
        if (value instanceof Long) {
            return new BigDecimal((Long) value);
        }
        throw new FilterSetupException(I18n.get(SBFExceptionStr.typeMismatch), Long.class, value);
    }
}
