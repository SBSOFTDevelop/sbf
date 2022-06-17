package ru.sbsoft.shared.api.i18n.consts;

import ru.sbsoft.shared.api.i18n.I18nResourceInfo;
import ru.sbsoft.shared.consts.I18nSection;
import ru.sbsoft.shared.consts.I18nType;

/**
 *
 * @author sokolov
 */
public enum SBFReportStr implements I18nResourceInfo {

    titleCustomReports,
    titleFormReportSet,
    titleFormFieldName,
    hintFormFieldName,
    titleFormFieldURL,
    hintFormFieldURL,
    titleReportFilters,
    titleFormParams,
    titleFormFieldIncId,
    hintFormFieldIncId,
    tabFormHeaderSQL,
    titleFormHeaderSQL,
    hintFormHeaderSQL,
    
    titleAddParameters,
    nameStringParameter,
    nameLongParameter,
    nameDateParameter,
    
    errorParamCodeEmpty,
    errorParamTypeEmpty,
    errorParamNameEmpty,
    
    msgJasperServerNoSet,
    msgJasperServerNoUsr,
    msgJasperServerNoPwd,
    msgCreateReport,
    msgReportRunError,
    msgReportParameterError,
    msgReportEntityError,
    msgReportHeaderParameterError,
    msgReportHeaderExecError,
    msgReportURLEncodingError    

    ;

    @Override
    public I18nSection getLib() {
        return I18nType.REPORT;
    }

    @Override
    public String getKey() {
        return name();
    }

}
