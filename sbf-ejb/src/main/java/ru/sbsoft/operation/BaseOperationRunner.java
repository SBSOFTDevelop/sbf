package ru.sbsoft.operation;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.zip.Deflater;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import javax.persistence.EntityManager;
import ru.sbsoft.common.IO;
import ru.sbsoft.shared.exceptions.ApplicationException;
import ru.sbsoft.shared.model.LookupInfoModel;
import ru.sbsoft.shared.param.BigDecimalParamInfo;
import ru.sbsoft.shared.param.BooleanParamInfo;
import ru.sbsoft.shared.param.ListParamInfo;
import ru.sbsoft.shared.param.LookUpParamInfo;
import ru.sbsoft.shared.param.ParamInfo;
import ru.sbsoft.shared.kladr.AddrUtil;
import ru.sbsoft.dao.entity.BaseEntity;
import ru.sbsoft.dao.IGridDao;
import ru.sbsoft.dao.IStorageDao;
import ru.sbsoft.generator.api.Lookup;
import ru.sbsoft.model.PageFilterInfo;
import ru.sbsoft.sbf.app.model.YearMonthDay;
import ru.sbsoft.shared.FilterInfo;
import ru.sbsoft.shared.param.DateParamInfo;
import ru.sbsoft.shared.param.IntegerParamInfo;
import ru.sbsoft.shared.param.LongParamInfo;
import ru.sbsoft.shared.param.StringParamInfo;
import ru.sbsoft.shared.model.YearMonth;
import ru.sbsoft.shared.model.YearQuarter;
import ru.sbsoft.server.utils.SrvUtl;
import ru.sbsoft.shared.GridContext;
import ru.sbsoft.shared.api.i18n.consts.SBFExceptionStr;
import ru.sbsoft.shared.api.i18n.consts.SBFGeneralStr;
import ru.sbsoft.shared.interfaces.GridType;
import ru.sbsoft.shared.interfaces.NamedItem;
import ru.sbsoft.shared.param.YearMonthDayParamInfo;

/**
 *
 * @author Kiselev
 */
public abstract class BaseOperationRunner extends AbstractOperationRunner {

    @Lookup
    private IGridDao gridDao;

    @Lookup
    protected EntityManager em;

    public IGridDao getGridDao() {
        return gridDao;
    }

    public GridType getGridType() {
        return getGridContext().getGridType();
    }

    protected List<BigDecimal> createIdentifiers() {
        return getMarks();
    }

    public BigDecimal getSelectedRecord() {
        return getOperationCommand().getGridContext().getSelectedRecord();
    }

    public GridContext getGridContext() {
        return getOperationCommand().getGridContext().getGridContext();
    }

    public List<BigDecimal> getMarks() {
        return getOperationCommand().getGridContext().getMarks();
    }

    public PageFilterInfo getPageFilterInfo() {
        return getOperationCommand().getGridContext().getPageFilterInfo();
    }

    // ----------- Helper methods ----------
    protected void putReportFile(String fileName, byte[] file) throws IOException {
        putReportFile(fileName, new ByteArrayInputStream(file));
    }

    protected void putReportFile(String fileName, InputStream file) throws IOException {
        final File report = File.createTempFile("report-", null);
        report.deleteOnExit();
        IO.copy(file, report);
        putReportFile(fileName, report);
    }

    protected void putReportFile(String fileName, File file) throws IOException {
        saveReport(file, fileName);
    }

    protected LookupInfoModel getGridFilterLookup(final String filterName) {
        return getGridFilterListSingle(filterName, LookupInfoModel.class);
    }

    protected List<LookupInfoModel> getGridFilterLookups(final String filterName) {
        return getGridFilterList(filterName, LookupInfoModel.class);
    }

    protected <T> T getGridFilterListSingle(final String filterName, final Class<T> elementClass) {
        List<T> l = getGridFilterList(filterName, elementClass);
        if (l != null && l.size() > 1) {
            throw new IllegalArgumentException("FilterInfo " + filterName + ": requested single value; found [" + l.size() + "] values.");
        }
        return l != null ? l.get(0) : null;
    }

    protected <T> List<T> getGridFilterList(final String filterName, final Class<T> elementClass) {
        List<T> l = getGridFilterVal(filterName, List.class);
        return normValList(l, elementClass);
    }

