package ru.sbsoft.shared.consts;

import ru.sbsoft.sbf.app.model.YearMonthDay;

/**
 *
 * @author vk
 */
public interface GlobalConst {
    
    // даты для назнчения темпорального периода по умолчанию ----------------
    YearMonthDay DEFAULT_MIN_DATE = new YearMonthDay();
    YearMonthDay FAR_FUTURE = new YearMonthDay(9999, 12, 31);    
}
