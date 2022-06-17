package ru.sbsoft.shared.filter;

import java.math.BigDecimal;
import java.util.*;
import ru.sbsoft.shared.FilterInfo;
import ru.sbsoft.shared.api.i18n.consts.SBFExceptionStr;
import ru.sbsoft.shared.exceptions.ApplicationException;
import ru.sbsoft.shared.model.LookupInfoModel;
import ru.sbsoft.shared.param.ParamInfo;
import static ru.sbsoft.shared.param.ParamTypeEnum.BIGDECIMAL;

/**
 * Вспомогательный класс со статическим методом, на основе передаваемого
 * {@code ParamInfo parameter} создает соответствующий объект наследник класса
 * {@link ru.sbsoft.shared.FilterInfo}.
 *
 * @see ru.sbsoft.shared.param.ParamInfo
 * @author sychugin
 */
public class FilterHelper {

    public static List<FilterInfo> toFilters(List<ParamInfo> parameters) {
        if (parameters != null && !parameters.isEmpty()) {
            List<FilterInfo> res = new ArrayList<FilterInfo>();
            for (ParamInfo p : parameters) {
                res.add(FilterHelper.createFilterInfo(p));
            }
            return res;
        } else {
            return Collections.emptyList();
        }
    }

    public static List<FilterInfo> createFilterInfo(final Collection<ParamInfo> parameters) {
        final List<FilterInfo> result = new ArrayList<FilterInfo>();
        if (parameters != null) {
            for (final ParamInfo parameter : parameters) {
                result.add(createFilterInfo(parameter));
            }
        } else {
            return null;
        }
        return result;
    }

    public static FilterInfo createFilterInfo(final ParamInfo parameter) {
        final String n = parameter.getName();
        final Object v = parameter.getValue();
        switch (parameter.getType()) {
            case BIGDECIMAL:
                return new BigDecimalFilterInfo(n, (BigDecimal) v);
            case BOOLEAN:
                return new BooleanFilterInfo(n, (Boolean) v);
            case DATE:
                return new DateFilterInfo(n, (Date) v);
            case INTEGER:
                return new IntegerFilterInfo(n, (Integer) v);
            case LIST:
                return new ListFilterInfo(n, (List<String>) v);
            case LONG:
                return new LongFilterInfo(n, (Long) v);
            case LOOKUP:
                return new LookUpFilterInfo(n, (List<LookupInfoModel>) v);
            case STRING:
                return new StringFilterInfo(n, (String) v);
            case TIMESTAMP:
                return new TimeStampFilterInfo(n, (Date) v);
            case OBJECT:
                return new StringFilterInfo(parameter.getName(), (String) parameter.getValue());
            default:
                throw new ApplicationException(SBFExceptionStr.filterUnsupported, parameter.getType().name());
        }
    }
}
