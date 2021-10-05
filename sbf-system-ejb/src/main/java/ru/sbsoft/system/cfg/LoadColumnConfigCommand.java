package ru.sbsoft.system.cfg;

import java.util.ArrayList;
import java.util.List;
import ru.sbsoft.shared.GridContext;
import ru.sbsoft.shared.meta.config.ColumnCfg;
import ru.sbsoft.system.dao.common.utils.QueryBuilder;
import static ru.sbsoft.system.dao.common.utils.StorageObjectType.COLUMN;
import ru.sbsoft.system.grid.SYS_COLUMN;
import ru.sbsoft.system.grid.SYS_OBJECT;
import ru.sbsoft.system.grid.SYS_OBJ_STORAGE;

/**
 * Загрузка колонок
 *
 * @author balandin
 * @since Aug 6, 2015
 */
public class LoadColumnConfigCommand extends ConfigCommand {

    public LoadColumnConfigCommand(IConfigCommandContext context, String applicationPrefix, GridContext gridContext) {
        super(context, applicationPrefix, gridContext);
    }

    public List<ColumnCfg> exec() {
        SYS_OBJECT sysObject = getSysObject(false);
        if (sysObject == null) {
            return null;
        }

        SYS_OBJ_STORAGE sysObjStorage = getSysObjStore(sysObject, COLUMN, context.getUserName());
        if (sysObjStorage == null) {
            return null;
        }

        List<SYS_COLUMN> eList = new QueryBuilder(context.getEm()).add("select o from ").add(SYS_COLUMN.class).add(" o where")
                .eq("o.SYS_OBJECT_STORAGE_RECORD_ID", sysObjStorage.getRECORD_ID())
                .add(" order by o.NUM")
                .query().getResultList();

        List<ColumnCfg> result = new ArrayList<>();
        for (SYS_COLUMN e : eList) {
            ColumnCfg c = new ColumnCfg();
            c.setAlias(e.getALIAS());
            c.setWidth(e.getWIDTH());
            c.setVisible(e.getVISIBLE() == 1);
            result.add(c);
        }
        return result;
    }
}
