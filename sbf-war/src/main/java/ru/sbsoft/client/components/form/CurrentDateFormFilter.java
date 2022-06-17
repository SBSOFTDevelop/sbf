package ru.sbsoft.client.components.form;

import ru.sbsoft.sbf.svc.components.VerticalFieldSet;
import ru.sbsoft.svc.widget.core.client.form.FieldLabel;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import ru.sbsoft.client.I18n;
import ru.sbsoft.shared.FilterInfo;
import ru.sbsoft.shared.filter.DateFilterInfo;
import ru.sbsoft.shared.filter.FilterConsts;
import ru.sbsoft.shared.api.i18n.consts.SBFFormStr;

/**
 * Базовый класс для диалогов фильтра таблицы, содержащих поле для установки
 * текущей даты.
 *
 * @deprecated filter of this type is not used in browsers any more
 */
public abstract class CurrentDateFormFilter extends BaseFormFilter {

    protected CurrendDateField fieldCURRENT_DATE;

    public CurrentDateFormFilter(String header) {
        super(header);
    }

    @Override
    protected void fillFilterPage(SimplePageFormContainer pageContainer) {
        final VerticalFieldSet dataFieldSet = new VerticalFieldSet(I18n.get(SBFFormStr.labelAccordingData));

        fieldCURRENT_DATE = new CurrendDateField();
        dataFieldSet.addField(new FieldLabel(fieldCURRENT_DATE, I18n.get(SBFFormStr.labelCurrentDate)));

        fillDataFileldSet(dataFieldSet);

        pageContainer.addFieldSet(dataFieldSet);
    }

    @Override
    public List<FilterInfo> getFilters() {
        final List<FilterInfo> filters = new ArrayList<FilterInfo>();
        filters.add(createCurrentDateFilter());
        return filters;
    }

    private FilterInfo createCurrentDateFilter() {
        return DateFilterInfo.createCurrentDateFilter(fieldCURRENT_DATE.getCurrentValue());
    }

    @Override
    protected boolean setFilterValue(FilterInfo config) {
        if (!FilterConsts.CURRENT_DATE_FIELD.equals(config.getColumnName())) {
            return false;
        }
        fieldCURRENT_DATE.setValue((Date) config.getValue());
        fieldCURRENT_DATE.finishEditing();
        return true;
    }

    protected abstract void fillDataFileldSet(VerticalFieldSet dataFieldSet);
}
