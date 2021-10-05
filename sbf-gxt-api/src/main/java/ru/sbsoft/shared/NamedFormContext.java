package ru.sbsoft.shared;

import ru.sbsoft.shared.interfaces.FormType;

/**
 *
 * @author Kiselev
 */
public class NamedFormContext extends FormContext {

    private static final long serialVersionUID = 1L;

    public NamedFormContext() {
    }

    public NamedFormContext(NamedFormType formType) {
        super(formType);
    }

    @Override
    public void setFormType(FormType formType) {
        if (!(formType instanceof NamedFormType)) {
            throw new IllegalArgumentException("formType must be: " + NamedFormType.class.getName());
        }
        super.setFormType(formType);
    }

    @Override
    public NamedFormType getFormType() {
        return (NamedFormType) super.getFormType();
    }
}
