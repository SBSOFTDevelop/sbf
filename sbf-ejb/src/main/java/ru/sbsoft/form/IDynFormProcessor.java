package ru.sbsoft.form;

import ru.sbsoft.sbf.app.model.IFormModel;
import ru.sbsoft.shared.interfaces.IDynamicFormType;

/**
 *
 * @author sokolov
 * @param <M>
 */
public interface IDynFormProcessor<M extends IFormModel> extends IFormProcessor<M> {

    IDynamicFormType getFormType();

    void setFormType(IDynamicFormType formType);
}
