package ru.sbsoft.client.components.operation;

import java.util.ArrayList;
import java.util.List;
import ru.sbsoft.client.I18n;
import ru.sbsoft.client.components.form.GeneralParamForm;
import ru.sbsoft.client.components.form.ParamHandlerCollector;
import ru.sbsoft.client.components.form.handler.IFieldHandler;
import ru.sbsoft.client.components.form.handler.param.EnumHandler;
import ru.sbsoft.client.components.form.handler.param.LongHandler;
import ru.sbsoft.client.components.form.handler.param.TextHandler;
import ru.sbsoft.client.components.form.handler.param.YearMonthDayHandler;
import ru.sbsoft.shared.api.i18n.consts.SBFEditorStr;
import ru.sbsoft.shared.api.i18n.consts.SBFGeneralStr;
import ru.sbsoft.shared.consts.CustomReportType;
import ru.sbsoft.shared.exceptions.ApplicationException;
import ru.sbsoft.shared.model.CustomReportInfo;
import ru.sbsoft.shared.model.CustomReportParamModel;
import ru.sbsoft.shared.param.BigDecimalParamInfo;
import ru.sbsoft.shared.param.LongParamInfo;
import ru.sbsoft.shared.param.ParamInfo;
import ru.sbsoft.shared.param.StringParamInfo;
import ru.sbsoft.shared.param.YearMonthDayParamInfo;

/**
 *
 * @author sokolov
 */
public class CustomReportParamForm extends GeneralParamForm {
    
    private final CustomReportInfo info;
    private final EnumHandler<CustomReportType> typeHandler;
    private final List<IFieldHandler> params = new ArrayList<>();
    
    public CustomReportParamForm(CustomReportInfo info) {
        super(I18n.get(SBFGeneralStr.enumCustomReport));
        this.info = info;
        typeHandler = new EnumHandler<CustomReportType>(I18n.get(SBFEditorStr.labelReportFormat)).setReq().setItems(CustomReportType.values());
        if (info.getParams() != null) {
            info.getParams().forEach(pm -> params.add(createParamHandler(pm)));
        }
    }

    @Override
    protected void addHandlers(ParamHandlerCollector hc) {
        hc.add(typeHandler);
        params.forEach(fh -> hc.add(fh));
    }

    @Override
    public List<ParamInfo> getParams() {
        List<ParamInfo> lp = super.getParams();
        lp.add(new BigDecimalParamInfo(ParamInfo.REPORT_ID, info.getReportId()));
        lp.add(new StringParamInfo(ParamInfo.REPORT_FORMAT, typeHandler.getVal().getCode()));
        params.forEach(fh -> lp.add(getParamInfo(fh)));
        return lp;
    }
    
    private IFieldHandler createParamHandler(CustomReportParamModel pm) {
        switch (pm.getParamType()) {
            case STRING:
                return new TextHandler(pm.getCode(), pm.getName()).setReq();
            case LONG:
                return new LongHandler(pm.getCode(), pm.getName()).setReq();
            case DATE:
                return new YearMonthDayHandler(pm.getCode(), pm.getName()).setReq();
            default:
                throw new ApplicationException("Unknown parameter type " + pm.getParamType().getCode());
        }
    }
    
    private ParamInfo getParamInfo(IFieldHandler fh) {
        if (fh instanceof TextHandler) {
            TextHandler tH = (TextHandler)fh;
            return new StringParamInfo(tH.getName(), tH.getVal());
        }
        if (fh instanceof LongHandler) {
            LongHandler lH = (LongHandler)fh;
            return new LongParamInfo(lH.getName(), lH.getVal());
        }
        if (fh instanceof YearMonthDayHandler) {
            YearMonthDayHandler ymdH = (YearMonthDayHandler)fh;
            return new YearMonthDayParamInfo(ymdH.getName(), ymdH.getVal());
        }
        throw new ApplicationException("Unknown type field handler " + fh.getClass().getName());
    }
    
}
