package ru.sbsoft.client.components.form;

import ru.sbsoft.sbf.gxt.utils.FieldUtils;
import com.google.gwt.event.logical.shared.HasValueChangeHandlers;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.sencha.gxt.widget.core.client.form.IntegerField;
import com.sencha.gxt.widget.core.client.form.validator.MinNumberValidator;
import java.util.ArrayList;
import java.util.List;
import ru.sbsoft.client.I18n;
import ru.sbsoft.client.components.AllowBlankControl;
import ru.sbsoft.sbf.gxt.components.FieldsContainer;
import ru.sbsoft.client.components.form.fields.ReadOnlyControl;
import ru.sbsoft.client.components.form.model.IntStrComboBoxModel;
import ru.sbsoft.client.utils.HLC;
import ru.sbsoft.shared.api.i18n.consts.SBFExceptionStr;
import ru.sbsoft.shared.model.IYearMonth;
import ru.sbsoft.shared.model.YearMonth;

/**
 *
 * @author Kiselev
 * @param <T>
 */
public abstract class AbstractYearMonthField<T extends IYearMonth> extends CustomField<T> implements HasValueChangeHandlers<T>, AllowBlankControl, ReadOnlyControl {

    private final IntegerField year;
    private final IntStrComboBox month;
    private final List<ValueChangeHandler<T>> changeHandlers = new ArrayList<ValueChangeHandler<T>>();
    private T minVal = null;
    private T maxVal = null;

    public AbstractYearMonthField() {
        year = new IntegerField();
        year.addValidator(new MinNumberValidator<Integer>(YearMonth.MIN_YEAR));
        year.setAutoValidate(true);
        year.addValueChangeHandler(new ChInt());

        month = new IntStrComboBox();
        month.setAutoValidate(true);
        month.addValueChangeHandler(new ChIs());

        FieldsContainer c = new FieldsContainer();
        c.add(year, HLC.CONST);
        c.add(FieldUtils.createSeparator(), HLC.CONST);
        c.add(month, HLC.CONST);

        setWidget(c);
    }
    
    protected IntStrComboBox getMonthField(){
        return month;
    }
    
    public AbstractYearMonthField setMin(T ym) {
        this.minVal = ym;
        return this;
    }

    public AbstractYearMonthField setMax(T ym) {
        this.maxVal = ym;
        return this;
    }

    public AbstractYearMonthField setRange(T ymMin, T ymMax) {
        setMin(ymMin);
        return setMax(ymMax);
    }

    @Override
    public void setValue(T value) {
        IntStrComboBoxModel mod = null;
        if (value != null) {
            Integer mon = value.getMonth();
            for (IntStrComboBoxModel mod2 : month.getStore().getAll()) {
                if (mon.equals(mod2.getId())) {
                    mod = mod2;
                    break;
                }
            }
        }
        month.setValue(mod);

        year.setValue(value != null ? value.getYear() : null);

        fireChanged();
    }

    @Override
    public T getValue() {
        if (year.getCurrentValue() == null || month.getCurrentValue() == null) {
            return null;
        }
        return createNewValue(year.getCurrentValue(), month.getCurrentValue().getId());
    }
    
    protected abstract T createNewValue(int year, int month);

    @Override
    public boolean isReadOnly() {
        return year.isReadOnly() || month.isReadOnly();
    }

    @Override
    public void setReadOnly(boolean readOnly) {
        year.setReadOnly(readOnly);
        month.setReadOnly(readOnly);
    }

    @Override
    public HandlerRegistration addValueChangeHandler(ValueChangeHandler<T> handler) {
        if (!changeHandlers.contains(handler)) {
            changeHandlers.add(handler);
        }
        return new HR(handler);
    }

    @Override
    public void setAllowBlank(boolean value) {
        year.setAllowBlank(value);
        month.setAllowBlank(value);
    }

    @Override
    public boolean isAllowBlank() {
        return year.isAllowBlank() && month.isAllowBlank();
    }

    @Override
    public boolean validate(boolean preventMark) {
        boolean res = year.validateCurrent(preventMark) & month.validateCurrent(preventMark);
        if (res) {
            T val = getValue();
            if(minVal != null && val != null && minVal.compareTo(val) > 0){
                markInvalid(I18n.get(SBFExceptionStr.valueNotLess, minVal.toString()));
                res = false;
            }
            if(res && maxVal != null && val != null && maxVal.compareTo(val) < 0){
                markInvalid(I18n.get(SBFExceptionStr.valueNotMore, maxVal.toString()));
                res = false;
            }
        }
        return res;
    }

    @Override
    public boolean isValid(boolean preventMark) {
        return validate(preventMark);
    }

    @Override
    public void markInvalid(String msg) {
        year.markInvalid(msg);
        month.markInvalid(msg);
    }

    @Override
    public void clearInvalid() {
        super.clearInvalid();
        year.clearInvalid();
        month.clearInvalid();
    }

    @Override
    public void clear() {
        super.clear();
        year.clear();
        month.clear();
    }

    private void fireChanged() {
        ValueChangeEvent<T> e = new ValueChangeEvent<T>(getValue()) {
        };
        for (ValueChangeHandler<T> h : changeHandlers) {
            h.onValueChange(e);
        }
    }

    private class ChInt implements ValueChangeHandler<Integer> {

        @Override
        public void onValueChange(ValueChangeEvent<Integer> event) {
            fireChanged();
        }

    }

    private class ChIs implements ValueChangeHandler<IntStrComboBoxModel> {

        @Override
        public void onValueChange(ValueChangeEvent<IntStrComboBoxModel> event) {
            fireChanged();
        }

    }

    private class HR implements HandlerRegistration {

        private final ValueChangeHandler<T> h;

        public HR(ValueChangeHandler<T> h) {
            this.h = h;
        }

        @Override
        public void removeHandler() {
            changeHandlers.remove(h);
        }

    }

}
