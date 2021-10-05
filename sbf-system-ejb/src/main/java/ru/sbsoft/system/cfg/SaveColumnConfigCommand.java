package ru.sbsoft.system.cfg;

import java.util.List;
import ru.sbsoft.shared.GridContext;
import ru.sbsoft.shared.meta.config.ColumnCfg;
import static ru.sbsoft.system.dao.common.utils.StorageObjectType.COLUMN;
import ru.sbsoft.system.grid.SYS_COLUMN;
import ru.sbsoft.system.grid.SYS_OBJECT;
import ru.sbsoft.system.grid.SYS_OBJ_STORAGE;

/**
 * Сохранение колонок
 *
 * @author balandin
 * @since Aug 6, 2015
 */
public class SaveColumnConfigCommand extends ConfigCommand {

    public SaveColumnConfigCommand(IConfigCommandContext context, String applicationPrefix, GridContext gridContext) {
        super(context, applicationPrefix, gridContext);
    }

    public void exec(List<ColumnCfg> columns) {
        SYS_OBJECT sysObject = getSysObject(true);
        SYS_OBJ_STORAGE sysObjStorage = getOrCreateSysObjStore(sysObject, COLUMN, context.getUserName());

        delete(SYS_COLUMN.class, sysObjStorage.getRECORD_ID());

        int num = 0;
        for (ColumnCfg c : columns) {
            SYS_COLUMN e = new SYS_COLUMN();
            e.setSYS_OBJECT_STORAGE_RECORD_ID(sysObjStorage.getRECORD_ID());
            e.setNUM(++num);
            e.setALIAS(c.getAlias());
            e.setWIDTH(c.getWidth());
            e.setVISIBLE(c.isVisible() ? 1 : 0);
            context.getEm().persist(e);
        }
    }
}
