package ru.sbsoft.client.components.browser.filter;

import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import ru.sbsoft.client.I18n;
import ru.sbsoft.client.components.grid.IGridInfo;
import ru.sbsoft.client.components.grid.column.CustomColumnConfig;
import ru.sbsoft.client.components.grid.column.LookupColumnConfig;
import ru.sbsoft.client.components.grid.column.cell.LookupCell;
import ru.sbsoft.common.Strings;
import ru.sbsoft.shared.BooleanOperator;
import ru.sbsoft.shared.ColumnsFilterInfo;
import ru.sbsoft.shared.ComparisonEnum;
import ru.sbsoft.shared.FilterInfo;
import ru.sbsoft.shared.FilterInfoGroup;
import ru.sbsoft.shared.FilterTypeEnum;
import ru.sbsoft.shared.filter.LookUpFilterInfo;
import ru.sbsoft.shared.meta.IColumn;
import ru.sbsoft.shared.meta.IColumns;
import ru.sbsoft.shared.meta.filter.FilterDefinition;
import ru.sbsoft.shared.model.LookupInfoModel;
import ru.sbsoft.shared.api.i18n.consts.SBFBrowserStr;
import ru.sbsoft.shared.api.i18n.consts.SBFGeneralStr;

/**
 * Формирование строки состояния фильтра
 *
 * @author balandin
 */
public class FilterStatus {

    public enum OutType {
        HTML, PLAIN
    }

    private final IGridInfo baseGrid;
    private String caption = null;
    //
    private boolean multiLine = false;
    private OutType outType = OutType.HTML;
    private boolean noWrap = true;

    public FilterStatus(IGridInfo baseGrid) {
        this.baseGrid = baseGrid;
    }

    public static SafeHtml generate(IGridInfo baseGrid, FilterInfoGroup filterInfoGroup, boolean toolTip, String caption) {
        return new FilterStatus(baseGrid).setMultiLine(toolTip).setCaption(caption).generateHtml(filterInfoGroup);
    }

    public FilterStatus setMultiLine() {
        return setMultiLine(true);
    }

    public FilterStatus setMultiLine(boolean multiLine) {
        this.multiLine = multiLine;
        return this;
    }

    public FilterStatus setOutType(OutType outType) {
        this.outType = outType != null ? outType : OutType.HTML;
        return this;
    }

    public FilterStatus setCaption(String caption) {
        this.caption = caption;
        return this;
    }

    public FilterStatus setWrap() {
        return setNoWrap(false);
    }

    public FilterStatus setNoWrap(boolean noWrap) {
        this.noWrap = noWrap;
        return this;
    }

    public String generatePlain(FilterInfoGroup filterInfoGroup) {
        setOutType(OutType.PLAIN);
        return generate(filterInfoGroup);
    }

    public SafeHtml generateHtml(FilterInfoGroup filterInfoGroup) {
        setOutType(OutType.HTML);
        return SafeHtmlUtils.fromSafeConstant(generate(filterInfoGroup));
    }

    private String getElementIndent() {
        return isHtlm() ? "&nbsp;&nbsp;&nbsp;&nbsp;" : "    ";
    }

    private String getSpace() {
        return isHtlm() ? "&nbsp;" : " ";
    }

    private String getNewLine() {
        return isHtlm() ? "<br>" : "\n";
    }

    private boolean isHtlm() {
        return outType == OutType.HTML;
    }

    public String generate(FilterInfoGroup filterInfoGroup) {
        StringBuilder buffer = new StringBuilder();
        if (isHtlm()) {
            buffer.append("<p style='font-family:tahoma,arial,verdana,sans-serif;font-size:12px;line-height:16px;font-weight:normal;");
            if (noWrap) {
                buffer.append("white-space:nowrap;");
            }
            buffer.append("text-overflow:ellipsis;overflow:hidden;'>");
        }
        if (caption != null) {
            buffer.append(bold(caption));
            buffer.append(" (");
        }
        filters(filterInfoGroup, 0, BooleanOperator.AND, buffer);
        if (caption != null) {
            buffer.append(")");
        }
        if (isHtlm()) {
            buffer.append("</p>");
        }
        return buffer.toString();
    }

