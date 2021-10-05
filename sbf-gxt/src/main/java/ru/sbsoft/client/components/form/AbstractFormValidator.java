package ru.sbsoft.client.components.form;

import com.sencha.gxt.widget.core.client.Component;
import com.sencha.gxt.widget.core.client.form.IsField;
import com.sencha.gxt.widget.core.client.form.ValueBaseField;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import ru.sbsoft.client.I18n;
import ru.sbsoft.client.components.form.handler.IFieldHandler;
import ru.sbsoft.client.utils.CliUtil;
import ru.sbsoft.shared.api.i18n.consts.SBFExceptionStr;
import ru.sbsoft.shared.api.i18n.consts.SBFGeneralStr;
import ru.sbsoft.shared.exceptions.ApplicationException;

/**
 *
 * @author Kiselev
 */
public abstract class AbstractFormValidator implements IFormValidator {

    protected static final String NONEMPTY_MSG = I18n.get(SBFGeneralStr.msgNeedFill);
    protected static final String EMPTY_MSG =  I18n.get(SBFGeneralStr.msgImpossibleFill);
    private final Map<Integer, String> messages;
    private final List<ValidateFormError> errors = new ArrayList<ValidateFormError>();

    public AbstractFormValidator() {
        this(null);
    }

    public AbstractFormValidator(Map<Integer, String> messages) {
        this.messages = messages != null ? messages : new HashMap<Integer, String>();
    }

    @Override
    public List<ValidateFormError> validate() {
        errors.clear();
        doValidate();
        return errors;
    }

    protected abstract void doValidate();

    protected final void nonEmpty(LookupField<?> f) {
        if (isEmpty(f)) {
            addErr(f, NONEMPTY_MSG);
        }
    }

    protected final void nonEmpty(ValueBaseField<?> f) {
        if (isEmpty(f)) {
            addErr(f, NONEMPTY_MSG);
        }
    }

    protected final void empty(LookupField<?> f) {
        if (!isEmpty(f)) {
            addErr(f, EMPTY_MSG);
        }
    }

    protected final void empty(ValueBaseField<?> f) {
        if (!isEmpty(f)) {
            addErr(f, EMPTY_MSG);
        }
    }

    protected final boolean isEmpty(ValueBaseField<?> f) {
        Object val = f.getCurrentValue();
        return val == null || ((val instanceof String) && ((String) val).trim().length() == 0);
    }

    protected final boolean isEmpty(LookupField<?> f) {
        return f.getValue() == null;
    }

    protected final ValidateFormError addErr(IsField<?> errField, int num, Object... args) {
        return addErr(errField, getErrMsg(num, args));
    }

    protected final <F extends Component & IsField<?>> ValidateFormError addErr(IFieldHandler<F, ?, ?> errField, String msg, IFieldHandler<F, ?, ?>... affectedFields) {
        IsField<?>[] af = new IsField[affectedFields != null ? affectedFields.length : 0];
        if (affectedFields != null) {
            for (int i = 0; i < affectedFields.length; i++) {
                IFieldHandler<F, ?, ?> h = affectedFields[i];
                af[i] = h.getField();
            }
        }
        return addErr(errField.getField(), msg, af);
    }

    protected final ValidateFormError addErr(IsField<?> errField, String msg, IsField<?>... affectedFields) {
        ValidateFormError err = new ValidateFormError(msg, errField);
        errors.add(err);
        err.addAffectedFields(affectedFields);
        return err;
    }

    protected final String getErrMsg(int num, Object... args) {
        String templ = messages.get(num);
        if (templ == null || templ.length() == 0) {
            throw new ApplicationException(SBFExceptionStr.errorNotFound, Integer.toString(num));
        }
        return args != null && args.length > 0 ? CliUtil.format(templ, args) : templ;
    }

}
