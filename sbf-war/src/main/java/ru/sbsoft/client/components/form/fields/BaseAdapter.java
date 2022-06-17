package ru.sbsoft.client.components.form.fields;

import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;
import ru.sbsoft.svc.widget.core.client.form.AdapterField;
import ru.sbsoft.svc.widget.core.client.form.Field;
import ru.sbsoft.svc.widget.core.client.form.IsField;
import ru.sbsoft.client.components.form.FormGridView;
import ru.sbsoft.client.components.form.FormChangesControl;
import ru.sbsoft.client.components.form.IEditorComponent;

/**
 *
 * @author balandin
 * @since Oct 8, 2013 2:06:28 PM
 */
public abstract class BaseAdapter implements Adapter {

    private final Widget component;
    private final boolean originalReadOnly;
    private final HasWidgets holder;
    private boolean temporaryReadOnly;

    public BaseAdapter(final Widget component, final boolean originalReadOnly, HasWidgets holder) {
        this.component = component;
        this.originalReadOnly = originalReadOnly;
        this.holder = holder;
    }

    public HasWidgets getHolder() {
        return holder;
    }

    public void setTemporaryReadOnly(boolean temporaryReadOnly) {
        this.temporaryReadOnly = temporaryReadOnly;
    }

    public boolean isOriginalReadOnly() {
        return originalReadOnly;
    }

    public void resetReadOnlyState() {
        this.temporaryReadOnly = originalReadOnly;
    }

    public static BaseAdapter create(Widget component, HasWidgets mainControl) {
        if (component instanceof IEditorComponent) {
            return new EditorComponentAdapter((Widget & IEditorComponent) component, mainControl);
        } else if (component instanceof FormGridView) {
            return new FormGridViewAdapter((FormGridView) component, mainControl);
        } else if (component instanceof Field) {
            return new FieldAdapter((Field) component, mainControl);
        } else if (component instanceof AdapterField) {
            ReadOnlyControl roc = null;
            if (component instanceof ReadOnlyControl) {
                roc = (ReadOnlyControl) component;
            } else {
                throw new IllegalArgumentException("ReadOnlyControl not implemented");
            }
            return new IsFieldAdapter((AdapterField) component, roc, mainControl);
        }
        return null;
    }

    @Override
    public void registr(FormChangesControl changesControl) {
        changesControl.registr(component);
    }

    @Override
    public Widget getWidget() {
        return component;
    }

    @Override
    public void finishEditing() {
        if (component instanceof Field) {
            ((Field) component).finishEditing();
        }
    }

    @Override
    public void clearInvalid() {
        if (component instanceof IsField) {
            ((IsField) component).clearInvalid();
        }
    }

    @Override
    public boolean isValid() {
        return true;
    }

    public boolean isReadOnly() {
        return originalReadOnly || temporaryReadOnly;
    }

    @Override
    public void setReadOnly(boolean readOnly) {
        if (component instanceof ReadOnlyControl) {
            ((ReadOnlyControl) component).setReadOnly(readOnly || isReadOnly());
        }
    }
}
