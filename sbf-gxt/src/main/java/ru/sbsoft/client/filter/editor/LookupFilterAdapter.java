package ru.sbsoft.client.filter.editor;

import com.sencha.gxt.cell.core.client.form.ComboBoxCell;
import com.sencha.gxt.data.shared.LabelProvider;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.widget.core.client.form.SimpleComboBox;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import ru.sbsoft.client.I18n;
import ru.sbsoft.client.components.NoRestrictStringComboBoxCell;
import ru.sbsoft.client.components.browser.filter.FilterSetupException;
import ru.sbsoft.client.components.form.LookupItem;
import ru.sbsoft.client.components.form.LookupLabelProvider;
import ru.sbsoft.client.components.form.LookupItemsList;
import ru.sbsoft.client.components.browser.filter.FilterItem;
import ru.sbsoft.client.components.form.LookupValComparator;
import ru.sbsoft.client.utils.ClientUtils;
import ru.sbsoft.shared.ComparisonEnum;
import ru.sbsoft.shared.Condition;
import static ru.sbsoft.shared.Condition.*;
import ru.sbsoft.shared.FilterInfo;
import ru.sbsoft.shared.FilterTypeEnum;
import ru.sbsoft.shared.api.i18n.consts.SBFExceptionStr;
import ru.sbsoft.shared.api.i18n.consts.SBFGeneralStr;
import ru.sbsoft.shared.meta.Column;

public class LookupFilterAdapter extends FilterAdapter {

    private final SimpleComboBox<LookupItem> comboBox;
    private final boolean allowUserInput;

    public LookupFilterAdapter(FilterTypeEnum filterType, List<Condition> filterConditions) {
        this(filterType, filterConditions, null);
    }

    public LookupFilterAdapter(FilterTypeEnum filterType, List<Condition> filterConditions, LookupItemsList store) {
        super(filterType, filterConditions);
        
        allowUserInput = (store == null);

        comboBox = new SimpleComboBox<LookupItem>(new FilterComboBoxCell(new LookupLabelProvider()));
        comboBox.setWidth(RangeFilterAdapter.SINGLE_WIDTH);
        comboBox.setMinListWidth(300);
        comboBox.setAutoValidate(true);
        comboBox.setTriggerAction(ComboBoxCell.TriggerAction.ALL);

        init(store);
    }

    public void init(LookupItemsList items) {
        comboBox.getStore().clear();
        if (items != null) {
            List<LookupItem> addItems = new ArrayList<LookupItem>(items);
            Collections.sort(addItems, new LookupValComparator());
            ListStore<LookupItem> store = comboBox.getStore();
            store.addAll(addItems);
            LookupItem val = comboBox.getValue();
            if (val != null && val.isUserInput() && !addItems.isEmpty()) {
                val = findLookupItem(val.getKey());
                if (val != null) {
                    comboBox.setValue(val, false);
                }
            }
        }
    }

    @Override
    protected List<Condition> getDefaultConditionsList() {
        return Arrays.asList(EQUAL, EMPTY);
    }

    @Override
    public void build(Column column, FilterItem filterItem) {
        if (filterItem.getWidgetIndex(comboBox) == -1) {
            if (column.getDescription() != null) {
                comboBox.setToolTip(I18n.get(column.getDescription()));
            }
            filterItem.add(comboBox, FilterItem.MARGINS);
        }
    }

    @Override
    public boolean validate() {
        comboBox.finishEditing();
        return comboBox.getValue() != null;
    }

    @Override
    public FilterInfo makeFilterInfo() {
        FilterInfo f = null;
        if (validate()) {
            f = createFilterInfo(null, comboBox.getValue().getKey());
        }
        return f;
    }

    @Override
    public void restoreValues(FilterInfo filterInfo) throws FilterSetupException {
        final Object value = filterInfo.getValue();
        if (value == null && filterInfo.getComparison() != ComparisonEnum.is_null) {
            throw new FilterSetupException(I18n.get(SBFExceptionStr.emptyValue));
        }
        if (value != null && filterInfo.getComparison() == ComparisonEnum.is_null) {
            throw new FilterSetupException(I18n.get(SBFGeneralStr.captError));
        }
        LookupItem item = findLookupItem(value);
        if (item == null) {
            item = new LookupItem(value, String.valueOf(value), true);
        }
        comboBox.setValue(item, false);
    }

    private LookupItem findLookupItem(Object value) {
        final List<LookupItem> items = comboBox.getStore().getAll();
        for (LookupItem item : items) {
            if (ClientUtils.equals(item.getKey(), value)) {
                return item;
            }
        }
        return null;
    }

    @Override
    public void clearValue() {
        comboBox.clear();
    }

    class FilterComboBoxCell extends NoRestrictStringComboBoxCell {

        public FilterComboBoxCell(LabelProvider<LookupItem> labelProvider) {
            super(labelProvider);
        }

        @Override
        protected void selectNullValue(String value) {
            if (allowUserInput) {
                comboBox.setValue(new LookupItem(value, value, true), false, false);
            }
        }
    }
}
