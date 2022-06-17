package ru.sbsoft.model;

import ru.sbsoft.shared.model.SortingInfo;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import ru.sbsoft.shared.FilterInfo;
import ru.sbsoft.shared.FiltersBean;

/**
 * Класс предоставляет методы для работы с фильтрами и сортировкой в Гриде, (класс и подклассы
 * {@link ru.sbsoft.client.components.grid.SystemGrid}). Используется экземпляром наследником класса
 * {@link  ru.sbsoft.dao.AbstractBuilder} для формирования выражения SQL
 * {@code where <условие отбора записей>} и сортировки
 * {@code order by <field1 [desc].., fieldN [desc]>}.
 *
 * @author balandin
 */
public class PageFilterInfo implements Serializable {

    private List<FilterInfo> parentFilters;
    private FiltersBean filters;
    private FilterInfo tempFilter;
    //
    private int limit;
    private int offset;
    private List<SortingInfo> sorts;
    //
    private String columnName;

    public PageFilterInfo() {
    }

    public List<FilterInfo> getParentFilters() {
        if (parentFilters == null) {
            parentFilters = Collections.EMPTY_LIST;
        }
        return parentFilters;
    }

    public void setParentFilters(List<FilterInfo> parentFilters) {
        this.parentFilters = parentFilters;
    }

    /**
     * Максимальное количество записей возращаемых на клиент. {@code offset + limit} -номер
     * последней записи
     *
     * @return
     */
    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    /**
     * Смещение (номер первой записи)
     *
     * @return
     */
    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public List<SortingInfo> getSorts() {
        return sorts;
    }

    public void setSorts(List<SortingInfo> sorts) {
        this.sorts = sorts;
    }

    public void addSort(SortingInfo sortingInfo) {
        if (sorts == null) {
            sorts = new ArrayList<SortingInfo>();
        }
        sorts.add(sortingInfo);
    }

    public FilterInfo getTempFilter() {
        return tempFilter;
    }

    public void setTempFilter(FilterInfo tempFilterInfo) {
        this.tempFilter = tempFilterInfo;
    }

    public FiltersBean getFilters() {
        if (null == filters) {
            filters = new FiltersBean();
        }
        return filters;
    }

    public void setFilters(FiltersBean filters) {
        this.filters = filters;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }
}
