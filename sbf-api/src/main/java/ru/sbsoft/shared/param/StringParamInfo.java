package ru.sbsoft.shared.param;

/**
 * @author balandin
 * @since Mar 29, 2013 3:38:05 PM
 */
public class StringParamInfo extends ParamInfo<String> {

    public StringParamInfo() {
        this(null, null);
    }

    public StringParamInfo(String name, String value) {
        super(name, ParamTypeEnum.STRING, value);
    }
}
