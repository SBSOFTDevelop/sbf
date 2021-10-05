package ru.sbsoft.client.components.form.fields;

import com.sencha.gxt.widget.core.client.Component;
import ru.sbsoft.client.components.form.FormChangesControl;

/**
 * Посредник между формой и ее визуальными компонентами.
 * Позволяет унифицировать обработку различных компонент.
 * Используется внутри {@link ru.sbsoft.client.components.form.BaseForm} и ее наследников.
 * @author balandin
 * @since Oct 8, 2013 2:08:12 PM
 */
public interface Adapter {

	public abstract void registr(FormChangesControl changesControl);

	public abstract void setReadOnly(boolean readOnly);

	public void finishEditing();

	public void clearInvalid();

	public boolean isValid();

	public Component getWidget();
}
