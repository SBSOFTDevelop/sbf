package ru.sbsoft.shared;

import java.util.Map;

/**
 *
 * @author Fedor Resnyanskiy
 */
public interface IContext {

    Map<String, Object> getParameters();

    void addParameter(String name, Object value);

    Object getParameter(String name);
}
