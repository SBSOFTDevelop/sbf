package ru.sbsoft.meta;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import ru.sbsoft.common.Strings;
import ru.sbsoft.meta.columns.AddressColumnInfo;
import ru.sbsoft.meta.columns.BooleanColumnInfo;
import ru.sbsoft.meta.columns.ColumnInfo;
import ru.sbsoft.meta.columns.ColumnKind;
import ru.sbsoft.meta.columns.CurrencyColumnInfo;
import ru.sbsoft.meta.columns.DateColumnInfo;
import ru.sbsoft.meta.columns.DateTimeColumnInfo;
import ru.sbsoft.meta.columns.IdNameColumnInfo;
import ru.sbsoft.meta.columns.IdentifierColumnInfo;
import ru.sbsoft.meta.columns.IntegerColumnInfo;
import ru.sbsoft.meta.columns.KeyColumnInfo;
import ru.sbsoft.meta.columns.StringColumnInfo;
import ru.sbsoft.meta.columns.TemporalKeyColumnInfo;
import ru.sbsoft.meta.columns.TimestampColumnInfo;
import ru.sbsoft.meta.columns.YearMonthDayColumnInfo;
import ru.sbsoft.meta.columns.style.IGridConditionFactory;
import ru.sbsoft.shared.api.i18n.I18nResourceInfo;
import ru.sbsoft.shared.api.i18n.ILocalizedString;
import ru.sbsoft.shared.api.i18n.LocalizedString;
import ru.sbsoft.shared.grid.condition.IGridCondition;
import ru.sbsoft.shared.grid.style.CStyle;
import ru.sbsoft.shared.grid.style.ConditionalCellStyle;
import ru.sbsoft.shared.api.i18n.NonLocalizedString;
import ru.sbsoft.shared.meta.ColumnType;
import ru.sbsoft.shared.meta.Columns;
import ru.sbsoft.shared.meta.IColumn;
import ru.sbsoft.shared.meta.IGridCustomInfo;
import ru.sbsoft.shared.meta.Style;

/**
 * Класс представляет методы для работы с коллекцией столбцов.
 * <p>
 * Инкапсулирует мета информацию о колонках в виде generic списка экземпляров
 * класса({@link ru.sbsoft.meta.columns.ColumnInfo}) сетки.
 * <p>
 * Экземпляр создается экземпляром класса, расширяющего класс
 * {@link ru.sbsoft.dao.AbstractTemplate}</p>
 * <p>
 * в методе <code>public ColumnsInfo.createColumns()</code>. Например:
 * <pre>
 *  &#64;Override
 * public ColumnsInfo createColumns() {
 * ColumnsInfo c = new ColumnsInfo();
 * c.addColumn(KEY, "A.RECORD_ID");
 * c.addColumn(VCHAR, 100, "Период", "A.CALC_PERIOD");
 * ...
 * return c;
 * </pre> Информация о столбцах экземпляра класса <code>ColumnsInfo</code>
 * используется экземпляром построителя {@link ru.sbsoft.meta.sql.SQLBuilder}
 * для построения SQL запроса
 *
 * @author balandin
 */
public class ColumnsInfo {

    private String selectModifier;
    private final ColumnInfoList items = new ColumnInfoList();
    private List<Style> styles;
    private String templateName;
    private final List<ConditionalCellStyle> gridStyles = new ArrayList<>();
    //
    private IGridCondition editCondition;
    //
    private IGridCustomInfo customInfo;
    //
    private String updateTableName;
    
    private boolean signPositive = false;
    
    private boolean rowStyleDominate = true;

