package ru.sbsoft.client.components.operation;

import ru.sbsoft.svc.widget.core.client.form.FieldLabel;
import java.util.List;
import ru.sbsoft.client.I18n;
import ru.sbsoft.client.components.form.IntStrComboBox;
import ru.sbsoft.sbf.svc.components.VerticalFieldSet;
import ru.sbsoft.client.components.form.model.IntStrComboBoxModel;
import ru.sbsoft.shared.ImportOperationParamConst;
import ru.sbsoft.shared.interfaces.OperationType;
import ru.sbsoft.shared.param.IntegerParamInfo;
import ru.sbsoft.shared.param.ParamInfo;
import ru.sbsoft.shared.api.i18n.consts.SBFFormStr;

/**
 * Форма ввода параметров для загрузки файлов
 *
 * @author panarin
 */
public class ImportOperationParamForm extends BaseImportOperationParamForm {

    private final IntStrComboBoxModel loadTypeModel = new IntStrComboBoxModel(0, I18n.get(SBFFormStr.labelLoad));
    private IntStrComboBox fieldLOAD_TYPE;

    public ImportOperationParamForm(final String header, final OperationType type) {
        super(header, type);
    }

    @Override
    protected void fillParameterPage(final VerticalFieldSet fieldSet) {
        super.fillParameterPage(fieldSet);
        fieldSet.addField(new FieldLabel(getFieldLOAD_TYPE(), I18n.get(SBFFormStr.labelOperationMode)));
    }

    private IntStrComboBox getFieldLOAD_TYPE() {
        if (null == fieldLOAD_TYPE) {
            fieldLOAD_TYPE = new IntStrComboBox();
            fieldLOAD_TYPE.setToolTip(I18n.get(SBFFormStr.hintOperationMode));
            fieldLOAD_TYPE.setAutoValidate(true);
            fieldLOAD_TYPE.add(loadTypeModel);
            fieldLOAD_TYPE.add(new IntStrComboBoxModel(1, I18n.get(SBFFormStr.labelCheck)));
            fieldLOAD_TYPE.setValue(loadTypeModel);
        }
        return fieldLOAD_TYPE;
    }

    @Override
    public List<ParamInfo> getParams() {
        params.clear();
        params.add(new IntegerParamInfo(ImportOperationParamConst.LOAD_TYPE_PARAM, getFieldLOAD_TYPE().getIntValue()));
        return params;
    }
}