    protected <T> T getGridFilterVal(final String filterName, final Class<T> valClass) {
        List<FilterInfo> gridFilters = getOperationCommand().getGridContext().getPageFilterInfo().getFilters().getSystemFilters();
        if (gridFilters != null && !gridFilters.isEmpty()) {
            for (FilterInfo f : gridFilters) {
                if (filterName.equals(f.getColumnName())) {
                    if (f.getValue() != null && !valClass.isAssignableFrom(f.getValue().getClass())) {
                        if (String.class.isAssignableFrom(f.getValue().getClass()) && Enum.class.isAssignableFrom(valClass)) {
                            try {
                                Class ccc = valClass;
                                Class<Enum> eclass = ccc;
                                T e = (T) Enum.valueOf(eclass, (String) f.getValue());
                                return e;
                            } catch (Exception ex) {
                                throw new IllegalArgumentException("FilterInfo " + filterName + ": requested[" + valClass.getName() + "]; found[" + f.getValue().getClass().getName() + "] and conversion fail.", ex);
                            }
                        }
                        throw new IllegalArgumentException("FilterInfo " + filterName + ": requested[" + valClass.getName() + "]; found[" + f.getValue().getClass().getName() + "]");
                    }
                    return valClass.cast(f.getValue());
                }
            }
        }
        return null;
    }

    protected List<BigDecimal> askMarkedRecords() {
        return nonEmpty(createIdentifiers(), getLocaleResource(SBFExceptionStr.notMarkedLines));
    }

    protected BigDecimal askSelectedRecord() {
        return askSelectedRecord(null);
    }

    protected BigDecimal askSelectedRecord(String errMsg) {
        return notNull(getSelectedRecord(), errMsg != null && !(errMsg = errMsg.trim()).isEmpty() ? errMsg : getLocaleResource(SBFExceptionStr.rowNotSelected));
    }

    public void warn(final String message) {
        warning(message);
    }

    protected static <T> T notNull(final T obj, final String msg, final Object... args) {
        if (obj == null) {
            throw new NullPointerException(args == null || args.length == 0 ? msg : String.format(msg, args));
        }
        return obj;
    }

    protected static String nonEmpty(String s, final String msg, final Object... args) {
        if (s == null || (s = s.trim()).isEmpty()) {
            throw new NullPointerException(args == null || args.length == 0 ? msg : String.format(msg, args));
        }
        return s;
    }

    protected static <T extends Collection<?>> T nonEmpty(final T coll, final String msg, final Object... args) {
        notNull(coll, msg, args);
        if (coll.isEmpty()) {
            throw new IllegalArgumentException(args == null || args.length == 0 ? msg : String.format(msg, args));
        }
        return coll;
    }

    protected static String checkLen(String s, final int exactLen, final String msg, final Object... args) {
        s = nonEmpty(s, msg, args);
        if (s.length() != exactLen) {
            throw new IllegalArgumentException(args == null || args.length == 0 ? msg : String.format(msg, args));
        }
        return s;
    }

    protected static <T extends Number> T checkPositive(final T num, final String msg, final Object... args) {
        notNull(num, msg, args);
        if (num.longValue() <= 0) {
            throw new IllegalArgumentException(args == null || args.length == 0 ? msg : String.format(msg, args));
        }
        return num;
    }

    protected static <T extends Comparable<T>> int compare(final T o1, final T o2) {
        return o1 == o2 ? 0 : o1 == null ? 1 : o2 == null ? -1 : o1.compareTo(o2);
    }

    protected static String join(final String... strs) {
        return AddrUtil.join(" ", true, strs);
    }

    protected List<BigDecimal> getIdentifiers(final boolean singleRow) {
        String errMsg = getLocaleResource(SBFExceptionStr.notMarkedLines);
        List<BigDecimal> res;
        if (singleRow) {
            BigDecimal id = notNull(getSelectedRecord(), errMsg);
            res = Collections.singletonList(id);
        } else {
            res = nonEmpty(createIdentifiers(), errMsg);
        }
        return res;
    }

    protected <T extends BaseEntity> T find(final Class<T> entityClass, final Object primaryKey) {
        return this.find(entityClass, primaryKey, null);
    }

