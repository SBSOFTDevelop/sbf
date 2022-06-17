package ru.sbsoft.client.components.form.handler;

import ru.sbsoft.svc.widget.core.client.form.LongField;
import ru.sbsoft.client.I18n;
import ru.sbsoft.shared.FilterInfo;
import ru.sbsoft.shared.filter.LongFilterInfo;
import ru.sbsoft.shared.param.LongParamInfo;
import ru.sbsoft.shared.param.ParamInfo;
import ru.sbsoft.shared.interfaces.NamedItem;

/**
 *
 * @author Kiselev
 * @param <SelfType>
 */
public class LongHandler<SelfType extends LongHandler<SelfType>> extends NumberHandler<LongField, Long, SelfType> {

    public LongHandler(NamedItem paramItem) {
        this(paramItem.getCode(), I18n.get(paramItem.getItemName()));
    }
    
    public LongHandler(String name, String label) {
        super(name, label);
    }

    @Override
    protected LongField createField() {
        return new LongField();
    }

    @Override
    protected FilterInfo createFilter() {
        return new LongFilterInfo(null, getVal());
    }

    @Override
    protected ParamInfo createParamInfo() {
        return new LongParamInfo(null, getVal());
    }
}
