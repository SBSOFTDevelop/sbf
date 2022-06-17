package ru.sbsoft.meta.sql;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import ru.sbsoft.common.Strings;
import ru.sbsoft.dao.AbstractTemplate;
import ru.sbsoft.dao.IFilterWrapper;
import ru.sbsoft.dao.ISqlTemplateInfo;
import ru.sbsoft.meta.columns.ColumnInfo;
import ru.sbsoft.meta.ColumnsInfo;
import ru.sbsoft.model.PageFilterInfo;
import ru.sbsoft.shared.BooleanOperator;
import ru.sbsoft.shared.ColumnsFilterInfo;
import ru.sbsoft.shared.ComparisonEnum;
import ru.sbsoft.shared.FilterInfo;
import ru.sbsoft.shared.FilterInfoGroup;
import ru.sbsoft.shared.FiltersBean;
import ru.sbsoft.shared.meta.Aggregate;
import ru.sbsoft.shared.meta.aggregate.IAggregateDef;
import ru.sbsoft.shared.meta.filter.EditorFilterDefinition;
import ru.sbsoft.shared.meta.filter.FilterDefinition;
import ru.sbsoft.shared.meta.filter.LookupFilterDefinition;
import ru.sbsoft.shared.meta.filter.LookupKeyType;

/**
 * Класс <code>SQLBuilder</code> представляет построитель
 * <acronym title="structured query language">SQL</acronym> выражений.
 *
 * <p>
 * Используется как вспомогательный класс классом
 * {@link ru.sbsoft.dao.AbstractBuilder}. (Экземпляр класса
 * <code>AbstractBuilder</code> передается в конструктор класса
 * <code>SQLBuilder</code>)
 *
 * @author balandin
 * @since May 5, 2014 6:09:45 PM
 */
public class SQLBuilder {

    private static final String[] DEFAULT_LOOKUP_ENTITY_SIGNS = {"RECORD_IN_", "_RECORD_IN"};
    private static final String[] DEFAULT_LOOKUP_RECORD_SIGNS = {"_ID", "RECORD_UQ_", "_RECORD_UQ"};

    public static final String IDENTIFIER_PARAM_NAME = "IDENTIFIER_PARAM";
    public static final String INDENT_ELEMENT = "    ";
    //
    private final StringBuilder buffer = new StringBuilder();
    private final ISqlTemplateInfo builder;
    private final List<String> lookupEntitySigns = new ArrayList<>(Arrays.asList(DEFAULT_LOOKUP_ENTITY_SIGNS));
    private final List<String> lookupRecordSigns = new ArrayList<>(Arrays.asList(DEFAULT_LOOKUP_RECORD_SIGNS));

    /**
     * Конструктор построителя SQL запросов.
     *
     * @param builder - буффер содержащий результирующий SQL запрос, формируемый
     * поСтроителем.
     */
    public SQLBuilder(ISqlTemplateInfo builder) {
        this.builder = builder;
    }

    public SQLBuilder add(String value) {
        buffer.append(value);
        return this;
    }

    /**
     * Метод формирует в {@link #buffer} оператор SQL
     * <code>select &lt;fragment&gt\n;</code>.
     *
     * @param fragment - фрагмент SQL без самого select оператора, например
     * список полей или агрегатов (field1,field2..fieldN;
     * sum(field1)..sum(fieldN)).
     * @return
     */
    public SQLBuilder select(String fragment) {
        buffer.append("SELECT ").append(fragment).append("\n");
        return this;
    }

    /**
     * Метод формирует в {@link #buffer} для всех столбцов, имеющих футер,
     * агрегаты (оборачивает выражения столбца в вызов агрегатной SQL функции).
     *
     * @return
     */
    public SQLBuilder aggregate() {
        final List<ColumnInfo> columns = builder.getColumnsInfo().getItems();
        for (ColumnInfo c : columns) {
            if (c.hasFooter()) {
                addAggStr(buffer, c.getFooter().build(c.getClause()), c.getAlias());
            }
        }
        return this;
    }

    /**
     * Метод формирует в {@link #buffer} агрегированные значения по переданным
     * столбцам и агрегатным функциям (оборачивает выражения столбца в вызов
     * агрегатной SQL функции).
     *
     * @return
     */
    public SQLBuilder aggregate(List<IAggregateDef> defs) {
        final ColumnsInfo cols = builder.getColumnsInfo();
        for (IAggregateDef def : defs) {
            ColumnInfo c = cols.get(def.getColumnAlias());
            if (c == null) {
                throw new IllegalArgumentException("Column '" + def.getColumnAlias() + "' not found in '" + cols.getTemplateName() + "'");
            }
            Aggregate a = def.getAggregate();
            a.checkApplicable(c.getType());
            String aggName = a.name();
            String clause = c.getClause();
            String aggClause = new StringBuilder(aggName.length() + clause.length() + 2).append(aggName).append('(').append(clause).append(')').toString();
            addAggStr(buffer, aggClause, def.getAggregateAlias());
        }
        return this;
    }

