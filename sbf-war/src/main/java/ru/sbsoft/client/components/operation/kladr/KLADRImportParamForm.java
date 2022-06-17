package ru.sbsoft.client.components.operation.kladr;

import com.google.gwt.user.client.ui.IsWidget;
import ru.sbsoft.svc.widget.core.client.form.FieldLabel;
import gwtupload.client.IUploader;
import java.util.ArrayList;
import java.util.List;
import ru.sbsoft.client.I18n;
import ru.sbsoft.client.components.form.OperationFileInput;
import ru.sbsoft.sbf.svc.components.VerticalFieldSet;
import ru.sbsoft.client.components.operation.BaseOperationParamForm;
import ru.sbsoft.client.utils.ClientUtils;
import ru.sbsoft.shared.param.LongParamInfo;
import ru.sbsoft.shared.param.ParamInfo;
import ru.sbsoft.shared.services.ServiceConst;
import ru.sbsoft.shared.api.i18n.consts.SBFFormStr;

/**
 * Форма параметров для {@link KLADRImportOperationMaker}.
 * @author balandin
 * @since Mar 11, 2013 2:12:48 PM
 */
public class KLADRImportParamForm extends BaseOperationParamForm {

    private OperationFileInput fieldFILE;
    private final String operationCode;
    private final List<ParamInfo> params = new ArrayList<ParamInfo>();

    public KLADRImportParamForm(final String header, final String operationCode) {
        super(header);
        setWidth(600);
        this.operationCode = operationCode;
    }

    @Override
    protected void fillParameterPage(final VerticalFieldSet fieldSet) {
        fieldSet.addField(new FieldLabel(getFieldFILE(), I18n.get(SBFFormStr.labelFileToDownload)));
    }

    private IsWidget getFieldFILE() {
        if (null == fieldFILE) {
            fieldFILE = new OperationFileInput();
            fieldFILE.setFileInputPrefix(operationCode);
            fieldFILE.setServletPath(ClientUtils.getAppURL() + ServiceConst.UPLOAD_OPERATION_SERVICE);
            fieldFILE.setValidExtensions("7z", "arj");
            fieldFILE.addOnFinishUploadHandler(new IUploader.OnFinishUploaderHandler() {
                @Override
                public void onFinish(final IUploader uploader) {
                    switch (uploader.getStatus()) {
                        case DONE:
                        case SUCCESS:
                            getRunButton().setEnabled(true);
                            String response = uploader.getServerMessage().getMessage();
                            setSavedFiles(response);
                            break;
                        default:
                            break;
                    }
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

    private void setSavedFiles(String response) {
        params.clear();
        params.add(new LongParamInfo("file", Long.valueOf(response)));
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
