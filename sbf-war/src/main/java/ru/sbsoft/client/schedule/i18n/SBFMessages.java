package ru.sbsoft.client.schedule.i18n;

import ru.sbsoft.svc.messages.client.XMessages;
import ru.sbsoft.client.I18n;
import ru.sbsoft.shared.api.i18n.consts.XMessagesStr;

/**
 * Реализует интерфейс XMessages для предоставления строковых ресурсов на основании серверного механизма загрузки
 * ресурсов. Используется компонентами SVC.
 *
 * @author Sokoloff
 */
public class SBFMessages implements XMessages {

    public SBFMessages() {
    }

    @Override
    public String updateManager_indicatorText() {
        return I18n.get(XMessagesStr.updateManager_indicatorText);
    }

    @Override
    public String grid_ddText(int rows) {
        return I18n.get(XMessagesStr.grid_ddText);
    }

    @Override
    public String tabPanelItem_closeText() {
        return I18n.get(XMessagesStr.tabPanelItem_closeText);
    }

    @Override
    public String tabPanelItem_closeOtherText() {
        return I18n.get(XMessagesStr.tabPanelItem_closeOtherText);
    }

    @Override
    public String field_invalidText() {
        return I18n.get(XMessagesStr.field_invalidText);
    }

    @Override
    public String field_parseExceptionText(String value) {
        return I18n.get(XMessagesStr.field_parseExceptionText);
    }

    @Override
    public String loadMask_msg() {
        return I18n.get(XMessagesStr.loadMask_msg);
    }

    @Override
    public String messageBox_ok() {
        return I18n.get(XMessagesStr.messageBox_ok);
    }

    @Override
    public String messageBox_cancel() {
        return I18n.get(XMessagesStr.messageBox_cancel);
    }

    @Override
    public String messageBox_yes() {
        return I18n.get(XMessagesStr.messageBox_yes);
    }

    @Override
    public String messageBox_no() {
        return I18n.get(XMessagesStr.messageBox_no);
    }

    @Override
    public String messageBox_close() {
        return I18n.get(XMessagesStr.messageBox_close);
    }

    @Override
    public String datePicker_todayText() {
        return I18n.get(XMessagesStr.datePicker_todayText);
    }

    @Override
    public String datePicker_minText() {
        return I18n.get(XMessagesStr.datePicker_minText);
    }

    @Override
    public String datePicker_maxText() {
        return I18n.get(XMessagesStr.datePicker_maxText);
    }

    @Override
    public String datePicker_disabledDaysText() {
        return I18n.get(XMessagesStr.datePicker_disabledDaysText);
    }

    @Override
    public String datePicker_disabledDatesText() {
        return I18n.get(XMessagesStr.datePicker_disabledDatesText);
    }

    @Override
    public String datePicker_nextText() {
        return I18n.get(XMessagesStr.datePicker_nextText);
    }

    @Override
    public String datePicker_prevText() {
        return I18n.get(XMessagesStr.datePicker_prevText);
    }

    @Override
    public String datePicker_monthYearText() {
        return I18n.get(XMessagesStr.datePicker_monthYearText);
    }

    @Override
    public String datePicker_todayTip(String date) {
        return I18n.get(XMessagesStr.datePicker_todayTip, date);
    }

    @Override
    public String datePicker_okText() {
        return I18n.get(XMessagesStr.datePicker_okText);
    }

    @Override
    public String datePicker_cancelText() {
        return I18n.get(XMessagesStr.datePicker_cancelText);
    }

    @Override
    public String datePicker_startDay() {
        return I18n.get(XMessagesStr.datePicker_startDay);
    }

    @Override
    public String colorPalette() {
        return I18n.get(XMessagesStr.colorPalette);
    }

    @Override
    public String pagingToolBar_beforePageText() {
        return I18n.get(XMessagesStr.pagingToolBar_beforePageText);
    }

    @Override
    public String pagingToolBar_afterPageText(int pages) {
        return I18n.get(XMessagesStr.pagingToolBar_afterPageText, String.valueOf(pages));
    }

    @Override
    public String pagingToolBar_firstText() {
        return I18n.get(XMessagesStr.pagingToolBar_firstText);
    }

    @Override
    public String pagingToolBar_prevText() {
        return I18n.get(XMessagesStr.pagingToolBar_prevText);
    }

