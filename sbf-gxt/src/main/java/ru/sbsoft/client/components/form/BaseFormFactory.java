package ru.sbsoft.client.components.form;

import com.google.gwt.user.client.rpc.AsyncCallback;
import java.util.HashSet;
import java.util.Set;
import ru.sbsoft.client.I18n;
import ru.sbsoft.client.utils.RoleCheker;
import ru.sbsoft.sbf.app.model.IFormModel;
import ru.sbsoft.shared.NamedFormContext;
import ru.sbsoft.shared.NamedFormType;
import ru.sbsoft.shared.interfaces.ConstraintType;
import ru.sbsoft.shared.model.MarkModel;

/**
 *
 * @author Kiselev
 * @param <M>
 * @param <R>
 */
public abstract class BaseFormFactory<M extends IFormModel, R extends MarkModel> implements IFormFactory<M, R> {

    private final NamedFormContext formContext;
    private int width = 800;
    private Integer height = null;
    private String tablePrefix = null;
    private Set<ConstraintType> constraintInfos = new HashSet<>();

    public BaseFormFactory(NamedFormType formType) {
        this(new NamedFormContext(formType));
    }

    protected BaseFormFactory(NamedFormContext ctx) {
        this.formContext = ctx;
    }

    public void setTablePrefix(String tablePrefix) {
        this.tablePrefix = tablePrefix;
    }

    public BaseFormFactory setWidth(int width) {
        this.width = width;
        return this;
    }

    public BaseFormFactory setHeight(int height) {
        this.height = height;
        return this;
    }

    @Deprecated
    public BaseFormFactory addUniqConstraintInfo(ConstraintType inf) {
        constraintInfos.add(inf);
        return this;
    }

    @Override
    public void createEditForm(R model, AsyncCallback<BaseForm<M>> callback) {
        try {
            BaseValidatedForm<M> f = createFormInstance();
            f.setWidth(width);
            if (height != null) {
                f.setHeight(height);
            }
            if (!constraintInfos.isEmpty()) {
                for (ConstraintType t : constraintInfos) {
                    f.addUniqConstraintInfo(t);
                }
            }
            callback.onSuccess(f);
        } catch (Throwable ex) {
            callback.onFailure(ex);
        }
    }

    @Override
    public NamedFormContext getFormContext(R model) {
        return formContext;
    }

    protected NamedFormType getFormType() {
        return formContext.getFormType();
    }

    protected boolean isAdmin() {
        return RoleCheker.getInstance().isAdmin();
    }

    protected abstract BaseFactoryForm createFormInstance();

    protected final String getCaption() {
        return I18n.get(getFormType().getItemName());
    }
//    
//    protected final String getCode(){
//        return getFormType().getCode();
//    }

    protected abstract class BaseFactoryForm extends BaseValidatedForm<M> {

        public BaseFactoryForm() {
            super(formContext);
            if (tablePrefix != null) {
                setTablePrefix(tablePrefix);
            }
        }
    }
}
