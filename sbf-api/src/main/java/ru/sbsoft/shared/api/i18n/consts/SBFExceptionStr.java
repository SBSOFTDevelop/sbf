package ru.sbsoft.shared.api.i18n.consts;

import ru.sbsoft.shared.api.i18n.I18nResourceInfo;
import ru.sbsoft.shared.consts.I18nSection;
import ru.sbsoft.shared.consts.I18nType;

/**
 * Идентификаторы сообщений исключений для локализации на клиенте
 *
 * @author Sokoloff
 */
public enum SBFExceptionStr implements I18nResourceInfo {

    notSetLookupProvider,
    entityNotFound,
    objectNotFound,
    errorConvertingDecimal,
    failedSaveTableLabel,
    parameterNotDefined,
    parameterTypeIncorrect,
    parameterMustPositive,
    parameterMustNotNegative,
    errorMinYear,
    errorQuarter,
    notMarkedLines,
    unexpectedValueType,
    rowNotSelected,
    classNotDefConstructor,
    documNotFound,
    quarterRange,
    quarterLess,
    quarterMore,
    monthRange,
    monthLess,
    monthMore,
    dayRange,
    yearLess,
    filterUnsupported,
    columnNotDefined,
    objectNotSaved,
    newObjectNotSaved,
    querySaveCancel,
    compareMustValue,
    valueNotIncuded,
    failedChangeStatus,
    operCreateError,
    operSetParamsError,
    operConvertParamError,
    operConvertParamTypeError,
    operRetParamError,
    operListError,
    operInfoError,
    operStartError,
    operStartErrorP,
    operCancelError,
    operMarkError,
    operNotRegistered,
    operAlreadyRegistr,
    commandInvalidType,
    entityNotLookup,
    errorNotFound,
    valueNotLess,
    valueNotMore,
    differentItems,
    noInsertForm,
    typeMismatch,
    emptyValue,
    codeAlreadyUse,
    cannotDeleteRecord,
    //
    actRangeUndefined,
    actRangeBeginUndefined,
    actRangeEndUndefined,
    actRangeUnsupported,
    actRangeIntersection,
    actRangeParamIntersection,
    actRangeBoundsReverse,
    actRangeNotIn;

    @Override
    public I18nSection getLib() {
        return I18nType.EXCEPTION;
    }

    @Override
    public String getKey() {
        return name();
    }

}
