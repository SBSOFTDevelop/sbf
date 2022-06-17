package ru.sbsoft.client.components.form.handler;

import ru.sbsoft.svc.widget.core.client.form.ShortField;
import ru.sbsoft.client.I18n;
import ru.sbsoft.shared.FilterInfo;
import ru.sbsoft.shared.filter.ShortFilterInfo;
import ru.sbsoft.shared.param.ParamInfo;
import ru.sbsoft.shared.interfaces.NamedItem;
import ru.sbsoft.shared.param.ShortParamInfo;

/**
 *
 * @author Kiselev
 * @param <SelfType>
 */
public class ShortHandler<SelfType extends ShortHandler<SelfType>> extends NumberHandler<ShortField, Short, SelfType> {

    public ShortHandler(NamedItem paramItem) {
        this(paramItem.getCode(), I18n.get(paramItem.getItemName()));
    }

    public ShortHandler(String name, String label) {
        super(name, label);
    }

    @Override
    protected ShortField createField() {
        return new ShortField();
    }

    @Override
    protected FilterInfo createFilter() {
        return new ShortFilterInfo(null, getVal());
    }

    @Override
    protected ParamInfo createParamInfo() {
        return new ShortParamInfo(null, getVal());
    }
}
