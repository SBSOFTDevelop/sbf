package ru.sbsoft.client.components.form.handler;

import com.google.gwt.event.dom.client.ChangeHandler;
import ru.sbsoft.sbf.gxt.components.CheckBoxField;
import ru.sbsoft.shared.FilterInfo;
import ru.sbsoft.shared.filter.BooleanFilterInfo;
import ru.sbsoft.shared.param.BooleanParamInfo;
import ru.sbsoft.shared.param.ParamInfo;

/**
 *
 * @author Kiselev
 * @param <SelfType>
 */
public class CheckHandler<SelfType extends CheckHandler<SelfType>> extends BaseHandler<CheckBoxField, Boolean, SelfType> {

    public CheckHandler(String name, String label) {
        super(name, label);
    }

    @Override
    protected CheckBoxField createField() {
        return new CheckBoxField();
    }

    @Override
    protected FilterInfo createFilter() {
        return new BooleanFilterInfo(null, getVal());
    }

    @Override
    protected ParamInfo createParamInfo() {
        return new BooleanParamInfo(null, getVal());
    }

    @Override
    public SelfType setVal(Boolean val) {
        return super.setVal(val != null ? val : Boolean.FALSE);
    }

    @Override
    public SelfType setReq(boolean required) {
        return (SelfType) this; //do nothing becouse check value is always present
    }

    @Override
    public SelfType setRO(boolean readOnly) {
        getField().setReadOnly(readOnly);
        return (SelfType) this;
    }

    @Override
    public boolean isRO() {
        return getField().getCell().isReadOnly();
    }

    public SelfType addChangeHandler(ChangeHandler h) {
        getField().addChangeHandler(h);
        return (SelfType) this;
    }
}
