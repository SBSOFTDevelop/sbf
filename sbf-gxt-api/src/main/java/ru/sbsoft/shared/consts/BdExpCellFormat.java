package ru.sbsoft.shared.consts;

import java.math.BigDecimal;
import java.util.Arrays;
import ru.sbsoft.shared.exceptions.ApplicationException;

import ru.sbsoft.shared.grid.condition.expression.IGridExpression;
import ru.sbsoft.shared.grid.format.IExpCellFormat;
import ru.sbsoft.shared.meta.Row;

/**
 *
 * @author sychugin
 */
public class BdExpCellFormat implements IExpCellFormat {

    private final static String THOUSAND_PREFIX = "#,##";
    private final static Long MAX_VAL = Long.valueOf(Integer.MAX_VALUE);
    private final static Long ZERO_VAL = 0L;
    private String alias;
    private boolean thousandsSep;

    private IGridExpression<Long> expr;

    //for serialization only
    private BdExpCellFormat() {
    }

    public BdExpCellFormat thousandSep() {
        this.thousandsSep = true;
        return this;
    }

    public BdExpCellFormat(IGridExpression<Long> expr, String alias) {
        this.expr = expr;
        this.alias = alias;
    }

    protected String createFormat(int repeat) {

        char[] cc = new char[repeat];
        String d = "0";
        if (repeat > 0) {

            Arrays.fill(cc, 0, cc.length, '0');
            d = "0.".concat(new String(cc));
        }

        return (thousandsSep ? THOUSAND_PREFIX : "").concat(d);
    }

    private int getFracDigitsCount(Row row) {
        Long p = expr.getValue(row);
        if (p == null || p.compareTo(ZERO_VAL) < 0) {
            p = ZERO_VAL;
        }
        if (p.compareTo(MAX_VAL) > 0) {
            throw new ApplicationException("Precision exceeds max value: " + p + " > " + MAX_VAL);
        }
        return p.intValue();
    }

    @Override
    public String getFormat(Row row) {

        int p = getFracDigitsCount(row);
        BigDecimal value = (BigDecimal) row.getValue(alias);

        if (value != null) {
            p = Math.max(p, value.scale());
        }

        return createFormat(p);
    }

    @Override
    public Integer getPrecision(Row row) {
        Long p = expr.getValue(row);
        if (p == null) {
            return null;
        }
        return p.intValue();
    }

}
