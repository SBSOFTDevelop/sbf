package ru.sbsoft.shared.meta;

import ru.sbsoft.shared.interfaces.ObjectType;

/**
 *
 * @author Kiselev
 */
public enum CodebaseField implements ObjectType {

    CODE_VALUE,
    NAME_VALUE;

    @Override
    public String getCode() {
        return name();
    }

}
