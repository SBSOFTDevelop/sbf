package ru.sbsoft.client.components.form.handler;

import ru.sbsoft.client.components.field.UploadFileField;
import ru.sbsoft.shared.FilterInfo;
import ru.sbsoft.shared.param.ParamInfo;

/**
 *
 * @author sokolov
 * @param <SelfType>
 */
public class UploadFileFieldHandler <SelfType extends UploadFileFieldHandler<SelfType>> extends BaseHandler<UploadFileField, String, SelfType> {

    public UploadFileFieldHandler(String name, String label) {
        super(name, label);
    }

    @Override
    protected UploadFileField createField() {
        return new UploadFileField();
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
