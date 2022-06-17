package ru.sbsoft.client.components.grid;

import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import ru.sbsoft.svc.widget.core.client.grid.AggregationRenderer;
import ru.sbsoft.svc.widget.core.client.grid.Grid;

/**
 * Форматирует значение типа {@link String} для отображения в ячейке итоговой строки таблицы.
 * @author balandin
 * @since May 16, 2014 4:47:57 PM
 */
public class TextRenderer<DataInfoModel> implements AggregationRenderer<DataInfoModel> {
	
	private String value;

	public TextRenderer() {
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public SafeHtml render(int colIndex, Grid<DataInfoModel> grid) {
		return new SafeHtmlBuilder().appendHtmlConstant(value).toSafeHtml();
	}
}
