package ru.sbsoft.shared.meta;

import ru.sbsoft.shared.FilterTypeEnum;
import static ru.sbsoft.shared.meta.ProtoType.*;

/**
 * Перечисление, содержит предопределенные типы колонок.
 * <p>Типы используются конструкторами классов наследников класса {@link ru.sbsoft.meta.columns.ColumnInfo} для
 * создания типизированных экземпляров.
 * <p>Например:
 * <pre>
 * public class StringColumnInfo extends ColumnInfo<String> {
 *
 *	public StringColumnInfo() {
 *		super(ColumnType.VCHAR);
 *	}
 * </pre>
 * @author balandin
 */
public enum ColumnType {

    KEY(NONE),
    TEMPORAL_KEY(NONE),
    IDENTIFIER(NONE),
    //
    DATE(DATETIME),
    DATE_TIME(DATETIME),
    TIMESTAMP(DATETIME),
    VCHAR(TEXT, FilterTypeEnum.STRING),
    BOOL(LOGICAL, FilterTypeEnum.BOOLEAN),
    INTEGER(NUMERIC, FilterTypeEnum.INTEGER),
    CURRENCY(NUMERIC),
    //
    // LOOKUP(),
    ADDRESS(NONE),
    YMDAY(NONE);


//
    private final ProtoType protoType;
    private final FilterTypeEnum filterTypeEnum;
    private transient Class columnClass;

    private ColumnType(ProtoType protoType) {
        this(protoType, null);
    }

    private ColumnType(ProtoType protoType, FilterTypeEnum filterTypeEnum) {
        this.protoType = protoType;
        this.filterTypeEnum = filterTypeEnum;
    }

    public FilterTypeEnum getFilterTypeEnum() {
        return filterTypeEnum;
    }

    public Class getColumnClass() {
        return columnClass;
    }

    public void setColumnClass(Class columnClass) {
        this.columnClass = columnClass;
    }

    public ProtoType getProtoType() {
        return protoType;
    }

    @Override
    public String toString() {
        return name();
    }
}
