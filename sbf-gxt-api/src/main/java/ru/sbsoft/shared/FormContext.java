package ru.sbsoft.shared;

import java.io.Serializable;
import ru.sbsoft.shared.interfaces.FormType;

/**
 * Класс представляет контекст формы.
 * <p> Уникальный строковый идентификатор контекста конкретной формы передается в конструкторе {@link #FormContext(java.lang.String)}.
 * <p> Уникальный идентификатор или контекст формы используется классом {@link ru.sbsoft.generator.FormDaoBeanGenerator} <br>
 * при генерации класса {@code ru.sbsoft.common.CommonFormDaoBean} для поиска класса наследника {@code BaseFormProcessor} и последующей инъекции полей (em, scontext, @Lookup).
 * @author balandin
 * @since May 24, 2013 4:37:32 PM
 */
public class FormContext implements Serializable {

    private FormType formType;
    private String formTypeString;

    public FormContext() {
    }

    public FormContext(String formType) {
        formTypeString = formType;
    }

    public FormContext(FormType formType) {
        this.formType = formType;
    }

    public FormType getFormType() {
        return formType;
    }

    public void setFormType(FormType formType) {
        this.formType = formType;
    }

    public String getFormTypeString() {
        if (formTypeString != null) {
            return formTypeString;
        }
        if (getFormType() == null) {
            return null;
        }
        return getFormType().getCode();
    }
}
