package ru.sbsoft.client.components.form.handler;

import ru.sbsoft.svc.widget.core.client.form.IntegerField;
import ru.sbsoft.client.I18n;
import ru.sbsoft.shared.FilterInfo;
import ru.sbsoft.shared.filter.IntegerFilterInfo;
import ru.sbsoft.shared.param.IntegerParamInfo;
import ru.sbsoft.shared.param.ParamInfo;
import ru.sbsoft.shared.interfaces.NamedItem;

/**
 *
 * @author Kiselev
 * @param <SelfType>
 */
public class IntHandler<SelfType extends IntHandler<SelfType>> extends NumberHandler<IntegerField, Integer, SelfType> {

    public IntHandler(NamedItem paramItem) {
        this(paramItem.getCode(), I18n.get(paramItem.getItemName()));
    }

    public IntHandler(String name, String label) {
        super(name, label);
    }

    @Override
    protected IntegerField createField() {
        return new IntegerField();
    }

    @Override
    protected FilterInfo createFilter() {
        return new IntegerFilterInfo(null, getVal());
    }

    @Override
    protected ParamInfo createParamInfo() {
        return new IntegerParamInfo(null, getVal());
    }
}
