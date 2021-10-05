package ru.sbsoft.shared;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import ru.sbsoft.common.Strings;
import ru.sbsoft.shared.filter.FilterConsts;

/**
 * Базовый абстрактный класс представляющий элемент пользовательского фильтра.
 * <ul>
 * <li>private String columnName - имя поля таблицы </li>
 * <li>private ComparisonEnum comparison - оператор сравнения</li>
 * <li>private FilterTypeEnum type -тип операции (определяется типом данных
 * поля)</li>
 * <li>private T value - значение (с чем сравнивать)</li>
 * <li>private boolean caseSensitive - регистро-зависимость </li>
 * <li>private boolean notExpression -инверсия условия ({@code not <condition>})
 * </li>
 * </ul>
 *
 * @author balandin
 * @param <VALUE_TYPE>
 */
public abstract class FilterInfo<VALUE_TYPE> implements Serializable {

    private String columnName;
    private ComparisonEnum comparison;
    private FilterTypeEnum type;
    private VALUE_TYPE value, secondValue;
    private boolean caseSensitive = true;
    private boolean notExpression;
    //
    //private transient int quickFilterIndex = -1;
    private int quickFilterIndex = -1;

    public FilterInfo() {
    }

    public FilterInfo(FilterTypeEnum type) {
        this.type = type;
    }

    public static List<FilterInfo> list(FilterInfo... filters) {
        List<FilterInfo> result = null;
        if (filters != null) {
            result = new ArrayList<FilterInfo>(filters.length);
            result.addAll(Arrays.asList(filters));
        }
        return result;
    }

    public static boolean isFitersSetted(final List<FilterInfo> filters) {
        if (null != filters) {
            for (final FilterInfo f : filters) {
                if (null != f && null != f.getValue()) {
                    return true;
                }
            }
        }
        return false;
    }

    public static FilterInfo findFilter(final List<FilterInfo> filters, String name) {
        if (null != filters) {
            for (final FilterInfo f : filters) {
                if (Strings.equals(name, f.getColumnName())) {
                    return f;
                }
            }
        }
        return null;
    }

    public static List<FilterInfo> mergeFilters(List<FilterInfo> dest, List<FilterInfo> src) {
        if (src == null || src.isEmpty()) {
            return dest;
        }

        if (dest == null) {
            return src;
        }

        HashMap<String, FilterInfo> tmp = new HashMap<String, FilterInfo>(dest.size());
        for (FilterInfo info : dest) {
            tmp.put(info.getColumnName(), info);
        }

        for (FilterInfo info : src) {
            final FilterInfo existed = tmp.get(info.getColumnName());
            if (existed != null) {
                dest.remove(existed);
            }
            dest.add(info);
        }

        return dest;
    }

    public FilterInfo(String columnName, ComparisonEnum comparison, FilterTypeEnum type, VALUE_TYPE value) {
        this.columnName = columnName;
        this.comparison = comparison;
        this.type = type;
        this.value = value;
    }

    public String getColumnName() {
        return columnName;
    }

    public FilterInfo setColumnName(String columnName) {
        this.columnName = columnName;
        return this;
    }

    public ComparisonEnum getComparison() {
        return comparison;
    }

    public FilterInfo setComparison(ComparisonEnum comparison) {
        this.comparison = comparison;
        return this;
    }

    public FilterTypeEnum getType() {
        return type;
    }

    public FilterInfo setType(FilterTypeEnum type) {
        this.type = type;
        return this;
    }

    public VALUE_TYPE getValue() {
        return value;
    }

    public FilterInfo setValue(VALUE_TYPE value) {
        this.value = value;
        return this;
    }

    protected static <V, F extends FilterInfo<V>> V getValue(List<FilterInfo> filters, F filter) {
        return getValue(filters, (Class<F>) filter.getClass(), filter.getColumnName());
    }

    public static <V, F extends FilterInfo<V>> V getValue(List<FilterInfo> filters, Class<F> clazz, String name) {
        F f = getFilter(filters, clazz, name);
        return f != null ? (V) f.getValue() : null;
    }

    public static <V, F extends FilterInfo<V>> F getFilter(List<FilterInfo> filters, Class<F> clazz, String name) {
        F found = null;
        for (FilterInfo f : filters) {
            if (f != null && Objects.equals(f.getColumnName(), name)) {
                if (deepEq(clazz, f.getClass())) {
                    if (found != null) {
                        throw new IllegalArgumentException("Threre are many filters with name '" + name + "' and class '" + clazz.getName() + "'");
                    } else {
                        found = (F) f;
                    }
                }
            }
        }
        return found;
    }

    public static boolean deepEq(Class<?> c1, Class<?> c2) {
        while (c2 != null) {
            if (c2.equals(c1)) {
                return true;
            }
            c2 = c2.getSuperclass();
        }
        return false;
    }

    public VALUE_TYPE getSecondValue() {
        return secondValue;
    }

    public FilterInfo setSecondValue(VALUE_TYPE secondValue) {
        this.secondValue = secondValue;
        return this;
    }

    public boolean isCaseSensitive() {
        return caseSensitive;
    }

    public FilterInfo setCaseSensitive(boolean caseSensitive) {
        this.caseSensitive = caseSensitive;
        return this;
    }

    public FilterInfo setQuickFilterIndex(int quickFilterIndex) {
        this.quickFilterIndex = quickFilterIndex;
        return this;
    }

    public String getSQLParameterName() {
        return getSQLParameterName(1);
    }

    public String getSQLParameterName(int num) {
        return (quickFilterIndex >= 0 ? "QUICK_FILTER_" + quickFilterIndex : columnName)
                + (num == 0 ? Strings.EMPTY : "_" + num);
    }

    public boolean isCurrentDate() {
        return FilterConsts.CURRENT_DATE_FIELD.equals(columnName);
    }

//    public static Date getCurrentDate(final List<FilterInfo> filters) {
//        if (filters == null) {
//            return null;
//        }
//        for (final FilterInfo f : filters) {
//            if (!f.isCurrentDate()) {
//                continue;
//            }
//            if (!(f.getValue() instanceof Date)) {
//                break;
//            }
//            return (Date) f.getValue();
//        }
//        return null;
//    }
    public boolean isNotExpression() {
        return notExpression;
    }

    public FilterInfo setNotExpression(boolean notExpression) {
        this.notExpression = notExpression;
        return this;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 31 * hash + (this.columnName != null ? this.columnName.hashCode() : 0);
        hash = 31 * hash + (this.comparison != null ? this.comparison.hashCode() : 0);
        hash = 31 * hash + (this.type != null ? this.type.hashCode() : 0);
        hash = 31 * hash + (this.value != null ? this.value.hashCode() : 0);
        hash = 31 * hash + (this.caseSensitive ? 1 : 0);
        hash = 31 * hash + (this.notExpression ? 1 : 0);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final FilterInfo<?> other = (FilterInfo<?>) obj;
        if ((this.columnName == null) ? (other.columnName != null) : !this.columnName.equals(other.columnName)) {
            return false;
        }
        if (this.comparison != other.comparison) {
            return false;
        }
        if (this.type != other.type) {
            return false;
        }
        if (this.value != other.value && (this.value == null || !this.value.equals(other.value))) {
            return false;
        }
        if (this.caseSensitive != other.caseSensitive) {
            return false;
        }
        if (this.notExpression != other.notExpression) {
            return false;
        }
        return true;
    }

    public boolean isEmpty() {
        return columnName == null || columnName.trim().isEmpty();
    }
}
