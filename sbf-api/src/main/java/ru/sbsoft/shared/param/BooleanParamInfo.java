package ru.sbsoft.shared.param;

/**
 *
 * @author panarin
 */
public class BooleanParamInfo extends ParamInfo<Boolean> {

    public BooleanParamInfo() {
        this(null, false);
    }

    public BooleanParamInfo(final String name, final Boolean value) {
        super(name, ParamTypeEnum.BOOLEAN, value);
    }
}
