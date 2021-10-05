package ru.sbsoft.shared;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import ru.sbsoft.sbf.app.model.YearMonthDay;

/**
 * Перечисление содержит экземпляры типов элементов пользовательского фильтра сетки.
 * <p> Взависимости от типа определяются допустимые операторы в предикате SQL оператора {@code where}, 
 * <p> а в графическом интерфейсе визуально в комбобоксе с логическими операторами.
 * @author sychugin
 */
public enum FilterTypeEnum {

    BOOLEAN(Boolean.class),
    INTEGER(Integer.class),
    LONG(Long.class),
    SHORT(Short.class),
    NUMERIC(BigDecimal.class),
    STRING(String.class),
    //
    DATE(Date.class),
    TIME(Date.class),
    TIMESTAMP(Date.class),
    YMD(YearMonthDay.class),
    //
    LIST(List.class),
    LOOKUP(List.class),
    //
    LOOKUP_CODE(String.class),
    LOOKUP_NAME(String.class);
    //
    private Class typeClass;

    private FilterTypeEnum() {
        this(null);
    }

    private FilterTypeEnum(Class typeClass) {
        this.typeClass = typeClass;
    }

    public Class getTypeClass() {
        return typeClass;
    }
}
