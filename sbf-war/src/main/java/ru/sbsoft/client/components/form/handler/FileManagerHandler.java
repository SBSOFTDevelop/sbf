package ru.sbsoft.client.components.form.handler;

import com.google.gwt.event.logical.shared.SelectionHandler;
import ru.sbsoft.svc.widget.core.client.menu.Item;
import ru.sbsoft.client.components.form.BaseForm;
import ru.sbsoft.client.components.form.FileManagerField;
import ru.sbsoft.client.components.operation.BaseImportOperationParamForm;
import ru.sbsoft.client.components.operation.BaseOperationParamForm;
import ru.sbsoft.client.components.operation.FormOperationAction;
import ru.sbsoft.shared.FilterInfo;
import ru.sbsoft.shared.interfaces.OperationType;
import ru.sbsoft.shared.param.ParamInfo;

/**
 *
 * @author Kiselev
 * @param <SelfType>
 */
public class FileManagerHandler<SelfType extends FileManagerHandler<SelfType>> extends BaseHandler<FileManagerField, String, SelfType> {

    public FileManagerHandler(String name, String label) {
        super(name, label);
    }

    @Override
    protected FileManagerField createField() {
        return new FileManagerField();
    }

    public SelfType addLoadOperation(final BaseForm<?> f, final OperationType oper, final String... extensions) {
        addLoadHandler(new FormOperationAction(f, oper) {
            @Override
            protected BaseOperationParamForm createParamForm() {
                return new BaseImportOperationParamForm(null, getType()) {
                    @Override
                    protected String[] getValidExtensions() {
                        return extensions != null && extensions.length > 0 ? extensions : null;
                    }
                };
            }
        }.getSelectHandler());
        return (SelfType)this;
    }

    public SelfType addUnloadOperation(final BaseForm<?> f, final OperationType oper) {
        addUnloadHandler(new FormOperationAction(f, oper, false).getSelectHandler());
        return (SelfType)this;
    }

    public SelfType addLoadHandler(SelectionHandler<Item> handler) {
        getField().addLoadHandler(handler);
        return (SelfType)this;
    }

    public SelfType addUnloadHandler(SelectionHandler<Item> handler) {
        getField().addUnloadHandler(handler);
        return (SelfType)this;
    }

    public SelfType addClearHandler(SelectionHandler<Item> handler) {
        getField().addClearHandler(handler);
        return (SelfType)this;
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
}
