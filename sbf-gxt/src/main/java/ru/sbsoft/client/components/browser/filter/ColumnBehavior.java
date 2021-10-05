package ru.sbsoft.client.components.browser.filter;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.sencha.gxt.cell.core.client.form.ComboBoxCell;
import com.sencha.gxt.data.shared.LabelProvider;
import java.util.Collection;
import ru.sbsoft.client.I18n;
import ru.sbsoft.client.components.actions.ActionManager;
import ru.sbsoft.client.components.browser.filter.fields.CustomHolder;
import ru.sbsoft.client.components.browser.filter.fields.FieldHolder;
import ru.sbsoft.client.components.form.ComboBox;
import ru.sbsoft.client.components.form.LookupItemsList;
import ru.sbsoft.client.components.grid.column.CustomColumnConfig;
import ru.sbsoft.client.components.grid.column.StringColumnConfig;
import ru.sbsoft.client.components.grid.dlgbase.HoldersStore;
import ru.sbsoft.client.components.grid.dlgbase.Item;
import ru.sbsoft.client.filter.editor.FilterAdapter;
import ru.sbsoft.client.filter.editor.LookupFilterAdapter;
import ru.sbsoft.client.utils.ComboBoxes;
import ru.sbsoft.shared.BooleanOperator;
import ru.sbsoft.shared.ColumnsFilterInfo;
import ru.sbsoft.shared.ComparisonEnum;
import ru.sbsoft.shared.Condition;
import static ru.sbsoft.shared.Condition.EMPTY;
import static ru.sbsoft.shared.Condition.STARTS_WITH;
import ru.sbsoft.shared.FilterInfo;
import ru.sbsoft.shared.filter.StringFilterInfo;
import ru.sbsoft.shared.meta.Column;
import ru.sbsoft.shared.meta.ColumnType;
import ru.sbsoft.shared.meta.Columns;
import ru.sbsoft.shared.api.i18n.consts.SBFBrowserStr;

/**
 * Обработка колоночных элементов фильтра
 *
 * @author balandin
 * @since Nov 6, 2015
 */
public class ColumnBehavior extends CustomBehavior implements FieldsComboBox.ColumnSource, LookupSuccessHandler {

    protected FieldsComboBox fields2;
    protected ComboBox<Boolean> negation;
    protected ComboBox<Condition> conditions;
    protected CustomColumnConfig column;
    protected Condition condition;
    protected boolean compareFieldsMode;
    //
    protected FilterAdapter filterAdapter;
    private final DictionaryLoader dictionaryLoader;

    public ColumnBehavior(FilterItem filterItem, FieldsComboBox fields, CustomColumnConfig column, DictionaryLoader dictionaryLoader) {
        super(filterItem, fields);
        this.dictionaryLoader = dictionaryLoader;

        negation = new ComboBox<Boolean>(new LabelProvider<Boolean>() {
            @Override
            public String getLabel(Boolean item) {
                return item ? I18n.get(BooleanOperator.NOT) : " - ";
            }
        });
        negation.setEditable(false);
        negation.setAllowBlank(true);
        negation.setTriggerAction(ComboBoxCell.TriggerAction.ALL);
        negation.getStore().add(Boolean.FALSE);
        negation.getStore().add(Boolean.TRUE);
        negation.setWidth(50);
        negation.setValue(Boolean.FALSE);
        negation.setVisible(false);

        conditions = new ComboBox<Condition>(new LabelProvider<Condition>() {
            @Override
            public String getLabel(Condition item) {
                return I18n.get(item);
            }
        });
        conditions.setEditable(false);
        conditions.setAllowBlank(true);
        conditions.setTriggerAction(ComboBoxCell.TriggerAction.ALL);
        conditions.setWidth(125);
        conditions.setVisible(false);
        conditions.setSelectImmediately(true).addValueChangeHandler(new ValueChangeHandler<Condition>() {
            @Override
            public void onValueChange(ValueChangeEvent<Condition> event) {
                setCondition(event.getValue());
            }
        });

        if (!isSystem()) {
            filterItem.addControl(filterItem.getErrorControl(), Item.MARGINS);
            filterItem.addControl(negation, Item.MARGINS);
            filterItem.addControl(conditions, Item.MARGINS);
            setColumn(column, false);
            initComplete();
        } else {
            this.column = column;
        }
    }

    @Override
    public void initFilterTemplate() throws FilterSetupException {
        CustomHolder holder = filterItem.getHoldersList().findHolder(column.getColumn().getAlias());

        filterItem.setCaption(makeCaption(holder.getShortTitle(), filterTemplate.getRequired()));
        filterItem.addControl(filterItem.getErrorControl(), Item.MARGINS);
        // filterItem.addControl(negation, MARGINS);
        filterItem.addControl(conditions, Item.MARGINS);

        setColumn(column, true);

        conditions.setEnabled(!filterTemplate.getStrict());
        Condition c = filterTemplate.getCondition();
        if (c == null && column.getColumn().getType() == ColumnType.VCHAR && column instanceof StringColumnConfig) {
            c = STARTS_WITH;
        }
        if (c != null) {
            restoreCondition(c.getComparison());
        }

        initComplete();
    }

