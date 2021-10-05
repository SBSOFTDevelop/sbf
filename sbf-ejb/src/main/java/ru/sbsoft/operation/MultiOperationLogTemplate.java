package ru.sbsoft.operation;

import ru.sbsoft.dao.AbstractTemplate;
import ru.sbsoft.meta.ColumnsInfo;
import ru.sbsoft.meta.columns.ColumnInfo;
import ru.sbsoft.meta.columns.ColumnKind;
import ru.sbsoft.meta.columns.style.condition.Eq;
import ru.sbsoft.meta.columns.style.condition.Exp;
import ru.sbsoft.meta.columns.style.condition.Substr;
import ru.sbsoft.shared.api.i18n.consts.SBFGeneralStr;
import ru.sbsoft.shared.grid.style.CStyle;
import ru.sbsoft.shared.grid.style.ColorConst;
import ru.sbsoft.shared.grid.style.FontWeight;

/**
 * Представление журнала операций.
 *
 * @author Fedor Resnyanskiy, SBSOFT
 */
public class MultiOperationLogTemplate extends AbstractTemplate {

    public MultiOperationLogTemplate() {
        setDefaultSort("CREATE_DATE");
    }

    @Override
    public ColumnsInfo createColumns() {
        final ColumnsInfo c = new ColumnsInfo();
        c.add(KEY, "RECORD_ID");
        c.add(TIMESTAMP, 150, SBFGeneralStr.labelTime, "CREATE_DATE");
        c.add(VCHAR, 24, "!", "case when TRACE is null then '' else '...' end", "TRACE_FLAG").setToolTip("Признак ошибки")
                .addStyle(new CStyle().setFontWeight(FontWeight.bold));
        c.add(VCHAR, 600, SBFGeneralStr.labelMessage, "MESSAGE", "MESSAGE").setAutoExpand(true);
        c.add(VCHAR, 600, SBFGeneralStr.labelInfoError, " #if ($EX or $SYSTEM_EXPORT) TRACE #else null #end", "TRACE").setVisible(false);
        ColumnInfo<String> ltype = c.add(ColumnKind.VCHAR, 100, SBFGeneralStr.labelType, "TYPE_VALUE", "LTYPE").setVisible(false);

        c.addStyle(new CStyle().setColor(ColorConst.Blue), new Exp(new Eq<>(ltype, "START")).or(new Eq<>(ltype, "FINISH_OK")));
        c.addStyle(new CStyle().setColor(ColorConst.Red), new Substr(ltype, "ERROR"));
        c.addStyle(new CStyle().setColor(0xff8000), new Eq<>(ltype, "WARNING"));

        return c;
    }

    @Override
    public void buildFromClause(StringBuilder sb) {
        sb.append("SR_MULTI_OPERATION_LOG t1\n");
    }

    @Override
    public void buildWhereClause(StringBuilder sb) {
        sb.append(" AND t1.OPERATION_RECORD_ID = :OPERATION_RECORD_ID\n");
    }

}
