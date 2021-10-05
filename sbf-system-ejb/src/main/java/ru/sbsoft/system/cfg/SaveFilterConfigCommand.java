package ru.sbsoft.system.cfg;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import ru.sbsoft.shared.BooleanOperator;
import ru.sbsoft.shared.ColumnsFilterInfo;
import ru.sbsoft.shared.FilterInfo;
import ru.sbsoft.shared.FilterInfoGroup;
import ru.sbsoft.shared.FiltersBean;
import ru.sbsoft.shared.GridContext;
import ru.sbsoft.shared.filter.LookUpFilterInfo;
import ru.sbsoft.shared.meta.filter.StoredFilterPath;
import ru.sbsoft.system.dao.common.utils.FilterPersist;
import ru.sbsoft.system.dao.common.utils.LookupPersist;
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
public class SaveFilterConfigCommand extends ConfigCommand {

    private long storageRecordID;
    private AtomicInteger num;

    public SaveFilterConfigCommand(IConfigCommandContext context, String applicationPrefix, GridContext gridContext) {
        super(context, applicationPrefix, gridContext);
    }

    public void exec(FiltersBean filters, StoredFilterPath filterPath) {
        StoreKey filterKey = checkOwnerGranted(new StoreKey(filterPath));
        saveFilterGroup(new FilterInfoGroup(BooleanOperator.AND, filters.getSystemFilters()), SYSTEM_FILTER, filterKey);
        saveFilterGroup(filters.getUserFilters(), USER_FILTER, filterKey);
    }

    private void saveFilterGroup(FilterInfoGroup group, StorageObjectType type, StoreKey filterKey) {
        num = new AtomicInteger(0);

        SYS_OBJECT sysObject = getSysObject(true);
        SYS_OBJ_STORAGE sysObjStorage = getOrCreateSysObjStore(sysObject, type, filterKey);

        storageRecordID = sysObjStorage.getRECORD_ID();

        delete(SYS_FILTER_LOOKUP.class, storageRecordID);
        delete(SYS_FILTER.class, storageRecordID);

        if (group != null && group.getChildFilters().size() > 0) {
            saveFiltersGroup(null, group);
        }
    }

    private void saveFiltersGroup(SYS_FILTER parentEntity, FilterInfoGroup group) {
        SYS_FILTER e = createNewEntityFilter(parentEntity);
        e.setTYPE(group.getValue() == null ? null : group.getValue().name());
        saveFilters(e, group.getChildFilters());
    }

    private void saveFilters(SYS_FILTER parentEntity, List<FilterInfo> childs) {
        for (FilterInfo f : childs) {
            if (f instanceof FilterInfoGroup) {
                saveFiltersGroup(parentEntity, (FilterInfoGroup) f);
            } else {
                SYS_FILTER e = createNewEntityFilter(parentEntity);
                e.setTYPE(f.getType().name());
                e.setCONDITION(f.getComparison().getCondition().name());
                e.setNOPE(f.isNotExpression() ? 1 : 0);
                e.setSENS(f.isCaseSensitive() ? 1 : 0);
                e.setPARAM1(f.getColumnName());

                if (f instanceof LookUpFilterInfo) {
                    LookupPersist.save(context.getEm(), storageRecordID, e.getNUM(), ((LookUpFilterInfo) f).getValue());
                } else if (f instanceof ColumnsFilterInfo) {
                    e.setPARAM3(((ColumnsFilterInfo) f).getColumnName2());
                } else {
                    e.setPARAM2(FilterPersist.valueToString(f.getType(), f.getValue()));
                    if (f.getSecondValue() != null) {
                        e.setPARAM3(FilterPersist.valueToString(f.getType(), f.getSecondValue()));
                    }
                }
            }
        }
    }

    private SYS_FILTER createNewEntityFilter(SYS_FILTER parentEntity) {
        SYS_FILTER e = new SYS_FILTER();
        e.setSYS_OBJECT_STORAGE_RECORD_ID(storageRecordID);
        e.setNUM(num.incrementAndGet());
        e.setPARENT_NUM(parentEntity == null ? 0 : parentEntity.getNUM());
        context.getEm().persist(e);
        return e;
    }
}
