package ru.sbsoft.operation;

import ru.sbsoft.dao.AbstractTemplate;
import ru.sbsoft.meta.ColumnsInfo;
import static ru.sbsoft.meta.columns.consts.ColumnDefinitions.KEY;
import static ru.sbsoft.meta.columns.consts.ColumnDefinitions.TIMESTAMP;
import ru.sbsoft.meta.context.GlobalQueryContext;
import ru.sbsoft.shared.TemplateParameterConst;
import ru.sbsoft.shared.api.i18n.consts.SBFGeneralStr;

/**
 * Шаблон для представления журнала операций.
 *
 * @author Fedor Resnyanskiy, SBSOFT
 */
public class MultiOperationStatusTemplate extends AbstractTemplate {

    public MultiOperationStatusTemplate() {
        setDefaultSort("RECORD_ID");
    }

    @Override
    public ColumnsInfo createColumns() {
        final ColumnsInfo c = new ColumnsInfo();
        c.add(KEY, "RECORD_ID");
        c.add(TEXT, 150, SBFGeneralStr.labelCode, "OPERATION_CODE").setVisible(false);
        c.add(TEXT, 150, SBFGeneralStr.labelName, "TITLE");
        c.add(TIMESTAMP, 140, SBFGeneralStr.labelCreate, "CREATE_DATE");
        c.add(TIMESTAMP, 140, SBFGeneralStr.labelRunning, "RUN_DATE");
        c.add(NUMBER, 80, SBFGeneralStr.labelProgress, "PROGRESS");
        c.add(TEXT, 80, SBFGeneralStr.labelStatus, "STATUS");
        return c;
    }

    @Override
    public void buildFromClause(StringBuilder sb) {
        sb.append("SR_MULTI_OPERATION");
    }

    @Override
    public void buildWhereClause(StringBuilder sb) {
        sb
                .append(" and CREATE_USER = :" + GlobalQueryContext.CURRENT_USERNAME_PARAMETER)
                .append(" and (APP_CODE = :" + GlobalQueryContext.CURRENT_APP_CODE_PARAMETER)
                .append(" or APP_CODE = :" + GlobalQueryContext.JMS_PARAMETER).append(")")
                .append(" and ((STATUS in ('CREATED', 'READY_TO_START', 'STARTED', 'CANCEL')) ")
                .append("   or (STATUS in ('CANCELED','ERROR','DONE') and VISIBLE = '1')) ")
                .append(" #if($DATE_FROM) and CREATE_DATE >= #if($MS_SQL) cast(:DATE_FROM as date) #else trunc(CAST(:DATE_FROM AS date)) #end  #end ")
                .append(" #if($" + TemplateParameterConst.CURRENT_MODULE_CODE_PARAMETER + ") and MODULE_CODE = :" + TemplateParameterConst.CURRENT_MODULE_CODE_PARAMETER + " #end ");

    }

}
