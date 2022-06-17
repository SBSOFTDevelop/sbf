package ru.sbsoft.svc.cell.core.client.form;

import java.text.ParseException;
import java.util.Date;

import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import ru.sbsoft.svc.cell.core.client.form.DateCell.DateCellAppearance;
import ru.sbsoft.svc.core.client.SVC;
import ru.sbsoft.svc.core.client.Style.Anchor;
import ru.sbsoft.svc.core.client.Style.AnchorAlignment;
import ru.sbsoft.svc.core.client.dom.XElement;
import ru.sbsoft.svc.widget.core.client.DatePicker;
import ru.sbsoft.svc.widget.core.client.event.CollapseEvent;
import ru.sbsoft.svc.widget.core.client.event.CollapseEvent.CollapseHandler;
import ru.sbsoft.svc.widget.core.client.event.CollapseEvent.HasCollapseHandlers;
import ru.sbsoft.svc.widget.core.client.event.ExpandEvent;
import ru.sbsoft.svc.widget.core.client.event.ExpandEvent.ExpandHandler;
import ru.sbsoft.svc.widget.core.client.event.ExpandEvent.HasExpandHandlers;
import ru.sbsoft.svc.widget.core.client.event.HideEvent;
import ru.sbsoft.svc.widget.core.client.event.HideEvent.HideHandler;
import ru.sbsoft.svc.widget.core.client.menu.DateMenu;
import ru.sbsoft.sbf.app.model.YearMonthDay;

/**
 *
 * @author Fedor Resnyanskiy
 */
public class YearMonthDayCell extends TriggerFieldCell<YearMonthDay> implements HasExpandHandlers, HasCollapseHandlers {

    private DateMenu menu;
    private boolean expanded;

    /**
     * Creates a new date cell.
     */
    public YearMonthDayCell() {
        this(GWT.<DateCellAppearance>create(DateCellAppearance.class));
    }

    /**
     * Creates a new date cell.
     *
     * @param appearance the date cell appearance
     */
    public YearMonthDayCell(DateCellAppearance appearance) {
        super(appearance);
        setPropertyEditor(new YearMonthDayEditor());
    }

    @Override
    public HandlerRegistration addCollapseHandler(CollapseHandler handler) {
        return addHandler(handler, CollapseEvent.getType());
    }

    @Override
    public HandlerRegistration addExpandHandler(ExpandHandler handler) {
        return addHandler(handler, ExpandEvent.getType());
    }

    public void collapse(final Context context, final XElement parent) {
        if (!expanded) {
            return;
        }

        expanded = false;

        menu.hide();
        fireEvent(context, new CollapseEvent(context));
    }

    public void expand(final Context context, final XElement parent, YearMonthDay value, ValueUpdater<YearMonthDay> valueUpdater) {
        if (expanded) {
            return;
        }

        this.expanded = true;

        // expand may be called without the cell being focused
        // saveContext sets focusedCell so we clear if cell 
        // not currently focused
        boolean focused = focusedCell != null;
        saveContext(context, parent, null, valueUpdater, value);
        if (!focused) {
            focusedCell = null;
        }

        DatePicker picker = getDatePicker();

        YearMonthDay d;
        try {
            d = getPropertyEditor().parse(getText(parent));
        } catch (ParseException e) {
            d = value == null ? null : value;
        }

        picker.setValue(d != null ? SvcYearMonthDayConverter.convert(d) : new Date(), false);

        // handle case when down arrow is opening menu
        Scheduler.get().scheduleDeferred(new ScheduledCommand() {

            @Override
            public void execute() {
                menu.show(parent, new AnchorAlignment(Anchor.TOP_LEFT, Anchor.BOTTOM_LEFT, true));
                menu.getDatePicker().focus();

                fireEvent(context, new ExpandEvent(context));
            }
        });

    }

    /**
     * Returns the cell's date picker.
     *
     * @return the date picker
     */
    public DatePicker getDatePicker() {
        if (menu == null) {
            menu = new DateMenu();
            menu.getDatePicker().addValueChangeHandler(new ValueChangeHandler<Date>() {

                @Override
                public void onValueChange(ValueChangeEvent<Date> event) {

                    String s = getPropertyEditor().render(SvcYearMonthDayConverter.convert(event.getValue()));
                    FieldViewData viewData = ensureViewData(lastContext, lastParent);
                    if (viewData != null) {
                        viewData.setCurrentValue(s);
                    }
                    getInputElement(lastParent).setValue(s);
                    getInputElement(lastParent).focus();

                    Scheduler.get().scheduleFinally(new ScheduledCommand() {

                        @Override
                        public void execute() {
                            getInputElement(lastParent).focus();
                        }
                    });

                    menu.hide();
                }
            });
            menu.addHideHandler(new HideHandler() {

                @Override
                public void onHide(HideEvent event) {
                    collapse(lastContext, lastParent);
                }
            });
        }
        return menu.getDatePicker();
    }

    public boolean isExpanded() {
        return expanded;
    }

    @Override
    protected boolean isFocusedWithTarget(Element parent, Element target) {
        boolean result = super.isFocusedWithTarget(parent, target)
                || (menu != null && (menu.getElement().isOrHasChild(target) || menu.getDatePicker().getElement().isOrHasChild(
                target)));
        return result;
    }

    @Override
    protected void onNavigationKey(Context context, Element parent, YearMonthDay value, NativeEvent event,
            ValueUpdater<YearMonthDay> valueUpdater) {
        if (event.getKeyCode() == KeyCodes.KEY_DOWN && !isExpanded()) {
            event.stopPropagation();
            event.preventDefault();
            onTriggerClick(context, parent.<XElement>cast(), event, value, valueUpdater);
        }
    }

    @Override
    protected void onTriggerClick(Context context, XElement parent, NativeEvent event, YearMonthDay value,
            ValueUpdater<YearMonthDay> updater) {
        super.onTriggerClick(context, parent, event, value, updater);
        if (!isReadOnly() && !isDisabled()) {
            // blur is firing after the expand so context info on expand is being cleared
            // when value change fires lastContext and lastParent are null without this code
            if ((SVC.isWebKit()) && lastParent != null && lastParent != parent) {
                getInputElement(lastParent).blur();
            }
            expand(context, parent, value, updater);
        }
    }
}
