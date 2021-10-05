package ru.sbsoft.dao;

import ru.sbsoft.meta.sql.SQLNativeMaker;
import ru.sbsoft.meta.context.VelocityContextAdapter;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.ejb.EJBException;
import ru.sbsoft.common.DB;
import ru.sbsoft.common.Defs;
import ru.sbsoft.common.Strings;
import ru.sbsoft.common.jdbc.QueryParamImpl;
import ru.sbsoft.db.query.VelocityRender;
import ru.sbsoft.meta.columns.ColumnInfo;
import ru.sbsoft.meta.ColumnsInfo;
import ru.sbsoft.common.jdbc.QueryContext;
import ru.sbsoft.common.jdbc.SQLQuery;
import ru.sbsoft.meta.sql.SQLBuilder;
import ru.sbsoft.model.PageFilterInfo;
import ru.sbsoft.shared.model.SortingInfo;
import ru.sbsoft.shared.FilterInfo;
import ru.sbsoft.shared.FiltersBean;
import ru.sbsoft.shared.PageList;
import ru.sbsoft.shared.exceptions.FilterRequireException;
import ru.sbsoft.shared.filter.StringFilterInfo;
import ru.sbsoft.shared.meta.ColumnType;
import ru.sbsoft.shared.meta.Columns;
import ru.sbsoft.shared.meta.aggregate.IAggregateDef;
import ru.sbsoft.shared.meta.Row;
import ru.sbsoft.shared.meta.Wrapper;

/**
 * Построитель запросов для Grid, использующий для построения метаинформацию о
 * столбцах таблицы и информацию об установленных фильтрах.
 * <p>
 * Для построения SQL запросов использует вспомогательный класс
 * {@link ru.sbsoft.meta.sql.SQLBuilder}
 * <p>
 * Базовый класс для {@link ru.sbsoft.dao.AbstractTemplateBuilder} реализует
 * методы DAO слоя для Grid.
 *
 * @author balandin
 * @since Mar 5, 2014 4:31:14 PM
 */
public abstract class AbstractBuilder implements BrowserDao<Row> {

    private static final int MARK_ROWS_MAX = 512;

    private ColumnsInfo columnsInfo;
    private Columns columns;

    private IJdbcWorkExecutor jdbcExecutor;

    private SQLNativeMaker nativeSQL;

    private SQLNativeMaker getNativeSQL() {

        if (nativeSQL == null) {
            nativeSQL = new SQLNativeMaker();
        }
        return nativeSQL;
    }

    protected AbstractBuilder(IJdbcWorkExecutor jdbcExecutor) {
        this.jdbcExecutor = jdbcExecutor;
    }

    /**
     * Метод-фабрика объектов {@link SQLBuilder} или их наследников для
     * использования внутри этого класса для построения SQL-выражений. Позволяет
     * потомкам создавать объекты со специфическим поведением.
     *
     * @return конструктор SQL-выражений.
     */
    protected abstract SQLBuilder newSQLBuilder();

    protected abstract ColumnsInfo createColumns();

    /**
     * Кэш серверного определения столбцов
     *
     * @return
     */
    @Override
    public ColumnsInfo getColumnsInfo() {
        if (columnsInfo == null) {
            columnsInfo = createColumns();
        }
        return columnsInfo;
    }

    /**
     * Кэш клиентского определения столбцов
     *
     * @return
     */
    public Columns getColumns() {
        if (columns == null) {
            columns = getColumnsInfo().getColumns();
        }
        return columns;
    }

    /**
     * Генерация готового объекта для отправки на клиента Вызывается один раз
     *
     * @return
     */
    @Override
    public Columns getMeta() {
        return getColumns();
    }

