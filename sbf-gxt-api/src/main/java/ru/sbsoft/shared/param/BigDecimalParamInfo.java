package ru.sbsoft.shared.param;

import java.math.BigDecimal;

/**
 * @author balandin
 * @since Jun 19, 2013 1:53:08 PM
 */
public class BigDecimalParamInfo extends ParamInfo<BigDecimal> {

    public BigDecimalParamInfo() {
        this( null, null);
    }

    
    public BigDecimalParamInfo(String name, BigDecimal value) {
        super(name, ParamTypeEnum.BIGDECIMAL, value);
    }
}
