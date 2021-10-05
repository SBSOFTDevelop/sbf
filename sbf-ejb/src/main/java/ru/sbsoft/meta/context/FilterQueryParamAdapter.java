package ru.sbsoft.meta.context;

import java.util.Date;
import javax.persistence.TemporalType;
import ru.sbsoft.common.DBType;
import ru.sbsoft.common.jdbc.QueryParam;
import ru.sbsoft.sbf.app.model.YearMonthDay;
import ru.sbsoft.server.utils.SrvUtl;
import ru.sbsoft.shared.ComparisonEnum;
import ru.sbsoft.shared.FilterInfo;
import ru.sbsoft.shared.FilterTypeEnum;
import static ru.sbsoft.shared.FilterTypeEnum.STRING;

/**
 *
 * @author sychugin
 */
public class FilterQueryParamAdapter implements QueryParam {

    private final FilterInfo filterInfo;
    private final boolean secondParam;
    private final DBType dbType;

    /**
     *
     * @param filterInfo - источник
     * @param secondParam - признак необходимости обращения ко второму параметру
     */
    public FilterQueryParamAdapter(FilterInfo filterInfo, boolean secondParam) {
        this.filterInfo = filterInfo;
        this.secondParam = secondParam;
        this.dbType = DBType.getCurrentType();
    }

    @Override
    public TemporalType getTemporalType() {
        switch (filterInfo.getType()) {
            case DATE:
            case YMD:
                return TemporalType.DATE;
            case TIME:
                return TemporalType.TIME;
            case TIMESTAMP:
                return TemporalType.TIMESTAMP;
        }
        return null;
    }

    

    @Override
    public Object getValue() {
        final Object value = secondParam ? filterInfo.getSecondValue() : filterInfo.getValue();
        final FilterTypeEnum type = filterInfo.getType();
        switch (type) {
            case BOOLEAN:
                if (dbType == DBType.DBTYPE_POSTGRES) {
                    return (Boolean) value;
                } else {
                    return (Boolean) value ? (int) 1 : (int) 0;
                }
            case DATE:
                return new java.sql.Date(((Date) value).getTime());
            case TIME:
                return new java.sql.Time(((Date) value).getTime());
            case TIMESTAMP:
                return new java.sql.Timestamp(((Date) value).getTime());
            case YMD:
                Date d = SrvUtl.toDate((YearMonthDay) value);
                return new java.sql.Date(d.getTime());
            case STRING:
                String tmp = (String) value;
                if (!filterInfo.isCaseSensitive()) {
                    tmp = tmp.toUpperCase();
                }
                return getCompareExpression(tmp, filterInfo.getComparison());
            default:
                return value;
        }
    }

    private String getCompareExpression(String value, ComparisonEnum c) {
        if (c == null) {
            c = ComparisonEnum.startswith;
        }
        switch (c) {
            case like:
                return value.replace('*', '%').replace('?', '_');
            case contains:
                return "%" + value + "%";
            case startswith:
                return value + "%";
            case endswith:
                return "%" + value;
            case eq:
            default:
                return value;
        }
    }

    @Override
    public String toString() {
        return String.valueOf(getValue());
    }
}
