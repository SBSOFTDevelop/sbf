package ru.sbsoft.shared.model;

import ru.sbsoft.sbf.app.model.YearMonthDay;

/**
 *
 * @author vk
 */
public interface IActRangeModel extends IActRange {

    void setBegDate(YearMonthDay begDate);

    void setEndDate(YearMonthDay endDate);
}
