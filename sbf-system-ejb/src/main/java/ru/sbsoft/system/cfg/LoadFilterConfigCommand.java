package ru.sbsoft.system.cfg;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;
import ru.sbsoft.common.Enums;
import ru.sbsoft.shared.BooleanOperator;
import ru.sbsoft.shared.ColumnsFilterInfo;
import ru.sbsoft.shared.Condition;
import ru.sbsoft.shared.FilterInfo;
import ru.sbsoft.shared.FilterInfoGroup;
import ru.sbsoft.shared.FilterTypeEnum;
import ru.sbsoft.shared.FiltersBean;
import ru.sbsoft.shared.GridContext;
import ru.sbsoft.shared.filter.FilterInfoImpl;
import ru.sbsoft.shared.filter.LookUpFilterInfo;
import ru.sbsoft.shared.meta.filter.StoredFilterPath;
import ru.sbsoft.system.dao.common.utils.FilterPersist;
import ru.sbsoft.system.dao.common.utils.LookupPersist;
import ru.sbsoft.system.dao.common.utils.QueryBuilder;
import ru.sbsoft.system.dao.common.utils.StorageObjectType;
import static ru.sbsoft.system.dao.common.utils.StorageObjectType.SYSTEM_FILTER;
import static ru.sbsoft.system.dao.common.utils.StorageObjectType.USER_FILTER;
import ru.sbsoft.system.grid.SYS_FILTER;
import ru.sbsoft.system.grid.SYS_OBJECT;
import ru.sbsoft.system.grid.SYS_OBJ_STORAGE;

/**
 * Загрузка фильтров
 *
 * @author balandin
 * @since Aug 6, 2015
 */
public class LoadFilterConfigCommand extends ConfigCommand {

    public LoadFilterConfigCommand(IConfigCommandContext context, String applicationPrefix, GridContext gridContext) {
        super(context, applicationPrefix, gridContext);
    }

    public FiltersBean exec(StoredFilterPath filterPath) {
        StoreKey filterKey = new StoreKey(filterPath);
        final FilterInfoGroup sysGroupFilter = loadFilterGroup(SYSTEM_FILTER, filterKey);
        final FilterInfoGroup usrGroupFilter = loadFilterGroup(USER_FILTER, filterKey);
        return new FiltersBean(sysGroupFilter == null ? null : sysGroupFilter.getChildFilters(), usrGroupFilter);
    }

    public FilterInfoGroup loadFilterGroup(StorageObjectType storageObjectType, StoreKey filterKey) {
        EntityManager em = context.getEm();
        SYS_OBJECT sysObject = getSysObject(false);
        if (sysObject == null) {
            return null;
        }

        SYS_OBJ_STORAGE sysObjStorage = getSysObjStore(sysObject, storageObjectType, filterKey);
        if (sysObjStorage == null) {
            return null;
        }

        List<SYS_FILTER> eList = new QueryBuilder(em).add("select o from ").add(SYS_FILTER.class).add(" o where")
                .eq("o.SYS_OBJECT_STORAGE_RECORD_ID", sysObjStorage.getRECORD_ID())
                .add(" order by o.NUM")
                .query().getResultList();

        Map<Integer, FilterInfoGroup> cache = new HashMap<>();
        for (SYS_FILTER e : eList) {

            String filterType = e.getTYPE();
            FilterInfoGroup g = null;
            if (filterType == null) {
                g = new FilterInfoGroup();
            } else {
                BooleanOperator operator = Enums.find(BooleanOperator.class, filterType);
                if (operator != null) {
                    g = new FilterInfoGroup(operator);
                }
            }

            if (g != null) {
                g.setChildFilters(new ArrayList<FilterInfo>());
                cache.put(e.getNUM(), g);
            }

            FilterInfoGroup parentFilterGroup = null;
            int parentNum = e.getPARENT_NUM();
            if (parentNum != 0) {
                parentFilterGroup = cache.get(parentNum);
                if (parentFilterGroup == null) {
                    throw new IllegalArgumentException("parent not found " + e.getSYS_OBJECT_STORAGE_RECORD_ID() + " / " + e.getPARENT_NUM());
                }
            }

            FilterInfo f = g;
            if (f == null) {
                FilterTypeEnum type = Enums.valueOf(FilterTypeEnum.class, filterType);
                if (type == FilterTypeEnum.LOOKUP) {
                    LookUpFilterInfo lookFilterInfo = new LookUpFilterInfo(null, LookupPersist.load(em, sysObjStorage.getRECORD_ID(), e.getNUM()));
                    lookFilterInfo.setTmp1(sysObjStorage.getRECORD_ID());
                    lookFilterInfo.setTmp2(e.getNUM());
                    f = lookFilterInfo;
                } else if (e.getPARAM2() == null && e.getPARAM3() != null) {
                    f = new ColumnsFilterInfo();
                    f.setValue(e.getPARAM3());
                } else {
                    f = new FilterInfoImpl();
                    f.setValue(FilterPersist.stringToValue(type, e.getPARAM2()));
                    f.setSecondValue(FilterPersist.stringToValue(type, e.getPARAM3()));
                }
                f.setType(type);
                f.setColumnName(e.getPARAM1());
                f.setComparison(Enums.valueOf(Condition.class, e.getCONDITION()).getComparison());
                f.setCaseSensitive(e.getSENS() != 0);
                f.setNotExpression(e.getNOPE() != 0);
            }

            if (parentFilterGroup != null) {
                parentFilterGroup.getChildFilters().add(f);
            }

        }
        return cache.get(1);
    }
}
