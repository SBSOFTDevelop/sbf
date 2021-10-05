package ru.sbsoft.client.components.form;

import gwtupload.client.IUploader;
import ru.sbsoft.client.I18n;
import ru.sbsoft.shared.api.i18n.consts.SBFUploadStr;

/**
 * Интернационализация компонента SingleUploader.
 * @author Sokoloff
 */
public class SBFUploaderConstants implements IUploader.UploaderConstants {

    @Override
    public String uploaderActiveUpload() {
        return I18n.get(SBFUploadStr.uploaderActiveUpload);
    }

    @Override
    public String uploaderAlreadyDone() {
        return I18n.get(SBFUploadStr.uploaderAlreadyDone);
    }

    @Override
    public String uploaderBlobstoreError() {
        return I18n.get(SBFUploadStr.uploaderBlobstoreError);
    }

    @Override
    public String uploaderBrowse() {
        return I18n.get(SBFUploadStr.uploaderBrowse);
    }

    @Override
    public String uploaderInvalidExtension() {
        return I18n.get(SBFUploadStr.uploaderInvalidExtension);
    }

    @Override
    public String uploaderSend() {
        return I18n.get(SBFUploadStr.uploaderSend);
    }

    @Override
    public String uploaderServerError() {
        return I18n.get(SBFUploadStr.uploaderServerError);
    }

    @Override
    public String submitError() {
        return I18n.get(SBFUploadStr.submitError);
    }

    @Override
    public String uploaderServerUnavailable() {
        return I18n.get(SBFUploadStr.uploaderServerUnavailable);
    }

    @Override
    public String uploaderTimeout() {
        return I18n.get(SBFUploadStr.uploaderTimeout);
    }

    @Override
    public String uploaderBadServerResponse() {
        return I18n.get(SBFUploadStr.uploaderBadServerResponse);
    }

    @Override
    public String uploaderBlobstoreBilling() {
        return I18n.get(SBFUploadStr.uploaderBlobstoreBilling);
    }

    @Override
    public String uploaderInvalidPathError() {
        return I18n.get(SBFUploadStr.uploaderInvalidPathError);
    }

    @Override
    public String uploadLabelCancel() {
        return I18n.get(SBFUploadStr.uploadLabelCancel);
    }

    @Override
    public String uploadStatusCanceled() {
        return I18n.get(SBFUploadStr.uploadStatusCanceled);
    }

    @Override
    public String uploadStatusCanceling() {
        return I18n.get(SBFUploadStr.uploadStatusCanceling);
    }

    @Override
    public String uploadStatusDeleted() {
        return I18n.get(SBFUploadStr.uploadStatusDeleted);
    }

    @Override
    public String uploadStatusError() {
        return I18n.get(SBFUploadStr.uploadStatusError);
    }

    @Override
    public String uploadStatusInProgress() {
        return I18n.get(SBFUploadStr.uploadStatusInProgress);
    }

    @Override
    public String uploadStatusQueued() {
        return I18n.get(SBFUploadStr.uploadStatusQueued);
    }

    @Override
    public String uploadStatusSubmitting() {
        return I18n.get(SBFUploadStr.uploadStatusSubmitting);
    }

    @Override
    public String uploadStatusSuccess() {
        return I18n.get(SBFUploadStr.uploadStatusSuccess);
    }

    @Override
    public String uploaderDrop() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String uploaderBadParsing() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
