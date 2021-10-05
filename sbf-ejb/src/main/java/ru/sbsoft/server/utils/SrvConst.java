package ru.sbsoft.server.utils;

import java.math.MathContext;
import java.math.RoundingMode;
import java.text.DecimalFormatSymbols;
import ru.sbsoft.shared.consts.GlobalConst;

/**
 * Прикладные контанты.
 *
 */
public interface SrvConst extends GlobalConst {

    // ----------------------------------------------------------------- форматы число
    MathContext MathContext24 = new MathContext(24, RoundingMode.HALF_UP);
    String DECIMAL_SEPARATOR = String.valueOf(DecimalFormatSymbols.getInstance().getDecimalSeparator());

}
