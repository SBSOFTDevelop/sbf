package ru.sbsoft.client.components.form.handler.form;

import com.google.gwt.event.shared.HandlerRegistration;
import com.sencha.gxt.core.client.ValueProvider;
import gwtupload.client.IUploader;
import gwtupload.client.Uploader;
import ru.sbsoft.client.components.field.RunnableOper;
import ru.sbsoft.client.utils.ClientUtils;
import ru.sbsoft.sbf.app.model.FormModel;
import ru.sbsoft.shared.services.ServiceConst;

/**
 *
 * @author sokolov
 * @param <M>
 */
public class UploadFileFieldHandler <M extends FormModel> extends ru.sbsoft.client.components.form.handler.UploadFileFieldHandler<UploadFileFieldHandler<M>> implements IFormFieldHandler<M> {
        
    private final ValueProvider<? super M, String> valueProvider;

    public UploadFileFieldHandler(String label, RunnableOper oper, ValueProvider<? super M, String> valueProvider) {
        super(null, label);
        this.valueProvider = valueProvider;

        getUploader().setFileInputPrefix(oper.getOperType().getCode());
        getUploader().setServletPath(ClientUtils.getAppURL() + ServiceConst.UPLOAD_OPERATION_SERVICE);

        getUploader().addOnFinishUploadHandler(new IUploader.OnFinishUploaderHandler() {
            @Override
            public void onFinish(final IUploader uploader) {
                switch (uploader.getStatus()) {
                    case DONE:
                    case SUCCESS:
                        oper.run();
                        reset();
                        break;
                }
            }
        });

    }

    @Override
    public void fromModel(M model) {
        if (valueProvider != null) {
            setVal(valueProvider.getValue(model));
        }
    }

    @Override
    public void toModel(M model) {
        if (valueProvider != null) {
            valueProvider.setValue(model, getVal());
        }
    }

    private Uploader getUploader() {
        return getField().getUploader();
    }

    public void setValidExtensions(final String... validExtensions) {
        getUploader().setValidExtensions(validExtensions);
    }

    public void setValidExtensions(final String ext) {
        getUploader().setValidExtensions(ext);
    }

    public void reset() {
        getUploader().getFileInput().setVisible(true);
    }
    
    public HandlerRegistration addOnStartUploadHandler(final IUploader.OnStartUploaderHandler handler) {
        return getField().addOnStartUploadHandler(handler);
    }
    
}
