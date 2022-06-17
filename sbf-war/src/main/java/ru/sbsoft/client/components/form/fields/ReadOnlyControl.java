package ru.sbsoft.client.components.form.fields;

/**
 * Визуальный компонент поддерживающий ограничение редактирования. Для компонент не унаследованных от {@link ru.sbsoft.svc.widget.core.client.form.Field}.
 * @author balandin
 * @since Oct 8, 2013 2:03:17 PM
 */
public interface ReadOnlyControl {

	boolean isReadOnly();

	void setReadOnly(boolean readOnly);
}
