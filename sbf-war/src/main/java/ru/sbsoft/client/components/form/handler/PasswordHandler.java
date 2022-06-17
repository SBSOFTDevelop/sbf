package ru.sbsoft.client.components.form.handler;

import ru.sbsoft.svc.widget.core.client.form.PasswordField;
import ru.sbsoft.svc.widget.core.client.form.validator.MaxLengthValidator;
import ru.sbsoft.svc.widget.core.client.form.validator.MinLengthValidator;
import ru.sbsoft.shared.FilterInfo;
import ru.sbsoft.shared.filter.StringFilterInfo;
import ru.sbsoft.shared.param.ParamInfo;
import ru.sbsoft.shared.param.StringParamInfo;

/**
 *
 * @author Kiselev
 * @param <SelfType>
 */
public class PasswordHandler<SelfType extends PasswordHandler<SelfType>> extends ValFieldBaseHandler<PasswordField, String, SelfType> {

    public PasswordHandler(String name, String label) {
        super(name, label);
    }

    public SelfType setMaxLen(int len) {
        getField().addValidator(new MaxLengthValidator(len));
        return (SelfType) this;
    }

    public SelfType setMinLen(int len) {
        getField().addValidator(new MinLengthValidator(len));
        return (SelfType) this;
    }

    public SelfType setLenRange(int minLen, int maxLen) {
        setMinLen(minLen);
        return setMaxLen(maxLen);
    }

    @Override
    protected PasswordField createField() {
        return new PasswordField();
    }

    @Override
    protected FilterInfo createFilter() {
        return new StringFilterInfo(null, getVal());
    }

    @Override
    protected ParamInfo createParamInfo() {
        return new StringParamInfo(null, getVal());
    }
}
