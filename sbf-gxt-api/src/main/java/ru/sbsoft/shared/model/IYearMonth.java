package ru.sbsoft.shared.model;

/**
 *
 * @author Kiselev
 */
public interface IYearMonth<T extends IYearMonth> extends Comparable<T>{

    int getMonth();

    int getYear();
    
}
