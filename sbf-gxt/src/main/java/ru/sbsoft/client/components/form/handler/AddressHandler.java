package ru.sbsoft.client.components.form.handler;

import com.google.gwt.editor.client.Editor;
import com.google.gwt.editor.client.EditorError;
import com.sencha.gxt.widget.core.client.form.Validator;
import com.sencha.gxt.widget.core.client.form.error.DefaultEditorError;
import java.util.Collections;
import java.util.List;
import ru.sbsoft.client.components.kladr.AddressEdit;
import ru.sbsoft.shared.FilterInfo;
import ru.sbsoft.shared.kladr.AddrUtil;
import ru.sbsoft.shared.kladr.AddressModel;
import ru.sbsoft.shared.param.ParamInfo;

/**
 *
 * @author Kiselev
 * @param <SelfType>
 */
public class AddressHandler<SelfType extends AddressHandler<SelfType>> extends BaseHandler<AddressEdit, AddressModel, SelfType> {

    private final String title;
    private final Validator<AddressModel> emptyValidator = new EmptyValidator();

    public AddressHandler(final String name, final String title) {
        super(name, title);
        this.title = title;
    }

    @Override
    protected AddressEdit createField() {
        return new AddressEdit(60, title);
    }

    @Override
    public AddressModel getVal() {
        return getField().getValue();
    }

    @Override
    public SelfType setReq(boolean required) {
        if (required) {
            if (!getField().getValidators().contains(emptyValidator)) {
                getField().getValidators().add(0, emptyValidator);
            }
        } else {
            getField().removeValidator(emptyValidator);
        }
        return (SelfType) this;
    }

    @Override
    protected void setFilter(FilterInfo config) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected FilterInfo createFilter() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected ParamInfo createParamInfo() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private static class EmptyValidator implements Validator<AddressModel> {

        @Override
        public List<EditorError> validate(Editor<AddressModel> editor, AddressModel value) {
            if (value == null || AddrUtil.isAddrEmpty(value)) {
                return Collections.<EditorError>singletonList(new DefaultEditorError(editor, "Необходимо заполнить", value));
            }
            return null;
        }

    }
}
