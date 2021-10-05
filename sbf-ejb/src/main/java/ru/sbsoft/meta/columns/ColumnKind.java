package ru.sbsoft.meta.columns;

import java.math.BigDecimal;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import ru.sbsoft.sbf.app.model.YearMonthDay;
import ru.sbsoft.shared.kladr.AddressModel;
import ru.sbsoft.shared.meta.ColumnType;

/**
 *
 * @author Kiselev
 * @param <V>
 * @param <T>
 */
public class ColumnKind<V, T extends ColumnInfo<V>> {
    
    public static final ColumnKind<BigDecimal, KeyColumnInfo> KEY = new ColumnKind<>(KeyColumnInfo.class, ColumnType.KEY);
    public static final ColumnKind<BigDecimal, TemporalKeyColumnInfo> TEMPORAL_KEY = new ColumnKind<>(TemporalKeyColumnInfo.class, ColumnType.TEMPORAL_KEY);
    public static final ColumnKind<BigDecimal, IdentifierColumnInfo> IDENTIFIER = new ColumnKind<>(IdentifierColumnInfo.class, ColumnType.IDENTIFIER);

    public static final ColumnKind<Date, DateColumnInfo> DATE = new ColumnKind<>(DateColumnInfo.class, ColumnType.DATE);
    public static final ColumnKind<YearMonthDay, YearMonthDayColumnInfo> YMDAY = new ColumnKind<>(YearMonthDayColumnInfo.class, ColumnType.YMDAY);
    public static final ColumnKind<Date, DateTimeColumnInfo> DATE_TIME = new ColumnKind<>(DateTimeColumnInfo.class, ColumnType.DATE_TIME);
    public static final ColumnKind<Date, TimestampColumnInfo> TIMESTAMP = new ColumnKind<>(TimestampColumnInfo.class, ColumnType.TIMESTAMP);
    public static final ColumnKind<String, StringColumnInfo> VCHAR = new ColumnKind<>(StringColumnInfo.class, ColumnType.VCHAR);
    public static final ColumnKind<Boolean, BooleanColumnInfo> BOOL = new ColumnKind<>(BooleanColumnInfo.class, ColumnType.BOOL);
    public static final ColumnKind<Long, IntegerColumnInfo> INTEGER = new ColumnKind<>(IntegerColumnInfo.class, ColumnType.INTEGER);
    public static final ColumnKind<BigDecimal, CurrencyColumnInfo> CURRENCY = new ColumnKind<>(CurrencyColumnInfo.class, ColumnType.CURRENCY);
    
    public static final ColumnKind<AddressModel, AddressColumnInfo> ADDRESS = new ColumnKind<>(AddressColumnInfo.class, ColumnType.ADDRESS);
    
    
    
    
    
    
    
    private final Class<T> clazz;
    private final ColumnType type;

    private ColumnKind(Class<T> clazz, ColumnType type) {
        this.clazz = clazz;
        this.type = type;
    }

    public T createColumnInfo() {
        try {
            return clazz.newInstance();
        } catch (InstantiationException | IllegalAccessException ex) {
            Logger.getLogger(ColumnKind.class.getName()).log(Level.SEVERE, null, ex);
            throw new RuntimeException("Grid column info instantiation exception", ex);
        }
    }

    public ColumnType getType() {
        return type;
    }

}