    private FilterAdapter getFilterAdapter() {
        if (filterAdapter == null && column != null) {
            filterAdapter = column.createFilterAdapter();
        }
        return filterAdapter;
    }

    public void setColumn(Column column, boolean force) {
        setColumn((CustomColumnConfig) column.getData(Column.COLUMN_CONFIG_PREFIX), force);
    }

    public void setColumn(CustomColumnConfig column, boolean force) {
        if (this.column != column || force) {
            this.column = column;
            this.compareFieldsMode = false;
            this.filterAdapter = null;
            clean();
            initEditors();
        }
    }

    private void setCondition(Condition condition) {
        if (this.condition != condition) {
            this.condition = condition;
            if (!compareFieldsMode) {
                initEditors();
            }
        }
    }

    private static class JobIsDone extends RuntimeException {

        public JobIsDone() {
        }
    }

    private void initEditors() {
        try {
            initEmptyEditor();
            initDictionaryEditor();
            initDistinctColumnEditor();
            initDefaultEditor();
        } catch (JobIsDone ok) {
        } finally {
            initComplete();
        }
    }

    private void initEmptyEditor() {
        negation.setVisible(column != null);
        conditions.setVisible(column != null);

        if (column == null) {
            conditions.getStore().clear();
            clean();
            done();
        }
    }

    private void initDictionaryEditor() {
        if (column.getColumn().getLookupGridInfo() != null) {
            if (condition == EMPTY) {
                clean();
            } else {
                if (condition == null) {
                    clean();
                }
                if (!filterItem.initialized()) {
                    initLookup();
                    dictionaryLoader.load(column.getColumn(), this, column.getColumn().getLookupGridInfo());
                }
            }
            done();
        }
    }

    private void initDistinctColumnEditor() {
        if (column.getColumn().isEnumerated()) {
            if (condition == EMPTY) {
                clean();
            } else {
                if (condition == null) {
                    clean();
                }
                initLookup();
                dictionaryLoader.load(column.getColumn(), this, column.getColumn().getAlias());
            }
            done();
        }
    }

    private void done() {
        throw new JobIsDone();
    }

    private void initDefaultEditor() {
        clean();
        initConditions();
        if (condition != EMPTY) {
            getFilterAdapter().build(condition, column.getColumn(), filterItem);
        }
    }

    public void setCompareFieldsMode(boolean compareFieldsMode) {
        if (this.compareFieldsMode != compareFieldsMode) {
            this.compareFieldsMode = compareFieldsMode;

            if (compareFieldsMode) {

                clean();
                filterItem.add(getFields_II(), Item.MARGINS);
                getFilterAdapter().buildModifiers(condition, column.getColumn(), filterItem);

                ComboBoxes.checkValue(getFields_II(), false, false);
                initConditions();

                initComplete();

            } else {
                setColumn(column, true);
            }
        }
    }

    public void initLookup() {
        filterAdapter = new LookupFilterAdapter(column.getColumn().getType().getFilterTypeEnum(), column.getColumn().getFilterConditions());
        initConditions();
        getFilterAdapter().build(condition, column.getColumn(), filterItem);
    }

    public void initConditions() {
        conditions.getStore().clear();
        Collection<Condition> cList = getFilterAdapter().getConditionsList();
        for (Condition c : cList) {
            if (!compareFieldsMode || (compareFieldsMode && c.isCanCompareFields())) {
                conditions.getStore().add(c);
            }
        }
        condition = (Condition) ComboBoxes.checkValue(conditions, false, true);
    }

    @Override
    public void onLookupLoad(LookupItemsList store) {
        ((LookupFilterAdapter) getFilterAdapter()).init(store);
        initConditions();
        initComplete();
    }

    @Override
    public String getValidateMessage() {
        if (column == null && condition == null) {
            return null;
        }
        if (column == null || condition == null) {
            return I18n.get(SBFBrowserStr.msgDataNotFilled);
        }
        if (compareFieldsMode) {
            if (!(getFields_II().getValue() instanceof FieldHolder)) {
                return I18n.get(SBFBrowserStr.msgDataNotFilled);
            };
            FieldHolder h = (FieldHolder) getFields_II().getValue();
            if (!column.getColumn().isComparable(h.getColumn().getColumn())) {
                return I18n.get(SBFBrowserStr.msgBaseColumnNotIdent);
            }
        }
        if (condition == EMPTY) {
            return null;
        }
        if (filterTemplate != null && !filterTemplate.getRequired()) {
            return null;
        }
        if (getFilterAdapter().validate()) {
            return null;
        }
        return I18n.get(SBFBrowserStr.msgDataNotFilled);
    }

