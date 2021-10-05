package ru.sbsoft.system;

/**
 *
 * @author Kiselev
 */
public interface ISysParameters {

    String get(String name);

    String get(String name, String defValue);

    int getInt(String name) throws ParameterException;

    int getInt(String name, Integer defValue) throws ParameterException;
    
}
