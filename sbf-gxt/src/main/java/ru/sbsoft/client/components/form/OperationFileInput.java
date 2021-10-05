package ru.sbsoft.client.components.form;

import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Label;
import com.sencha.gxt.widget.core.client.container.HorizontalLayoutContainer;
import gwtupload.client.IFileInput;
import gwtupload.client.IUploadStatus;
import gwtupload.client.IUploader;
import gwtupload.client.IUploader.OnChangeUploaderHandler;
import gwtupload.client.IUploader.OnFinishUploaderHandler;
import gwtupload.client.IUploader.OnStartUploaderHandler;
import gwtupload.client.Uploader;
import ru.sbsoft.client.I18n;
import ru.sbsoft.shared.api.i18n.consts.SBFEditorStr;
import ru.sbsoft.shared.api.i18n.consts.SBFGeneralStr;

/**
 * Загрузчик файлов со строкой состояния.
 */
public class OperationFileInput extends HorizontalLayoutContainer {
    
    private Uploader uploader;
    private Label statusLabel;
    
    public OperationFileInput() {
        add(getUploader(), new HorizontalLayoutData(1, -1));
        add(getStatusLabel(), new HorizontalLayoutData(-1, -1));
        getStatusLabel().setVisible(false);
        
    }
    
    private Uploader getUploader() {
        if (null == uploader) {
            uploader = new Uploader(IFileInput.FileInputType.LABEL);
            uploader.setI18Constants(new SBFUploaderConstants());
            //uploader.setMultipleSelection(false);
            uploader.setMultipleSelection(true);
            uploader.setAutoSubmit(true);
            //uploader.setAvoidRepeatFiles(false);
            uploader.addOnFinishUploadHandler(new IUploader.OnFinishUploaderHandler() {
                @Override
                public void onFinish(final IUploader uploader) {
                    switch (uploader.getStatus()) {
                        case CANCELED:
                        case ERROR:
                        case INVALID:
                            setStatusText(I18n.get(SBFGeneralStr.msgDownloadInterrupted));
                            break;
                        case SUCCESS:
                        case DONE:
                            setStatusText(I18n.get(SBFGeneralStr.msgFileHosted));
                            break;
                    }
                }
            });
            uploader.addOnCancelUploadHandler(new IUploader.OnCancelUploaderHandler() {
                @Override
                public void onCancel(IUploader parUploader) {
                    setStatusText(I18n.get(SBFGeneralStr.msgDownloadInterrupted));
                    final IUploadStatus status = getUploader().getStatusWidget();
                    status.setVisible(false);
                    final IFileInput fileInput = getUploader().getFileInput();
                    fileInput.setVisible(true);
                    getUploader().reset();
                }
            });
            uploader.addOnStartUploadHandler(new IUploader.OnStartUploaderHandler() {
                @Override
                public void onStart(IUploader uploader) {
                    getUploader().getFileInput().setVisible(false);
                    setStatusText("");
                }
            });
        }
        return uploader;
    }
    
    private Label getStatusLabel() {
        if (null == statusLabel) {
            statusLabel = new Label(I18n.get(SBFEditorStr.labelFileNotLoad));
        }
        return statusLabel;
    }
    
    public HandlerRegistration addOnFinishUploadHandler(final OnFinishUploaderHandler handler) {
        return getUploader().addOnFinishUploadHandler(handler);
    }
    
    public HandlerRegistration addOnStartUploadHandler(final OnStartUploaderHandler handler) {
        return getUploader().addOnStartUploadHandler(handler);
    }
    
    public HandlerRegistration addOnChangeUploadHandler(final OnChangeUploaderHandler handler) {
        return getUploader().addOnChangeUploadHandler(handler);
    }
    
    public void setFileInputPrefix(final String prefix) {
        getUploader().setFileInputPrefix(prefix);
    }
    
    public void setFileInputSize(final int length) {
        getUploader().setFileInputSize(length);
    }
    
    public void setServletPath(final String path) {
        getUploader().setServletPath(path);
    }
    
    public void setValidExtensions(final String... validExtensions) {
        getUploader().setValidExtensions(validExtensions);
    }
    
    public void setValidExtensions(final String ext) {
        getUploader().setValidExtensions(ext);
    }
    
    private void setStatusText(final String text) {
        getStatusLabel().setText(text);
    }
    
    public void reset() {
        setStatusText(I18n.get(SBFEditorStr.labelFileNotLoad));
        getUploader().getFileInput().setText(getUploader().getI18NConstants().uploaderBrowse());
    }
    
}
