package ru.sbsoft.client.components.form;


import com.google.gwt.user.client.rpc.AsyncCallback;
import ru.sbsoft.client.components.IFormMaker;

/**
 *
 * @author Kiselev
 */
public class FormMakerAdapter implements IFormMaker {
    private final IFormFactory f;

    public FormMakerAdapter(IFormFactory f) {
        this.f = f;
    }
    
    @Override
    public void createForm(AsyncCallback<BaseForm> callback) {
        f.createEditForm(null, callback);
    }

}
