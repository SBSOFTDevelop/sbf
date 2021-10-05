package ru.sbsoft.client.components.form;

import ru.sbsoft.sbf.gxt.components.VerticalFieldSet;
import com.google.gwt.user.client.ui.HasName;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.Component;
import com.sencha.gxt.widget.core.client.TabPanel;
import com.sencha.gxt.widget.core.client.box.AlertMessageBox;
import com.sencha.gxt.widget.core.client.form.AdapterField;
import com.sencha.gxt.widget.core.client.form.Field;
import com.sencha.gxt.widget.core.client.form.FieldLabel;
import com.sencha.gxt.widget.core.client.form.FormPanelHelper;
import com.sencha.gxt.widget.core.client.form.IsField;
import java.util.ArrayList;
import java.util.List;
import ru.sbsoft.client.I18n;
import ru.sbsoft.client.components.form.handler.IFieldHandler;
import ru.sbsoft.client.components.operation.BaseOperationParamForm;
import ru.sbsoft.client.utils.ClientUtils;
import ru.sbsoft.common.Strings;
import ru.sbsoft.shared.api.i18n.consts.SBFGeneralStr;
import ru.sbsoft.shared.param.ParamInfo;
import ru.sbsoft.shared.interfaces.NamedItem;

/**
 *
 * @author Kiselev
 */
public abstract class GeneralParamForm extends BaseOperationParamForm {

    private List<IFormValidator> formValidators = null;
    private List<IFieldHandler<?, ?, ?>> handlers;
    private boolean needStart = false;

    public GeneralParamForm(String header) {
        super(header);
        setWidth(800);
        if (formValidators == null) {
            formValidators = new ArrayList<IFormValidator>();
        }
    }

    protected abstract void addHandlers(ParamHandlerCollector hc);

    @Override
    protected void fillParameterPage(VerticalFieldSet fieldSet) {
        if (formValidators == null) {
            formValidators = new ArrayList<IFormValidator>();
        }
        handlers = new ArrayList<IFieldHandler<?, ?, ?>>();
        addHandlers(new ParamHandlerCollector(handlers));
        for (IFieldHandler<?, ?, ?> h : handlers) {
            fieldSet.addField(h.getWidget());
        }
    }

    @Override
    public List<ParamInfo> getParams() {
        List<ParamInfo> params = new ArrayList<ParamInfo>();
        for (IFieldHandler<?, ?, ?> h : handlers) {
            if (h.isParamGen()) {
                params.add(h.getParam());
            }
        }
        return params;
    }

    @Override
    protected void onShow() {
        super.onShow();
        needStart = false;
    }

    @Override
    public boolean isNeedStart() {
        return needStart;
    }

    protected final IFieldHandler<?, ?, ?> getHandler(NamedItem p) {
        for (IFieldHandler<?, ?, ?> h : handlers) {
            String pName = p.getCode();
            try {
                if (pName.equals(h.getName())) {
                    return h;
                }
            } catch (Exception ex) {
                // no name
            }
        }
        try {
            return getHandler(I18n.get(p.getItemName()));
        } catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException("Handler not found. Code: '" + p.getCode() + "'; Label: '" + I18n.get(p.getItemName()) + "'");
        }
    }

    protected final IFieldHandler<?, ?, ?> getHandler(String label) {
        for (IFieldHandler<?, ?, ?> h : handlers) {
            if (label.equals(h.getLabel())) {
                return h;
            }
        }
        throw new IllegalArgumentException("Handler not found: '" + label + "'");
    }

    @Override
    protected void run() {
        if (validate()) {
            needStart = true;
            hide();
        } else {
            ClientUtils.message(I18n.get(SBFGeneralStr.labelDataValidation), getCustomValidateMessage());
        }

    }

    protected final void addValidator(IFormValidator formValidator) {
        this.formValidators.add(formValidator);
    }

    protected boolean validate() {
        final List<IsField<?>> fields = FormPanelHelper.getFields(this.getWindow());
        for (IsField<?> f : fields) {
            if (f instanceof LookupField) {
                f.clearInvalid();
            }
        }
        boolean res = validateFields(fields);
        if (res && formValidators != null && !formValidators.isEmpty()) {
            for (IFormValidator formValidator : formValidators) {
                List<ValidateFormError> errs = formValidator.validate();
                if (errs != null && errs.size() > 0) {
                    ValidateFormError err = errs.get(0);
                    res = false;
                    IsField<?> errf = err.getErrField();
                    String msg = err.getMsg();
                    markInvalid(errf, msg);
                    for (IsField<?> f : err.getAffectedFields()) {
                        markInvalid(f, msg);
                    }
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

    private void alert(Component f) {
        selectActiveTab(f);
        (new AlertMessageBox(I18n.get(SBFGeneralStr.captQuery), I18n.get(SBFGeneralStr.msgFieldNotCorrect, getName(f)))).show();
    }

    private void selectActiveTab(Widget w) {
        TabPanel mainTab = findTab(w);
        while ((w = w.getParent()) != null) {
            if (w.getParent() == mainTab.getContainer()) {
                mainTab.setActiveWidget(w);
                return;
            }
        }
    }

    private TabPanel findTab(Widget w) {
        Widget p = w.getParent();
        while (p != null && !(p instanceof TabPanel)) {
            p = p.getParent();
        }
        return (TabPanel) p;
    }

    private FieldLabel findFieldLabel(Component f) {
        Widget c = f;
        int i = 0;
        while ((c = c.getParent()) != null && ++i < 5) {
            if (c instanceof FieldLabel) {
                return (FieldLabel) c;
            }
        }
        return null;
    }

    private String getName(Component f) {
        final FieldLabel label = findFieldLabel(f);
        if (label != null) {
            final String s = Strings.clean(label.getText());
            if (s != null) {
                return s;
            }
        }

        if (f.getToolTip() != null && f.getToolTip().getToolTipConfig() != null) {
            final String s = Strings.clean(f.getToolTip().getToolTipConfig().getBody().asString());
            if (s != null) {
                return s;
            }
        }
        if (f instanceof HasName) {
            final String s = Strings.clean(((HasName) f).getName());
            if (s != null) {
                return s;
            }
        }
        return null;
    }

    private boolean validateFields(final List<IsField<?>> fields) {
        boolean valid = true;
        for (final IsField<?> field : fields) {
            if (field instanceof Field) {
                ((Field) field).finishEditing();
            }
            valid &= field.validate(false);
        }
        return valid;
    }

}
