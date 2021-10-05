package ru.sbsoft.shared.api.i18n.consts;

import ru.sbsoft.shared.api.i18n.I18nResourceInfo;
import ru.sbsoft.shared.consts.I18nSection;
import ru.sbsoft.shared.consts.I18nType;

/**
 *
 * @author Kiselev
 */
public enum SBFUploadStr implements I18nResourceInfo {

    uploaderActiveUpload,
    uploaderAlreadyDone,
    uploaderBlobstoreError,
    uploaderBrowse,
    uploaderInvalidExtension,
    uploaderServerError,
    submitError,
    uploaderServerUnavailable,
    uploaderTimeout,
    uploaderBadServerResponse,
    uploaderBlobstoreBilling,
    uploaderInvalidPathError,
    uploadLabelCancel,
    uploadStatusCanceled,
    uploadStatusCanceling,
    uploadStatusDeleted,
    uploadStatusError,
    uploadStatusInProgress,
    uploadStatusQueued,
    uploadStatusSubmitting,
    uploadStatusSuccess,
    uploaderSend;

    @Override
    public I18nSection getLib() {
        return I18nType.UPLOAD;
    }

    @Override
    public String getKey() {
        return name();
    }

}
