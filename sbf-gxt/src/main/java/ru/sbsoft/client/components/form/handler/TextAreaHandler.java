package ru.sbsoft.client.components.form.handler;

import com.sencha.gxt.widget.core.client.form.TextArea;
import ru.sbsoft.shared.FilterInfo;
import ru.sbsoft.shared.filter.StringFilterInfo;
import ru.sbsoft.shared.param.ParamInfo;
import ru.sbsoft.shared.param.StringParamInfo;

/**
 *
 * @author Kiselev
 * @param <SelfType>
 */
public class TextAreaHandler<SelfType extends TextAreaHandler<SelfType>> extends ValFieldBaseHandler<TextArea, String, SelfType> {

    private final static int TEXT_HEIGHT = 40;

    public TextAreaHandler(String name, String label) {
        super(name, label);
    }

    @Override
    protected TextArea createField() {
        TextArea f = new TextArea();
        f.setHeight(TEXT_HEIGHT);
        return f;
    }

    public SelfType setH(int height) {
        getField().setHeight(height);
        return (SelfType) this;
    }

    @Override
    public String getVal() {
        String val = super.getVal();
        return val != null && !(val = val.trim()).isEmpty() ? val : null;
    }

    @Override
    public SelfType setVal(String val) {
        return super.setVal(val != null ? val : "");
    }

    @Override
    protected FilterInfo createFilter() {
        return new StringFilterInfo(null, getVal());
    }

    @Override
    protected ParamInfo createParamInfo() {
        return new StringParamInfo(null, getVal());
    }
}
