package ru.sbsoft.client.components.grid;

import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.sencha.gxt.widget.core.client.grid.AggregationRenderer;
import com.sencha.gxt.widget.core.client.grid.Grid;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import ru.sbsoft.common.Strings;

/**
 * Форматирует значение типа {@link BigDecimal} для отображения в ячейке итоговой строки таблицы.
 * @param <DataInfoModel> модель данных
 */
public class NumberRenderer<DataInfoModel> implements AggregationRenderer<DataInfoModel> {

	private BigDecimal value;
	private NumberFormat formatter;
	private final Set<String> styles = new HashSet<String>();

	public NumberRenderer() {
	}

	public void setFormat(String format) {
		this.formatter = NumberFormat.getFormat(format);
	}

	public BigDecimal getValue() {
		return value;
	}

	public void setValue(BigDecimal value) {
		this.value = value;
	}

	public SafeHtml render(int colIndex, Grid<DataInfoModel> grid) {
		//Нужен отдельный StringBuilder так как SafeHtmlBuilder не умеет обрабатывать незакрытые теги
		final StringBuilder sb = new StringBuilder("<div ");
		if (!styles.isEmpty()) {
			sb.append("class=\"").append(Strings.join(styles.toArray(), " ")).append("\" ");
		}
		sb.append("style=\"text-align: right;\">");
		final SafeHtmlBuilder builder = new SafeHtmlBuilder();
		builder.appendHtmlConstant(sb.toString());
		builder.appendHtmlConstant(formatter.format(value));
		builder.appendHtmlConstant("</div>");
		return builder.toSafeHtml();
	}

	public boolean addStyle(String cls) {
		return styles.add(cls);
	}

	public boolean removeStyle(String cls) {
		return styles.remove(cls);
	}
}
