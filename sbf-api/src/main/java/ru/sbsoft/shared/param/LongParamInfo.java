package ru.sbsoft.shared.param;

import ru.sbsoft.shared.param.ParamInfo;
import ru.sbsoft.shared.param.ParamTypeEnum;

/**
 *
 * @author Kiselev
 */
public class LongParamInfo extends ParamInfo<Long> {

    public LongParamInfo() {
        this(null, 0L);
    }

    public LongParamInfo(final String name, final Long value) {
        super(name, ParamTypeEnum.LONG, value);
    }
}
