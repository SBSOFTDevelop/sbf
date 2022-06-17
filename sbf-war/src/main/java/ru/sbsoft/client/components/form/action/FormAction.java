package ru.sbsoft.client.components.form.action;

import ru.sbsoft.client.components.actions.AbstractAction;
import ru.sbsoft.client.components.form.BaseForm;
import ru.sbsoft.sbf.app.model.IFormModel;

/**
 * Базовый класс действий, вызываемых на форме данных. 
 * Содержит указатель на связанную с ним форму.
 */
public abstract class FormAction<M extends IFormModel> extends AbstractAction {

    private final BaseForm<M> form;

    public FormAction(BaseForm<M> form) {
        super();
        this.form = form;
    }

    public BaseForm<M> getForm() {
        return form;
    }
    
    @Override
    public boolean checkEnabled() {
        return true;
    }
}
