package ru.sbsoft.client.components.form.handler;

import ru.sbsoft.client.components.form.HtmlEditField;
import ru.sbsoft.shared.FilterInfo;
import ru.sbsoft.shared.param.ParamInfo;

/**
 *
 * @author sychugin
 * @param <SelfType>
 */
public class HtmlTextHandler<SelfType extends HtmlTextHandler<SelfType>> extends  BaseHandler<HtmlEditField, String, SelfType>{

    public HtmlTextHandler(String name, String label) {
        super(name, label);
    }

    @Override
    protected HtmlEditField createField() {
        return new HtmlEditField();
    }

    @Override
    public SelfType setVal(String val) {
        getField().setValue(val);
        return (SelfType) this;
    }

    @Override
    public String getVal() {
        return  getField().getValue();
    }

    @Override
    public SelfType setRO() {
        getField().setReadOnly(true);
        return (SelfType) this;
    }

    @Override
    public SelfType setReq() {
        return (SelfType) this;
    }

    @Override
    public boolean isRO() {
        return getField().isReadOnly();
    }

    @Override
    protected FilterInfo createFilter() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    protected ParamInfo createParamInfo() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
