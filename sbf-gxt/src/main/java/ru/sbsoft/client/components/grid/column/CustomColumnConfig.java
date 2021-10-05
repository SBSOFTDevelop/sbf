package ru.sbsoft.client.components.grid.column;

import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import ru.sbsoft.client.I18n;
import ru.sbsoft.client.filter.editor.FilterAdapter;
import ru.sbsoft.shared.meta.Column;
import ru.sbsoft.shared.meta.ColumnWrapType;
import ru.sbsoft.shared.meta.Row;

/**
 * Базовый класс для параметров колонок таблицы разных типов.
 *
 * @param <N> тип значений в колонке
 */
public abstract class CustomColumnConfig<N> extends ColumnConfig<Row, N> {

    private final Column column;
    private String title;

    public CustomColumnConfig(ValueProvider<? super Row, N> valueProvider, Column column) {
        super(valueProvider, column.getWidth());

        this.column = column;

        setHeader(wrapString(I18n.get(column.getCaption())));
        ((CustomValueProvider) getValueProvider()).setPath(column.getAlias());
        setHorizontalHeaderAlignment(HasHorizontalAlignment.ALIGN_CENTER);
    }

    private SafeHtml wrapString(String value) {

        if (column.getWordWrap() == null || column.getWordWrap() == ColumnWrapType.QTIP) {
            return SafeHtmlUtils.fromTrustedString(value);
        }
        return SafeHtmlUtils.fromTrustedString("<span style='white-space: normal;'>" + SafeHtmlUtils.fromTrustedString(value).asString() + "</span>");
    }

//  USE setToolTip(String) FROM BASE
//    public CustomColumnConfig<N> setToolTip(String toolTip) { -- if this builder form is used anywhere the method must be renamed
//        setToolTip(SafeHtmlUtils.fromTrustedString(I18n.get(column.getCaption()))); -- usage column.getCaption() instead of toolTip value is strange. And force I18n in this place is IMHO unsutable.
//        return this;
//    }
    public Column getColumn() {
        return column;
    }

    public boolean isEnabled() {
        return true;
    }

    public String getTitle() {
        final Object t = column.getData(Column.TITLE_PREFIX);
        if (t instanceof String) {
            return (String) t;
        }
        title = I18n.get(column.getCaption());
        if (title != null) {
            title = title.replaceAll("<br>", " ").replaceAll("<br/>", " ");
            title = title.equals(I18n.get(column.getCaption())) ? I18n.get(column.getCaption()) : title;
        }
        column.setData(Column.TITLE_PREFIX, title);
        return title;
    }

    public FilterAdapter createFilterAdapter() {
        return null;
    }

    @Override
    public String toString() {
        return getTitle();
    }
}