    private void filters(FilterInfoGroup group, int level, BooleanOperator parentBooleanOperator, StringBuilder buffer) {
        String elementIndent = getElementIndent();
        String spaceStr = getSpace();
        String newLine = getNewLine();
        if (group == null) {
            return;
        }
        String indent = Strings.repl(elementIndent, level - 1);
        if (level > 0) {
            if (multiLine) {
                buffer.append(indent);
            }
            if (parentBooleanOperator != null) {
                if (!multiLine) {
                    buffer.append(spaceStr);
                }
                buffer.append(text(I18n.get(parentBooleanOperator).toLowerCase()));
                buffer.append(spaceStr);
            }
            buffer.append('(');
            if (multiLine) {
                buffer.append(newLine);
            }
        }

        boolean needTerminator = false;
        final List<FilterInfo> childFilters = group.getChildFilters();
        for (final FilterInfo f : childFilters) {

            if (f instanceof FilterInfoGroup) {
                filters((FilterInfoGroup) f, level + 1, needTerminator ? group.getValue() : null, buffer);
                needTerminator = true;
                continue;
            }

            String columnName = blue(getColumnName(f.getColumnName()));
            if (multiLine) {
                buffer.append(indent);
                if (level > 0) {
                    buffer.append(elementIndent);
                }
            }
            if (needTerminator) {
                if (!multiLine) {
                    buffer.append(spaceStr);
                }
                BooleanOperator booleanOperation = group.getValue();
                buffer.append(text(I18n.get(booleanOperation).toLowerCase()));
                buffer.append(spaceStr);
            }

            Object value = f.getValue();
            if (value == null) {
                buffer.append(columnName);
                buffer.append(spaceStr);
                if (f.isNotExpression()) {
                    buffer.append(text(I18n.get(SBFGeneralStr.labelNot))).append(spaceStr);
                }
                buffer.append(red(I18n.get(SBFBrowserStr.labelEmpty)));
            } else if (f.getComparison() == ComparisonEnum.in_bound || f.getComparison() == ComparisonEnum.in_range) {
                buffer.append(columnName);
                buffer.append(spaceStr);
                if (f.isNotExpression()) {
                    buffer.append(text(I18n.get(SBFGeneralStr.labelNot))).append(spaceStr);
                }
                buffer.append(text(null == f.getComparison().getKey() ? f.getComparison().getSql() : I18n.get(f.getComparison())));
                buffer.append(spaceStr);

                buffer.append(f.getComparison() == ComparisonEnum.in_range ? '[' : '(');
                buffer.append(red(format(f, f.getValue())));
                buffer.append(spaceStr).append("..").append(spaceStr);
                buffer.append(red(format(f, f.getSecondValue())));
                buffer.append(f.getComparison() == ComparisonEnum.in_range ? ']' : ')');
            } else {
                if (f.isNotExpression()) {
                    buffer.append(text(I18n.get(SBFGeneralStr.labelNot))).append(spaceStr);
                }

                LookupColumnConfig lookupColumnConfig = null;
                final IColumn colum = baseGrid.getMetaInfo().getColumnForAlias(f.getColumnName());
                if (colum != null) {
                    CustomColumnConfig config = (CustomColumnConfig) colum.getData(IColumn.COLUMN_CONFIG_PREFIX);
                    if (config instanceof LookupColumnConfig) {
                        lookupColumnConfig = (LookupColumnConfig) config;
                    }
                }

                boolean skipValue
                        = f.getType() == FilterTypeEnum.BOOLEAN
                        && !multiLine
                        && lookupColumnConfig == null
                        && (f.getValue() == Boolean.TRUE || f.getValue() == Boolean.FALSE);

                if (skipValue) {
                    if (f.getValue() == Boolean.TRUE) {
                        buffer.append(green(getColumnName(f.getColumnName())));
                    } else {
                        buffer.append(red(getColumnName(f.getColumnName())));
                    }
                } else {
                    buffer.append(columnName);
                    buffer.append(spaceStr);
                    buffer.append(text(null == f.getComparison().getKey() ? f.getComparison().getSql() : I18n.get(f.getComparison())));
                    buffer.append(spaceStr);
                }

                if (f instanceof ColumnsFilterInfo) {
                    buffer.append(blue(getColumnName(((ColumnsFilterInfo) f).getColumnName2())));
                } else if (!skipValue) {
                    if (lookupColumnConfig != null) {
                        buffer.append(red(((LookupCell) lookupColumnConfig.getCell()).format(value)));
                    } else {
                        buffer.append(red(format(f, value)));
                    }
                }
            }
            if (multiLine) {
                buffer.append(newLine);
            }
            needTerminator = true;
        }

        if (level > 0) {
            if (multiLine) {
                buffer.append(indent);
            }
            buffer.append(')');
            if (multiLine) {
                buffer.append(newLine);
            }
        }
    }

