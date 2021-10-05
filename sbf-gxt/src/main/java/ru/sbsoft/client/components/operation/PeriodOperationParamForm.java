package ru.sbsoft.client.components.operation;

import com.sencha.gxt.cell.core.client.form.ComboBoxCell.TriggerAction;
import com.sencha.gxt.widget.core.client.form.FieldLabel;
import com.sencha.gxt.widget.core.client.form.SimpleComboBox;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import ru.sbsoft.client.I18n;
import ru.sbsoft.client.components.field.DateField;
import ru.sbsoft.client.components.form.SimpleLabelProvider;
import ru.sbsoft.sbf.gxt.components.VerticalFieldSet;
import ru.sbsoft.shared.model.FileFormat;
import ru.sbsoft.shared.param.DateParamInfo;
import ru.sbsoft.shared.param.ParamInfo;
import ru.sbsoft.shared.param.StringParamInfo;
import ru.sbsoft.shared.api.i18n.consts.SBFEditorStr;

/**
 * Форма параметров операции, содержащая параметры темпорального диапазона и выбор формата экспортируемого файла.
 * Имеет единственное использование в проекте WOZM.
 * @author balandin
 * @since Mar 27, 2013 5:12:58 PM
 */
public class PeriodOperationParamForm extends BaseOperationParamForm {

    private DateField startDateField;
    private DateField endDateField;
    private SimpleComboBox<FileFormat> formatField;

    public PeriodOperationParamForm(String header) {
        super(header);
    }

    private DateField getStartDateField() {
        if (startDateField == null) {
            startDateField = new DateField();
            startDateField.setValue(new Date());
            startDateField.setAllowBlank(false);
        }
        return startDateField;
    }

    private DateField getEndDateField() {
        if (endDateField == null) {
            endDateField = new DateField();
            endDateField.setValue(new Date());
            endDateField.setAllowBlank(false);
        }
        return endDateField;
    }

    private SimpleComboBox<FileFormat> getFormatField() {
        if (formatField == null) {
            formatField = new SimpleComboBox<FileFormat>(new SimpleLabelProvider<FileFormat>());
            formatField.setAllowBlank(false);
            formatField.setAllowBlank(false);
            formatField.setEditable(false);
            formatField.setTriggerAction(TriggerAction.ALL);
        }
        return formatField;
    }

    @Override
    protected void fillParameterPage(final VerticalFieldSet fieldSet) {
        fieldSet.addField(new FieldLabel(getStartDateField(), I18n.get(SBFEditorStr.labelStartDate) + " (*)"));
        fieldSet.addField(new FieldLabel(getEndDateField(), I18n.get(SBFEditorStr.labelEndDate) + " (*)"));
        fieldSet.addField(new FieldLabel(getFormatField(), I18n.get(SBFEditorStr.labelFormatDischarge)));
    }

    @Override
    public List<ParamInfo> getParams() {
        final List<ParamInfo> params = new ArrayList<ParamInfo>(1);
        params.add(new DateParamInfo(ParamInfo.START_DATE, getStartDateField().getValue()));
        params.add(new DateParamInfo(ParamInfo.END_DATE, getEndDateField().getValue()));
        params.add(new StringParamInfo(ParamInfo.FILE_FORMAT, getFormatField().getValue().name()));
        return params;
    }

    public void addExportFormat(FileFormat fileFormat) {
        getFormatField().add(fileFormat);
        if (getFormatField().getStore().size() == 1) {
            getFormatField().setValue(fileFormat);
        }
    }
}
