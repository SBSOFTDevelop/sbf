package ru.sbsoft.client.components.form.handler;

import ru.sbsoft.client.components.field.RegExpMaskField;
import ru.sbsoft.shared.FilterInfo;
import ru.sbsoft.shared.filter.StringFilterInfo;
import ru.sbsoft.shared.param.ParamInfo;
import ru.sbsoft.shared.param.StringParamInfo;

/**
 *
 * @author sokolov
 * @param <SelfType>
 */
public class RegExpMaskHandler<SelfType extends RegExpMaskHandler<SelfType>> extends ValFieldBaseHandler<RegExpMaskField, String, SelfType> {

    public RegExpMaskHandler(String name, String label) {
        super(name, label);
    }

    @Override
    protected RegExpMaskField createField() {
        return new RegExpMaskField();
    }

    @Override
    protected FilterInfo createFilter() {
        return new StringFilterInfo(null, getVal());
    }

    @Override
    protected ParamInfo createParamInfo() {
        return new StringParamInfo(null, getVal());
    }

    public SelfType setMask(String mask) {
        getField().setMask(mask);
        return (SelfType) this;
    }
    
    public SelfType setPlaceholder(char placeholder) {
        getField().setPlaceholderChar(placeholder);
        return (SelfType) this;
    }
    
}
