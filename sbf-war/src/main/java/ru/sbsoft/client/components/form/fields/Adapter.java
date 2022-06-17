package ru.sbsoft.client.components.form.fields;

import com.google.gwt.user.client.ui.Widget;
import ru.sbsoft.client.components.form.FormChangesControl;

/**
 * Посредник между формой и ее визуальными компонентами. Позволяет унифицировать
 * обработку различных компонент. Используется внутри
 * {@link ru.sbsoft.client.components.form.BaseForm} и ее наследников.
 *
 * @author balandin
 * @since Oct 8, 2013 2:08:12 PM
 */
public interface Adapter {

    void registr(FormChangesControl changesControl);

    void setReadOnly(boolean readOnly);

    void finishEditing();

    void clearInvalid();

    boolean isValid();

    Widget getWidget();
}
