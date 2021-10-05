package ru.sbsoft.meta.columns.consts;

/**
 * @author balandin
 * @since Feb 25, 2015 5:27:51 PM
 */
public enum PropertiesEnum {

    //DEFAILT_SORT(String.class),
    INCLUDE_WHERE_ON_GET_ROW(Boolean.class),
    CAPTION(String.class);
    //
    private final Class type;

    private PropertiesEnum(Class type) {
        this.type = type;
    }

    public Class getType() {
        return type;
    }
}