    protected <T extends BaseEntity> T find(final Class<T> entityClass, final Object primaryKey, final String notFoundErrMsg, final Object... args) {
        return notNull(em.find(entityClass, primaryKey), (notFoundErrMsg != null ? (args == null || args.length == 0 ? notFoundErrMsg : String.format(notFoundErrMsg, args)) : "Объект не найден в базе: " + (entityClass != null ? entityClass.getSimpleName() : "null")) + ". ID: " + String.valueOf(primaryKey));
    }

    protected <V> V rq(V val, NamedItem p) {
        return rq(val, getI18nResource().i18n(p.getItemName()));
    }

    protected <V> V rq(V val, String pname) {
        if (val == null) {
            throw new ApplicationException(SBFExceptionStr.parameterNotDefined, pname);
        }
        return val;
    }

    // Param getters
    protected <P extends ParamInfo> P getParam(String name, Class<P> clazz) {
        final ParamInfo param = super.getParam(name);
        if (param != null) {
            if (clazz.isAssignableFrom(param.getClass())) {
                return (P) param;
            }
            throw new IllegalArgumentException(getLocaleResource(SBFExceptionStr.parameterTypeIncorrect, name, param.getClass().getName(), clazz.getName()));
        }
        return null;
    }

    protected <V, P extends ParamInfo<V>> V getParamVal(String name, Class<P> pclazz, Class<V> vclazz) {
        P p = getParam(name, pclazz);
        return p != null ? p.getValue() : null;
    }

    protected <T extends Enum<T>> T getEnumParam(String name, Class<T> enumClass) {
        String enumName = getParamVal(name, StringParamInfo.class, String.class);
        return Enum.valueOf(enumClass, enumName);
    }

    @Override
    protected BigDecimal getBigDecimalParam(String name) {
        return getParamVal(name, BigDecimalParamInfo.class, BigDecimal.class);
    }

    protected BigDecimal getBigDecimalParam(NamedItem name) {
        return getBigDecimalParam(name.getCode());
    }

    protected BigDecimal getBigDecimalParamRq(NamedItem name) {
        return rq(getBigDecimalParam(name), name);
    }
    
    @Override
    protected YearMonthDay getYearMonthDayParam(String name) {
        return getParamVal(name, YearMonthDayParamInfo.class, YearMonthDay.class);
    }

    protected YearMonthDay getYearMonthDayParam(NamedItem name) {
        return getYearMonthDayParam(name.getCode());
    }

    protected YearMonthDay getYearMonthDayParamRq(NamedItem name) {
        return rq(getYearMonthDayParam(name), name);
    }

    protected Boolean getBooleanParam(String name) {
        return getParamVal(name, BooleanParamInfo.class, Boolean.class);
    }

    protected Boolean getBooleanParam(NamedItem name) {
        return getBooleanParam(name.getCode());
    }

    protected Boolean getBooleanParamRq(NamedItem name) {
        return rq(getBooleanParam(name), name);
    }

    @Override
    protected Date getDateParam(String name) {
        return getParamVal(name, DateParamInfo.class, Date.class);
    }

    protected Date getDateParam(NamedItem name) {
        return getDateParam(name.getCode());
    }

    protected Date getDateParamRq(NamedItem name) {
        return rq(getDateParam(name), name);
    }

    @Override
    protected Integer getIntegerParam(String name) {
        return getParamVal(name, IntegerParamInfo.class, Integer.class);
    }

    protected Integer getIntegerParam(NamedItem name) {
        return getIntegerParam(name.getCode());
    }

    protected Integer getIntegerParam(NamedItem name, Integer defaulValue) {
        return getIntegerParam(name.getCode(), defaulValue);
    }

    protected Integer getIntegerParamRq(NamedItem name) {
        return rq(getIntegerParam(name), name);
    }

    protected Long getLongParam(String name) {
        return getParamVal(name, LongParamInfo.class, Long.class);
    }

    protected Long getLongParam(NamedItem name) {
        return getLongParam(name.getCode());
    }

    protected Long getLongParamRq(NamedItem name) {
        return rq(getLongParam(name), name);
    }

    @Override
    protected String getStringParam(String name) {
        return getParamVal(name, StringParamInfo.class, String.class);
    }

    protected String getStringParam(NamedItem name) {
        return getStringParam(name.getCode());
    }

