package ru.sbsoft.client.components.form;

import com.google.gwt.user.client.ui.Widget;
import ru.sbsoft.svc.core.client.ValueProvider;
import ru.sbsoft.svc.widget.core.client.Component;
import ru.sbsoft.svc.widget.core.client.TabPanel;
import ru.sbsoft.svc.widget.core.client.form.AdapterField;
import ru.sbsoft.svc.widget.core.client.form.Field;
import ru.sbsoft.svc.widget.core.client.form.IsField;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import ru.sbsoft.client.I18n;
import ru.sbsoft.client.components.ElementUtils;
import ru.sbsoft.client.components.field.DateConsts;
import ru.sbsoft.client.components.form.fields.Adapter;
import ru.sbsoft.client.components.form.handler.IFieldHandler;
import ru.sbsoft.client.components.form.handler.form.DateHandler;
import ru.sbsoft.client.components.form.handler.form.IFormFieldHandler;
import ru.sbsoft.client.components.form.handler.form.TextHandler;
import ru.sbsoft.client.components.form.handler.form.builder.FSet;
import ru.sbsoft.client.components.form.handler.form.builder.FaceBuilder;
import ru.sbsoft.client.components.form.handler.form.builder.IFaceBuilder;
import ru.sbsoft.client.components.form.model.ICreateInfoAccessor;
import ru.sbsoft.client.components.form.model.IUpdateInfoAccessor;
import ru.sbsoft.client.components.grid.BaseGrid;
import ru.sbsoft.client.components.operation.FormReportAction;
import ru.sbsoft.client.utils.CliUtil;
import ru.sbsoft.sbf.app.model.IFormModel;
import ru.sbsoft.shared.FormContext;
import ru.sbsoft.shared.NamedFormContext;
import ru.sbsoft.shared.api.i18n.I18nResourceInfo;
import ru.sbsoft.shared.api.i18n.consts.SBFGeneralStr;
import ru.sbsoft.shared.interfaces.OperationType;

/**
 *
 * @author Kiselev
 * @param <DataModel>
 */
public abstract class BaseValidatedForm<DataModel extends IFormModel> extends BaseForm<DataModel> {

    private List<IFormValidator> formValidators;
    private List<IFormFieldHandler<? super DataModel>> handlers;
    private List<IFormFromModelHandler<DataModel>> customHandlersFrom;
    private List<IFormToModelHandler<DataModel>> customHandlersTo;
    private final FormContext formContext;
    private List<ModelChangeListener<DataModel>> modelChangeListeners;
    private List<ModelChangeListener<DataModel>> userModelChangeListeners;
    private List<FormSaveListener<DataModel>> saveListeners;
    private final String securityId;

    public BaseValidatedForm(I18nResourceInfo caption) {
        this(I18n.get(caption));
    }

    public BaseValidatedForm(I18nResourceInfo caption, FormContext formContext) {
        this(I18n.get(caption), formContext, null);
    }

    public BaseValidatedForm(I18nResourceInfo caption, FormContext formContext, String tableName) {
        this(I18n.get(caption), formContext, tableName);
    }

    public BaseValidatedForm(String caption) {
        this(caption, null);
    }

    public BaseValidatedForm(NamedFormContext formContext) {
        this(I18n.get(formContext.getFormType().getItemName()), formContext, formContext.getFormType().getSecurityId());
    }

    public BaseValidatedForm(String caption, FormContext formContext) {
        this(caption, formContext, null);
    }

    public BaseValidatedForm(String caption, FormContext formContext, String tableName) {
        super(caption);
        this.formContext = formContext;
        this.securityId = tableName != null ? tableName : formContext != null ? formContext.getFormType().getRights() : null;
        initListeners();
        getMainTab().addSelectionHandler(e -> getActionManager().updateState());
        setWidth(600);
    }

    public void addFromModelHandler(IFormFromModelHandler<DataModel> h) {
        if (h != null && !customHandlersFrom.contains(h)) {
            customHandlersFrom.add(h);
        }
    }

    public void addToModelHandler(IFormToModelHandler<DataModel> h) {
        if (h != null && !customHandlersTo.contains(h)) {
            customHandlersTo.add(h);
        }
    }

    @Override
    public void setOwnerGrid(BaseGrid ownerGrid) {
        super.setOwnerGrid(ownerGrid);
    }

    public BaseForm getOwnerForm() {
        BaseGrid g = getOwnerGrid();
        return ElementUtils.findForm((Widget) g);
    }

    @Override
    protected FormContext getFormContext() {
        return formContext;
    }

    @Override
    protected String getSecurityId() {
        return securityId;
    }

    private void initListeners() {
        if (modelChangeListeners == null) {
            modelChangeListeners = new ArrayList<>();
        }
        if (userModelChangeListeners == null) {
            userModelChangeListeners = new ArrayList<>();
        }
        if (formValidators == null) {
            formValidators = new ArrayList<>();
        }
        if (saveListeners == null) {
            saveListeners = new ArrayList<>();
        }
        if (customHandlersFrom == null) {
            customHandlersFrom = new ArrayList<>();
        }
        if (customHandlersTo == null) {
            customHandlersTo = new ArrayList<>();
        }
    }

    @Override
    protected void createEditors(TabPanel tabEditors) {
        initListeners();
        FaceBuilder<DataModel> b = new FaceBuilder<>(this, tabEditors, getDefaultContext());
        buildFace(b);
        handlers = b.getHandlers();
        modelChangeListeners = b.getModelChangeListeners();
    }