    static {
        ColumnType.KEY.setColumnClass(KeyColumnInfo.class);
        ColumnType.TEMPORAL_KEY.setColumnClass(TemporalKeyColumnInfo.class);
        ColumnType.IDENTIFIER.setColumnClass(IdentifierColumnInfo.class);
        ColumnType.VCHAR.setColumnClass(StringColumnInfo.class);
        ColumnType.DATE.setColumnClass(DateColumnInfo.class);
        ColumnType.DATE_TIME.setColumnClass(DateTimeColumnInfo.class);
        ColumnType.TIMESTAMP.setColumnClass(TimestampColumnInfo.class);
        ColumnType.BOOL.setColumnClass(BooleanColumnInfo.class);
        ColumnType.INTEGER.setColumnClass(IntegerColumnInfo.class);
        ColumnType.CURRENCY.setColumnClass(CurrencyColumnInfo.class);
        ColumnType.ADDRESS.setColumnClass(AddressColumnInfo.class);
        ColumnType.ID_NAME.setColumnClass(IdNameColumnInfo.class);
        ColumnType.YMDAY.setColumnClass(YearMonthDayColumnInfo.class);
    }

    public ColumnsInfo() {
        this(null);
    }

    public ColumnsInfo(String selectModifier) {
        this.selectModifier = selectModifier;
    }

    public List<ColumnInfo> getItems() {
        return items;
    }

    public void setItems(List<ColumnInfo> items) {
        this.items.clear();
        if (items != null && !items.isEmpty()) {
            this.items.addAll(items);
        }
    }

    /**
     * Перегружаемый метод, добавляет новый столбец в коллекцию столбцов.
     *
     * @param type   тип столбца {@link ru.sbsoft.shared.meta.ColumnType}
     * @param clause имя поля физической таблицы
     * @return экземпляр {@link ru.sbsoft.meta.columns.ColumnInfo}
     */
    public <V, T extends ColumnInfo<V>> T add(ColumnKind<V, T> type, String clause) {
        return add(type, 100, (ILocalizedString) null, clause);
    }

    /**
     * Перегружаемый метод, добавляет новый столбец в коллекцию столбцов.
     *
     * @param type    тип столбца {@link ru.sbsoft.shared.meta.ColumnType}
     * @param caption заголовок столбца
     * @param clause  имя поля физической таблицы
     * @return экземпляр {@link ru.sbsoft.meta.columns.ColumnInfo}
     */
    public <V, T extends ColumnInfo<V>> T add(ColumnKind<V, T> type, ILocalizedString caption, String clause) {
        return add(type, 100, caption, clause);
    }

    public <V, T extends ColumnInfo<V>> T add(ColumnKind<V, T> type, I18nResourceInfo caption, String clause) {
        return add(type, 100, caption, clause);
    }

    public <V, T extends ColumnInfo<V>> T add(ColumnKind<V, T> type, String caption, String clause) {
        return add(type, 100, caption, clause);
    }

    /**
     * Перегружаемый метод, добавляет новый столбец в коллекцию столбцов.
     *
     * @param type    тип столбца {@link ru.sbsoft.shared.meta.ColumnType}
     * @param caption заголовок столбца
     * @param clause  имя поля физической таблицы
     * @param alias   псевдоним поля-алиас (<i>select fname <b>AS</b> alias...
     *                </i>)
     * @return экземпляр {@link ru.sbsoft.meta.columns.ColumnInfo}
     */
    public <V, T extends ColumnInfo<V>> T add(ColumnKind<V, T> type, ILocalizedString caption, String clause, String alias) {
        return add(type, 100, caption, clause, alias);
    }

    public <V, T extends ColumnInfo<V>> T add(ColumnKind<V, T> type, I18nResourceInfo caption, String clause, String alias) {
        return add(type, 100, caption, clause, alias);
    }

    public <V, T extends ColumnInfo<V>> T add(ColumnKind<V, T> type, String caption, String clause, String alias) {
        return add(type, 100, caption, clause, alias);
    }

