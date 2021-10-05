package ru.sbsoft.shared.model;

import ru.sbsoft.sbf.app.model.YearMonthDay;

/**
 *
 * @author vk
 */
public interface IActRange {

    YearMonthDay getBegDate();

    YearMonthDay getEndDate();
    
    default boolean isOnDate(YearMonthDay onDate){
        return onDate.compareTo(getBegDate()) >=0 && onDate.compareTo(getEndDate()) <= 0;
    }
}
