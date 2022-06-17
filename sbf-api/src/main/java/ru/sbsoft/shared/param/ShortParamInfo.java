package ru.sbsoft.shared.param;

/**
 *
 * @author panarin
 */
public class ShortParamInfo extends ParamInfo<Short> {

    public ShortParamInfo() {
        this(null, (short)0);
    }

    public ShortParamInfo(final String name, final Short value) {
        super(name, ParamTypeEnum.SHORT, value);
    }
}