    /**
     * Перегружаемый метод, добавляет новый столбец в коллекцию столбцов.
     *
     * @param type    тип столбца {@link ru.sbsoft.shared.meta.ColumnType}
     * @param width   ширина столбца на экране
     * @param caption заголовок столбца
     * @param clause  имя поля физической таблицы
     * @return экземпляр {@link ru.sbsoft.meta.columns.ColumnInfo}
     */
    public <V, T extends ColumnInfo<V>> T add(ColumnKind<V, T> type, int width, ILocalizedString caption, String clause) {
        return add(type, width, caption, clause, null);
    }

    public <V, T extends ColumnInfo<V>> T add(ColumnKind<V, T> type, int width, I18nResourceInfo caption, String clause) {
        return add(type, width, new LocalizedString(caption), clause, null);
    }

    public <V, T extends ColumnInfo<V>> T add(ColumnKind<V, T> type, int width, String caption, String clause) {
        return add(type, width, caption, clause, null);
    }

    /**
     * Перегружаемый метод, добавляет новый столбец в коллекцию столбцов.
     *
     * @param address массив элементов адреса
     * @param width   ширина столбца на экране
     * @param caption заголовок столбца
     * @param clause  имя поля физической таблицы
     * @param alias   псевдоним поля-алиас (<i>select fname <b>AS</b> alias...
     *                </i>)
     * @return экземпляр {@link ru.sbsoft.meta.columns.ColumnInfo}
     */
    public AddressColumnInfo addAddr(String[] address, int width, ILocalizedString caption, String clause, String alias) {
        AddressColumnInfo c = add(ColumnKind.ADDRESS, width, caption, clause, alias);
        c.setFields(address);
        return c;
    }

    public AddressColumnInfo addAddr(String[] address, int width, I18nResourceInfo caption, String clause, String alias) {
        return addAddr(address, width, new LocalizedString(caption), clause, alias);
    }

    public AddressColumnInfo addAddr(String[] address, int width, String caption, String clause, String alias) {
        return addAddr(address, width, new NonLocalizedString(caption), clause, alias);
    }

    /**
     * Перегружаемый метод, добавляет новый столбец в коллекцию столбцов.
     *
     * @param type    тип столбца {@link ru.sbsoft.shared.meta.ColumnType}
     * @param width   ширина столбца на экране
     * @param caption заголовок столбца
     * @param clause  имя поля физической таблицы
     * @param alias   псевдоним поля-алиас (<i>select fname <b>AS</b> alias...
     *                </i>)
     * @return экземпляр {@link ru.sbsoft.meta.columns.ColumnInfo}
     */
    public <V, T extends ColumnInfo<V>> T add(ColumnKind<V, T> type, int width, ILocalizedString caption, String clause, String alias) {
        return add(createColumn(type, width, caption, clause, alias));
    }

    public <V, T extends ColumnInfo<V>> T add(ColumnKind<V, T> type, int width, I18nResourceInfo caption, String clause, String alias) {
        return add(createColumn(type, width, caption, clause, alias));
    }

    public <V, T extends ColumnInfo<V>> T add(ColumnKind<V, T> type, int width, String caption, String clause, String alias) {
        return add(createColumn(type, width, caption, clause, alias));
    }

    /**
     * Перегружаемый метод, добавляет новый столбец в коллекцию столбцов.
     *
     * @param <V> column value type
     * @param <T> {@link ru.sbsoft.meta.columns.ColumnInfo}
     * @param c column for add
     * @return экземпляр {@link ru.sbsoft.meta.columns.ColumnInfo}
     */
    public <V, T extends ColumnInfo<V>> T add(T c) {
        items.add(c);
        return c;
    }

    public static <V, T extends ColumnInfo<V>> T createColumn(ColumnKind<V, T> type, int width, ILocalizedString caption, String clause, String alias) {
        T c = type.createColumnInfo();
        c.setWidth(width);
        c.setClause(clause);
        String calcAlias = extractAlias(clause, alias);
        c.setAlias(calcAlias);
        if (caption != null) {
            c.setCaption(caption);
        } else {
            c.setCaption(new NonLocalizedString(!Strings.isEmpty(calcAlias) ? calcAlias : "!!!"));
        }
        return c;
    }