    private String text(String value) {
        return value;
    }

    private String blue(String value) {
        return isHtlm() ? "<span style='color:#008'>" + value + "</span>" : value;
    }

    private String red(String value) {
        return isHtlm() ? "<span style='color:#800'>" + value + "</span>" : value;
    }

    private String green(String value) {
        return isHtlm() ? "<span style='color:#080'>" + value + "</span>" : value;
    }

    private String bold(String value) {
        return isHtlm() ? "<span style='font-weight:bold'>" + value + "</span>" : value;
    }

    private String getColumnName(String alias) {
        final IColumns metaInfo = baseGrid.getMetaInfo();
        final IColumn c = metaInfo.getColumnForAlias(alias);
        if (c != null) {
            return Strings.coalesce(I18n.get(c.getCaption()), alias);
        }
        final FilterDefinition def = metaInfo.getFilterDefinitions().get(alias);
        if (def != null) {
            return I18n.get(def.getCaption());
        }
        return alias;
    }

    private String format(FilterInfo f, Object v) {
        FilterTypeEnum t = f.getType();
        if (v instanceof Date) {
            DateTimeFormat formatter = null;
            if (t == FilterTypeEnum.DATE) {
                formatter = DateTimeFormat.getFormat(DateTimeFormat.PredefinedFormat.DATE_SHORT);
            } else if (t == FilterTypeEnum.TIMESTAMP) {
                formatter = DateTimeFormat.getFormat(DateTimeFormat.PredefinedFormat.DATE_TIME_MEDIUM);
            }
            if (formatter != null) {
                v = formatter.format((Date) v);
            }
        } else if (v instanceof String) {
            if (f.getType() == FilterTypeEnum.STRING && f.isCaseSensitive()) {
                v = ((String) v).toUpperCase();
            }
            v = quote((String) v);
        } else if (v instanceof Boolean) {
            v = (Boolean) v ? I18n.get(SBFGeneralStr.labelYes) : I18n.get(SBFGeneralStr.labelNo);
        } else if (f instanceof LookUpFilterInfo) {
            StringBuilder s = new StringBuilder();
            s.append('[');
            List<LookupInfoModel> items = ((LookUpFilterInfo) f).getValue();
            if (items.size() == 1) {
                final LookupInfoModel m = items.get(0);
                if (m.getSemanticKey() == null && m.getSemanticName() == null) {
                    s.append(m.getID());
                } else {
                    String tmp = m.getSemanticKey() + "/" + m.getSemanticName();
                    if (tmp.length() > 35) {
                        tmp = tmp.substring(0, 25) + "…";
                    }
                    s.append(tmp);
                }
            } else if (items.size() > 1) {
                // отображаем пару идентификаторов
                // мб надо сделать надпись "множественный выбор"
                final List<String> keys = new ArrayList<>();
                items.forEach((it) -> keys.add(String.valueOf(getKey(it))));
                Collections.sort(keys);
                int showNumber = Math.min(5, keys.size());
                for (int i = 0; i < showNumber; i++) {
                    s.append(keys.get(i));
                    s.append(',');
                }
                if (keys.size() > showNumber) {
                    s.append(", …");
                } else {
                    s.setLength(s.length() - 1);
                }
            }
            s.append(']');
            return s.toString();
        }
        return String.valueOf(v);
    }

    private Object getKey(LookupInfoModel m) {
        Object[] candidates = {m.getSemanticKey(), m.getSemanticID(), m.getID()};
        for (Object o : candidates) {
            if (o != null) {
                return o;
            }
        }
        return null;
    }

    private String quote(String value) {
        return "'" + value + "'";
    }
}
