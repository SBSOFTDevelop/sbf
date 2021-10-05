package ru.sbsoft.dao;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Date;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;
import ru.sbsoft.dao.entity.IBaseEntity;
import ru.sbsoft.meta.lookup.ILookupModelProvider;
import ru.sbsoft.sbf.app.model.YearMonthDay;
import ru.sbsoft.server.utils.SrvUtl;
import ru.sbsoft.shared.FilterInfo;
import ru.sbsoft.shared.consts.Dict;
import ru.sbsoft.shared.interfaces.GridType;
import ru.sbsoft.shared.interfaces.NamedItem;
import ru.sbsoft.shared.model.LookupInfoModel;

/**
 *
 * @author Kiselev
 */
public class FilterHelper {

    private static final Map<Class<? extends Number>, BdNarrow> narrowCache;

    static {
        Map<Class<? extends Number>, BdNarrow> m = new HashMap<>();
        add(m, new LongBdNarrow());
        add(m, new IntBdNarrow());
        add(m, new DoubleBdNarrow());
        narrowCache = Collections.unmodifiableMap(m);
    }

    private static void add(Map<Class<? extends Number>, BdNarrow> m, BdNarrow n) {
        if (m.get(n.getToClass()) != null) {
            throw new IllegalArgumentException("BdNarrow for class already added. Class: " + n.getToClass());
        }
        m.put(n.getToClass(), n);
    }

    private final EntityManager em;

    public FilterHelper(EntityManager em) {
        this.em = em;
    }

    public static <T> T getFilterVal(final List<FilterInfo> parentFilters, final NamedItem filterItem, final Class<T> clazz) {
        return getFilterVal(parentFilters, filterItem.getCode(), clazz);
    }

    public static <T> T getFilterVal(final List<FilterInfo> parentFilters, final String filterName, final Class<T> clazz) {
        if (parentFilters != null && !parentFilters.isEmpty()) {
            for (FilterInfo f : parentFilters) {
                if (filterName.equals(f.getColumnName())) {
                    if (f.getValue() == null) {
                        return null;
                    }
                    Object val = f.getValue();
                    if (Enum.class.isAssignableFrom(clazz) && (val instanceof String)) {
                        String valStr = (String) val;
                        EnumSet<?> es = EnumSet.allOf((Class<Enum>) clazz);
                        for (Enum e : es) {
                            if (valStr.equalsIgnoreCase(e.name())) {
                                return clazz.cast(e);
                            }
                        }
                        throw new IllegalArgumentException("FilterInfo " + filterName + ": value[" + valStr + "]; not found in " + clazz.getName() + " class");
                    } else if (clazz.isAssignableFrom(val.getClass())) {
                        return clazz.cast(val);
                    } else if ((val instanceof BigDecimal) && Number.class.isAssignableFrom(clazz)) {
                        BdNarrow n = getNarrow((Class<Number>) clazz);
                        if (n != null) {
                            return clazz.cast(n.narrow((BigDecimal) val, filterName));
                        }
                    } else if ((val instanceof YearMonthDay) && clazz.isAssignableFrom(Date.class)) {
                        Date d = SrvUtl.toDate((YearMonthDay) val);
                        return clazz.cast(d);
                    }
                    throw new IllegalArgumentException("FilterInfo " + filterName + ": requested[" + clazz.getName() + "]; found[" + f.getValue().getClass().getName() + "]");
                }
            }
        }
        return null;
    }

    private static <T extends Number> BdNarrow<T> getNarrow(Class<T> clazz) {
        return narrowCache.get(clazz);
    }

    private static abstract class BdNarrow<T extends Number> {

        private final Class<T> toClass;
        private final BigDecimal min;
        private final BigDecimal max;

        protected BdNarrow(Class<T> toClass, BigDecimal min, BigDecimal max) {
            this.toClass = toClass;
            this.min = min;
            this.max = max;
        }

        public T narrow(BigDecimal val) {
            return narrow(val, "");
        }

        public T narrow(BigDecimal val, String filterName) {
            check(val, min, max, filterName);
            return doNarrow(val);
        }

