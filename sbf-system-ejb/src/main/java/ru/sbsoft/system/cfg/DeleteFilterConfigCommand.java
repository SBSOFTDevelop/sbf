package ru.sbsoft.system.cfg;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import ru.sbsoft.shared.GridContext;
import ru.sbsoft.shared.meta.filter.FilterBox;
import ru.sbsoft.shared.meta.filter.StoredFilterPath;
import ru.sbsoft.system.dao.common.utils.StorageObjectType;
import static ru.sbsoft.system.dao.common.utils.StorageObjectType.SYSTEM_FILTER;
import static ru.sbsoft.system.dao.common.utils.StorageObjectType.USER_FILTER;
import ru.sbsoft.system.grid.SYS_FILTER;
import ru.sbsoft.system.grid.SYS_FILTER_LOOKUP;
import ru.sbsoft.system.grid.SYS_OBJECT;
import ru.sbsoft.system.grid.SYS_OBJ_STORAGE;

/**
 * Сохранение фильтров
 *
 * @author balandin
 * @since Aug 5, 2015
 */
public class DeleteFilterConfigCommand extends ComplexFilterCommand {

    public DeleteFilterConfigCommand(IConfigCommandContext context, String applicationPrefix, GridContext gridContext) {
        super(context, applicationPrefix, gridContext);
    }

    public Map<StoredFilterPath, Exception> exec(Collection<StoredFilterPath> filterPaths) {
        Map<StoredFilterPath, Exception> errors = new HashMap<>();
        SYS_OBJECT sysObject = getSysObject(false);
        if (sysObject != null) {
            if (filterPaths != null && !filterPaths.isEmpty()) {
                StoredFilterPath currentPath = norm(getCurrentFilterPath(sysObject));
                for (StoredFilterPath filterPath : new HashSet<>(filterPaths)) {
                    try {
                        StoreKey filterKey = checkOwnerGranted(new StoreKey(filterPath));
                        if(currentPath != null && equals(filterPath, currentPath)){
                            FilterBox f = getFilter(filterPath);
                            setFilter(new FilterBox(f.getFilter(), null));
                            setCurrentFilterPath(sysObject, null);
                            currentPath = null;
                        }
                        deleteFilterGroup(sysObject, SYSTEM_FILTER, filterKey);
                        deleteFilterGroup(sysObject, USER_FILTER, filterKey);
                    } catch (Exception ex) {
                        ex.fillInStackTrace();
                        errors.put(filterPath, ex);
                    }
                }
            }
        }
        return errors.isEmpty() ? null : errors;
    }

    private void deleteFilterGroup(final SYS_OBJECT sysObject, StorageObjectType type, StoreKey filterKey) {
        SYS_OBJ_STORAGE sysObjStorage = getSysObjStore(sysObject, type, filterKey);
        if (sysObjStorage != null) {
            long storageRecordID = sysObjStorage.getRECORD_ID();
            delete(SYS_FILTER_LOOKUP.class, storageRecordID);
            delete(SYS_FILTER.class, storageRecordID);
            context.getEm().remove(sysObjStorage);
        }
    }

}
