package ru.sbsoft.shared.api.i18n.consts;

import ru.sbsoft.shared.api.i18n.I18nResourceInfo;
import ru.sbsoft.shared.consts.I18nSection;
import ru.sbsoft.shared.consts.I18nType;

/**
 *
 * @author Kiselev
 */
public enum SBFEditorStr implements I18nResourceInfo {

    captionNew,
    captionUpdate,
    captionError,
    captionWizard,
    //
    menuSave,
    hintSave,
    menuSaveRefresh,
    hintSaveRefresh,
    menuChoice,
    menuItem,
    menuClear,
    //
    labelTechnology,
    labelCreate,
    hintDateCreated,
    hintUserCreated,
    labelUpdate,
    hintDateUpdated,
    hintUserUpdated,
    labelPeriodRecord,
    labelStartDate,
    hintStartDate,
    labelEndDate,
    hintEndDate,
    labelActRange,
    labelActRangeStartEnd,
    hintActRangeStart,
    hintActRangeEnd,
    hintCaseSensitive,
    hintEditAddress,
    labelFormAddress,
    hintClearAll,
    labelSearch,
    hintFormAddress,
    labelKLADR,
    labelRegion,
    labelArea,
    labelCity,
    labelSettlement,
    labelStreet,
    labelHose,
    labelBlock,
    labelFlat,
    labelPostcode,
    hintDeterminePostIndex,
    labelDetailed,
    labelOperations,
    labelFormatDischarge,
    hintSelectDictionary,
    labelMultipleChoice,
    labelFileNotLoad,
    labelReportFormat,
    labelIMLUploaded,
    labelIMLDragAndDrop,
    labelIMLDownload,
    labelIMLClear,
    //
    msgNotSpecifyHouse,
    msgAddressNotMeet,
    msgAddressChanged,
    msgMatchingAddress,
    msgErrorMapping,
    msgFirstLetterNames,
    msgToday,
    msgNotRunning,
    msgRunning,
    msgCompleted,
    msgWithError,
    msgOperationCompeted,
    msgOperationWithStatus,
    msgNeedToFill,
    msgInvalidCharacters,
    msgReguiredLength,
    msgMinLength,
    msgMaxLength,
    msgValueField,
    msgMaskNotCompile,
    msgNotMatchMask,
    msgMaskNotEditable,
    //
    formatDateShow,
    formatDateTimeShow,
    formatTimeShow,
    formatYearShow,
    formatMonthShow,
    formatDayShow;

    @Override
    public I18nSection getLib() {
        return I18nType.EDITOR;
    }

    @Override
    public String getKey() {
        return name();
    }
}
