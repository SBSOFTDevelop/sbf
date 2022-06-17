package ru.sbsoft.client.components.grid.column.cell;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import ru.sbsoft.client.components.grid.FormatPovider;
import ru.sbsoft.client.components.grid.StyleChecker;
import ru.sbsoft.client.components.grid.column.CustomColumnConfig;
import ru.sbsoft.common.Strings;

/**
 * Базовый класс для ячеек таблицы разных типов.
 *
 * @author balandin
 * @since Oct 23, 2014 2:40:14 PM
 */
public class CustomCell<C> extends AbstractCell<C> {

    private CustomColumnConfig config;
    private StyleChecker checker;
    private FormatPovider fmtProv;

    public CustomCell() {
    }

    @Override
    public void render(Context context, C value, SafeHtmlBuilder sb) {
        final String text = value == null ? null : format(value, context);
        applyText(sb, Strings.isEmpty(text) ? "&#160;" : text);
        applyStyle(context);
    }

    protected void applyText(SafeHtmlBuilder sb, String text) {
        sb.append(SafeHtmlUtils.fromTrustedString(text));
    }

    protected void applyStyle(Context context) {
        if (checker != null) {
            checker.apply(config, context.getIndex());
        }
    }

    private String format(C value, Context context) {
        if (fmtProv != null) {
            return format(value, fmtProv.getFormat(config, context.getIndex()));
        }
        return format(value);
    }

    protected String format(C value, String formatString) {
        return format(value);
    }

    protected String format(C value) {
        return String.valueOf(value);
    }

    public CustomColumnConfig getConfig() {
        return config;
    }

    public void setConfig(CustomColumnConfig config) {
        this.config = config;
    }

    public void setStyleChecker(StyleChecker checker) {
        this.checker = checker;
    }

    public StyleChecker getStyleChecker() {
        return checker;
    }

    public void setFormatProvider(FormatPovider fmtProv) {
        this.fmtProv = fmtProv;
    }

}
