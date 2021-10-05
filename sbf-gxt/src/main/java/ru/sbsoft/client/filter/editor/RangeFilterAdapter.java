package ru.sbsoft.client.filter.editor;

import com.sencha.gxt.widget.core.client.form.Field;
import java.util.List;
import ru.sbsoft.client.I18n;
import ru.sbsoft.client.components.browser.filter.FilterSetupException;
import ru.sbsoft.client.components.browser.filter.FilterItem;
import ru.sbsoft.common.Strings;
import ru.sbsoft.shared.Condition;
import ru.sbsoft.shared.FilterInfo;
import ru.sbsoft.shared.FilterTypeEnum;
import ru.sbsoft.shared.api.i18n.consts.SBFExceptionStr;
import ru.sbsoft.shared.meta.Column;

public abstract class RangeFilterAdapter extends FilterAdapter {

    public static final int PAIR_WIDTH = 150;
    public static final int SINGLE_WIDTH = 250;
    //
    protected final Field field1;
    protected final Field field2;

    public RangeFilterAdapter(FilterTypeEnum filterType, List<Condition> filterConditions, Field field1, Field field2) {
        super(filterType, filterConditions);
        this.field1 = field1;
        this.field2 = field2;
    }

    @Override
    public void build(Column column, FilterItem filterItem) {
        if (pair()) {
            add(filterItem, column, field1, true);
            add(filterItem, column, field2, true);
        } else {
            add(filterItem, column, field1, false);
        }
    }

    private static void add(FilterItem filterItem, Column column, Field f, boolean pair) {
        f.setWidth(pair ? PAIR_WIDTH : SINGLE_WIDTH);
        if (column.getDescription() != null) {
            f.setToolTip(I18n.get(column.getDescription()));
        }
        filterItem.add(f, FilterItem.MARGINS);
    }

    @Override
    public boolean validate() {
        field1.finishEditing();

        if (pair()) {
            field2.finishEditing();
            return notNull(field1) && notNull(field2);
        } else {
            return notNull(field1);
        }
    }

    private boolean notNull(Field f) {
        Object v = f.getValue();
        if (v instanceof String) {
            v = Strings.clean((String) v, false);
        }
        return v != null;
    }

    @Override
    public FilterInfo makeFilterInfo() {
        FilterInfo f = null;
        if (validate()) {
            f = createFilterInfo(null, field1.getValue());
            if (pair()) {
                f.setSecondValue(field2.getValue());
            }
        }
        return f;
    }

    @Override
    public void restoreValues(FilterInfo filterInfo) throws FilterSetupException {
        try {
            restoreValue(field1, filterInfo.getValue());
            if (pair()) {
                restoreValue(field2, filterInfo.getSecondValue());
            }
        } catch (FilterSetupException ex) {
            ex.getParams().add(filterInfo);
            throw ex;
        }
    }

    protected void restoreValue(Field field, Object value) throws FilterSetupException {
        if (value == null) {
            throw new FilterSetupException(I18n.get(SBFExceptionStr.emptyValue));
        }
        field.setValue(checkValue(value), false);
    }

    protected Object checkValue(Object value) throws FilterSetupException {
        return value;
    }

    @Override
    public void clearValue() {
        if (field1 != null) {
            field1.setValue(null, false, true);
        }
        if (field2 != null) {
            field2.setValue(null, false, true);
        }
    }
}
