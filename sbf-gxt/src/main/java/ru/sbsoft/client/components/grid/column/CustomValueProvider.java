package ru.sbsoft.client.components.grid.column;

import com.sencha.gxt.core.client.ValueProvider;
import ru.sbsoft.shared.meta.Row;

/**
 * Пытается вернуть из модели таблицы {@link Row} по индексу значение заданного типа.
 * Если тип значения не соответствует указанному, возникнет исключение.
 * @author balandin
 * @since Mar 17, 2014 5:04:22 PM
 */
public class CustomValueProvider<T> implements ValueProvider<Row, T> {

	private final int index;
	private String path;

	public CustomValueProvider(int index) {
		this.index = index;
	}

    @Override
	public T getValue(Row object) {
		return (T) object.getValues().get(index);
	}

    @Override
	public void setValue(Row object, T value) {
	}

	public void setPath(String path) {
		this.path = path;
	}

    @Override
	public String getPath() {
		return path;
	}

	public int getIndex() {
		return index;
	}
}
