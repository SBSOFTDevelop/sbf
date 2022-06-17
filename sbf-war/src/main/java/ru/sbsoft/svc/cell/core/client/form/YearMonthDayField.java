package ru.sbsoft.svc.cell.core.client.form;

import com.google.gwt.editor.client.Editor;
import com.google.gwt.editor.client.EditorError;
import com.google.gwt.event.shared.HandlerRegistration;
import ru.sbsoft.svc.core.client.util.DateWrapper;
import ru.sbsoft.svc.messages.client.DefaultMessages;
import ru.sbsoft.svc.widget.core.client.DatePicker;
import ru.sbsoft.svc.widget.core.client.event.*;
import ru.sbsoft.svc.widget.core.client.form.*;
import ru.sbsoft.svc.widget.core.client.form.validator.MaxDateValidator;
import ru.sbsoft.svc.widget.core.client.form.validator.MinDateValidator;
import java.util.Date;
import java.util.List;
import ru.sbsoft.sbf.app.model.YearMonthDay;

/**
 *
 * @author Fedor Resnyanskiy
 */
public class YearMonthDayField extends TriggerField<YearMonthDay> implements ExpandEvent.HasExpandHandlers, CollapseEvent.HasCollapseHandlers {

    private MaxDateValidator maxDateValidator;
    private MinDateValidator minDateValidator;

    /**
     * Creates a new date field.
     */
    public YearMonthDayField() {
        this(new YearMonthDayEditor());
    }

    /**
     * Creates a new date field.
     *
     * @param cell the date cell
     */
    public YearMonthDayField(YearMonthDayCell cell) {
        this(cell, new YearMonthDayEditor());
    }

    /**
     * Creates a new date field.
     *
     * @param cell the date cell
     * @param propertyEditor the property editor
     */
    public YearMonthDayField(YearMonthDayCell cell, YearMonthDayEditor propertyEditor) {
        super(cell);
        setPropertyEditor(propertyEditor);
        redraw();
    }

    /**
     * Creates a new date field.
     *
     * @param propertyEditor the property editor
     */
    public YearMonthDayField(YearMonthDayEditor propertyEditor) {
        this(new YearMonthDayCell(), propertyEditor);
    }

    @Override
    public YearMonthDayCell getCell() {
        return (YearMonthDayCell) super.getCell();
    }

    /**
     * Returns the field's date picker.
     *
     * @return the date picker
     */
    public DatePicker getDatePicker() {
        return getCell().getDatePicker();
    }

    /**
     * Returns the field's max value.
     *
     * @return the max value
     */
    public Date getMaxValue() {
        if (maxDateValidator != null) {
            return maxDateValidator.getMaxDate();
        }
        return null;
    }

    /**
     * Returns the field's minimum value.
     *
     * @return the minimum value
     */
    public Date getMinValue() {
        if (minDateValidator != null) {
            minDateValidator.getMinDate();
        }
        return null;
    }

    @Override
    public YearMonthDayEditor getPropertyEditor() {
        return (YearMonthDayEditor) super.getPropertyEditor();
    }

    /**
     * Sets the field's max value.
     *
     * @param maxValue the max value
     */
    public void setMaxValue(Date maxValue) {
        if (maxDateValidator == null) {
            maxDateValidator = new MaxDateValidator(maxValue);
            addValidator(wrap(maxDateValidator));
        }
        if (maxValue != null) {
            maxValue = new DateWrapper(maxValue).resetTime().asDate();
            maxDateValidator.setMaxDate(maxValue);
        }
    }

    /**
     * The maximum date allowed.
     *
     * @param minValue the max value
     */
    public void setMinValue(Date minValue) {
        if (minDateValidator == null) {
            minDateValidator = new MinDateValidator(minValue);
            addValidator(wrap(minDateValidator));
        }
        if (minValue != null) {
            minValue = new DateWrapper(minValue).resetTime().asDate();
            minDateValidator.setMinDate(minValue);
        }
    }

    private static Validator<YearMonthDay> wrap(final Validator<Date> validator) {
        return new Validator<YearMonthDay>() {
            @Override
            public List<EditorError> validate(Editor<YearMonthDay> editor, YearMonthDay value) {
                return validator.validate(null, SvcYearMonthDayConverter.convert(value));
            }
        };
    }

    protected void expand() {
        getCell().expand(createContext(), getElement(), getValue(), valueUpdater);
    }

//  @Override
//  protected void onCellParseError(ParseErrorEvent event) {
//    super.onCellParseError(event);
//    String value = event.getException().getMessage();
//    String f = getPropertyEditor().getFormat().getPattern();
//    String msg = DefaultMessages.getMessages().dateField_invalidText(value, f);
//    parseError = msg;
//    forceInvalid(msg);
//  }
    // SBSOFT
    @Override
    protected void onCellParseError(ParseErrorEvent event) {
        super.onCellParseError(event);

        String f = getPropertyEditor().getFormat().getPattern();
        f = f.replace("dd", "ДД");
        f = f.replace("MM", "ММ");
        f = f.replace("yyyy", "ГГГГ");
        f = DefaultMessages.getMessages().dateField_invalidText("", f);

        parseError = f;
        forceInvalid(f);
    }

    @Override
    public final HandlerRegistration addExpandHandler(ExpandEvent.ExpandHandler handler) {
        return addHandler(handler, ExpandEvent.getType());
    }

    @Override
    public final HandlerRegistration addCollapseHandler(CollapseEvent.CollapseHandler handler) {
        return addHandler(handler, CollapseEvent.getType());
    }

}
