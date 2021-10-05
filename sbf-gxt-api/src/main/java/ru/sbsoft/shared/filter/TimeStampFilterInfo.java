package ru.sbsoft.shared.filter;

import java.util.Date;
import ru.sbsoft.shared.FilterInfo;
import ru.sbsoft.shared.FilterTypeEnum;

public class TimeStampFilterInfo extends FilterInfo<Date> {

    public TimeStampFilterInfo() {
        this(null, null);
    }

    public TimeStampFilterInfo(String columnName, Date value) {
        super(columnName, null, FilterTypeEnum.TIMESTAMP, value);
    }
}
