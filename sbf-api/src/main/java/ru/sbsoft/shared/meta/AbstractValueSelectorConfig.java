package ru.sbsoft.shared.meta;

import java.util.ArrayList;
import java.util.List;
import ru.sbsoft.shared.FilterInfo;
import ru.sbsoft.shared.interfaces.ObjectType;
import ru.sbsoft.shared.meta.IValueSelectorConfig;

/**
 *
 * @author vk
 */
public abstract class AbstractValueSelectorConfig<T extends ObjectType> implements IValueSelectorConfig<T> {

    private T browserType;
    private List<FilterInfo> filters = new ArrayList<>();
    private boolean showCode = false;

    protected AbstractValueSelectorConfig(T browserType) {
        this.browserType = browserType;
    }

    protected AbstractValueSelectorConfig() {
    }

    @Override
    public T getBrowserType() {
        return browserType;
    }

    @Override
    public List<FilterInfo> getFilters() {
        return filters;
    }

    public void add(FilterInfo filter) {
        this.filters.add(filter);
    }

    public void setFilters(List<FilterInfo> filters) {
        this.filters.clear();
        this.filters.addAll(filters);
    }

    @Override
    public boolean isShowCode() {
        return showCode;
    }

    public void setShowCode(boolean showCode) {
        this.showCode = showCode;
    }
}