    @Override
    public String pagingToolBar_nextText() {
        return I18n.get(XMessagesStr.pagingToolBar_nextText);
    }

    @Override
    public String pagingToolBar_lastText() {
        return I18n.get(XMessagesStr.pagingToolBar_lastText);
    }

    @Override
    public String pagingToolBar_refreshText() {
        return I18n.get(XMessagesStr.pagingToolBar_refreshText);
    }

    @Override
    public String panel_expandPanel() {
        return I18n.get(XMessagesStr.panel_expandPanel);
    }

    @Override
    public String panel_collapsePanel() {
        return I18n.get(XMessagesStr.panel_collapsePanel);
    }

    @Override
    public String pagingToolBar_displayMsg(int start, int end, int total) {
        return I18n.get(XMessagesStr.pagingToolBar_displayMsg, String.valueOf(start), String.valueOf(end), String.valueOf(total));
    }

    @Override
    public String pagingToolBar_emptyMsg() {
        return I18n.get(XMessagesStr.pagingToolBar_emptyMsg);
    }

    @Override
    public String textField_minLengthText(int length) {
        return I18n.get(XMessagesStr.textField_minLengthText, String.valueOf(length));
    }

    @Override
    public String textField_maxLengthText(int length) {
        return I18n.get(XMessagesStr.textField_maxLengthText, String.valueOf(length));
    }

    @Override
    public String textField_blankText() {
        return I18n.get(XMessagesStr.textField_blankText);
    }

    @Override
    public String textField_regexText() {
        return I18n.get(XMessagesStr.textField_regexText);
    }

    @Override
    public String textField_emptyText() {
        return I18n.get(XMessagesStr.textField_emptyText);
    }

    @Override
    public String checkBoxGroup_text(String fieldLabel) {
        return I18n.get(XMessagesStr.checkBoxGroup_text, fieldLabel);
    }

    @Override
    public String radioGroup_text(String fieldLabel) {
        return I18n.get(XMessagesStr.radioGroup_text, fieldLabel);
    }

    @Override
    public String uploadField_browseText() {
        return I18n.get(XMessagesStr.uploadField_browseText);
    }

    @Override
    public String numberField_minText(double min) {
        return I18n.get(XMessagesStr.numberField_minText, Long.toString((long)min));
    }

    @Override
    public String numberField_maxText(double max) {
        return I18n.get(XMessagesStr.numberField_maxText, Long.toString((long)max));
    }

    @Override
    public String numberField_nanText(String num) {
        return I18n.get(XMessagesStr.numberField_nanText, num);
    }

    @Override
    public String numberField_negativeText() {
        return I18n.get(XMessagesStr.numberField_negativeText);
    }

    @Override
    public String dateField_disabledDaysText() {
        return I18n.get(XMessagesStr.dateField_disabledDaysText);
    }

    @Override
    public String dateField_disabledDatesText() {
        return I18n.get(XMessagesStr.dateField_disabledDatesText);
    }

    @Override
    public String dateField_minText(String min) {
        return I18n.get(XMessagesStr.dateField_minText, min);
    }

    @Override
    public String dateField_maxText(String max) {
        return I18n.get(XMessagesStr.dateField_maxText, max);
    }

    @Override
    public String dateField_invalidText(String date, String format) {
        return I18n.get(XMessagesStr.dateField_invalidText, date, format);
    }

    @Override
    public String comboBox_loading() {
        return I18n.get(XMessagesStr.comboBox_loading);
    }

    @Override
    public String listField_moveSelectedUp() {
        return I18n.get(XMessagesStr.listField_moveSelectedUp);
    }

    @Override
    public String listField_moveSelectedDown() {
        return I18n.get(XMessagesStr.listField_moveSelectedDown);
    }

    @Override
    public String listField_addAll() {
        return I18n.get(XMessagesStr.listField_addAll);
    }

    @Override
    public String listField_addSelected() {
        return I18n.get(XMessagesStr.listField_addSelected);
    }

    @Override
    public String listField_removeAll() {
        return I18n.get(XMessagesStr.listField_removeAll);
    }

    @Override
    public String listField_removeSelected() {
        return I18n.get(XMessagesStr.listField_removeSelected);
    }

    @Override
    public String listField_itemsSelected(int items) {
        return I18n.get(XMessagesStr.listField_itemsSelected, String.valueOf(items));
    }

