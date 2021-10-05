package ru.sbsoft.shared.filter;

import java.math.BigDecimal;
import ru.sbsoft.sbf.app.model.IFormModel;

public class KeyFilterInfo extends BigDecimalFilterInfo {

    public final static BigDecimal FILTER_NOTEXIST_VALUE = BigDecimal.ONE.negate();

    public KeyFilterInfo() {
        this(null, FILTER_NOTEXIST_VALUE);
    }

    public KeyFilterInfo(String columnName, IFormModel model) {
        super(columnName, makeFormModelID(model));
    }

    public KeyFilterInfo(String columnName, BigDecimal ID) {
        super(columnName, coalesce(ID));
    }

    public static BigDecimal coalesce(BigDecimal value) {
        return value == null ? FILTER_NOTEXIST_VALUE : value;
    }

    public static BigDecimal makeFormModelID(IFormModel model) {
        BigDecimal result = null;
        if (model != null) {
            result = model.getId();
        }
        return coalesce(result);
    }
}
