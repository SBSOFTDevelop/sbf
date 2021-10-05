package ru.sbsoft.client.filter.editor;

import com.sencha.gxt.cell.core.client.form.YearMonthDayField;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import ru.sbsoft.client.components.field.YearMonthDayFormat;
import ru.sbsoft.client.components.field.YearMonthDayMaskField;
import ru.sbsoft.shared.Condition;
import static ru.sbsoft.shared.Condition.EMPTY;
import static ru.sbsoft.shared.Condition.EQUAL;
import static ru.sbsoft.shared.Condition.GREATER;
import static ru.sbsoft.shared.Condition.GREATER_OR_EQUAL;
import static ru.sbsoft.shared.Condition.IN_BOUND;
import static ru.sbsoft.shared.Condition.IN_RANGE;
import static ru.sbsoft.shared.Condition.LESS;
import static ru.sbsoft.shared.Condition.LESS_OR_EQUAL;
import ru.sbsoft.shared.FilterTypeEnum;

/**
 *
 * @author sychugin
 */
public class YearMonthDayFilterAdapter extends RangeFilterAdapter{

    public YearMonthDayFilterAdapter(List<Condition> filterConditions, final YearMonthDayFormat format) {
        super(FilterTypeEnum.YMD, filterConditions, createEditField(format), createEditField(format));
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
     private static YearMonthDayField createEditField(final YearMonthDayFormat format) {
        YearMonthDayMaskField f = new YearMonthDayMaskField(format);
        f.setAllowBlank(true);
        return f;
    }
}
