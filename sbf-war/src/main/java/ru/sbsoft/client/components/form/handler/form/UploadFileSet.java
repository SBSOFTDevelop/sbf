package ru.sbsoft.client.components.form.handler.form;

import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.ui.Widget;
import ru.sbsoft.svc.widget.core.client.container.HorizontalLayoutContainer;
import gwtupload.client.IUploader;
import gwtupload.client.Uploader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import ru.sbsoft.client.components.field.RunnableOper;
import ru.sbsoft.client.components.field.UploadFileField;
import ru.sbsoft.client.components.form.BaseValidatedForm;
import ru.sbsoft.client.components.form.fields.BaseAdapter;
import ru.sbsoft.client.components.form.handler.form.builder.FSet;
import ru.sbsoft.client.components.multiOperation.MultiOperationMonitor;
import ru.sbsoft.client.components.operation.TypedOperation;
import ru.sbsoft.client.utils.ClientUtils;
import ru.sbsoft.client.utils.HLC;
import ru.sbsoft.sbf.app.model.IFormModel;
import ru.sbsoft.sbf.svc.components.VerticalFieldSet;
import ru.sbsoft.sbf.svc.utils.FieldUtils;
import ru.sbsoft.shared.consts.Dict;
import ru.sbsoft.shared.interfaces.OperationType;
import ru.sbsoft.shared.model.IFileLinksModel;
import ru.sbsoft.shared.model.LookupInfoModel;
import ru.sbsoft.shared.model.operation.OperationCommand;
import ru.sbsoft.shared.param.BigDecimalParamInfo;
import ru.sbsoft.shared.param.ParamInfo;
import ru.sbsoft.shared.services.ServiceConst;

/**
 *
 * @author sokolov
 * @author vk
 * @param <M> - model
 */
public class UploadFileSet<M extends IFormModel & IFileLinksModel> extends FSet<M> {

    private final BaseValidatedForm<M> form;
    private final OperationType operationType;
    private final Map<UploadFileField, LookupInfoModel> fields = new HashMap<>();
    private final RunnerOper oper;
    private boolean readOnly = false;

    public UploadFileSet(String cap, BaseValidatedForm<M> form, OperationType operationType) {
        super(cap);
        this.form = form;
        this.operationType = operationType;
        setCollapsible(true, true);
        oper = new RunnerOper();
        form.addFromModelHandler(m -> {
            setVals(m != null ? m.getFileLnks() : null);
        });
        form.addToModelHandler(m -> {
            m.setFileLnks(getVals());
        });
    }

    public boolean isReadOnly() {
        return readOnly || form.isReadOnly() || form.getModel() == null || form.getModel().getId() == null;
    }

    public UploadFileSet<M> setReadOnly(boolean readOnly) {
        this.readOnly = readOnly;
        for (UploadFileField f : fields.keySet()) {
            f.setReadOnly(isReadOnly());
        }
        return this;
    }

    public List<LookupInfoModel> getVals() {
        List<LookupInfoModel> res = new ArrayList<>();
        fields.keySet().stream().filter((f) -> (f.getValue() != null)).forEach((f) -> {
            res.add(fields.get(f));
        });
        return res;
    }

    private void initField(UploadFileField f) {
        final Uploader uploader = f.getUploader();
        f.setReadOnly(isReadOnly());
        uploader.setFileInputPrefix(oper.getOperType().getCode());
        uploader.setServletPath(ClientUtils.getAppURL() + ServiceConst.UPLOAD_OPERATION_SERVICE);
        f.setHeight(25);
        uploader.addOnFinishUploadHandler((final IUploader uploader1) -> {
            switch (uploader1.getStatus()) {
                case DONE:
                case SUCCESS:
                    oper.run(fields.get(f).getID());
                    uploader1.getFileInput().setVisible(true);
                    break;
            }
        });
    }

    private HorizontalLayoutContainer wrap(Widget w) {

        HorizontalLayoutContainer hc = new HorizontalLayoutContainer();

        hc.setHeight("" + FieldUtils.FIELD_HEIGHT + Style.Unit.PX.getType());
        hc.add(w, HLC.FILL);
        return hc;
    }

    public void setVals(List<LookupInfoModel> l) {
        VerticalFieldSet c = getManagedSet();
        Map<Widget, BaseAdapter> formFields = form.getFields();
        for (int i = 0; i < c.getWidgetCount(); i++) {
            formFields.remove(c.getWidget(i));
        }
        c.clear();
        fields.clear();
        if (l != null && !l.isEmpty()) {
            l.forEach((t) -> {
                UploadFileField f = new UploadFileField();
                initField(f);
                fields.put(f, t);

                f.setValue(t.getSemanticName());
                c.add(wrap(f));

            });
        }
        if (!isReadOnly()) {
            //for new File add empty field      
            UploadFileField f = new UploadFileField();
            initField(f);
            fields.put(f, new LookupInfoModel());
            f.setValue(null);
            c.add(wrap(f));
        }
        form.updateFieldsInfo(c);
    }

    private class RunnerOper extends TypedOperation implements RunnableOper {

        public RunnerOper() {
            super(operationType);
            super.setShowLogWindow(false);
        }

        @Override
        protected OperationCommand createOperationCommand() {
            OperationCommand c = super.createOperationCommand();
            c.setNeedNotify(false);
            return c;
        }

        @Override
        public OperationType getOperType() {
            return operationType;
        }

        public void run(BigDecimal fileId) {
            if (form.isVisible()) {
                form.mask("Загрузка файла на сервер...");
            }
            startOperation(ParamInfo.asList(
                    new BigDecimalParamInfo(Dict.RECORD_ID, form.getModel().getId()),
                    new BigDecimalParamInfo(Dict.FILE_ID, fileId)));
        }

        @Override
        public void run() {
        }

        @Override
        public void onCompleted() {
            super.onCompleted();
            MultiOperationMonitor.getInstance().forceUpdate();
            if (form.isVisible()) {
                form.unmask();
                form.setResetTabOnLoad(false);
                if (form.isChanged()) {
                    form.saveRefresh();
                } else {
                    form.refresh();
                }
            }
        }
    }
}
