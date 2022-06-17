package ru.sbsoft.client.components.form.handler;

import ru.sbsoft.svc.cell.core.client.form.ComboBoxCell;
import ru.sbsoft.svc.widget.core.client.form.SimpleComboBox;
import ru.sbsoft.client.I18n;
import ru.sbsoft.client.components.form.SimpleLabelProvider;
import ru.sbsoft.shared.FilterInfo;
import ru.sbsoft.shared.api.i18n.consts.SBFGeneralStr;
import ru.sbsoft.shared.filter.StringFilterInfo;
import ru.sbsoft.shared.model.FileFormat;
import ru.sbsoft.shared.param.ParamInfo;
import ru.sbsoft.shared.param.StringParamInfo;

/**
 *
 * @author Kiselev
 * @param <SelfType>
 */
public class ReportFormatHandler<SelfType extends ReportFormatHandler<SelfType>> extends BaseHandler<SimpleComboBox<FileFormat>, String, SelfType> {

    private final FileFormat[] formats;

    public ReportFormatHandler(FileFormat... formats) {
        super(ParamInfo.FILE_FORMAT, I18n.get(SBFGeneralStr.labelFormatUpload));
        this.formats = formats != null && formats.length > 0 ? formats : FileFormat.values();
    }

    @Override
    protected SimpleComboBox<FileFormat> createField() {
        SimpleComboBox<FileFormat> formatField = new SimpleComboBox<FileFormat>(new SimpleLabelProvider<FileFormat>());
        formatField.setAllowBlank(false);
        formatField.setAllowBlank(false);
        formatField.setEditable(false);
        formatField.setTriggerAction(ComboBoxCell.TriggerAction.ALL);
        for (FileFormat item : formats) {
            formatField.add(item);
        }
        formatField.setValue(formats[0]);
        return formatField;
    }

    @Override
    protected FilterInfo createFilter() {
        return new StringFilterInfo(null, getVal());
    }

    @Override
    public SelfType setVal(String val) {
        FileFormat mod = null;
        if (val != null) {
            for (FileFormat mod2 : getField().getStore().getAll()) {
                if (val.equals(mod2.name())) {
                    mod = mod2;
                    break;
                }
            }
        }
        getField().setValue(mod);
        return (SelfType) this;
    }

    @Override
    public String getVal() {
        return getField().getCurrentValue().name();
    }

    @Override
    protected ParamInfo createParamInfo() {
        return new StringParamInfo(null, getVal());
    }
}
