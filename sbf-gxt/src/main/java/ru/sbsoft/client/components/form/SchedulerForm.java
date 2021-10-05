package ru.sbsoft.client.components.form;

import ru.sbsoft.sbf.gxt.components.VerticalFieldSet;
import ru.sbsoft.sbf.gxt.components.CheckBoxField;
import com.google.gwt.core.client.GWT;
import com.sencha.gxt.widget.core.client.TabItemConfig;
import com.sencha.gxt.widget.core.client.TabPanel;
import com.sencha.gxt.widget.core.client.form.ComboBox;
import com.sencha.gxt.widget.core.client.form.FieldLabel;
import com.sencha.gxt.widget.core.client.form.IntegerField;
import com.sencha.gxt.widget.core.client.form.TextField;
import ru.sbsoft.client.I18n;
import ru.sbsoft.client.components.field.DateTimeField;
import ru.sbsoft.shared.FormContext;
import ru.sbsoft.shared.api.i18n.consts.SBFGeneralStr;
import ru.sbsoft.shared.model.SchedulerModel;
import ru.sbsoft.shared.model.enums.SchedulerStatus;

/**
 * Форма для редактирования параметров планировщика операций.
 *
 * @author Fedor Resnyanskiy, SBSOFT
 */
public class SchedulerForm extends BaseForm<SchedulerModel> {

    private TextField cronExpressionField;
    private TextField operationCodeField;
    private IntegerField counterField;
    private DateTimeField enableFromField;
    private DateTimeField enableToField;
    private CheckBoxField enabledField;
    private CheckBoxField notifyField;
    private CheckBoxField ignoreBackField;
    private ComboBox<SchedulerStatus> statusField;
    private DateTimeField lastRunField;
    private DateTimeField prevScheduleField;
    private DateTimeField nextScheduleField;

    private FormContext formContext;

    /**
     * @param formContext контекст формы в приложении.
     */
    public SchedulerForm(FormContext formContext) {
        super(I18n.get(SBFGeneralStr.labelScheduler), false);
        this.formContext = formContext;
    }

    @Override
    protected void dataToForm(SchedulerModel dataModel) {
        cronExpressionField.setValue(dataModel.getCRON_EXPRESSION());
        operationCodeField.setValue(dataModel.getOPERATION_CODE());
        counterField.setValue(dataModel.getCOUNTER());
        enableFromField.setValue(dataModel.getENABLE_FROM());
        enableToField.setValue(dataModel.getENABLE_TO());
        enabledField.setValue(dataModel.isENABLED());
        notifyField.setValue(dataModel.isNOTIFY());
        ignoreBackField.setValue(dataModel.isIGNORE_BACK());
        statusField.setValue(dataModel.getSTATUS());
        lastRunField.setValue(dataModel.getLAST_RUN());
        prevScheduleField.setValue(dataModel.getPREV_SCHEDULE());
        nextScheduleField.setValue(dataModel.getNEXT_SCHEDULE());
    }

    @Override
    protected void formToData(SchedulerModel dataModel) {
        dataModel.setCRON_EXPRESSION(cronExpressionField.getValue());
        dataModel.setOPERATION_CODE(operationCodeField.getValue());
        dataModel.setCOUNTER(counterField.getValue());
        dataModel.setENABLE_FROM(enableFromField.getValue());
        dataModel.setENABLE_TO(enableToField.getValue());
        dataModel.setENABLED(enabledField.getValue());
        dataModel.setNOTIFY(notifyField.getValue());
        dataModel.setIGNORE_BACK(ignoreBackField.getValue());
        dataModel.setMODULE_CODE(GWT.getModuleName());
    }

    @Override
    protected void createEditors(TabPanel tabEditors) {
        final SimplePageFormContainer tab = new SimplePageFormContainer();
        tabEditors.add(tab, new TabItemConfig(I18n.get(SBFGeneralStr.labelScheduler)));
        tab.setLabelWidth(220);

        final VerticalFieldSet fieldSet = new VerticalFieldSet(I18n.get(SBFGeneralStr.labelLock));
        tab.addFieldSet(fieldSet);

        cronExpressionField = new TextField();
        operationCodeField = new TextField();
        counterField = new IntegerField();
        enableFromField = new DateTimeField();
        enableToField = new DateTimeField();
        enabledField = new CheckBoxField();
        notifyField = new CheckBoxField();
        ignoreBackField = new CheckBoxField();

        statusField = ComboBoxUtils.createNamedComboBox(SchedulerStatus.class);
        statusField.setEditable(false);
        lastRunField = new DateTimeField();
        prevScheduleField = new DateTimeField();
        nextScheduleField = new DateTimeField();

        statusField.setReadOnly(true);
        lastRunField.setReadOnly(true);
        prevScheduleField.setReadOnly(true);
        nextScheduleField.setReadOnly(true);

        fieldSet.add(new FieldLabel(cronExpressionField, I18n.get(SBFGeneralStr.labelCronScheduler)));
        fieldSet.add(new FieldLabel(operationCodeField, I18n.get(SBFGeneralStr.labelCodOperation)));
        fieldSet.add(new FieldLabel(counterField, I18n.get(SBFGeneralStr.labelCountLaunches)));
        fieldSet.add(new FieldLabel(enableFromField, I18n.get(SBFGeneralStr.labelDateStart)));
        fieldSet.add(new FieldLabel(enableToField, I18n.get(SBFGeneralStr.labelDateTerminate)));
        fieldSet.add(new FieldLabel(enabledField, I18n.get(SBFGeneralStr.labelSchedulerActive)));
        fieldSet.add(new FieldLabel(notifyField, I18n.get(SBFGeneralStr.labelNotifyOperation)));
        fieldSet.add(new FieldLabel(ignoreBackField, I18n.get(SBFGeneralStr.labelIgnoreMissing)));

        fieldSet.add(new FieldLabel(statusField, I18n.get(SBFGeneralStr.labelCurrentStatus)));
        fieldSet.add(new FieldLabel(lastRunField, I18n.get(SBFGeneralStr.labelLastRun)));
        fieldSet.add(new FieldLabel(prevScheduleField, I18n.get(SBFGeneralStr.labelPrevIteration)));
        fieldSet.add(new FieldLabel(nextScheduleField, I18n.get(SBFGeneralStr.labelNextIteration)));

    }

    @Override
    protected String getSecurityId() {
        return "SR_SCHEDULER";
    }

    @Override
    protected FormContext getFormContext() {
        return formContext;
    }

}