    protected void convertLookupFilters(PageFilterInfo filterInfo) {
//        final List<FilterInfo> filters = filterInfo.getDynamicFilters();
//        final int size = filters.size();
//        for (int i = 0; i < size; i++) {
//            final FilterInfo f = filters.get(i);
//            if (f instanceof StringFilterInfo) {
//                if (f.getType() == FilterTypeEnum.LOOKUP_CODE || f.getType() == FilterTypeEnum.LOOKUP_NAME) {
//                    filters.set(i, convertLookupFilterItem((StringFilterInfo) f));
//                }
//            }
//        }
        final FilterInfo tempFilterInfo = filterInfo.getTempFilter();
        if (tempFilterInfo instanceof StringFilterInfo) {
            filterInfo.setTempFilter(convertLookupFilterItem((StringFilterInfo) tempFilterInfo));
        }
    }

    protected abstract FilterInfo convertLookupFilterItem(StringFilterInfo lookupInfo);

    protected abstract void checkFilters(FiltersBean filters) throws FilterRequireException;

    private SQLQuery createQuery(QueryContext ctx) {
        return new SQLQuery(ctx);
    }

    @Override
    public PageList<Row> getDataForBrowser(final PageFilterInfo filterInfo) throws FilterRequireException {
        checkFilters(filterInfo.getFilters());

        convertLookupFilters(filterInfo);

        final QueryContext ctx = createContext(filterInfo);

        try (SQLQuery query = createQuery(ctx)) {
            /////////////////////
            // TODO: переделать, выделить в отдельный метод
            final String distinctColumn = filterInfo.getColumnName();
            if (distinctColumn != null) {
                final ColumnInfo column = getColumnsInfo().get(distinctColumn);

                //
                //может быть макро-функция конкатенации для Oracle && Pg && DB2 == ||            
                //для MSSQL MYSQL == +            
//            c.getClause()
                String sql = newSQLBuilder().select(column)
                        .from().where().filters(filterInfo)
                        .add(" GROUP BY " + column.getClause())
                        .add(" ORDER BY " + column.getClause())
                        .toString();

                return jdbcExecutor.executeJdbcWork(new IJdbcWork<PageList<Row>>() {
                    @Override
                    public PageList<Row> execute(Connection conn) throws SQLException {
                        final ResultSet resultSet = query.query(conn, parse(ctx, sql));
                        final PageList<Row> result = new PageList<>();

                        //ResultSetMetaData md= resultSet.getMetaData();
                        while (resultSet.next()) {
                            ArrayList<Object> tmp = new ArrayList<>();
                            tmp.add(column.read(resultSet));
                            final Row row = new Row();
                            row.setValues(tmp);
                            result.add(row);
                        }
                        return result;
                    }
                });
            }
            /////////////////////

            // totals
            return jdbcExecutor.executeJdbcWork((conn) -> {
                String sql = newSQLBuilder().select("COUNT(*) AS TOTAL_COUNT").aggregate().from().where().filters(filterInfo).toString();
                sql = parse(ctx, sql);
                try (ResultSet rs = query.query(conn, sql)) {
                    DB.checkNext(rs);
                    final PageList<Row> result = new PageList<Row>();
                    result.setTotalSize(rs.getInt(1));
                    result.setAggs(readAggregates(getColumnsInfo(), rs));
                    DB.checkEnd(rs);

                    /////////////
                    // -1 - косвенный признак - не брать данные, когда нужны только итоги
                    // используется в updateSummary
                    // переделать
                    if (filterInfo.getOffset() >= 0) {
                        sql = newSQLBuilder().select().from().where().filters(filterInfo).toString();
                        sql = parse(ctx, sql);
                        sql = frame(sql, ctx, filterInfo);
                        try (ResultSet resultSet = query.query(conn, sql)) {
                            result.addAll(createModelList(resultSet));
                        }
                    }

                    return result;
                }
            });
        } catch (SQLException ex) {
            throw new EJBException(ex);
        }
    }

