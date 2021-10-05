package ru.sbsoft.client.components.validators;

import com.google.gwt.editor.client.Editor;
import com.google.gwt.editor.client.EditorError;
import com.sencha.gxt.widget.core.client.form.Validator;
import com.sencha.gxt.widget.core.client.form.error.DefaultEditorError;
import java.util.ArrayList;
import java.util.List;
import ru.sbsoft.client.I18n;
import ru.sbsoft.common.Strings;
import ru.sbsoft.shared.api.i18n.consts.SBFEditorStr;

/**
 * Проверка значения текстового поля ввода.
 * @see com.sencha.gxt.widget.core.client.form.Field#addValidator(com.sencha.gxt.widget.core.client.form.Validator) 
 * @author balandin
 * @since Jun 21, 2013 6:37:11 PM
 */
public class TextValidator implements Validator<String> {

    private final boolean allowBlank;
    private final boolean digitalOnly;
    private final int minLength;
    private final int maxLength;
    private List<EditorError> errors;

    public TextValidator(boolean allowBlank, boolean digitalOnly, int minLength, int maxLength) {
        if ((minLength <= 0)
                || (maxLength <= 0)
                || (maxLength < minLength)) {
            throw new IllegalArgumentException("");
        }

        this.allowBlank = allowBlank;
        this.digitalOnly = digitalOnly;
        this.minLength = minLength;
        this.maxLength = maxLength;
    }

    @Override
    public List<EditorError> validate(Editor<String> editor, String value) {

        while (true) {

            if (Strings.isEmpty(value, false)) {
                if (!allowBlank) {
                    addError(editor, I18n.get(SBFEditorStr.msgNeedToFill));
                }
                break;
            }

            if (digitalOnly && !Strings.isDigit(value)) {
                addError(editor, I18n.get(SBFEditorStr.msgInvalidCharacters));
            }

            if (minLength == maxLength) {
                if (value.length() != maxLength) {
                    addError(editor, I18n.get(SBFEditorStr.msgReguiredLength) + ": " + maxLength);
                }
            } else {
                if (value.length() < minLength) {
                    addError(editor, I18n.get(SBFEditorStr.msgMinLength) + ": " + maxLength);
                } else if (value.length() > maxLength) {
                    addError(editor, I18n.get(SBFEditorStr.msgMaxLength) + ": " + maxLength);
                }
            }

            break;
        }


        List<EditorError> result = errors;
        errors = null;
        return result;
    }

    private void addError(Editor<String> editor, String message) {
        if (errors == null) {
            errors = new ArrayList<EditorError>();
        }
        errors.add(new DefaultEditorError(editor, message, null));
    }
}
