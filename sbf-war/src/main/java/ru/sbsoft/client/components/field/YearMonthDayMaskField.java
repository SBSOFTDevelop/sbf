package ru.sbsoft.client.components.field;

import com.google.gwt.i18n.shared.DateTimeFormat;
import ru.sbsoft.svc.cell.core.client.form.YearMonthDayCell;
import ru.sbsoft.svc.cell.core.client.form.YearMonthDayEditor;
import ru.sbsoft.svc.cell.core.client.form.YearMonthDayField;
import ru.sbsoft.svc.core.client.dom.XElement;
import ru.sbsoft.svc.widget.core.client.event.CollapseEvent;
import ru.sbsoft.svc.widget.core.client.form.TriggerField;
import ru.sbsoft.common.Strings;

/**
 *
 * @author Kiselev
 */
public class YearMonthDayMaskField extends YearMonthDayField {

    private final YearMonthDayFormat format;

    public YearMonthDayMaskField() {
        this(YearMonthDayFormat.DATE_SHORT);
    }

    public YearMonthDayMaskField(final YearMonthDayFormat format) {
        this(format, format.getEmptyText());
    }

    public YearMonthDayMaskField(final YearMonthDayFormat format, final String emptyText) {
        super(new YearMonthDayCell() {
            @Override
            public String getText(XElement parent) {
                String text = getInputElement(parent).getValue();
                if ((text == null) || format.getNullValue().equals(text)) {
                    return Strings.EMPTY;
                }
                final String eText = getEmptyText();
                if (eText != null && eText.equals(text)) {
                    return Strings.EMPTY;
                }
                return text;
            }
        }, new YearMonthDayEditor(DateTimeFormat.getFormat(format.getFormat())));
        this.format = format;
        setClearValueOnParseError(false);
        setAllowBlank(true);
        setEmptyText(emptyText);
        addCollapseHandler(new CollapseEvent.CollapseHandler() {
            @Override
            public void onCollapse(CollapseEvent event) {
                final TriggerField  field = (TriggerField) event.getSource();
                field.isCurrentValid(false);
                field.focus();
            }
        });
        redraw();
    }

    @Override
    protected void onRedraw() {
        super.onRedraw();
        MaskUtils.mask(this, format.getPattern(), getEmptyText());
    }

}