    public static <V, T extends ColumnInfo<V>> T createColumn(ColumnKind<V, T> type, int width, I18nResourceInfo caption, String clause, String alias) {
        return createColumn(type, width, new LocalizedString(caption), clause, alias);
    }

    public static <V, T extends ColumnInfo<V>> T createColumn(ColumnKind<V, T> type, int width, String caption, String clause, String alias) {
        return createColumn(type, width, !Strings.isEmpty(caption) ? new NonLocalizedString(caption) : null, clause, alias);
    }

    public static <V, T extends ColumnInfo<V>> T createColumn(ColumnKind<V, T> type) {
        return type.createColumnInfo();
    }

    private static String extractAlias(String clause, String alias) {
        if (alias != null) {
            return alias;
        }
        int n = clause.lastIndexOf('.');
        if (n == -1) {
            return clause;
        }
        return clause.substring(n + 1);
    }

    public void setSelectModifier(String selectModifier) {
        this.selectModifier = selectModifier;
    }

    public String getSelectModifier() {
        return selectModifier;
    }

    /**
     * Добавляет стиль строки, применяемый в зависимости от условия.
     *
     * @param style оъект, содержащий строку стиля в формате css и строку
     *              условия на языке javascript
     * @deprecated используйте
     * {@link #addStyle(ru.sbsoft.shared.grid.style.CStyle, ru.sbsoft.meta.columns.style.IColumnConditionFactory)}
     * или {@link #addStyle(ru.sbsoft.shared.grid.style.CStyle)}
     */
    public void addStyle(Style style) {
        if (styles == null) {
            styles = new ArrayList<>();
        }
        styles.add(style);
    }

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public IGridCustomInfo getCustomInfo() {
        return customInfo;
    }

    public ColumnsInfo setCustomInfo(IGridCustomInfo customInfo) {
        this.customInfo = customInfo;
        return this;
    }

    /**
     * Добавляет безусловный (применяется всегда) стиль ячеек таблицы.
     *
     * @param style стиль
     * @return текущий объект
     */
    public ColumnsInfo addStyle(CStyle style) {
        return addStyle(style, (IGridCondition) null);
    }

    /**
     * Добавляет условный (применение зависит от {@code condition}) стиль ячеек
     * таблицы. При этом объект условия не может содержать специфичных для
     * сервера (не доступных на клиенте) объектов. Метод определен как
     * внутренний, т.к. предполагается, что в прикладном смысле достаточно будет
     * {@link #addStyle(ru.sbsoft.shared.grid.style.CStyle, ru.sbsoft.meta.columns.style.IGridConditionFactory)}
     *
     * @param style     стиль
     * @param condition условие
     * @return текущий объект
     */
    private ColumnsInfo addStyle(CStyle style, IGridCondition condition) {
        gridStyles.add(new ConditionalCellStyle(style).setCondition(condition));
        return this;
    }

    /**
     * Добавляет условный (применение зависит от {@code conditionFactory}) стиль
     * ячеек таблицы. При этом сгенерированный {@code conditionFactory} объект
     * условия не может содержать специфичных для сервера (не доступных на
     * клиенте) объектов. Для использования с этим методом имеются библиотечные
     * объекты:
     * {@link ru.sbsoft.meta.columns.style.condition.Eq}, {@link ru.sbsoft.meta.columns.style.condition.Gt}, {@link ru.sbsoft.meta.columns.style.condition.Substr}
     * и др. подобные.
     *
     * @param style            стиль
     * @param conditionFactory генератор условия
     * @return текущий объект
     */
    public ColumnsInfo addStyle(CStyle style, IGridConditionFactory conditionFactory) {
        return addStyle(style, conditionFactory != null ? conditionFactory.createCondition() : null);
    }

    public IGridCondition getEditCondition() {
        return editCondition;
    }

    public ColumnsInfo setEditCondition(IGridCondition editCondition) {
        this.editCondition = editCondition;
        return this;
    }

