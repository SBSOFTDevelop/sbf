package ru.sbsoft.dao;

import ru.sbsoft.meta.ColumnsInfo;
import ru.sbsoft.shared.consts.Dict;

/**
 *
 * @author sokolov
 */
public class CustomReportBrowserTemplate extends AbstractTemplate {

    public CustomReportBrowserTemplate() {
        setDefaultSort("r.report_name");
    }

    @Override
    protected ColumnsInfo createColumns() {
        ColumnsInfo c = new ColumnsInfo();
        c.add(KEY, "r.id");
        c.add(VCHAR, 600, "Наименование", "r.report_name").setAutoExpand(true);
        return c;
    }

    @Override
    public void buildFromClause(StringBuilder sb) {
        sb.append("SR_REPORT r\n");
    }
    
    @Override
    public void buildWhereClause(StringBuilder sb) {
        sb.append(String.format("AND r.grid_enum = #if ($%1$s) :%1$s #else null #end\n", Dict.CGRID_CODE));
    }
    
}
