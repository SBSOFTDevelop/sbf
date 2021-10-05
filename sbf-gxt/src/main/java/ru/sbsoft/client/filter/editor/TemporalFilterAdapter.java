package ru.sbsoft.client.filter.editor;

import com.google.gwt.i18n.client.DateTimeFormat;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import ru.sbsoft.client.I18n;
import ru.sbsoft.client.components.browser.filter.FilterSetupException;
import ru.sbsoft.client.components.field.DateConsts;
import ru.sbsoft.client.components.field.DateField;
import ru.sbsoft.shared.Condition;
import static ru.sbsoft.shared.Condition.*;
import ru.sbsoft.shared.FilterTypeEnum;
import ru.sbsoft.shared.api.i18n.consts.SBFExceptionStr;

public class TemporalFilterAdapter extends RangeFilterAdapter {

    public TemporalFilterAdapter(FilterTypeEnum filterType, List<Condition> filterConditions,  DateConsts consts, DateTimeFormat format) {
        super(filterType, filterConditions, createEditField(consts, format), createEditField(consts, format));
    }

    @Override
    protected Collection<Condition> getDefaultConditionsList() {
        return Arrays.asList(EQUAL,
                GREATER, GREATER_OR_EQUAL,
                LESS, LESS_OR_EQUAL,
                IN_RANGE, IN_BOUND,
                EMPTY
        );
    }

    //
    // Забили на формат в угоду наличия маскированного ввода
    // com.sencha.gxt.widget.core.client.form.DateField
    // DateField f = new DateField(new DateTimePropertyEditor(format)); 
    //
    private static DateField createEditField(DateConsts consts, DateTimeFormat format) {
        DateField f = new DateField(consts);
        f.setAllowBlank(true);
        return f;
    }

    @Override
    protected Object checkValue(Object value) throws FilterSetupException {
        if (value instanceof Date) {
            return value;
        }
        throw new FilterSetupException(I18n.get(SBFExceptionStr.typeMismatch), Date.class, value);
    }
}
