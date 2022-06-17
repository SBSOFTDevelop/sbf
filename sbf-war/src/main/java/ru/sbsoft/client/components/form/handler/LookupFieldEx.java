package ru.sbsoft.client.components.form.handler;

import ru.sbsoft.client.components.IFilterSource;
import ru.sbsoft.client.components.form.LookupField;
import ru.sbsoft.shared.filter.LookUpFilterInfo;
import ru.sbsoft.shared.model.LookupInfoModel;

/**
 *
 * @author Kiselev
 * @param <M>
 */
public class LookupFieldEx<M extends LookupInfoModel> extends LookupField<M> {

    private boolean caseSensitive = false;
    private boolean fieldsReadonly = false;

    public LookupFieldEx() {
        super();
    }

    public LookupFieldEx(boolean shortFormat) {
        super(shortFormat);
    }

    public LookupFieldEx(IFilterSource filterSource) {
        super(filterSource);
    }

    public LookupFieldEx(boolean shortFormat, IFilterSource filterSource) {
        super(shortFormat, filterSource);
    }

    public boolean isCaseSensitive() {
        return caseSensitive;
    }

    public void setCaseSensitive(boolean caseSensitive) {
        this.caseSensitive = caseSensitive;
    }

    @Override
    public LookUpFilterInfo createLookUpFilter(String fieldName) {
        LookUpFilterInfo fi = super.createLookUpFilter(fieldName);
        fi.setCaseSensitive(caseSensitive);
        return fi;
    }

    @Override
    public void setReadOnly(boolean readOnly) {
        super.setReadOnly(readOnly);
        if (!readOnly && fieldsReadonly) {
            doFieldsReadonly(true);
        }
    }

    public void setFieldsReadonly(boolean b) {
        this.fieldsReadonly = b;
        if (b) {
            doFieldsReadonly(true);
        } else if (!isReadOnly()) {
            doFieldsReadonly(false);
        }
    }

    private void doFieldsReadonly(boolean b) {
        getFieldKey().setReadOnly(b);
        getFieldName().setReadOnly(b);
    }
}
