package ru.sbsoft.client.filter.editor;

import ru.sbsoft.client.components.browser.filter.FilterItem;
import ru.sbsoft.client.components.browser.filter.FilterSetupException;
import ru.sbsoft.shared.ComparisonEnum;
import ru.sbsoft.shared.Condition;
import ru.sbsoft.shared.FilterInfo;
import ru.sbsoft.shared.FilterTypeEnum;
import ru.sbsoft.shared.filter.FilterInfoImpl;
import ru.sbsoft.shared.meta.IColumn;

import java.util.Collection;
import java.util.List;

import static ru.sbsoft.shared.Condition.IN_BOUND;
import static ru.sbsoft.shared.Condition.IN_RANGE;

/**
 * @author balandin
 */
public abstract class FilterAdapter {

    protected Condition condition;
    private final FilterTypeEnum filterType;
    private final List<Condition> filterConditions;

    public FilterAdapter(FilterTypeEnum filterType, List<Condition> filterConditions) {
        this.filterType = filterType;
        this.filterConditions = filterConditions;
    }

    public void build(Condition condition, IColumn column, FilterItem filterItem) {
        this.condition = condition;
        build(column, filterItem);
    }
    
    protected abstract void build(IColumn column, FilterItem filterItem);

    public void buildModifiers(Condition condition, IColumn column, FilterItem filterItem) {
        this.condition = condition;
        buildModifiers(column, filterItem);
    }

    protected void buildModifiers(IColumn column, FilterItem filterItem) {
    }

    protected boolean pair() {
        return condition == IN_RANGE || condition == IN_BOUND;
    }

    public Collection<Condition> getConditionsList() {
        if (null == filterConditions || filterConditions.isEmpty()) {
            return getDefaultConditionsList();
        }
        return filterConditions;
    }
    
    protected abstract Collection<Condition> getDefaultConditionsList();

    public abstract boolean validate();

    /**
     * Получить фильтр (для последующей отправки на сервер)
     *
     * @return
     */
    public abstract FilterInfo makeFilterInfo();

    protected FilterInfo createFilterInfo() {
        FilterInfoImpl f = new FilterInfoImpl(filterType);
        f.setCaseSensitive(true);
        return f;
    }

    protected FilterInfo createFilterInfo(ComparisonEnum comparation, Object value) {
        FilterInfo f = createFilterInfo();
        f.setComparison(comparation);
        f.setValue(value);
        return f;
    }

    /**
     * Восстановить (загрузить) состояния дополнительных компонентов фильтра
     *
     * @param filterInfo
     * @throws FilterSetupException
     */
    public void restoreControls(FilterInfo filterInfo) throws FilterSetupException {
    }

    /**
     * Восстановить (загрузить) значения фильтра
     *
     * @param filterInfo
     * @throws FilterSetupException
     */
    public abstract void restoreValues(FilterInfo filterInfo) throws FilterSetupException;

    public abstract void clearValue();
    
}
