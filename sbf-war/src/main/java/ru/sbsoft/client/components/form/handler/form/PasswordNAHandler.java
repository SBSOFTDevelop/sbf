/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.sbsoft.client.components.form.handler.form;

import ru.sbsoft.svc.core.client.ValueProvider;
import ru.sbsoft.svc.widget.core.client.form.PasswordField;
import ru.sbsoft.client.components.field.PasswordNAField;
import ru.sbsoft.sbf.app.model.IFormModel;

/**
 *
 * @author sychugin
 */
public class PasswordNAHandler<M extends IFormModel> extends PasswordHandler<M> {
    
    public PasswordNAHandler(String label, ValueProvider<? super M, String> valueProvider) {
        super(label, valueProvider);
    }

    @Override
    protected PasswordField createField() {
        return new PasswordNAField();
    }
    
    
    
    
}
