package ru.sbsoft.operation;

import ru.sbsoft.dao.AbstractTemplate;
import ru.sbsoft.meta.ColumnsInfo;
import static ru.sbsoft.meta.columns.consts.ColumnDefinitions.KEY;
import static ru.sbsoft.meta.columns.consts.ColumnDefinitions.VCHAR;
import ru.sbsoft.meta.context.GlobalQueryContext;
import ru.sbsoft.shared.TemplateParameterConst;

/**
 * Шаблон журнала планировщиков.
 *
 * @author Fedor Resnyanskiy, SBSOFT
 */
public class SchedulerTemplate extends AbstractTemplate {

    @Override
    public ColumnsInfo createColumns() {
        final ColumnsInfo c = new ColumnsInfo();
        c.add(KEY, "RECORD_ID");
        c.add(VCHAR, 150, "Активен", "case when ENABLED = true then 'да' else 'нет' end ", "ENABLED");
        c.add(VCHAR, 150, "Операция", "OPERATION_CODE");
        c.add(VCHAR, 150, "CRON-план запуска", "CRON_EXPRESSION");
        c.add(VCHAR, 120, "Статус", "STATUS");
        c.add(VCHAR, 200, "Последний запуск", "LAST_RUN");
        c.add(VCHAR, 200, "Следующая итерация", "NEXT_SCHEDULE");
        return c;
    }

    @Override
    public void buildFromClause(StringBuilder sb) {
        sb.append("SR_SCHEDULER");
    }

    @Override
    public void buildWhereClause(StringBuilder sb) {
        sb
            .append(" and USERNAME = :" + GlobalQueryContext.CURRENT_USERNAME_PARAMETER + " ")
            .append(" and APP_CODE = :" + GlobalQueryContext.CURRENT_APP_CODE_PARAMETER + " ")
            .append(" #if($" + TemplateParameterConst.CURRENT_MODULE_CODE_PARAMETER + ") and MODULE_CODE = :" + TemplateParameterConst.CURRENT_MODULE_CODE_PARAMETER + " #end ");
    }

}
