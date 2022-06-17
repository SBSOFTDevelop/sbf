package ru.sbsoft.client.components.field;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.dom.client.Document;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.editor.client.EditorError;
import com.google.gwt.event.dom.client.ChangeEvent;
import ru.sbsoft.svc.widget.core.client.event.BlurEvent;
import ru.sbsoft.svc.widget.core.client.event.FocusEvent;
import ru.sbsoft.svc.widget.core.client.form.TextField;
import ru.sbsoft.svc.widget.core.client.form.Validator;
import ru.sbsoft.svc.widget.core.client.form.error.DefaultEditorError;
import java.util.ArrayList;
import java.util.List;
import ru.sbsoft.client.components.AllowBlankControl;
import ru.sbsoft.common.Strings;

/**
 * Поле ввода с маской.
 * @see ru.sbsoft.common.validation.INN
 * @see ru.sbsoft.common.validation.KPP
 * @see ru.sbsoft.common.validation.OGRN
 * @see ru.sbsoft.common.validation.OKPO
 * @see ru.sbsoft.common.validation.SNILS
 * @author balandin
 * @since Sep 11, 2014 5:33:40 PM
 */
//TODO deprecated
public abstract class MaskField extends TextField implements AllowBlankControl {

    private final Validator<String> validator;
    private final String PATTERN;
    private final String NULL_VALUE;
    //
    private boolean formatted = true;
    private boolean isSettingValue = false;
    private String prechangedValue;

    public MaskField(String pattern, String nullValue) {
        this.PATTERN = pattern;
        this.NULL_VALUE = nullValue;

        this.validator = createValidator();

        setClearValueOnParseError(false);
        setAllowBlank(true);

        setEmptyText(NULL_VALUE);
        
        addFocusHandler(new FocusEvent.FocusHandler() {

            @Override
            public void onFocus(FocusEvent event) {
                prechangedValue = getText();
            }
        });
        addBlurHandler(new BlurEvent.BlurHandler() {

            @Override
            public void onBlur(BlurEvent event) {
                if (!prechangedValue.equals(getText())) {
                    ChangeEvent.fireNativeEvent(Document.get().createChangeEvent(), MaskField.this);
                }
            }
        });

    }

    @Override
    protected void onRedraw() {
        super.onRedraw();
        if (formatted) {
            MaskUtils.mask(this, PATTERN, NULL_VALUE);
        }
    }

    public abstract boolean isFormatted(String value);

    public abstract String[] getErrors(String value);

    protected Validator<String> createValidator() {
        return new Validator<String>() {
            @Override
            public List<EditorError> validate(Editor<String> editor, String value) {
                value = getText();
                value = (NULL_VALUE.equals(value)) ? null : value;
                final String testedValue = Strings.clean(value, false);
                if (testedValue == null && isAllowBlank()) {
                    return null;
                }
                final String[] errors = getErrors(testedValue);
                if (errors == null) {
                    return null;
                }
                List<EditorError> result = new ArrayList<EditorError>();
                for (String e : errors) {
                    result.add(new DefaultEditorError(editor, e, value));
                }
                return result;
            }
        };
    }

    @Override
    public void setAllowBlank(boolean allowBlank) {
        super.setAllowBlank(allowBlank);
        getValidators().clear();
        addValidator(validator);
    }

    @Override
    public void setValue(String value, boolean fireEvents, boolean redraw) {
        value = convert(value);

        final String testedValue = Strings.clean(value, false);
        if (testedValue == null) {
            formatted = true;
        } else {
            formatted = isFormatted(testedValue);
        }

        if (formatted) {
            if (PATTERN != null) {
                MaskUtils.mask(this, PATTERN, NULL_VALUE);
            }
        } else {
            MaskUtils.unmask(this);
        }

        isSettingValue = true;
        super.setValue(value, fireEvents, redraw);
        isSettingValue = false;

        if (!formatted) {
            Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {

                @Override
                public void execute() {
                    validate();
                }
            });
        }
    }

    protected boolean testNumber(String value) {
        if (value == null) {
            return false;
        }
        return testNumber(value, value.length());
    }

    protected boolean testNumber(String value, int length) {
        if (value == null) {
            return false;
        }
        if (value.length() != length) {
            return false;
        }
        for (int i = 0; i < length; i++) {
            final char c = value.charAt(i);
            if (!Strings.isDigit(c) && c != '_') {
                return false;
            }
        }
        return true;
    }

    @Override
    public String getCurrentValue() {
        return unconvert(super.getCurrentValue());
    }

    @Override
    public String getValue() {
        return isSettingValue ? super.getValue() : unconvert(super.getValue());
    }

    /**
     *
     *
     *
     * @param value может быть NULL !!!
     * @return
     */
    protected String convert(String value) {
        return value;
    }

    protected String unconvert(String value) {
        return value;
    }
}