    @Override
    public void processResultSet(final String username, final boolean isAdmin, final PageFilterInfo filterInfo, List<BigDecimal> ids, ResultSetHandler resultSetHandler) {
        final ColumnInfo keyColumn = Defs.coalesce(getColumnsInfo().get(filterInfo.getColumnName()), getColumnsInfo().getKeyColumn());
        final QueryContext ctx = createContext(username, isAdmin, filterInfo);

        try (SQLQuery query = createQuery(ctx)) {
            /**
             * Если отмеченных записей много, то делаем выборку уже по набору, в
             * противном случае добавляем условие in в запрос.
             */
            final boolean flagLittle = ids != null && ids.size() < MARK_ROWS_MAX;
            final SQLBuilder builder = newSQLBuilder().select().from();
            if (flagLittle) {
                ctx.put(SQLBuilder.IDENTIFIER_PARAM_NAME, new QueryParamImpl(ids));
                builder.whereIn(keyColumn);
            }
            builder.where(flagLittle);
            jdbcExecutor.executeJdbcWork((conn) -> {

                String sql = parse(ctx, builder.filters(filterInfo).toString()) + buildSortClause(filterInfo.getSorts());

                try (ResultSet resultSet = query.query(conn, sql)) {
                    while (resultSet.next()) {
                        if (flagLittle || ids == null || ids.contains(resultSet.getBigDecimal(keyColumn.getAlias()))) {
                            resultSetHandler.process(resultSet);
                        }
                    }
                } catch (InterruptedException ignore) {
                }
                return 0;
            });
        } catch (SQLException ex) {
            throw new EJBException(ex);
        }
    }

    private Map<String, Wrapper> readAggregates(ColumnsInfo columnsInfo, ResultSet resultSet) throws SQLException {
        final ResultSetMetaData metaData = resultSet.getMetaData();
        final int columnCount = metaData.getColumnCount();
        if (columnCount > 1) {
            HashMap<String, Wrapper> result = new HashMap<String, Wrapper>();
            for (int i = 2; i <= columnCount; i++) {
                final Object value = resultSet.getObject(i);
                if (value != null) {
                    final String columnLabel = metaData.getColumnLabel(i).toUpperCase();
                    //fix for postrgress columnLabel.toUpperCase()

                    result.put(columnLabel, columnsInfo.get(columnLabel).getFooter().wrap(value));
                }
            }
            return result;
        }
        return null;
    }

    private List<Row> createModelList(ResultSet resultSet) throws SQLException {
        List<Row> result = new ArrayList<>();
        while (resultSet.next()) {
            result.add(readRow(getColumnsInfo(), resultSet));
        }
        return result;
    }

    private Row readRow(final ColumnsInfo columnsInfo, ResultSet resultSet) throws SQLException {
        final int size = columnsInfo.getItems().size();

        List tmp = new ArrayList(size);
        final Row result = new Row();

        for (int i = 0; i < size; i++) {
            final ColumnInfo c = columnsInfo.getItems().get(i);
            final Object value = c.read(resultSet);
            tmp.add(value);
            if (ColumnType.KEY.equals(c.getType())) {
                if (value == null) {
                    throw new IllegalStateException("Key value type is null");
                }
                if (!(value instanceof BigDecimal)) {
                    throw new IllegalStateException("Unexpected key value type " + value);
                }
                result.setRECORD_ID((BigDecimal) value);
            }
        }

        result.setValues(tmp);
        return result;
    }

    @Override
    public Row getRow(PageFilterInfo pageFilterInfo, BigDecimal ID) {
        final QueryContext ctx = createContext(pageFilterInfo);

        ctx.put(SQLBuilder.IDENTIFIER_PARAM_NAME, new QueryParamImpl(ID));
        final SQLBuilder builder = newSQLBuilder().select().from().where(getColumnsInfo().getKeyColumn());
        if (!isSkipWhereOnGetRow()) {
            builder.where(true);
        }
        try {
            return jdbcExecutor.executeJdbcWork((conn) -> {
                String sql = builder.toString();
                sql = parse(ctx, sql);

                try (SQLQuery query = createQuery(ctx); ResultSet rs = query.query(conn, sql)) {
                    DB.checkNext(rs);
                    final Row result = readRow(getColumnsInfo(), rs);
                    DB.checkEnd(rs);
                    return result;
                }
            });
        } catch (SQLException ex) {
            throw new EJBException(ex);
        }
    }