    public ColumnsInfo setEditCondition(IGridConditionFactory conditionFactory) {
        return setEditCondition(conditionFactory != null ? conditionFactory.createCondition() : null);
    }

    public ColumnInfo get(String alias) {
        return items.getCache().get(alias);
    }
    
    public String findColumnAliasIgnoreCase(String aliasAnyCase){
        for(String al : items.getCache().keySet()){
            if(al.equalsIgnoreCase(aliasAnyCase)){
                return al;
            }
        }
        return null;
    }

    public Columns getColumns() {
        return items.getColumns();
    }

    public ColumnInfo getKeyColumn() {
        return items.getKeyColumn();
    }

    public void setUpdateTableName(String updateTableName) {
        this.updateTableName = updateTableName;
    }

    public boolean isSignPositive() {
        return signPositive;
    }

    public void setSignPositive(boolean signPositive) {
        this.signPositive = signPositive;
    }

    public boolean isRowStyleDominate() {
        return rowStyleDominate;
    }

    public void setRowStyleDominate(boolean rowStyleDominate) {
        this.rowStyleDominate = rowStyleDominate;
    }

    public class ColumnInfoList implements List<ColumnInfo> {

        private final List<ColumnInfo> delegate = new ArrayList<>();

        private transient HashMap<String, ColumnInfo> cache;
        private transient Columns columnsCache;
        private transient ColumnInfo keyColumn;

        public Columns getColumns() {
            if (columnsCache == null) {
                columnsCache = new Columns();
                columnsCache.setStyles(styles);
                columnsCache.setGridStyles(gridStyles.isEmpty() ? null : gridStyles);
                columnsCache.setEditCondition(editCondition);
                columnsCache.setCustomInfo(customInfo);
                columnsCache.setSignPositive(signPositive);
                columnsCache.setUpdateTableName(updateTableName);
                columnsCache.setRowStyleDominate(rowStyleDominate);
                for (ColumnInfo c : items) {
                    addToColCache(c);
                }
            }
            return columnsCache;
        }

        public HashMap<String, ColumnInfo> getCache() {
            if (cache == null) {
                cache = new HashMap<>();
                for (ColumnInfo c : items) {
                    putToCache(c);
                }
            }
            return cache;
        }

        public ColumnInfo getKeyColumn() {
            if (keyColumn == null) {
                keyColumn = findKeyColumn();
            }
            return keyColumn;
        }

        //=============== List methods ==========================
        @Override
        public int size() {
            return delegate.size();
        }

        @Override
        public boolean isEmpty() {
            return delegate.isEmpty();
        }

        @Override
        public boolean contains(Object o) {
            return delegate.contains(o);
        }

        @Override
        public Iterator<ColumnInfo> iterator() {
            return delegate.iterator();
        }

        @Override
        public Object[] toArray() {
            return delegate.toArray();
        }

        @Override
        public <T> T[] toArray(T[] a) {
            return delegate.toArray(a);
        }

        @Override
        public boolean add(ColumnInfo e) {
            if (e != null) {
                boolean res = delegate.add(e);
                if (res) {
                    addToCaches(e);
                }
                return res;
            }
            return false;
        }

        @Override
        public boolean remove(Object o) {
            boolean res = delegate.remove(o);
            if (res && (o instanceof ColumnInfo)) {
                removeFromCaches((ColumnInfo) o);
            }
            return res;
        }

        @Override
        public boolean containsAll(Collection<?> c) {
            return delegate.containsAll(c);
        }

        @Override
        public boolean addAll(Collection<? extends ColumnInfo> c) {
            return clearCaches(delegate.addAll(c));
        }

        @Override
        public boolean addAll(int index, Collection<? extends ColumnInfo> c) {
            return clearCaches(delegate.addAll(index, c));
        }

        @Override
        public boolean removeAll(Collection<?> c) {
            return clearCaches(delegate.removeAll(c));
        }

