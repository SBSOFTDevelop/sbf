package ru.sbsoft.client.components.form.handler.param;

import ru.sbsoft.svc.widget.core.client.form.Validator;
import ru.sbsoft.client.components.form.TextFilterField;
import ru.sbsoft.shared.FilterInfo;
import ru.sbsoft.shared.param.ParamInfo;
import ru.sbsoft.client.components.form.handler.BaseHandler;

/**
 *
 * @author Kiselev
 */
public class TextFilterFieldHandler extends BaseHandler<TextFilterField, String, TextFilterFieldHandler> {

    public TextFilterFieldHandler(String name, String label) {
        super(name, label);
    }

    @Override
    public TextFilterField createField() {
        TextFilterField field = new TextFilterField(getName());
        field.getTextField().setAutoValidate(true);
        return field;
    }

    @Override
    public TextFilterFieldHandler setReq(boolean required) {
        getField().getTextField().setAllowBlank(!required);
        return this;
    }

    @Override
    public TextFilterFieldHandler setToolTip(String s) {
        getField().getTextField().setToolTip(s);
        return this;
    }

    @Override
    public TextFilterFieldHandler addValidator(Validator<String> validator) {
        getField().getTextField().addValidator(validator);
        return this;
    }

    @Override
    public void setFilter(FilterInfo config) {
        config.setValue(((String) config.getValue()).trim());
        getField().setFilterInfo(config);
    }

    @Override
    public FilterInfo createFilter() {
        FilterInfo fi = getField().createFilter();
        if (fi.getValue() != null) {
            fi.setValue(((String) fi.getValue()).trim());
        }
        return fi;
    }

    @Override
    public TextFilterFieldHandler setVal(String val) {
        getField().getTextField().setValue(val);
        return this;
    }

    @Override
    public String getVal() {
        return getField().getTextField().getCurrentValue();
    }

    @Override
    protected ParamInfo createParamInfo() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