    protected String getStringParamRq(NamedItem name) {
        return rq(getStringParam(name), name);
    }

    private <T, P extends ParamInfo<List<T>>> List<T> getListParamValues(String name, Class<P> pc, Class<T> c) {
        List<T> pval = getParamVal(name, pc, (Class<List<T>>) Collections.<T>emptyList().getClass());
        if (pval != null) {
            List<T> pval2 = new ArrayList<>();
            for (T o : pval) {
                if (o != null) {
                    if (!c.isAssignableFrom(o.getClass())) {
                        throw new IllegalArgumentException(getLocaleResource(SBFExceptionStr.unexpectedValueType, c.getName(), o.getClass().getName()));
                    }
                    pval2.add(o);
                }
            }
            pval = pval2.isEmpty() ? null : pval2;
        }
        return pval;
    }

    private <T> List<T> normValList(List<T> pval, Class<T> c) {
        if (pval != null) {
            List<T> pval2 = new ArrayList<>();
            for (T o : pval) {
                if (o != null) {
                    if (!c.isAssignableFrom(o.getClass())) {
                        throw new IllegalArgumentException(getLocaleResource(SBFExceptionStr.unexpectedValueType, c.getName(), o.getClass().getName()));
                    }
                    pval2.add(o);
                }
            }
            pval = pval2.isEmpty() ? null : pval2;
        }
        return pval;
    }

    protected <T> List<T> getListParam(String name, Class<T> c) {
        return getListParamValues(name, ListParamInfo.class, c);
    }

    protected <T> List<T> getListParam(NamedItem name, Class<T> c) {
        return getListParam(name.getCode(), c);
    }

    protected <T> List<T> getListParamRq(NamedItem name, Class<T> c) {
        return rq(getListParam(name, c), name);
    }

    protected List<LookupInfoModel> getLookupParam(String name) {
        return getListParamValues(name, LookUpParamInfo.class, LookupInfoModel.class);
    }

    protected List<LookupInfoModel> getLookupParam(NamedItem name) {
        return getLookupParam(name.getCode());
    }

    protected List<LookupInfoModel> getLookupParamRq(NamedItem name) {
        return rq(getLookupParam(name), name);
    }

    protected Integer getYearParam(String name) {
        Integer year = getIntegerParam(name);
        if (year != null && year < YearMonth.MIN_YEAR) {
            throw new ApplicationException(SBFExceptionStr.errorMinYear, String.valueOf(YearMonth.MIN_YEAR), String.valueOf(year));
        }
        return year;
    }

    protected Integer getYearParam(NamedItem name) {
        return getYearParam(name.getCode());
    }

    protected Integer getYearParamRq(NamedItem name) {
        return rq(getYearParam(name), name);
    }

    protected YearMonth getMonthParam(String name) {
        Date d = getDateParam(name);
        return d != null ? SrvUtl.getYearMonth(d) : null;
    }

    protected YearMonth getMonthParam(NamedItem name) {
        return getMonthParam(name.getCode());
    }

    protected YearMonth getMonthParamRq(NamedItem name) {
        return rq(getMonthParam(name), name);
    }

    protected YearQuarter getQuarterParam(String name) {
        YearMonth ym = getMonthParam(name);
        return ym != null ? ym.toQuarter() : null;
    }

    protected YearQuarter getQuarterParam(NamedItem name) {
        return getQuarterParam(name.getCode());
    }

    protected YearQuarter getQuarterParamRq(NamedItem name) {
        return rq(getQuarterParam(name), name);
    }

    //  =========
    protected int getQuarterNumParam(String name) {
        int q = notNull(getIntegerParam(name), getLocaleResource(SBFExceptionStr.parameterNotDefined, name));
        if (q < 1 || q > 4) {
            throw new ApplicationException(SBFExceptionStr.errorQuarter, String.valueOf(q));
        }
        return q;
    }

    protected Date getDateStart(int year) {
        return new GregorianCalendar(year, 0, 1).getTime();
    }

    protected Date getDateEnd(int year, int quarter) {
        return getDateEnd(year, quarter, false);
    }