    @Override
    public String htmlEditor_boldTipText() {
        return I18n.get(XMessagesStr.htmlEditor_boldTipText);
    }

    @Override
    public String htmlEditor_boldTipTitle() {
        return I18n.get(XMessagesStr.htmlEditor_boldTipTitle);
    }

    @Override
    public String htmlEditor_italicTipText() {
        return I18n.get(XMessagesStr.htmlEditor_italicTipText);
    }

    @Override
    public String htmlEditor_italicTipTitle() {
        return I18n.get(XMessagesStr.htmlEditor_italicTipTitle);
    }

    @Override
    public String htmlEditor_underlineTipText() {
        return I18n.get(XMessagesStr.htmlEditor_underlineTipText);
    }

    @Override
    public String htmlEditor_underlineTipTitle() {
        return I18n.get(XMessagesStr.htmlEditor_underlineTipTitle);
    }

    @Override
    public String htmlEditor_justifyLeftTipText() {
        return I18n.get(XMessagesStr.htmlEditor_justifyLeftTipText);
    }

    @Override
    public String htmlEditor_justifyLeftTipTitle() {
        return I18n.get(XMessagesStr.htmlEditor_justifyLeftTipTitle);
    }

    @Override
    public String htmlEditor_justifyCenterTipText() {
        return I18n.get(XMessagesStr.htmlEditor_justifyCenterTipText);
    }

    @Override
    public String htmlEditor_justifyCenterTipTitle() {
        return I18n.get(XMessagesStr.htmlEditor_justifyCenterTipTitle);
    }

    @Override
    public String htmlEditor_justifyRightTipText() {
        return I18n.get(XMessagesStr.htmlEditor_justifyRightTipText);
    }

    @Override
    public String htmlEditor_justifyRightTipTitle() {
        return I18n.get(XMessagesStr.htmlEditor_justifyRightTipTitle);
    }

    @Override
    public String htmlEditor_sourceEditTipText() {
        return I18n.get(XMessagesStr.htmlEditor_sourceEditTipText);
    }

    @Override
    public String htmlEditor_sourceEditTipTitle() {
        return I18n.get(XMessagesStr.htmlEditor_sourceEditTipTitle);
    }

    @Override
    public String htmlEditor_olTipText() {
        return I18n.get(XMessagesStr.htmlEditor_olTipText);
    }

    @Override
    public String htmlEditor_olTipTitle() {
        return I18n.get(XMessagesStr.htmlEditor_olTipTitle);
    }

    @Override
    public String htmlEditor_ulTipText() {
        return I18n.get(XMessagesStr.htmlEditor_ulTipText);
    }

    @Override
    public String htmlEditor_ulTipTitle() {
        return I18n.get(XMessagesStr.htmlEditor_ulTipTitle);
    }

    @Override
    public String htmlEditor_linkTipText() {
        return I18n.get(XMessagesStr.htmlEditor_linkTipText);
    }

    @Override
    public String htmlEditor_linkTipTitle() {
        return I18n.get(XMessagesStr.htmlEditor_linkTipTitle);
    }

    @Override
    public String htmlEditor_createLinkText() {
        return I18n.get(XMessagesStr.htmlEditor_createLinkText);
    }

    @Override
    public String htmlEditor_increaseFontSizeTipText() {
        return I18n.get(XMessagesStr.htmlEditor_increaseFontSizeTipText);
    }

    @Override
    public String htmlEditor_increaseFontSizeTipTitle() {
        return I18n.get(XMessagesStr.htmlEditor_increaseFontSizeTipTitle);
    }

    @Override
    public String htmlEditor_decreaseFontSizeTipText() {
        return I18n.get(XMessagesStr.htmlEditor_decreaseFontSizeTipText);
    }

    @Override
    public String htmlEditor_decreaseFontSizeTipTitle() {
        return I18n.get(XMessagesStr.htmlEditor_decreaseFontSizeTipTitle);
    }

    @Override
    public String htmlEditor_foreColorTipText() {
        return I18n.get(XMessagesStr.htmlEditor_foreColorTipText);
    }

    @Override
    public String htmlEditor_foreColorTipTitle() {
        return I18n.get(XMessagesStr.htmlEditor_foreColorTipTitle);
    }