    @Override
    public List<Row> getRows(final PageFilterInfo pageFilterInfo, List<BigDecimal> ids) {
        if (ids == null || ids.isEmpty()) {
            return Collections.emptyList();
        }
        if (ids.size() == 1) {
            Row r = getRow(pageFilterInfo, ids.get(0));
            return r == null ? Collections.emptyList() : Collections.singletonList(r);
        }
        return new RowIdProcessor<List<Row>>(new ArrayList<Row>()) {
            @Override
            public void processChunk(List<BigDecimal> ids, boolean singleChunk, boolean lastChunk) {
                if (singleChunk) {
                    result = getRowsInternal(pageFilterInfo, ids);
                } else {
                    result.addAll(getRowsInternal(pageFilterInfo, ids));
                }
            }
        }.process(ids);
    }

    private List<Row> getRowsInternal(PageFilterInfo pageFilterInfo, List<BigDecimal> IDs) {
        final ColumnInfo keyColumn = getColumnsInfo().getKeyColumn();
        final QueryContext ctx = createContext(pageFilterInfo);
        /**
         * Если отмеченных записей много, то делаем выборку уже по набору, в
         * противном случае добавляем условие in в запрос.
         */
        final boolean flagLittle = IDs.size() < MARK_ROWS_MAX;
        final SQLBuilder builder = newSQLBuilder().select().from();
        if (flagLittle) {
            ctx.put(SQLBuilder.IDENTIFIER_PARAM_NAME, new QueryParamImpl(IDs));
            builder.whereIn(keyColumn);
        }
        if (!isSkipWhereOnGetRow()) {
            builder.where(flagLittle);
        }
        try {
            return jdbcExecutor.executeJdbcWork((conn) -> {
                String sql = builder.toString();
                sql = parse(ctx, sql);

                List<Row> result = new ArrayList<>();
                try (SQLQuery query = createQuery(ctx); ResultSet resultSet = query.query(conn, sql)) {
                    while (resultSet.next()) {
                        if (flagLittle || IDs.contains(resultSet.getBigDecimal(keyColumn.getAlias()))) {
                            result.add(readRow(getColumnsInfo(), resultSet));
                        }
                    }
                }
                return result;
            });
        } catch (SQLException ex) {
            throw new EJBException(ex);
        }
    }

