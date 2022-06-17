package ru.sbsoft.shared.api.i18n.consts;

import ru.sbsoft.shared.api.i18n.I18nResourceInfo;
import ru.sbsoft.shared.consts.I18nSection;
import ru.sbsoft.shared.consts.I18nType;

/**
 *
 * @author Kiselev
 */
public enum SBFFormStr implements I18nResourceInfo {

    captSaveProtocol,
    captView,
    captCheckData,
    //
    labelSelect,
    labelFilter,
    labelClearFields,
    labelCheck,
    labelCurrentDate,
    labelCurrentWorkDate,
    labelAccordingData,
    labelFileToDownload,
    labelNoDataPerfom,
    labelPerfomOperationFor,
    labelLoad,
    labelOperationMode,
    labelCheckFilter,
    //
    hintOperationMode,
    hintSelect,
    //
    msgDataNotFilled,
    msgSaveDocum,
    msgDocumSaved,
    msgValidationData,
    msgNotFilledFields;

    @Override
    public I18nSection getLib() {
        return I18nType.FORM;
    }

    @Override
    public String getKey() {
        return name();
    }

}
