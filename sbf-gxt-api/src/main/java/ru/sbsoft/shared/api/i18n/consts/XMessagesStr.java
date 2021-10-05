package ru.sbsoft.shared.api.i18n.consts;

import ru.sbsoft.shared.api.i18n.I18nResourceInfo;
import ru.sbsoft.shared.consts.I18nSection;
import ru.sbsoft.shared.consts.I18nType;

/**
 * Идентификаторы gxt сообщений из соответствующего файла properties на сервере.
 *
 * @author Sokoloff
 */
public enum XMessagesStr implements I18nResourceInfo {

    updateManager_indicatorText,
    // Grid
    grid_ddText,
    // TabPanelItem
    tabPanelItem_closeText,
    tabPanelItem_closeOtherText,
    // Field
    field_invalidText,
    field_parseExceptionText,
    // LoadMask
    loadMask_msg,
    // MessageBox
    messageBox_ok,
    messageBox_cancel,
    messageBox_yes,
    messageBox_no,
    messageBox_close,
    // DatePicker
    datePicker_todayText,
    datePicker_minText,
    datePicker_maxText,
    datePicker_disabledDaysText,
    datePicker_disabledDatesText,
    datePicker_nextText,
    datePicker_prevText,
    datePicker_monthYearText,
    datePicker_todayTip,
    datePicker_okText,
    datePicker_cancelText,
    datePicker_startDay,
    // ColorPalette
    colorPalette,
    // PagingToolBar
    pagingToolBar_beforePageText,
    pagingToolBar_afterPageText,
    pagingToolBar_firstText,
    pagingToolBar_prevText,
    pagingToolBar_nextText,
    pagingToolBar_lastText,
    pagingToolBar_refreshText,
    pagingToolBar_displayMsg,
    pagingToolBar_emptyMsg,
    // TextField
    textField_minLengthText,
    textField_maxLengthText,
    textField_blankText,
    textField_regexText,
    textField_emptyText,
    // NumberField
    numberField_minText,
    numberField_maxText,
    numberField_nanText,
    numberField_negativeText,
    // DateField
    dateField_disabledDaysText,
    dateField_disabledDatesText,
    dateField_minText,
    dateField_maxText,
    dateField_invalidText,
    // CheckBoxGroup
    checkBoxGroup_text,
    // RadioGroup
    radioGroup_text,
    // FileUploadFild
    uploadField_browseText,
    // ComboBox
    comboBox_loading,
    // TimeField
    timeField_minText,
    timeField_maxText,
    timeField_invalidText,
    // DualListField
    listField_moveSelectedUp,
    listField_moveSelectedDown,
    listField_addAll,
    listField_addSelected,
    listField_removeAll,
    listField_removeSelected,
    listField_itemsSelected,
    //HtmlEditor
    htmlEditor_boldTipText,
    htmlEditor_boldTipTitle,
    htmlEditor_italicTipText,
    htmlEditor_italicTipTitle,
    htmlEditor_underlineTipText,
    htmlEditor_underlineTipTitle,
    htmlEditor_justifyLeftTipText,
    htmlEditor_justifyLeftTipTitle,
    htmlEditor_justifyCenterTipText,
    htmlEditor_justifyCenterTipTitle,
    htmlEditor_justifyRightTipText,
    htmlEditor_justifyRightTipTitle,
    htmlEditor_sourceEditTipText,
    htmlEditor_sourceEditTipTitle,
    htmlEditor_olTipText,
    htmlEditor_olTipTitle,
    htmlEditor_ulTipText,
    htmlEditor_ulTipTitle,
    htmlEditor_linkTipText,
    htmlEditor_linkTipTitle,
    htmlEditor_createLinkText,
    htmlEditor_increaseFontSizeTipText,
    htmlEditor_increaseFontSizeTipTitle,
    htmlEditor_decreaseFontSizeTipText,
    htmlEditor_decreaseFontSizeTipTitle,
    htmlEditor_foreColorTipText,
    htmlEditor_foreColorTipTitle,
    htmlEditor_backColorTipText,
    htmlEditor_backColorTipTitle,
    //GridView
    gridView_sortAscText,
    gridView_sortDescText,
    gridView_columnsText,
    //GroupingView
    groupingView_emptyGroupText,
    groupingView_groupByText,
    groupingView_showGroupsText,
    // PropertyColumnModel
    propertyColumnModel_nameText,
    propertyColumnModel_valueText,
    // BorderLayout
    borderLayout_splitTip,
    borderLayout_collapsibleSplitTip,
    // ThemeSelector
    themeSelector_blueTheme,
    themeSelector_grayTheme,
    //RowEditor
    rowEditor_cancelText,
    rowEditor_dirtyText,
    rowEditor_saveText,
    rowEditor_tipTitleText,
    //ContentPanel
    panel_expandPanel,
    panel_collapsePanel,
    //Desktop
    desktop_startButton,
    //GridFilters
    gridFilters_filterText,
    //Filters
    stringFilter_emptyText,
    numericFilter_emptyText,
    booleanFilter_noText,
    booleanFilter_yesText,
    dateFilter_afterText,
    dateFilter_beforeText,
    dateFilter_onText,
    //Window
    window_ariaResize,
    window_ariaMove,
    window_ariaResizeDescription,
    window_ariaMoveDescription,
    //ARIA
    aria_leaveApplication;

    @Override
    public I18nSection getLib() {
        return I18nType.XMESSAGES;
    }

    @Override
    public String getKey() {
        return name();
    }

}
