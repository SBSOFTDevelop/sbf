package ru.sbsoft.client.components.field;

import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.i18n.shared.DateTimeFormat;
import com.sencha.gxt.cell.core.client.form.DateCell;
import com.sencha.gxt.core.client.dom.XElement;
import com.sencha.gxt.core.client.util.DateWrapper;
import com.sencha.gxt.messages.client.DefaultMessages;
import com.sencha.gxt.widget.core.client.DatePicker;
import com.sencha.gxt.widget.core.client.event.CollapseEvent;
import com.sencha.gxt.widget.core.client.event.CollapseEvent.CollapseHandler;
import com.sencha.gxt.widget.core.client.event.CollapseEvent.HasCollapseHandlers;
import com.sencha.gxt.widget.core.client.event.ExpandEvent;
import com.sencha.gxt.widget.core.client.event.ExpandEvent.ExpandHandler;
import com.sencha.gxt.widget.core.client.event.ExpandEvent.HasExpandHandlers;
import com.sencha.gxt.widget.core.client.event.ParseErrorEvent;
import com.sencha.gxt.widget.core.client.form.DateTimePropertyEditor;
import com.sencha.gxt.widget.core.client.form.TriggerField;
import com.sencha.gxt.widget.core.client.form.validator.MaxDateValidator;
import com.sencha.gxt.widget.core.client.form.validator.MinDateValidator;
import java.util.Date;
import ru.sbsoft.common.Strings;

/**
 * Поле с маской для отображения даты или даты и времени.
 *
 * @author balandin
 * @since Sep 15, 2014 1:52:41 PM
 */
public class DateField extends TriggerField<Date> implements HasExpandHandlers, HasCollapseHandlers {

    private MaxDateValidator maxDateValidator;
    private MinDateValidator minDateValidator;
    //
    private final DateConsts format;

    public DateField() {
        this(DateConsts.DATE);
    }

    public DateField(final DateConsts format) {
        this(format, format.EMPTY_TEXT);
    }

    public DateField(final DateConsts format, final String emptyText) {
        super(new DateCell() {
            @Override
            public String getText(XElement parent) {
                String text = getInputElement(parent).getValue();
                if ((text == null) || format.NULL_VALUE.equals(text)) {
                    return Strings.EMPTY;
                }
                final String eText = getEmptyText();
                if (eText != null && eText.equals(text)) {
                    return Strings.EMPTY;
                }
                return text;
            }
        });

        this.format = format;

        setPropertyEditor(new DateTimePropertyEditor(DateTimeFormat.getFormat(format.FORMAT)));
        setClearValueOnParseError(false);
        setAllowBlank(true);
        setEmptyText(emptyText);
        addCollapseHandler(new CollapseHandler() {
            @Override
            public void onCollapse(CollapseEvent event) {
                final DateField field = (DateField) event.getSource();
                field.isCurrentValid(false);
                field.focus();
            }
        });
        redraw();
    }

    @Override
    protected void onRedraw() {
        super.onRedraw();
        MaskUtils.mask(this, format.PATTERN, getEmptyText());
    }

    @Override
    public DateCell getCell() {
        return (DateCell) super.getCell();
    }

    public DatePicker getDatePicker() {
        return getCell().getDatePicker();
    }

    @Override
    public DateTimePropertyEditor getPropertyEditor() {
        return (DateTimePropertyEditor) super.getPropertyEditor();
    }

    @Override
    protected void onCellParseError(ParseErrorEvent event) {
        super.onCellParseError(event);
        // forceInvalid(parseError = DefaultMessages.getMessages().dateField_invalidText("", getEmptyText()));

        // super.onCellParseError(event);
        String f = getPropertyEditor().getFormat().getPattern();
        f = f.replace("dd", "ДД");
        f = f.replace("MM", "ММ");
        f = f.replace("yyyy", "ГГГГ");
        f = DefaultMessages.getMessages().dateField_invalidText("", f);

        parseError = f;
        forceInvalid(f);

    }

    @Override
    public HandlerRegistration addExpandHandler(ExpandHandler handler) {
        return addHandler(handler, ExpandEvent.getType());
    }

    @Override
    public HandlerRegistration addCollapseHandler(CollapseHandler handler) {
        return addHandler(handler, CollapseEvent.getType());
    }

    public void setMaxValue(Date maxValue) {
        if (maxDateValidator == null) {
            addValidator(maxDateValidator = new MaxDateValidator(maxValue));
        }
        if (maxValue != null) {
            maxValue = new DateWrapper(maxValue).resetTime().asDate();
            maxDateValidator.setMaxDate(maxValue);
        }
    }

    public void setMinValue(Date minValue) {
        if (minDateValidator == null) {
            addValidator(minDateValidator = new MinDateValidator(minValue));
        }
        if (minValue != null) {
            minValue = new DateWrapper(minValue).resetTime().asDate();
            minDateValidator.setMinDate(minValue);
        }
    }

    public Date getMaxValue() {
        if (maxDateValidator != null) {
            return maxDateValidator.getMaxDate();
        }
        return null;
    }

    public Date getMinValue() {
        if (minDateValidator != null) {
            minDateValidator.getMinDate();
        }
        return null;
    }
}
