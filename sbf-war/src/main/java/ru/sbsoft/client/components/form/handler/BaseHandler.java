package ru.sbsoft.client.components.form.handler;

import com.google.gwt.user.client.TakesValue;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.Widget;
import ru.sbsoft.svc.widget.core.client.Component;
import ru.sbsoft.svc.widget.core.client.form.AdapterField;
import ru.sbsoft.svc.widget.core.client.form.Field;
import ru.sbsoft.svc.widget.core.client.form.FieldLabel;
import ru.sbsoft.svc.widget.core.client.form.Validator;
import ru.sbsoft.svc.widget.core.client.form.ValueBaseField;
import java.util.List;
import ru.sbsoft.client.components.AllowBlankControl;
import ru.sbsoft.client.components.form.IntStrComboBox;
import ru.sbsoft.client.components.form.StrStrComboBox;
import ru.sbsoft.client.components.form.fields.ReadOnlyControl;
import ru.sbsoft.shared.ComparisonEnum;
import ru.sbsoft.shared.FilterInfo;
import ru.sbsoft.shared.param.ParamInfo;

/**
 *
 * @author Kiselev
 * @param <F>
 * @param <V>
 * @param <SelfType>
 */
public abstract class BaseHandler<F extends Component, V, SelfType extends BaseHandler<F, V, SelfType>> implements IFieldHandler<F, V, SelfType> {

    private String label;
    private F field;
    private Widget widget;
    private final String name;
    private boolean paramGen = true;
    private ComparisonEnum comparison = null;

    protected BaseHandler(String name, String label) {
        this.label = label;
        this.name = name;
    }

    @Override
    public boolean isParamGen() {
        return paramGen;
    }

    @Override
    public SelfType setParamGen(boolean b) {
        this.paramGen = b;
        return (SelfType) this;
    }

    @Override
    public F getField() {
        if (field == null) {
            field = createField();
            if (field instanceof Field) {
                ((Field) field).setAutoValidate(true);
            }
            if (field instanceof StrStrComboBox) {
                ((StrStrComboBox) field).setEditable(true);
            } else if (field instanceof IntStrComboBox) {
                ((IntStrComboBox) field).setEditable(true);
            }
            try {
                setReq(false);
            } catch (UnsupportedOperationException ex) {
                //это необязательное действие - ничего не делаем
            }
        }
        return field;
    }

    @Override
    public SelfType setReq() {
        return setReq(true);
    }
    
    @Override
    public SelfType setReq(boolean required) {
        if (getField() instanceof ValueBaseField) {
            ((ValueBaseField) getField()).setAllowBlank(!required);
            ((ValueBaseField) getField()).clearInvalid();
        } else if (getField() instanceof AllowBlankControl) {
            ((AllowBlankControl) getField()).setAllowBlank(!required);
        } else {
            throw getUnsuppFieldErr("setReq");
        }
        return (SelfType) this;
    }

    @Override
    public SelfType setToolTip(String s) {
        if (getField() != null) {
            getField().setToolTip(s);
        }
        return (SelfType) this;
    }

    @Override
    public SelfType setRO() {
        return setRO(true);
    }
    
    @Override
    public SelfType setRO(boolean readOnly) {
        if (getField() instanceof ReadOnlyControl) {
            ((ReadOnlyControl) getField()).setReadOnly(readOnly);
        } else if (getField() instanceof ValueBaseField) {
            ((ValueBaseField) getField()).setReadOnly(readOnly);
        } else {
            throw getUnsuppFieldErr("setRO");
        }
        return (SelfType) this;
    }

    @Override
    public boolean isRO() {
        if (getField() instanceof ReadOnlyControl) {
            return ((ReadOnlyControl) getField()).isReadOnly();
        } else if (getField() instanceof ValueBaseField) {
            return ((ValueBaseField) getField()).isReadOnly();
        } else {
            throw getUnsuppFieldErr("isRO");
        }
    }

    @Override
    public void setEnabled(boolean enabled) {
        getField().setEnabled(enabled);
    }

    @Override
    public String getLabel() {
        return label;
    }

    public SelfType setLabel(String label) {
        this.label = label;
        return (SelfType) this;
    }

    @Override
    public String getName() {
        if (name == null) {
            throw new IllegalStateException("Filter/Param Name is not set (is null)");
        }
        return name;
    }

