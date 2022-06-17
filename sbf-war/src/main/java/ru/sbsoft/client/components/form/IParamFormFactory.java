package ru.sbsoft.client.components.form;

import ru.sbsoft.client.components.operation.BaseOperationParamForm;

/**
 * Фабрика формы параметров.
 * @author Kiselev
 */
public interface IParamFormFactory {
    BaseOperationParamForm createForm();
}
