package ru.sbsoft.client.components.form.fields;

/**
 * Визуальный компонент поддерживающий ограничение редактирования. Для компонент не унаследованных от {@link com.sencha.gxt.widget.core.client.form.Field}.
 * @author balandin
 * @since Oct 8, 2013 2:03:17 PM
 */
public interface ReadOnlyControl {

	public boolean isReadOnly();

	public void setReadOnly(boolean readOnly);
}