    @Override
    public V getVal() {
        if (getField() instanceof TakesValue) {
            return ((TakesValue<V>) getField()).getValue();
        }
        throw getUnsuppFieldErr("getVal");
    }

    protected String getHumanValRaw() {
        F f = getField();
        if (f instanceof HasText) {
            return ((HasText) f).getText();
        } else {
            V val = getVal();
            return val != null ? val.toString() : "";
        }
    }

    @Override
    public String getHumanVal() {
        String s = getHumanValRaw();
        return s != null ? s.trim() : "";
    }

    @Override
    public SelfType setVal(V val) {
        if (getField() instanceof HasValue) {
            ((HasValue<V>) getField()).setValue(val, true);
        } else if (getField() instanceof TakesValue) {
            ((TakesValue<V>) getField()).setValue(val);
        } else {
            throw getUnsuppFieldErr("setVal");
        }
        return (SelfType) this;
    }

    private UnsupportedOperationException getUnsuppFieldErr(String methodName) {
        return new UnsupportedOperationException("Current field type is unsupported by this base method:\n '" + methodName + "' -> " + (getField() != null ? getField().getClass() : "null"));
    }

    @Override
    public SelfType addValidator(Validator<V> validator) {
        if (getField() instanceof Field) {
            ((Field) getField()).addValidator(validator);
        } else if (getField() instanceof AdapterField) {
            ((AdapterField) getField()).addValidator(validator);
        } else {
            throw getUnsuppFieldErr("addValidator");
        }
        return (SelfType) this;
    }

    protected void removeValidator(Validator<V> validator) {
        if (getField() instanceof Field) {
            ((Field) getField()).removeValidator(validator);
        } else if (getField() instanceof AdapterField) {
            ((AdapterField) getField()).removeValidator(validator);
        } else {
            throw getUnsuppFieldErr("removeValidator");
        }
    }

    @Override
    public List<Validator<V>> getValidators() {
        if (getField() instanceof Field) {
            return ((Field) getField()).getValidators();
        } else if (getField() instanceof AdapterField) {
            return ((AdapterField) getField()).getValidators();
        } else {
            throw getUnsuppFieldErr("getValidators");
        }
    }

    @Override
    public Widget getWidget() {
        if (widget == null) {
            widget = label != null ? new FieldLabel(getField(), label) : getField();
        }
        return widget;
    }

    @Override
    public SelfType setVisible(boolean b) {
        getWidget().setVisible(b);
        return (SelfType) this;
    }

    @Override
    public boolean isEmpty() {
        V val = getVal();
        return (val == null) || ((val instanceof String) && ((String) val).trim().isEmpty());
    }

    @Override
    public final boolean setFilterValue(FilterInfo config) {
        if (getName().equals(config.getColumnName()) && null != config.getValue()) {
            setFilter(config);
            return true;
        }
        return false;
    }

    @Override
    public final FilterInfo getFilter() {
        FilterInfo fi = createFilter();
        if (fi != null) {
            if (fi.getColumnName() == null) {
                fi.setColumnName(getName());
            }
            if (this.comparison != null) {
                fi.setComparison(comparison);
            } else if (fi.getComparison() == null) {
                fi.setComparison(ComparisonEnum.eq);
            }
        }
        return fi;
    }

    @Override
    public SelfType setComparison(ComparisonEnum c) {
        this.comparison = c;
        return (SelfType) this;
    }

    protected abstract F createField();

    protected void setFilter(FilterInfo config) {
        setVal((V) config.getValue());
    }

    protected abstract FilterInfo createFilter();

    @Override
    public ParamInfo getParam() {
        if (paramGen) {
            ParamInfo inf = createParamInfo();
            inf.setName(getName());
            return inf;
        }
        return null;
    }

    protected abstract ParamInfo createParamInfo();

    @Override
    public String toString() {
        String clz = getClass().getName();
        return new StringBuilder(clz.length() + len(label) + len(name) + 5).append(clz).append('{').append(label).append(", ").append(name).append('}').toString();
    }

    private int len(String s) {
        return s != null ? s.length() : 4;
    }

    //======================= ru.sbsoft.client.components.browser.filter.editor.FilterEditor compatibility functions ================
    @Override
    public F getComponent() {
        return getField();
    }

    @Override
    public void setFilterInfo(FilterInfo filterInfo) {
        setFilterValue(filterInfo);
    }

    @Override
    public FilterInfo getFilterInfo() {
        return getFilter();
    }
    //=============================================

}
