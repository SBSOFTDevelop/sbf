package ru.sbsoft.shared.param;

public enum ParamTypeEnum {

    BOOLEAN,
    STRING,
    //
    INTEGER,
    LONG,
    SHORT,
    BIGDECIMAL,
    //
    LIST,
    LOOKUP,
    //
    DATE,
    TIMESTAMP,
    YMD,
    //
    OBJECT;

    ParamTypeEnum() {
    }

    @Override
    public String toString() {
        return name();
    }
}