    private static void addAggStr(StringBuilder buffer, String aggClause, String aggAlias) {
        buffer.append(", ").append(aggClause).append(" AS ").append(aggAlias).append("\n");
    }

    /**
     * Метод формирует в {@link #buffer} оператор SQL
     * <code>select &lt;clause&gt; as &lt;alias&gt\n;</code> для переданного
     * парметра column.
     *
     * @param column экземпляр ColumnInfo, для которого формируется оператор.
     * @return
     */
    public SQLBuilder select(ColumnInfo column) {
        buffer.append("SELECT ");
        column.buildSelectClause(buffer);
        buffer.append(" \n");
        return this;
    }

    /**
     * Метод формирует в {@link #buffer} оператор SQL
     * <code>select &lt;clause1&gt; as &lt;alias1&gt..&lt;clauseN&gt; as &lt;aliasN&gt\n;</code>
     * для коллекции <code>ColumnsInfo.</code>.
     * <p>
     * Коллекция столбцов возвращается экземпляром AbstractBuilder, который был
     * передан в конструктор при создании инстанса SQLBuilder.
     *
     * @return
     */
    public SQLBuilder select() {
        final ColumnsInfo cInfo = builder.getColumnsInfo();

        buffer.append("SELECT ");
        buffer.append(Strings.coalesce(cInfo.getSelectModifier()));
        buffer.append('\n');

        for (ColumnInfo c : cInfo.getItems()) {

            buffer.append("\t");
            if (builder.getFilterWrapper(c.getAlias()) != null) {
                buffer.append("null");
                buffer.append(" AS ");
                buffer.append(c.getAlias());

            } else {
                c.buildSelectClause(buffer);
            }
            buffer.append(",\n");
        }
        if (buffer.charAt(buffer.length() - 2) == ',') {
            buffer.setCharAt(buffer.length() - 2, ' ');
        }
        if (buffer.charAt(buffer.length() - 1) != '\n') {
            buffer.append("\n");
        }
        return this;
    }

    /**
     * Метод формирует <code>FROM table_references</code> оператора
     * <code>select</code>.
     * <p>
     * Для формирования используется переопределенный в наследниках класса
     * {@link ru.sbsoft.dao.AbstractBuilder}</p>
     * <p>
     * метод <code>public void buildFromClause(StringBuilder sb)</code>.
     * <p>
     * Например:
     * <pre>
     * &#64;Override
     * public void buildFromClause(StringBuilder sb) {
     * sb.append("DX_BASE_AUDIT DBA\n"
     *           + " JOIN DT_FUND_ENTITY FE ON FE.ENTITY_ID = DBA.FUND_ENTITY_ID\n"
     *           + " LEFT JOIN DT_FUND FND ON FND.TEMPORAL_ENTITY_ID = FE.ENTITY_ID\n"
     *           + "             AND :currentDate BETWEEN FND.DATE_START AND FND.DATE_END\n");
     *}
     * </pre>
     *
     * @return
     */
    public SQLBuilder from() {
        buffer.append("FROM ");
        builder.buildFromClause(buffer);
        buffer.append("\n");
        return this;
    }

    public SQLBuilder where() {
        where(false);
        return this;
    }

    /**
     * Метод формирует в {@link #buffer} оператор SQL
     * <code>where &lt;предикат критерия отбора записей&gt;</code>.
     *
     * <p>
     * Для формирования предиката используется переопределенный в наследниках
     * класса {@link ru.sbsoft.dao.AbstractBuilder} метод
     * <code>public void buildWhereClause(StringBuilder sb)</code>.
     * <p>
     * Например:
     * <pre>
     * &#64;Override
     * public void buildWhereClause(StringBuilder sb) {
     *   sb.append(" and :currentDate BETWEEN F.DATE_START AND F.DATE_END\n"
     *           + "#if ($fundParentEntityList) and F.PARENT_ENTITY_ID IN (:fundParentEntityList)\n#end"
     *           + "#if ($fundType) and F.DTYPE = :fundType\n#end"
     *           + "#if ($ENTITY_ID_FUND_LIST) and F.TEMPORAL_ENTITY_ID in (:ENTITY_ID_FUND_LIST)\n#end");
     *}
     * </pre>
     *
     * @param append при значении true оператор <code>where</code> не
     * добавляется в результат в {@link #buffer}.
     * @return
     */
    public SQLBuilder where(boolean append) {
        if (!append) {
            buffer.append("WHERE (1=1) \n");
        }
        builder.buildWhereClause(buffer);
        return this;
    }

