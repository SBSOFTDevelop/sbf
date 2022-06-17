package ru.sbsoft.client.components.form.fields;

import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;
import ru.sbsoft.client.components.form.IEditorComponent;

/**
 *
 * @author balandin
 * @since Oct 8, 2013 2:35:16 PM
 */
public class EditorComponentAdapter extends BaseAdapter {

    private final IEditorComponent editor;

    public <E extends Widget & IEditorComponent> EditorComponentAdapter(final E editor, HasWidgets mainControl) {
        super(editor, editor.isReadOnly(), mainControl);
        this.editor = editor;
    }

    @Override
    public void finishEditing() {
        editor.finishEditing();
    }
}
