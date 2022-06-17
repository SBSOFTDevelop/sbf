package ru.sbsoft.shared.param;

/**
 *
 * @author panarin
 */
public class IntegerParamInfo extends ParamInfo<Integer> {

    public IntegerParamInfo() {
        this(null, 0);
    }

    public IntegerParamInfo(final String name, final Integer value) {
        super(name, ParamTypeEnum.INTEGER, value);
    }
}
