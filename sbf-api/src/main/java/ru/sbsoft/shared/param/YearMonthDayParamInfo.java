package ru.sbsoft.shared.param;

import ru.sbsoft.sbf.app.model.YearMonthDay;

/**
 *
 * @author sokolov
 */
public class YearMonthDayParamInfo extends ParamInfo<YearMonthDay> {

    public YearMonthDayParamInfo() {
        this(null, null);
    }

    public YearMonthDayParamInfo(String name, YearMonthDay value) {
        super(name, ParamTypeEnum.YMD, value);
    }
    
}
