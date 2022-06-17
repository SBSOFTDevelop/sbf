package ru.sbsoft.client.components.operation;

import ru.sbsoft.svc.widget.core.client.form.FieldLabel;
import java.util.ArrayList;
import java.util.List;
import ru.sbsoft.client.I18n;
import ru.sbsoft.client.components.form.IntStrComboBox;
import ru.sbsoft.sbf.svc.components.VerticalFieldSet;
import ru.sbsoft.client.components.form.model.IntStrComboBoxModel;
import ru.sbsoft.client.components.grid.BaseGrid;
import ru.sbsoft.shared.GridOperationParamConst;
import ru.sbsoft.shared.param.IntegerParamInfo;
import ru.sbsoft.shared.param.ParamInfo;
import ru.sbsoft.shared.param.StringParamInfo;
import ru.sbsoft.client.schedule.i18n.SBFi18nLocale;
import ru.sbsoft.shared.api.i18n.consts.SBFBrowserStr;
import ru.sbsoft.shared.api.i18n.consts.SBFFormStr;

/**
 * Форма ввода параметров табличной операции
 */
public class GridOperationParamForm extends BaseOperationParamForm {

    private final BaseGrid grid;
    private IntStrComboBox SELECTION_TYPE;

    public GridOperationParamForm(final String header, final BaseGrid grid) {
        super(header);
        this.grid = grid;

        if (grid.getTotalRecordsCount() == 0) {
            throw new IllegalStateException(I18n.get(SBFFormStr.labelNoDataPerfom));
        }
    }

    @Override
    protected void fillParameterPage(final VerticalFieldSet fieldSet) {
        fieldSet.addField(new FieldLabel(getFieldSELECTION_TYPE(), I18n.get(SBFFormStr.labelPerfomOperationFor)));
    }

    protected IntStrComboBox getFieldSELECTION_TYPE() {
        if (SELECTION_TYPE == null) {
            SELECTION_TYPE = new IntStrComboBox();
            SELECTION_TYPE.setAutoValidate(true);
            if (grid.getTotalMarkedRecordsCount() > 0) {
                SELECTION_TYPE.add(new IntStrComboBoxModel(1, I18n.get(SBFBrowserStr.labelMarkedRecords) + 
                        " (" + grid.getTotalMarkedRecordsCount() + ")"));
            }
            SELECTION_TYPE.add(new IntStrComboBoxModel(0, I18n.get(SBFBrowserStr.labelAllRecords) + 
                    " (" + grid.getTotalRecordsCount() + ")"));
            SELECTION_TYPE.setValue(SELECTION_TYPE.getStore().get(0));
        }
        return SELECTION_TYPE;
    }

    @Override
    public List<ParamInfo> getParams() {
        final List<ParamInfo> params = new ArrayList<ParamInfo>(1);
        params.add(new IntegerParamInfo(GridOperationParamConst.GRID_SELECT_TYPE_PARAM, getFieldSELECTION_TYPE().getIntValue()));
        params.add(new StringParamInfo(GridOperationParamConst.GRID_ID_COLUMN_PARAM, grid.getKeyProvider().getPath()));
        params.add(new StringParamInfo(GridOperationParamConst.GRID_SELECT_LOCALE, SBFi18nLocale.getLocaleName()));
        return params;
    }
}
