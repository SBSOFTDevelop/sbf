package ru.sbsoft.client.components.grid.aggregate.operation;

import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.NumberFormat;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import ru.sbsoft.client.components.grid.aggregate.IAggregate;
import ru.sbsoft.client.components.grid.column.CurrencyColumnConfig;
import ru.sbsoft.client.components.grid.column.CustomColumnConfig;
import ru.sbsoft.client.components.grid.column.DateColumnConfig;
import ru.sbsoft.client.components.grid.column.NumericColumnConfig;
import ru.sbsoft.client.components.grid.column.TemporalColumnConfig;
import ru.sbsoft.client.utils.ClientUtils;
import ru.sbsoft.shared.meta.Aggregate;
import ru.sbsoft.shared.meta.Row;

/**
 *
 * @author Kiselev
 */
public abstract class AbstractAggregate implements IAggregate {

    protected final static String NO_RESULT = "-";

    private final Aggregate aggregate;
    private final Class[] supportedTypes;
    private final DateTimeFormat dateFormat = DateTimeFormat.getFormat(DateTimeFormat.PredefinedFormat.DATE_SHORT);
    private final DateTimeFormat dateTimeFormat = DateTimeFormat.getFormat(DateTimeFormat.PredefinedFormat.DATE_TIME_SHORT);

    protected AbstractAggregate(Aggregate aggregate, Class<? extends CustomColumnConfig>... supportedTypes) {
        this.aggregate = aggregate;
        this.supportedTypes = supportedTypes;
    }

    @Override
    public Aggregate getKind(){
        return aggregate;
    }
    
    @Override
    public String getName() {
        return aggregate.getTitle();
    }

    @Override
    public <T extends CustomColumnConfig> boolean isSupported(Class<T> configClass) {
        for (Class c : supportedTypes) {
            if (ClientUtils.isAssignableFrom(c, configClass)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String calc(CustomColumnConfig c, Iterable<Row> rows) {
        if (c != null && rows != null) {
            if (!isSupported(c.getClass())) {
                throw new IllegalArgumentException("Not supported: " + c);
            }
            return calcImpl(c, rows);
        }
        return NO_RESULT;
    }

    protected abstract String calcImpl(CustomColumnConfig c, Iterable<Row> rows);

    protected String format(Number num, NumericColumnConfig c) {
        String format = null;
        if(c instanceof CurrencyColumnConfig){
            format = "0.00";
        }
        if(format == null){
            format = aggregate.getFormat();
        }
        NumberFormat formatter = null;
        if(format != null){
            formatter = NumberFormat.getFormat(format);
        }
        return num != null ? formatter != null ? formatter.format(num) : num.toString() : NO_RESULT;
    }

    protected String format(Date d, TemporalColumnConfig c) {
        DateTimeFormat format = dateTimeFormat;
        if(c instanceof DateColumnConfig){
            format = dateFormat;
        }
        return d != null ? format.format(d) : NO_RESULT;
    }

    protected final BigDecimal toBigDecimal(Number num) {
        if (num == null) {
            return null;
        }
        if (num instanceof BigDecimal) {
            return (BigDecimal) num;
        } else if (num instanceof BigInteger) {
            return new BigDecimal((BigInteger) num);
        } else if ((num instanceof Double) || (num instanceof Float)) {
            return BigDecimal.valueOf(num.doubleValue());
        } else if ((num instanceof Long) || (num instanceof Integer) || (num instanceof Short) || (num instanceof Byte)) {
            return BigDecimal.valueOf(num.longValue());
        } else {
            return new BigDecimal(num.toString());
        }
    }

}
