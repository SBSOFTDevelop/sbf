package ru.sbsoft.shared.model;

import ru.sbsoft.shared.consts.Month;

/**
 *
 * @author Kiselev
 */
public interface IYearMonth<T extends IYearMonth> extends Comparable<T>{

    int getMonthNum();

    int getYear();
    
    Month getMonth();
    
}