    protected void buildFace(IFaceBuilder<DataModel> b) {
    }

    public static <A extends ICreateInfoAccessor & IUpdateInfoAccessor> FSet createTehnologySet(A a) {
        return createTehnologySet(a.createDate(), a.createUser(), a.updateDate(), a.updateUser());
    }

    private static <M extends IFormModel> FSet createTehnologySet(ValueProvider<M, Date> createDate, ValueProvider<M, String> createUser, ValueProvider<M, Date> updateDate, ValueProvider<M, String> updateUser) {
        return new FSet(I18n.get(SBFGeneralStr.labelTechnology))
                .addComposite(I18n.get(SBFGeneralStr.labelCreation),
                        new DateHandler(null, DateConsts.DATE_TIME, createDate).setRO(true).setToolTip(I18n.get(SBFGeneralStr.labelDateCreate)),
                        new TextHandler(null, createUser).setRO(true).setToolTip(I18n.get(SBFGeneralStr.labelUserCreated))
                ).addComposite(I18n.get(SBFGeneralStr.labelUpdate),
                        new DateHandler(null, DateConsts.DATE_TIME, updateDate).setRO(true).setToolTip(I18n.get(SBFGeneralStr.labelDateUpdate)),
                        new TextHandler(null, updateUser).setRO(true).setToolTip(I18n.get(SBFGeneralStr.labelUserUpdated))
                );
    }
    
    public void addModelChangeListener(ModelChangeListener<DataModel> l) {
        if (!userModelChangeListeners.contains(l)) {
            userModelChangeListeners.add(l);
        }
    }

    public void addSaveListener(FormSaveListener<DataModel> l) {
        if (!saveListeners.contains(l)) {
            saveListeners.add(l);
        }
    }

    public void addReport(OperationType type, IParamFormFactory paramsFormFactory) {
        FormReportAction<DataModel> a = new FormReportAction<>(this, type, paramsFormFactory);
        addAction(a);
    }

    @Override
    protected void dataToForm(DataModel dataModel) {
        handlers.forEach((h) -> {
            h.fromModel(dataModel);
        });
        customHandlersFrom.forEach((h) -> {
            h.fromModel(dataModel);
        });
    }

    @Override
    protected void formToData(DataModel dataModel) {
        handlers.forEach((h) -> {
            h.toModel(dataModel);
        });
        customHandlersTo.forEach((h) -> {
            h.toModel(dataModel);
        });
    }

    @Override
    public void setModel(DataModel value, boolean toUpdateRowInGrid) {
        super.setModel(value, toUpdateRowInGrid);
        modelChangeListeners.forEach((l) -> {
            l.modelChanged(getDataModel());
        });
        userModelChangeListeners.forEach((l) -> {
            l.modelChanged(getDataModel());
        });
    }

    @Override
    public void save(Runnable callback) {
        super.save(callback);
        saveListeners.forEach((l) -> {
            l.onSave(this);
        });
    }

    protected String getDefaultContext() {
        return getCaption();
    }

    protected String getContext(String prefix) {
        return new StringBuilder().append(prefix).append('_').append(getDefaultContext()).toString();
    }

    protected final void addFormValidator(IFormValidator formValidator) {
        this.formValidators.add(formValidator);
    }

    @Override
    protected boolean validate() {
        for (Adapter adapter : getFields().values()) {
            if (adapter.getWidget() instanceof LookupField) {
                adapter.clearInvalid();
            }
        }
        boolean res = super.validate();
        if (res && formValidators != null && !formValidators.isEmpty()) {
            for (IFormValidator formValidator : formValidators) {
                List<ValidateFormError> errs = formValidator.validate();
                if (errs != null && errs.size() > 0) {
                    ValidateFormError err = errs.get(0);
                    res = false;
                    IsField<?> errf = err.getErrField();
                    String msg = err.getMsg();
                    markInvalid(errf, msg);
                    err.getAffectedFields().forEach((f) -> {
                        markInvalid(f, msg);
                    });
                    if (errf instanceof Component) {
                        alert((Component) errf);
                    }
                    break;
                }
            }
        }
        return res;
    }

    private void markInvalid(IsField<?> f, String msg) {
        if (f instanceof Field) {
            ((Field) f).markInvalid(msg);
        } else if (f instanceof AdapterField) {
            ((AdapterField) f).markInvalid(msg);
        }
    }

    public <H extends IFieldHandler> H setReadOnly(H h, boolean readOnly) {
        boolean registeredHandler = false;
        for (IFormFieldHandler<? super DataModel> hh : handlers) {
            if (hh == h) {
                registeredHandler = true;
                break;
            }
        }
        if (registeredHandler) {
            super.setReadOnly(h.getField(), readOnly);
            h.setRO(readOnly);
        } else {
            throw new IllegalArgumentException("Handler is not registered on this form: " + h);
        }
        return h;
    }

    public void refreshWithParentGrid() {
        CliUtil.refreshWithParentGrid(this);
    }

    protected static <T> T nonNull(final T obj, final String msg, final Object... args) {
        if (obj == null) {
            throw new NullPointerException(args == null || args.length == 0 ? msg : CliUtil.format(msg, args));
        }
        return obj;
    }
}