    /**
     * Метод формирует в {@link #buffer} оператор SQL
     * <code>where &lt;имя столбца&gt; =:&lt;имя параметра&gt;</code> для
     * переданного в качестве параметра столбца.
     *
     * @param column экземпляр класса {@link ru.sbsoft.meta.columns.ColumnInfo}
     * с метаинформацией о столбце сетки.
     * @return
     */
    public SQLBuilder where(ColumnInfo column) {
        buffer.append("WHERE ").append(column.getClause()).append(" = :").append(IDENTIFIER_PARAM_NAME).append("\n");
        return this;
    }

    /**
     * Метод формирует в {@link #buffer} оператор SQL <code>where &lt;имя столбца&gt; in :&lt;имя параметра списка&gt;
     * </code> для переданного в качестве параметра столбца.
     *
     * @param column экземпляр класса {@link ru.sbsoft.meta.columns.ColumnInfo}
     * с метаинформацией о столбце сетки.
     * @return
     */
    public SQLBuilder whereIn(ColumnInfo column) {
        buffer.append("WHERE ").append(column.getClause()).append(" in (:").append(IDENTIFIER_PARAM_NAME).append(")\n");
        return this;
    }

    /**
     * Метод формирует дополнительные критерии отбора для оператора SQL
     * <code>where</code>.
     * <p>
     * В отличии от статичесих критериев, формируемых в методах {@link #where()}
     * и {@link #whereIn(ru.sbsoft.meta.columns.ColumnInfo)}, критерий отбора
     * формируются динамически пользователем во время работы приложения через
     * графический интерфейс, представляющий фильтр.
     *
     * @param info
     * @return
     */
    public SQLBuilder filters(PageFilterInfo info) {
        if (info != null) {
            FiltersBean filters = info.getFilters();
            if (filters != null) {
                filters(new FilterInfoGroup(BooleanOperator.AND, filters.getSystemFilters()), true);

                FilterInfoGroup usr = filters.getUserFilters();
                FilterInfo tmp = info.getTempFilter();
                if (tmp != null && ! usr.getChildFilters().contains(tmp)) {
                    usr = new FilterInfoGroup(BooleanOperator.AND, usr.getChildFilters());
                    usr.getChildFilters().add(tmp);
                }
                filters(usr, false);
            }
        }
        return this;
    }

    private SQLBuilder filters(FilterInfoGroup g, boolean system) {
        if (g != null && !g.getChildFilters().isEmpty()) {
            buffer.append('\n');
            buffer.append('\n');
            filters(g, system ? null : new AtomicInteger(0), 2, BooleanOperator.AND);
        }
        return this;
    }

    private SQLBuilder filters(FilterInfoGroup filterInfoGroup, AtomicInteger index, int level, BooleanOperator parentBooleanOperator) {
        String indent = Strings.repl(INDENT_ELEMENT, level - 1);

        buffer.append(indent);
        if (parentBooleanOperator != null) {
            buffer.append(parentBooleanOperator.name()).append(' ');

        }
        buffer.append("(");

        if (level == 2) {
            buffer.append(" -- generate ").append(index == null ? "template filter" : "user filters");
        }
        buffer.append('\n');

        boolean needTerminator = false;
        List<FilterInfo> childFilters = filterInfoGroup.getChildFilters();
        for (final FilterInfo f : childFilters) {
            IFilterWrapper wrapper = builder.getFilterWrapper(f.getColumnName());
            final StringBuilder filterBuffer = new StringBuilder();

            while (true) {
                if (f instanceof FilterInfoGroup) {
                    filters((FilterInfoGroup) f, index, level + 1, needTerminator ? filterInfoGroup.getValue() : null);
                    break;
                }

                try {
                    filterBuffer.append(indent).append(INDENT_ELEMENT);
                    if (wrapper == null && needTerminator) {
                        filterBuffer.append(filterInfoGroup.getValue().name()).append(" ");
                    }

                    if (index != null) {
                        f.setQuickFilterIndex(index.getAndIncrement());
                    }
//если фильтер определен в template браузера через defineFilter методы требующие регистрации
                    if (buildFilterSql(filterBuffer, f)) {
                        break;
                    }

                    if (f.isNotExpression()) {
                        filterBuffer.append("NOT ");
                    }

                    String columnName = getClause(f.getColumnName());
                    if (f.getValue() == null) {
                        filterBuffer.append(columnName).append(" IS NULL");
                        break;
                    }

                    columnName = f.isCaseSensitive() ? columnName : "UPPER(" + columnName + ')';
                    if (f.getComparison() == ComparisonEnum.in_bound || f.getComparison() == ComparisonEnum.in_range) {
                        final Object value1 = f.getValue();
                        final Object value2 = f.getSecondValue();
                        final boolean fullExpression = value1 != null && value2 != null;
                        if (fullExpression) {
                            filterBuffer.append("(");
                        }
                        if (value1 != null) {
                            filterBuffer.append(columnName).append(" ");
                            filterBuffer.append(f.getComparison() == ComparisonEnum.in_range ? ">=" : ">").append(" ");
                            filterBuffer.append(":").append(f.getSQLParameterName(1));
                        }
                        if (fullExpression) {
                            filterBuffer.append(' ').append("AND").append(" ");
                        }
                        if (value2 != null) {
                            filterBuffer.append(columnName).append(" ");
                            filterBuffer.append(f.getComparison() == ComparisonEnum.in_range ? "<=" : "<").append(" ");
                            filterBuffer.append(":").append(f.getSQLParameterName(2));
                        }
                        if (fullExpression) {
                            filterBuffer.append(")");
                        }
                        break;
                    }

                    filterBuffer.append(columnName);
                    filterBuffer.append(" ");

                    filterBuffer.append(f.getComparison().getSql()).append(" ");
                    if (f instanceof ColumnsFilterInfo) {
                        String c2 = getClause(((ColumnsFilterInfo) f).getColumnName2());
                        filterBuffer.append(f.isCaseSensitive() ? c2 : "UPPER(" + c2 + ')');
                    } else {
                        filterBuffer.append(":").append(f.getSQLParameterName(1));
                    }

                    break;

                } finally {
                    filterBuffer.append("\n");
                }

            } // end while

       
            if (wrapper != null) {
                wrapper.wrap(filterBuffer);
                if (needTerminator) {
                    buffer.append(filterInfoGroup.getValue().name()).append(" ");
                }
            }

            buffer.append(filterBuffer);
            needTerminator = true;
        }
        buffer.append(indent).append(")\n");

        return this;
    }