    public Map<String, ?> getAggregates(PageFilterInfo filterInfo, List<BigDecimal> ids, final List<IAggregateDef> defs) {
        final QueryContext ctx = createContext(filterInfo);
        // totals
        final SQLBuilder builder = newSQLBuilder().select("COUNT(*) AS " + IAggregateDef.ALIAS_TOTAL_COUNT).aggregate(defs).from();
        builder.whereIn(getColumnsInfo().getKeyColumn());
        if (!isSkipWhereOnGetRow()) {
            builder.where(true);
        }
        final String sql = builder.toString();
        try {
            return jdbcExecutor.executeJdbcWork((conn) -> {
                return new RowIdProcessor<Map<String, Object>>(new HashMap<String, Object>()) {
                    private SQLQuery query;

                    @Override
                    public Map<String, Object> process(List<BigDecimal> ids) {
                        try (SQLQuery query = createQuery(ctx)) {
                            this.query = query;
                            return super.process(ids);
                        }
                    }

                    @Override
                    public void processChunk(List<BigDecimal> ids, boolean singleChunk, boolean lastChunk) {
                        ctx.put(SQLBuilder.IDENTIFIER_PARAM_NAME, new QueryParamImpl(ids));
                        try (ResultSet rs = query.query(conn, parse(ctx, sql))) {
                            DB.checkNext(rs);
                            BigDecimal count = nvl(rs.getBigDecimal(IAggregateDef.ALIAS_TOTAL_COUNT), BigDecimal.ZERO);
                            BigDecimal resCount = nvl((BigDecimal) result.get(IAggregateDef.ALIAS_TOTAL_COUNT), BigDecimal.ZERO);
                            BigDecimal sumCount = resCount.add(count);
                            result.put(IAggregateDef.ALIAS_TOTAL_COUNT, sumCount);
                            final ResultSetMetaData meta = rs.getMetaData();
                            for (IAggregateDef d : defs) {
                                String aggName = d.getAggregateAlias();
                                int columnIndex = findColumn(aggName, meta);
                                if (columnIndex > 0) {
                                    if (isNumber(meta.getColumnType(columnIndex))) {
                                        BigDecimal val = rs.getBigDecimal(columnIndex);
                                        if (val != null) {
                                            BigDecimal resVal = (BigDecimal) result.get(aggName);
                                            switch (d.getAggregate()) {
                                                case COUNT:
                                                case SUM:
                                                    resVal = resVal != null ? resVal.add(val) : val;
                                                    break;
                                                case MIN:
                                                    resVal = resVal != null ? resVal.min(val) : val;
                                                    break;
                                                case MAX:
                                                    resVal = resVal != null ? resVal.max(val) : val;
                                                    break;
                                                case AVG:
                                                    resVal = resVal != null ? resVal.multiply(resCount).add(val.multiply(count)).divide(sumCount, 100, RoundingMode.HALF_UP) : val;
                                                    break;
                                                default:
                                                    throw new IllegalArgumentException("Unsupported aggregate: " + d.getAggregate());
                                            }
                                            result.put(aggName, resVal);
                                        }
                                    } else if (isTemporal(meta.getColumnType(columnIndex))) {
                                        Object objVal = rs.getObject(columnIndex);
                                        if (objVal instanceof Date) {
                                            Date val = new Date(((Date) objVal).getTime());
                                            Date resVal = (Date) result.get(aggName);
                                            switch (d.getAggregate()) {
                                                case MIN:
                                                    if (resVal == null || resVal.after(val)) {
                                                        resVal = val;
                                                    }
                                                    break;
                                                case MAX:
                                                    if (resVal == null || resVal.before(val)) {
                                                        resVal = val;
                                                    }
                                                    break;
                                                default:
                                                    throw new IllegalArgumentException("Unsupported aggregate: " + d.getAggregate());
                                            }
                                            result.put(aggName, resVal);
                                        }
                                    }
                                }
                            }
                            DB.checkEnd(rs);
                        } catch (SQLException ex) {
                            throw new EJBException(ex);
                        }
                    }
                }.process(ids);
            });
        } catch (SQLException impossible) {
            throw new EJBException("Impossible exception", impossible);
        }
    }

    private int findColumn(String name, ResultSetMetaData meta) throws SQLException {
        for (int i = 1; i <= meta.getColumnCount(); i++) {
            String colName = meta.getColumnLabel(i);
            if (colName.equalsIgnoreCase(name)) {
                return i;
            }
        }
        return -1;
    }

//    private BigDecimal toBigDecimal(Number num) {
//        return num != null ? num instanceof BigDecimal ? (BigDecimal) num : new BigDecimal(num.toString()) : null;
//    }
    private static <T> T nvl(T val, T defVal) {
        return val != null ? val : defVal;
    }

    private static boolean isNumber(int sqlType) {
        final int[] numTypes = new int[]{Types.BIT, Types.TINYINT, Types.SMALLINT, Types.INTEGER, Types.BIGINT, Types.FLOAT, Types.REAL, Types.DOUBLE, Types.NUMERIC, Types.DECIMAL};
        for (int i : numTypes) {
            if (i == sqlType) {
                return true;
            }
        }
        return false;
    }