    @Override
    public String htmlEditor_backColorTipText() {
        return I18n.get(XMessagesStr.htmlEditor_backColorTipText);
    }

    @Override
    public String htmlEditor_backColorTipTitle() {
        return I18n.get(XMessagesStr.htmlEditor_backColorTipTitle);
    }

    @Override
    public String gridView_sortAscText() {
        return I18n.get(XMessagesStr.gridView_sortAscText);
    }

    @Override
    public String gridView_sortDescText() {
        return I18n.get(XMessagesStr.gridView_sortDescText);
    }

    @Override
    public String gridView_columnsText() {
        return I18n.get(XMessagesStr.gridView_columnsText);
    }

    @Override
    public String groupingView_emptyGroupText() {
        return I18n.get(XMessagesStr.groupingView_emptyGroupText);
    }

    @Override
    public String groupingView_groupByText() {
        return I18n.get(XMessagesStr.groupingView_groupByText);
    }

    @Override
    public String groupingView_showGroupsText() {
        return I18n.get(XMessagesStr.groupingView_showGroupsText);
    }

    @Override
    public String rowEditor_cancelText() {
        return I18n.get(XMessagesStr.rowEditor_cancelText);
    }

    @Override
    public String rowEditor_dirtyText() {
        return I18n.get(XMessagesStr.rowEditor_dirtyText);
    }

    @Override
    public String rowEditor_saveText() {
        return I18n.get(XMessagesStr.rowEditor_saveText);
    }

    @Override
    public String rowEditor_tipTitleText() {
        return I18n.get(XMessagesStr.rowEditor_tipTitleText);
    }

    @Override
    public String propertyColumnModel_nameText() {
        return I18n.get(XMessagesStr.propertyColumnModel_nameText);
    }

    @Override
    public String propertyColumnModel_valueText() {
        return I18n.get(XMessagesStr.propertyColumnModel_valueText);
    }

    @Override
    public String borderLayout_splitTip() {
        return I18n.get(XMessagesStr.borderLayout_splitTip);
    }

    @Override
    public String borderLayout_collapsibleSplitTip() {
        return I18n.get(XMessagesStr.borderLayout_collapsibleSplitTip);
    }

    @Override
    public String themeSelector_blueTheme() {
        return I18n.get(XMessagesStr.themeSelector_blueTheme);
    }

    @Override
    public String themeSelector_grayTheme() {
        return I18n.get(XMessagesStr.themeSelector_grayTheme);
    }

    @Override
    public String desktop_startButton() {
        return I18n.get(XMessagesStr.desktop_startButton);
    }

    @Override
    public String window_ariaResize() {
        return I18n.get(XMessagesStr.window_ariaResize);
    }

    @Override
    public String window_ariaMove() {
        return I18n.get(XMessagesStr.window_ariaMove);
    }

    @Override
    public String window_ariaResizeDescription() {
        return I18n.get(XMessagesStr.window_ariaResizeDescription);
    }

    @Override
    public String window_ariaMoveDescription() {
        return I18n.get(XMessagesStr.window_ariaMoveDescription);
    }

    @Override
    public String aria_leaveApplication() {
        return I18n.get(XMessagesStr.aria_leaveApplication);
    }

    @Override
    public String stringFilter_emptyText() {
        return I18n.get(XMessagesStr.stringFilter_emptyText);
    }

    @Override
    public String booleanFilter_noText() {
        return I18n.get(XMessagesStr.booleanFilter_noText);
    }

    @Override
    public String booleanFilter_yesText() {
        return I18n.get(XMessagesStr.booleanFilter_yesText);
    }

    @Override
    public String dateFilter_afterText() {
        return I18n.get(XMessagesStr.dateFilter_afterText);
    }

    @Override
    public String dateFilter_beforeText() {
        return I18n.get(XMessagesStr.dateFilter_beforeText);
    }

    @Override
    public String dateFilter_onText() {
        return I18n.get(XMessagesStr.dateFilter_onText);
    }

    @Override
    public String numericFilter_emptyText() {
        return I18n.get(XMessagesStr.numericFilter_emptyText);
    }

    @Override
    public String gridFilters_filterText() {
        return I18n.get(XMessagesStr.gridFilters_filterText);
    }

    @Override
    public String timeField_invalidText(String date, String format) {
        return I18n.get(XMessagesStr.timeField_invalidText);
    }

}
