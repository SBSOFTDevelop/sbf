package ru.sbsoft.client.components.form;

import com.sencha.gxt.widget.core.client.form.IsField;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Kiselev
 */
public class ValidateFormError {

    private final String msg;
    private final IsField<?> errField;
    private final List<IsField<?>> affectedFields = new ArrayList<IsField<?>>();

    public ValidateFormError(String msg, IsField<?> errField) {
        this.msg = msg;
        this.errField = errField;
    }

    public ValidateFormError addAffectedFields(IsField<?>... fs) {
        if (fs != null) {
            for (IsField<?> f : fs) {
                if (f != null) {
                    affectedFields.add(f);
                }
            }
        }
        return this;
    }

    public String getMsg() {
        return msg;
    }

    public IsField<?> getErrField() {
        return errField;
    }

    public List<IsField<?>> getAffectedFields() {
        return affectedFields;
    }

}
