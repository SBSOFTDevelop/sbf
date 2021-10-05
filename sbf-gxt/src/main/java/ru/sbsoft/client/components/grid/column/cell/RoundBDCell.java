package ru.sbsoft.client.components.grid.column.cell;

import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 *
 * @author sychugin
 */
public class RoundBDCell<T> extends NumberCell<BigDecimal> {

    private int prec;

    public RoundBDCell(NumberFormat format, int prec) {

        super(format);
        this.prec = prec;
    }

    @Override
    public void render(Context context, BigDecimal value, SafeHtmlBuilder sb) {
        super.render(context, value != null ? fmt2(value.toString()) : value, sb);
    }

    private BigDecimal fmt2(String s) {
        Double dv = Double.valueOf(s);
        Double e = Math.pow(10, prec);
        dv = Math.round(dv * e) / e;
        return BigDecimal.valueOf(dv).setScale(prec, RoundingMode.HALF_UP);
    }
}
