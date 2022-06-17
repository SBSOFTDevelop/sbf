package ru.sbsoft.client.components.form;

import ru.sbsoft.svc.widget.core.client.container.ResizeContainer;
import ru.sbsoft.svc.widget.core.client.form.AdapterField;
import ru.sbsoft.client.components.form.fields.ReadOnlyControl;

/**
 * Добавляет принудительную прорисовывку объекта при изменении размеров.
 * @see AdapterField
 * @author balandin
 * @since Oct 15, 2013 2:38:02 PM
 */
public abstract class CustomField<M> extends AdapterField<M> implements ReadOnlyControl {

    public CustomField() {
        super(null);
    }

    @Override
    public void setPixelSize(int width, int height) {
        super.setPixelSize(width, height);
        if (getWidget() instanceof ResizeContainer) {
            ResizeContainer container = (ResizeContainer) getWidget();
            container.setPixelSize(width, height);
            container.forceLayout();
        }
    }
}