    private String getClause(String columnName) {
        ColumnInfo column = builder.getColumnsInfo().get(columnName);
        return (column == null) ? columnName : column.getClause();
    }

    /**
     * Метод формирует и возвращает строку вида
     * <pre>
     * SELECT yyy.* FROM (SELECT xxx.*, ROWNUM rnum FROM
     *   (&lt;параметр String sql&gt;) xxx) yyy WHERE rnum > :rowOffset and rnum <= :rowLimit;
     * </pre> <p> Э т
     * о
     * позволяет возвращать на клиент кадр из исходного результата, ограниченный
     * снизу номером первой строки и сверху номером последней строки результата.
     *
     * @param sql -исходный SQL запрос.
     * @return "обернутый в кадр" исходный SQL запрос.
     */
//    public static String frame(final String sql) {
//        final StringBuilder s = new StringBuilder();
//        s.append("SELECT yyy.* FROM (SELECT xxx.*, ROWNUM rnum FROM (");
//        s.append(sql);
//        s.append(") xxx) yyy\n");
//        s.append("WHERE rnum > :rowOffset and rnum <= :rowLimit");
//        return s.toString();
//        
//      
//    }
    @Override
    public String toString() {
        return buffer.toString();
    }

    private boolean buildFilterSql(StringBuilder buffer, FilterInfo filterInfo) {
        FilterDefinition def = builder.getFilterDefinitions().get(filterInfo.getColumnName());
        if (def == null) {
            return false;
        }
        if (def.isNoSql()) {
            filterInfo.setQuickFilterIndex(-1);
            buffer.append("(1=1)");
            return true;
        }
        if (def instanceof LookupFilterDefinition) {
            LookupFilterDefinition lDef = (LookupFilterDefinition) def;
            LookupKeyType t = lDef.getKeyType();
            if (t == null) {
                lDef.setKeyType(t = determLookupKeyType(lDef.getClause()));
            }
            AbstractTemplate.buildLookupSql(buffer, def.getClause(), t, filterInfo.getSQLParameterName(1), filterInfo.getSQLParameterName(2));
            return true;
        }
        if (def instanceof EditorFilterDefinition) {
            String clause = def.getClause();
            String s = filterInfo.getSQLParameterName(0);
            clause = clause.replace(":?", ":" + s);
            clause = clause.replace("$?", "$" + s);
            buffer.append(clause);
            return true;
        }
        throw new IllegalStateException();
    }

    private LookupKeyType determLookupKeyType(String clause) {
        if (containsAnyIgnoreCase(clause, lookupEntitySigns)) {
            return LookupKeyType.ENTITY;
        }
        if (containsAnyIgnoreCase(clause, lookupRecordSigns)) {
            return LookupKeyType.RECORD;
        }
        throw new IllegalArgumentException();
    }

    private static boolean containsAnyIgnoreCase(String clause, List<String> signs) {
        for (String sign : signs) {
            String signUp = sign.toUpperCase();
            String signLow = sign.toLowerCase();
            if (clause.contains(signUp) || clause.contains(signLow)) {
                return true;
            }
        }
        return false;
    }

}
