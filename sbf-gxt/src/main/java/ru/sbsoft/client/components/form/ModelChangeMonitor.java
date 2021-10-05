package ru.sbsoft.client.components.form;

import ru.sbsoft.sbf.app.model.FormModel;

/**
 *
 * @author Kiselev
 */
public class ModelChangeMonitor<M extends FormModel> implements ModelChangeListener<M>{
    private M currentModel = null;

    @Override
    public void modelChanged(M m) {
        currentModel = m;
    }

    public M getCurrentModel() {
        return currentModel;
    }
    
}
