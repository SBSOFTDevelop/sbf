package ru.sbsoft.client.components.form.handler;

import com.google.gwt.editor.client.Editor;
import com.google.gwt.editor.client.EditorError;
import com.sencha.gxt.widget.core.client.form.TextField;
import com.sencha.gxt.widget.core.client.form.error.DefaultEditorError;
import com.sencha.gxt.widget.core.client.form.validator.AbstractValidator;
import com.sencha.gxt.widget.core.client.form.validator.MaxLengthValidator;
import com.sencha.gxt.widget.core.client.form.validator.MinLengthValidator;
import java.util.List;
import ru.sbsoft.client.components.form.TrimTextField;
import ru.sbsoft.common.Strings;
import ru.sbsoft.shared.FilterInfo;
import ru.sbsoft.shared.filter.StringFilterInfo;
import ru.sbsoft.shared.param.ParamInfo;
import ru.sbsoft.shared.param.StringParamInfo;

/**
 *
 * @author Kiselev
 * @param <SelfType>
 */
public class TextHandler<SelfType extends TextHandler<SelfType>> extends ValFieldBaseHandler<TextField, String, SelfType> {

    public TextHandler(String name, String label) {
        super(name, label);
    }

    public SelfType setDigitOnly() {
        getField().addValidator(new DigitOnlyValidator());
        return (SelfType) this;
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

    public SelfType setFixLen(int len) {
        return setLenRange(len, len);
    }

    @Override
    protected TextField createField() {
        return new TrimTextField();
    }

    @Override
    protected FilterInfo createFilter() {
        return new StringFilterInfo(null, getVal());
    }

    @Override
    protected ParamInfo createParamInfo() {
        return new StringParamInfo(null, getVal());
    }

    private class DigitOnlyValidator extends AbstractValidator<String> {

        @Override
        public List<EditorError> validate(Editor<String> field, String value) {
            if (value != null && !Strings.isDigit(value)) {
                return createError(new DefaultEditorError(field, "Допустимы только цифры", value));
            }
            return null;
        }
    }
}
