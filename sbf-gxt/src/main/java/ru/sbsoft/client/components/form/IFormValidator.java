package ru.sbsoft.client.components.form;

import java.util.List;

/**
 *
 * @author Kiselev
 */
public interface IFormValidator {

    List<ValidateFormError> validate();
}
