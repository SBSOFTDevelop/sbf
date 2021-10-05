package ru.sbsoft.client.components.grid;

import com.google.gwt.core.client.JavaScriptException;
import com.google.gwt.safecss.shared.SafeStyles;
import com.google.gwt.safecss.shared.SafeStylesBuilder;
import java.util.ArrayList;
import java.util.List;
import ru.sbsoft.client.components.grid.column.CustomColumnConfig;
import ru.sbsoft.shared.grid.style.ConditionalCellStyle;
import ru.sbsoft.shared.meta.Columns;
import ru.sbsoft.shared.meta.Row;
import ru.sbsoft.shared.meta.Style;

/**
 * Конструирует html-стили колонок таблицы по их {@link CustomColumnConfig}.
 *
 * @author balandin
 * @since Oct 28, 2014 4:51:36 PM
 */
public class StyleChecker {

    private final SystemGrid.IRowByIndexProvider grid;
    private final Columns columns;
    private final SafeStyles EMPTY = new SafeStylesBuilder().toSafeStyles();

    public StyleChecker(SystemGrid.IRowByIndexProvider grid, Columns columns) {
        this.grid = grid;
        this.columns = columns;
    
    }

    public void apply(CustomColumnConfig config, int index) {
        config.setColumnTextStyle(EMPTY);
        boolean hasJsStyles = config.getColumn().getStyles() != null || columns.getStyles() != null;
        boolean hasStyles = config.getColumn().getGridStyles() != null || columns.getGridStyles() != null;
        if (hasStyles || hasJsStyles) {
            SafeStylesBuilder result = new SafeStylesBuilder();
            Row row = grid.getRow(index);
            if (hasJsStyles) {
                calcStyles(row, result, config.getColumn().getStyles());
                calcStyles(row, result, columns.getStyles());
            }
            if (hasStyles) {
                applyStyles(row, result, config.getColumn().getGridStyles());
                applyStyles(row, result, columns.getGridStyles());
            }
            
            config.setColumnTextStyle(result.toSafeStyles());
        }
    }
    
    private void applyStyles(Row row, SafeStylesBuilder safeStylesBuilder, List<ConditionalCellStyle> styles){
        if(styles != null){
            for(ConditionalCellStyle cs : styles){
                if(cs.isApplicable(row)){
                    for(String s : cs.getStyle().getCssStyles()){
                        safeStylesBuilder.appendTrustedString(s);
                    }
                }
            }
        }
    }

    private void calcStyles(Row row, SafeStylesBuilder safeStylesBuilder, final List<Style> styles) {
        if (styles != null) {
            for (Style style : styles) {
                if (check(row, style)) {
                    safeStylesBuilder.appendTrustedString(style.getTemplate());
                }
            }
        }
    }

    private boolean check(Row row, Style style) {
        if (style.getParams() != null && style.getParams().size() > 10) {
            return false;
        }
        StringBuilder js = new StringBuilder();
        if (style.getParams() != null) {
            js.append("try { ");
            for (String paramName : style.getParams()) {
                Object v = row.getValue(paramName);
                String value = null;
                if (v instanceof String) {
                    value = "'" + ((String) v).replaceAll("'", "\'").replaceAll("\"", "\\") + "'";
                } else if (v instanceof Number) {
                    value = String.valueOf(((Number) v).intValue());
                } else if (v instanceof Boolean) {
                    value = ((Boolean) v) ? "true" : "false";
                }
                js.append("var ").append(paramName).append("=").append(value).append(";");
            }

            js.append("; var result = ");
            js.append(style.getCondition());
            js.append("; } finally {");

            for (String paramName : style.getParams()) {
                js.append(";delete ").append(paramName).append(";");
            }
            js.append("} result;");
        } else {
            js.append(style.getCondition());
        }

        Object result = __execute(js.toString());
        if (result instanceof Boolean) {
            return (Boolean) result;
        } else if (result instanceof String) {
            final String paramName = (String) result;
            if (row.getColumns().getColumnForAlias(paramName) == null) {
                return false;
            }
            if (style.getParams() == null) {
                style.setParams(new ArrayList<String>());
            }
            style.getParams().add(paramName);
            return check(row, style);
        } else {
            return false;
        }
    }

    private Object __execute(String expression) {
        try {
            return execute(expression);
        } catch (JavaScriptException ex) {
            return ex.getDescription().replaceAll("'?([^' ]*)[' ]?.*", "$1");
        }
    }

    private native Object execute(String expression) /*-{
     var r = eval(expression);
     if (typeof(r) == 'boolean') {
     return @java.lang.Boolean::new(Z)(r);
     };
     return @java.lang.IllegalArgumentException::new(Ljava/lang/String;)(typeof(r) + ' ' + r);
     }-*/;
}