    @Override
    public FilterInfo getFilterInfo(boolean system) {
        if (isSystem() != system) {
            return null;
        }
        if (column == null && condition == null) {
            return null;
        }
        if (!validate()) {
            throw new IllegalStateException();
        }

        if (compareFieldsMode) {
            CustomColumnConfig column2 = ((FieldHolder) getFields_II().getValue()).getColumn();
            ColumnsFilterInfo f = new ColumnsFilterInfo(column.getPath(), condition.getComparison(), column2.getPath());
            f.setNotExpression(negation.getValue());
            return f;
        }

        FilterInfo f = (condition == EMPTY) ? new StringFilterInfo() : getFilterAdapter().makeFilterInfo();
        if (f != null) {
            f.setColumnName(column.getPath());
            f.setComparison(condition.getComparison());
            f.setNotExpression(negation.getValue());
        }
        return f;
    }

    @Override
    public CustomColumnConfig getColumn() {
        return column;
    }

    public boolean isCompareFieldsMode() {
        return compareFieldsMode;
    }

    public boolean hasSimilarFields() {
        return column != null && filterItem.getHoldersList().getMultyProtoTypes().contains(column.getColumn().getType().getProtoType());
    }

    public FieldsComboBox getFields() {
        return fields;
    }

    public FieldsComboBox getFields_II() {
        if (fields2 == null) {
            fields2 = new FieldsComboBox(new HoldersStore(filterItem.getHoldersList()), this);
        }
        return fields2;
    }

    private void setValid(boolean valid) {
        filterItem.setError(valid ? null : I18n.get(SBFBrowserStr.msgDataNotFilled));
    }

    public void clean() {
        setValid(true);
        filterItem.removeEditors();
    }

    @Override
    public void restore(FilterInfo filterInfo, ErrorStore errors) {
        new ResoreFiltersAction(filterInfo, errors).execute();
    }

    private void restoreCondition(ComparisonEnum comparison) throws FilterSetupException {
        Condition c1 = comparison.getCondition();
        Condition c2 = ComboBoxes.findValue(conditions, c1);
        if (c2 == null && column.getColumn().getType() == ColumnType.VCHAR && c1 == Condition.EQUAL) {
            c2 = ComboBoxes.findValue(conditions, Condition.EQUAL_ALT);
        }
        if (c2 == null) {
            throw new FilterSetupException.Handled(I18n.get(SBFBrowserStr.msgOperationNotFound), column, comparison, c1, c2);
        }
        conditions.setValue(c2, false, true);
        setCondition(c2);
    }

    /**
     * Загрузка фильтра
     */
    private class ResoreFiltersAction {

        private final FilterInfo filterInfo;
        private final ErrorStore errors;

        public ResoreFiltersAction(FilterInfo filterInfo, ErrorStore errors) {
            this.filterInfo = filterInfo;
            this.errors = errors;
        }

        public void execute() {
            try {
                Column c = restoreField(getFields(), filterInfo.getColumnName(), "");
                setColumn(c, true);
                if (filterInfo instanceof ColumnsFilterInfo) {
                    setCompareFieldsMode(true);
                    Column altColumn = restoreField(getFields_II(), ((ColumnsFilterInfo) filterInfo).getColumnName2(), "(2)");
                    if (!c.isComparable(altColumn)) {
                        throw new FilterSetupException.Handled(I18n.get(SBFBrowserStr.msgBaseColumnNotIdent), c, altColumn);
                    }
                } else if (c.getLookupGridInfo() != null) {
                    //initLookup();
                    //dictionaryLoader.load(c, ColumnBehavior.this, c.getLookupGridType());
                } else if (c.isEnumerated()) {
                    //initLookup();
                    //dictionaryLoader.load(c, ColumnBehavior.this, c.getAlias());
                }

                negation.setValue(filterInfo.isNotExpression() ? Boolean.TRUE : Boolean.FALSE);
                restoreCondition(filterInfo.getComparison());
                getFilterAdapter().restoreControls(filterInfo);
                getFilterAdapter().restoreValues(filterInfo);
            } catch (FilterSetupException ex) {
                errors.add(filterItem, filterInfo, ex.getMessage());
            }
        }

        public Column restoreField(FieldsComboBox cb, String colName, String prefix) throws FilterSetupException {
            Columns meta = filterItem.getHoldersList().getGrid().getMetaInfo();
            Column c = check(meta.getColumnForAlias(colName), I18n.get(SBFBrowserStr.msgColumnNotFound, colName));
            check((CustomColumnConfig) c.getData(Column.COLUMN_CONFIG_PREFIX), "ColumnConfig" + prefix);
            selectField(cb, colName);
            return c;
        }

        public void selectField(FieldsComboBox cb, String alias) {
            cb.setValue(filterItem.getHoldersList().findHolder(alias), false, true);
        }

        private <T> T check(T object, String message) throws FilterSetupException {
            return check(object, message, false);
        }

        private <T> T check(T object, String message, boolean handledError) throws FilterSetupException {
            if (object == null) {
                throw handledError ? new FilterSetupException.Handled(message) : new FilterSetupException(" empty " + message);
            }
            return object;
        }
    }

    @Override
    public void clearValue() {
        if (filterAdapter != null) {
            filterAdapter.clearValue();
        }
    }
}