        @Override
        public boolean retainAll(Collection<?> c) {
            return clearCaches(delegate.retainAll(c));
        }

        @Override
        public void clear() {
            delegate.clear();
            clearCaches();
        }

        @Override
        public boolean equals(Object o) {
            return delegate.equals(o);
        }

        @Override
        public int hashCode() {
            return delegate.hashCode();
        }

        @Override
        public ColumnInfo get(int index) {
            return delegate.get(index);
        }

        @Override
        public ColumnInfo set(int index, ColumnInfo element) {
            ColumnInfo res = delegate.set(index, element);
            if (res != null) {
                removeFromCaches(res);
            }
            if (element != null) {
                addToCaches(element);
            }
            return res;
        }

        @Override
        public void add(int index, ColumnInfo element) {
            delegate.add(index, element);
            if (element != null) {
                addToCaches(element);
            }
        }

        @Override
        public ColumnInfo remove(int index) {
            ColumnInfo res = delegate.remove(index);
            if (res != null) {
                removeFromCaches(res);
            }
            return res;
        }

        @Override
        public int indexOf(Object o) {
            return delegate.indexOf(o);
        }

        @Override
        public int lastIndexOf(Object o) {
            return delegate.lastIndexOf(o);
        }

        @Override
        public ListIterator<ColumnInfo> listIterator() {
            return delegate.listIterator();
        }

        @Override
        public ListIterator<ColumnInfo> listIterator(int index) {
            return delegate.listIterator(index);
        }

        @Override
        public List<ColumnInfo> subList(int fromIndex, int toIndex) {
            return delegate.subList(fromIndex, toIndex);
        }

        @Override
        public String toString() {
            return delegate.toString();
        }

        //====================== Caches =============================
        private void addToCaches(ColumnInfo inf) {
            if (inf != null) {
                if (isKeyColumn(inf)) {
                    ColumnInfo keyInf = findKeyColumn();
                    if (keyInf != null && keyInf != inf) {
                        throw new IllegalArgumentException("Key column is already set");
                    }
                    keyColumn = keyInf;
                }
                if (cache != null) {
                    putToCache(inf);
                }
                if (columnsCache != null) {
                    addToColCache(inf);
                }
            }
        }

        private void removeFromCaches(ColumnInfo inf) {
            if (inf != null) {
                String infAlias = inf.getAlias();
                if (inf == keyColumn) {
                    keyColumn = null;
                }
                if (cache != null) {
                    cache.remove(infAlias);
                    if (cache.isEmpty()) {
                        cache = null;
                    }
                }
                if (columnsCache != null) {
                    List<IColumn> cols = columnsCache.getColumns();
                    for (int i = cols.size() - 1; i >= 0; i--) {
                        IColumn col = cols.get(i);
                        if (infAlias.equals(col.getAlias())) {
                            cols.remove(i);
                        }
                    }
                    if (cols.isEmpty()) {
                        columnsCache = null;
                    }
                }
            }
        }

        private boolean clearCaches(boolean clear) {
            if (clear) {
                clearCaches();
            }
            return clear;
        }

        private void clearCaches() {
            cache = null;
            columnsCache = null;
            keyColumn = null;
        }

        private void addToColCache(ColumnInfo c) {
            IColumn col = c.getColumn(columnsCache.getColumns().size());
            columnsCache.add(col, c.isAutoExpand());
        }

        private void putToCache(ColumnInfo c) {
            if (!cache.containsKey(c.getAlias())) {
                cache.put(c.getAlias(), c);
            } else {
                throw new IllegalArgumentException("Alias '" + c.getAlias() + "' already added");
            }
        }

        private ColumnInfo findKeyColumn() {
            for (ColumnInfo column : items) {
                if (isKeyColumn(column)) {
                    return column;
                }
            }
            return null;
        }

        private boolean isKeyColumn(ColumnInfo inf) {
            return ColumnType.KEY.equals(inf.getType());
        }

    }

}