        private void check(BigDecimal val, BigDecimal min, BigDecimal max, String filterName) {
            if (val.compareTo(min) < 0 || val.compareTo(max) > 0) {
                throw new IllegalArgumentException("FilterInfo " + filterName + ": value " + val + " is not in range " + min + " - " + max + " and can't be converted to " + getToClass().getName());
            }
        }

        public Class<T> getToClass() {
            return toClass;
        }

        protected abstract T doNarrow(BigDecimal val);

    }

    private static class LongBdNarrow extends BdNarrow<Long> {

        public LongBdNarrow() {
            super(Long.class, BigDecimal.valueOf(Long.MIN_VALUE), BigDecimal.valueOf(Long.MAX_VALUE));
        }

        @Override
        protected Long doNarrow(BigDecimal val) {
            return val.longValue();
        }

    }

    private static class IntBdNarrow extends BdNarrow<Integer> {

        public IntBdNarrow() {
            super(Integer.class, BigDecimal.valueOf(Integer.MIN_VALUE), BigDecimal.valueOf(Integer.MAX_VALUE));
        }

        @Override
        protected Integer doNarrow(BigDecimal val) {
            return val.intValue();
        }

    }

    private static class DoubleBdNarrow extends BdNarrow<Double> {

        public DoubleBdNarrow() {
            super(Double.class, BigDecimal.valueOf(Double.MIN_VALUE), BigDecimal.valueOf(Double.MAX_VALUE));
        }

        @Override
        protected Double doNarrow(BigDecimal val) {
            return val.doubleValue();
        }

    }

    public static BigDecimal getParentId(final List<FilterInfo> parentFilters) {
        return getParentId(parentFilters, Dict.FILTER_PARENT_ID);
    }

    public static BigDecimal getParentId(final List<FilterInfo> parentFilters, GridType gridType) {
        return getParentId(parentFilters, gridType.getParentIdName());
    }

    public static BigDecimal getParentId(final List<FilterInfo> parentFilters, final String filterName) {
        return getFilterVal(parentFilters, filterName, BigDecimal.class);
    }

    public final <E extends IBaseEntity> E getParentEntity(final List<FilterInfo> parentFilters, Class<E> c) {
        return getParentEntity(parentFilters, c, (String) null);
    }

    public final <E extends IBaseEntity> E getParentEntity(final List<FilterInfo> parentFilters, Class<E> c, GridType gridType) {
        return getParentEntity(parentFilters, c, gridType.getParentIdName());
    }

    public final <E extends IBaseEntity> E getParentEntity(final List<FilterInfo> parentFilters, Class<E> c, final String filterName) {
        return getParentEntity(em, parentFilters, c, filterName);
    }

    public static final <E extends IBaseEntity> E getParentEntity(EntityManager em, final List<FilterInfo> parentFilters, Class<E> c, final String filterName) {
        BigDecimal parentId = filterName == null || filterName.trim().isEmpty() ? getParentId(parentFilters) : getParentId(parentFilters, filterName);
        return parentId != null ? em.find(c, parentId) : null;
    }

    public final <E extends IBaseEntity & ILookupModelProvider> LookupInfoModel getParentLookup(final List<FilterInfo> parentFilters, Class<E> c) {
        return getParentLookup(parentFilters, c, (String) null);
    }

    public final <E extends IBaseEntity & ILookupModelProvider> LookupInfoModel getParentLookup(final List<FilterInfo> parentFilters, Class<E> c, GridType gridType) {
        return getParentLookup(parentFilters, c, gridType.getParentIdName());
    }

    public final <E extends IBaseEntity & ILookupModelProvider> LookupInfoModel getParentLookup(final List<FilterInfo> parentFilters, Class<E> c, final String filterName) {
        E e = getParentEntity(parentFilters, c, filterName);
        return toLookup(e);
    }

    private <E extends ILookupModelProvider> LookupInfoModel toLookup(E e) {
        return toLookup(em, e);
    }

    public static <E extends ILookupModelProvider> LookupInfoModel toLookup(EntityManager em, E e) {
        return e != null ? e.toLookupModel(em) : null;
    }
}
