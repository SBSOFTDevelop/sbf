package ru.sbsoft.meta.context;

import java.util.HashMap;
import java.util.List;
import ru.sbsoft.common.jdbc.QueryParam;
import ru.sbsoft.model.PageFilterInfo;
import ru.sbsoft.shared.ComparisonEnum;
import ru.sbsoft.shared.FilterInfo;
import ru.sbsoft.shared.FilterInfoGroup;
import ru.sbsoft.shared.filter.LongFilterInfo;
import ru.sbsoft.shared.filter.LookUpFilterInfo;

/**
 * Класс представляет контекст (окружение) фильтра. Экземпляр класса
 * инкапсулирует в <code>HashMap<String, FilterInfo> cache</code> параметры
 * фильтра SQL запроса, подставляемые парсером построителя запросов
 * {@link ru.sbsoft.dao.AbstractBuilder} в предикаты оператора SQL
 * <code>where</code>.
 *
 * @author balandin
 * @since May 6, 2014 11:24:06 AM
 */
public class FiltersContext extends AbstractQueryContext {

    private final PageFilterInfo pageFilterInfo;
    private HashMap<String, FilterInfo> cache;

    public FiltersContext(PageFilterInfo pageFilterInfo) {
        this.pageFilterInfo = pageFilterInfo;
    }

    private HashMap<String, FilterInfo> getCache() {
        if (cache == null) {
            cache = new HashMap<String, FilterInfo>();
            fillCache(cache, pageFilterInfo.getParentFilters(), true);
            fillCache(cache, pageFilterInfo.getFilters().getSystemFilters(), true);
            fillCache(cache, pageFilterInfo.getFilters().getUserFilters().getChildFilters(), false);
            fillCache(cache, pageFilterInfo.getTempFilter(), false);
        }
        return cache;
    }

    @Override
    public QueryParam getParam(String name) {
        final FilterInfo filterInfo = getCache().get(name);
        if (filterInfo != null) {
            return new FilterQueryParamAdapter(filterInfo, name.endsWith("_2"));
        }
        return null;
    }

    private void fillCache(HashMap<String, FilterInfo> cache, List<FilterInfo> filters, boolean system) {
        if (filters != null) {
            for (final FilterInfo f : filters) {
                fillCache(cache, f, system);
            }
        }
    }

    private void fillCache(HashMap<String, FilterInfo> cache, FilterInfo f, boolean system) {
        if (f != null && f.getValue() != null) {
            if (f instanceof FilterInfoGroup) {
                fillCache(cache, ((FilterInfoGroup) f).getChildFilters(), system);
            } else if (f instanceof LookUpFilterInfo) {
                LookUpFilterInfo lookupFilterInfo = (LookUpFilterInfo) f;

                LongFilterInfo t = new LongFilterInfo();
                t.setValue(lookupFilterInfo.getTmp1());
                t.setSecondValue(lookupFilterInfo.getTmp2());

                cache.put(f.getSQLParameterName(0), t);
                cache.put(f.getSQLParameterName(1), t);
                cache.put(f.getSQLParameterName(2), t);
            } else if (f.getComparison() == ComparisonEnum.in_bound || f.getComparison() == ComparisonEnum.in_range) {
                cache.put(f.getSQLParameterName(0), f);
                cache.put(f.getSQLParameterName(1), f);
                cache.put(f.getSQLParameterName(2), f);
            } else {
                cache.put(f.getSQLParameterName(0), f);
                cache.put(f.getSQLParameterName(1), f);
            }
        }
    }
}


