package ru.sbsoft.shared;

import java.util.Map;

/**
 *
 * @author Fedor Resnyanskiy
 */
public interface IContext {

    public Map<String, Object> getParameters();

    public void addParameter(String name, Object value);

    public Object getParameter(String name);
}