    private static boolean isTemporal(int sqlType) {
        final int[] tempTypes = new int[]{Types.DATE, Types.TIME, Types.TIMESTAMP};
        for (int i : tempTypes) {
            if (i == sqlType) {
                return true;
            }
        }
        return false;
    }

    @Override
    public List<BigDecimal> getOnlyIdsForBrowser(PageFilterInfo filterInfo, String columnName) {
        final QueryContext ctx = createContext(filterInfo);
        final ColumnInfo columnInfo = getColumnsInfo().get(columnName);
        if (null == columnInfo) {
            throw new IllegalArgumentException(String.format("Column \"%s\" not found.", columnName));
        }
        try (SQLQuery query = createQuery(ctx)) {
            return jdbcExecutor.executeJdbcWork((conn) -> {
                String sql = newSQLBuilder().select(columnInfo).from().where().filters(filterInfo).toString();
                sql = parse(ctx, sql);
                sql = frame(sql, ctx, filterInfo);
                try (ResultSet resultSet = query.query(conn, sql)) {
                    return readIDs(resultSet);
                }
            });
        } catch (SQLException ex) {
            throw new EJBException(ex);
        }
    }

    private List<BigDecimal> readIDs(final ResultSet resultSet) throws SQLException {
        final List<BigDecimal> result = new ArrayList<BigDecimal>();
        while (resultSet.next()) {
            result.add(resultSet.getBigDecimal(1));
        }
        return result;
    }

    private String parse(final QueryContext context, final String template) {

        getNativeSQL().prepare(context);
        try {
            return VelocityRender.parse(new VelocityContextAdapter(context), template);
        } catch (Exception ex) {
            throw new EJBException(ex);
        }
    }

    private String frame(String sql, QueryContext context, PageFilterInfo filterInfo) {
        final int offset = filterInfo.getOffset();
        final int limit = filterInfo.getLimit();
        if (offset > 0 || limit > 0) {
            context.put("rowOffset", new QueryParamImpl(offset));
            context.put("rowLimit", new QueryParamImpl(offset + limit));
            return getNativeSQL().getFrame(sql, buildSortClause(filterInfo.getSorts()));
        }
        return sql;
    }

    private static String buildSortClause(List<SortingInfo> sorts) {
        if (sorts != null && !sorts.isEmpty()) {
            final StringBuilder s = new StringBuilder();
            s.append("ORDER BY ");
            s.append(Strings.join(sorts.toArray(), ", "));
            s.append("\n");
            return s.toString();
        }
        return Strings.EMPTY;
    }

    protected abstract QueryContext createContext(final PageFilterInfo filterInfo);

    protected abstract QueryContext createContext(final String username, final boolean isAdmin, final PageFilterInfo filterInfo);

    protected abstract boolean isSkipWhereOnGetRow();

    private static abstract class RowIdProcessor<T> {

        protected T result = null;

        public RowIdProcessor() {
        }

        public RowIdProcessor(T initResult) {
            this.result = initResult;
        }

        public T process(List<BigDecimal> ids) {
            int maxIdNum = MARK_ROWS_MAX;
            int idsSize = ids.size();
            if (idsSize <= maxIdNum) {
                processChunk(ids, true, true);
            } else {
                int p1 = 0;
                while (p1 < idsSize) {
                    int p2 = p1 + maxIdNum;
                    if (p2 > idsSize) {
                        p2 = idsSize;
                    }
                    processChunk(ids.subList(p1, p2), false, p2 >= idsSize);
                    p1 = p2;
                }
            }
            return result;
        }

        protected abstract void processChunk(List<BigDecimal> ids, boolean singleChunk, boolean lastChunk);
    }
}
