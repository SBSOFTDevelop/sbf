package ru.sbsoft.client.filter.editor;

import com.google.gwt.i18n.client.NumberFormat;
import com.sencha.gxt.widget.core.client.form.NumberField;
import com.sencha.gxt.widget.core.client.form.NumberPropertyEditor;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import ru.sbsoft.shared.Condition;
import static ru.sbsoft.shared.Condition.*;
import ru.sbsoft.shared.FilterTypeEnum;

public abstract class NumberFilterAdapter extends RangeFilterAdapter {

    public NumberFilterAdapter(FilterTypeEnum filterType, List<Condition> filterConditions, NumberFormat format) {
        super(filterType, filterConditions, createFieldEditor(format), createFieldEditor(format));
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

    private static NumberField<BigDecimal> createFieldEditor(NumberFormat format) {
        NumberField<BigDecimal> f = new NumberField<BigDecimal>(new NumberPropertyEditor.BigDecimalPropertyEditor(format)) {

            @Override
            protected void onRedraw() {
                super.onRedraw();
                getCell().getInputElement(getElement()).getStyle().setProperty("textAlign", "right");
            }
        };
        f.setAllowBlank(true);
        return f;
    }
}
