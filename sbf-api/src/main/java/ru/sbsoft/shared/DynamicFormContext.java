package ru.sbsoft.shared;

import ru.sbsoft.shared.interfaces.FormType;
import ru.sbsoft.shared.interfaces.IDynamicFormType;

/**
 *
 * @author Kiselev
 */
public class DynamicFormContext extends NamedFormContext {

    private static final long serialVersionUID = 1L;

    public DynamicFormContext() {
    }

    public DynamicFormContext(IDynamicFormType formType) {
        super(formType);
    }

    @Override
    public void setFormType(FormType formType) {
        if (!(formType instanceof IDynamicFormType)) {
            throw new IllegalArgumentException("formType must be: " + IDynamicFormType.class.getName());
        }
        super.setFormType(formType);
    }

    @Override
    public IDynamicFormType getFormType() {
        return (IDynamicFormType) super.getFormType();
    }
}
