package ru.sbsoft.client.components.operation;

import ru.sbsoft.svc.widget.core.client.form.FieldLabel;
import gwtupload.client.IUploader;

import java.util.ArrayList;
import java.util.List;

import ru.sbsoft.client.I18n;
import ru.sbsoft.client.components.form.OperationFileInput;
import ru.sbsoft.client.components.form.SBFFormPanelHelper;
import ru.sbsoft.sbf.svc.components.VerticalFieldSet;
import ru.sbsoft.client.utils.ClientUtils;
import ru.sbsoft.shared.interfaces.OperationType;
import ru.sbsoft.shared.param.ParamInfo;
import ru.sbsoft.shared.services.ServiceConst;
import ru.sbsoft.shared.api.i18n.consts.SBFFormStr;

/**
 * Базовая форма для операций, выполняющих импорт файла.
 * Добавляет поле, загружающее выбранный файл на сервер для дальнейшей обработки.
 */
public class BaseImportOperationParamForm extends BaseOperationParamForm {

    protected final List<ParamInfo> params = new ArrayList<ParamInfo>();
    private final OperationType type;
    private OperationFileInput fieldFILE;

    private boolean autoStart = true;

    public BaseImportOperationParamForm(final String header, final OperationType type) {
        super(header);
        setWidth(600);
        this.type = type;
    }

    public void setAutoStart(boolean autoStart) {
        this.autoStart = autoStart;
    }

    @Override
    protected void fillParameterPage(final VerticalFieldSet fieldSet) {
        fieldSet.addField(new FieldLabel(getFieldFILE(), I18n.get(SBFFormStr.labelFileToDownload)));
    }

    protected String[] getValidExtensions() {
        return new String[]{"xml", "zip"};
    }

    protected OperationFileInput getFieldFILE() {
        if (null == fieldFILE) {
            fieldFILE = new OperationFileInput();
            fieldFILE.setFileInputPrefix(type.getCode());
            fieldFILE.setServletPath(ClientUtils.getAppURL() + ServiceConst.UPLOAD_OPERATION_SERVICE);
            fieldFILE.setValidExtensions(this.getValidExtensions());
            fieldFILE.addOnFinishUploadHandler(new IUploader.OnFinishUploaderHandler() {
                @Override
                public void onFinish(final IUploader uploader) {
                    switch (uploader.getStatus()) {
                        case DONE:
                        case SUCCESS:
                            if (SBFFormPanelHelper.validate(getWindow())) {
                                getRunButton().setEnabled(true);
                                if (autoStart) run();
                            }

                            //break;
                    }
                }
            });

            fieldFILE.addOnStartUploadHandler(new IUploader.OnStartUploaderHandler() {
                @Override
                public void onStart(IUploader uploader) {
                    BaseImportOperationParamForm.this.getWindow().forceLayout();
                }
            });

            fieldFILE.addOnChangeUploadHandler(new IUploader.OnChangeUploaderHandler() {
                @Override
                public void onChange(final IUploader uploader) {
                    getRunButton().setEnabled(false);
                }
            });
        }

        return fieldFILE;
    }

    @Override
    public List<ParamInfo> getParams() {
        return params;
    }

    @Override
    protected void onShow() {
        getRunButton().setEnabled(false);
        super.onShow();
    }
}
