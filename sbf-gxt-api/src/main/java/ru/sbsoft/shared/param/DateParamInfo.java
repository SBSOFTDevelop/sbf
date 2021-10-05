package ru.sbsoft.shared.param;

import java.util.Date;

/**
 * @author balandin
 * @since Mar 27, 2013 5:22:56 PM
 */
public class DateParamInfo extends ParamInfo<Date> {

    public DateParamInfo() {
        this(null, null);
    }

    public DateParamInfo(String name, Date value) {
        super(name, ParamTypeEnum.DATE, value);
    }
}