    protected Date getDateEnd(int year, int quarter, boolean trim) {
        GregorianCalendar gcEnd = new GregorianCalendar(year, quarter * 3 - 1, 1, 23, 59, 59);
        gcEnd.set(Calendar.DAY_OF_MONTH, gcEnd.getActualMaximum(Calendar.DAY_OF_MONTH));
        if (trim) {
            gcEnd.set(Calendar.HOUR_OF_DAY, 0);
            gcEnd.set(Calendar.MINUTE, 0);
            gcEnd.set(Calendar.SECOND, 0);
            gcEnd.set(Calendar.MILLISECOND, 0);
        }
        return gcEnd.getTime();
    }

    protected Date getDateTo(int year, int quarter) {
        GregorianCalendar dateTo = new GregorianCalendar();
        dateTo.setTime(getDateEnd(year, quarter, true));
        dateTo.add(Calendar.DAY_OF_MONTH, 1);
        return dateTo.getTime();
    }

    protected BigDecimal getBigDecimalPositiveParam(String name) {
        BigDecimal res = getBigDecimalParam(name);
        if (BigDecimal.ZERO.compareTo(res) != -1) {
            throw new IllegalArgumentException(getLocaleResource(SBFExceptionStr.parameterMustPositive, name));
        }
        return res;
    }

    protected Integer getIntegerPositiveParam(String name) {
        Integer res = getIntegerParam(name);
        if (res == null || res <= 0) {
            throw new IllegalArgumentException(getLocaleResource(SBFExceptionStr.parameterMustPositive, name));
        }
        return res;
    }

    protected Integer getIntegerNonNegativeParam(NamedItem param) {
        Integer res = getIntegerParam(param.getCode());
        if (res == null || res < 0) {
            throw new IllegalArgumentException(getLocaleResource(SBFExceptionStr.parameterMustNotNegative, getI18nResource().i18n(param.getItemName())));
        }
        return res;
    }

    protected class ReportFileSet {

        private final String name;
        private final ZipOutputStream zipOutputStream;
        private final File archFile;

        public ReportFileSet(String name) throws IOException {
            this.name = !name.endsWith(".zip") ? name + ".zip" : name;
            archFile = File.createTempFile("arch-", null);
            archFile.deleteOnExit();
            zipOutputStream = new ZipOutputStream(new FileOutputStream(archFile));
            zipOutputStream.setLevel(Deflater.DEFAULT_COMPRESSION);
        }

        public void addFile(String name, byte[] body) throws IOException {
            ZipEntry zipEntry = new ZipEntry(name);
            zipOutputStream.putNextEntry(zipEntry);
            try {
                zipOutputStream.write(body);
            } finally {
                zipOutputStream.closeEntry();
            }
        }

        public void sendReport() throws IOException {
            zipOutputStream.close();
            putReportFile(name, archFile);
        }
        
        public void sendExport() throws IOException {
            zipOutputStream.close();
            saveExport(archFile, name);
        }
    
    }

    protected interface ProgressIndicator {

        void init(long total);

        void inc(int delta);

        void inc();
    }

    protected class ProgressIndicatorImpl implements ProgressIndicator {

        private final long rangeBegin;
        private final long rangeEnd;
        private long K = 0;
        private long userRange = 0;
        private long userProgress = 0;

        public ProgressIndicatorImpl(long range_begin, long range_end) {
            this.rangeBegin = range_begin;
            this.rangeEnd = range_end;
        }

        @Override
        public void init(long total) {
            userRange = total;
            userProgress = 0;
            K = userRange > 0 ? (rangeEnd - rangeBegin) / userRange : 0;
            update();
        }

        @Override
        public void inc(int delta) {
            userProgress += delta;
            update();
        }

        @Override
        public void inc() {
            this.inc(1);
        }

        private void update() {
            updateProgress(userProgress >= userRange ? rangeEnd : userProgress * K);
        }

    }

    protected void showStatistics(int countTotal, int countCond, int countLock, int countError) {
        String statistics = getLocaleResource(SBFGeneralStr.msgOperTotal, 
                String.valueOf(countTotal),
                String.valueOf(countCond),
                String.valueOf(countLock),
                String.valueOf(countError));
        info(statistics);
    }

    protected void showShortStatistics(int countTotal, int countCond, int countError) {
        String statistics = getLocaleResource(SBFGeneralStr.msgOperTotalShort, 
                String.valueOf(countTotal),
                String.valueOf(countCond),
                String.valueOf(countError));
        info(statistics);
    }

}
