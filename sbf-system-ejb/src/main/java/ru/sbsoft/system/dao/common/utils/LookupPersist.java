package ru.sbsoft.system.dao.common.utils;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import ru.sbsoft.shared.model.LookupInfoModel;
import ru.sbsoft.system.grid.SYS_FILTER_LOOKUP;

/**
 * Сериализация луукап значений
 * @author balandin
 * @since Aug 10, 2015
 */
public class LookupPersist {

    public static List<LookupInfoModel> load(EntityManager em, long storageRecordID, int filterNum) {
        List<SYS_FILTER_LOOKUP> elist = new QueryBuilder(em)
                .add("select o from ").add(SYS_FILTER_LOOKUP.class)
                .add(" o where")
                .eq("o.SYS_OBJECT_STORAGE_RECORD_ID", storageRecordID)
                .eq("and o.SYS_FILTER_NUM", filterNum)
                .add(" order by o.NUM")
                .query().getResultList();

        List<LookupInfoModel> result = new ArrayList<>();
        for (SYS_FILTER_LOOKUP e : elist) {
            LookupInfoModel m = new LookupInfoModel();
            m.setID(e.getLOOKUP_RECORD_ID());
            m.setSemanticID(e.getLOOKUP_ENTITY_ID());
            m.setSemanticKey(e.getLOOKUP_KEY());
            m.setSemanticName(e.getLOOKUP_NAME());
            result.add(m);
        }
        return result;
    }

    public static void save(EntityManager em, long storageRecordID, int filterNum, List<LookupInfoModel> lookups) {
        int num = 0;
        for (LookupInfoModel m : lookups) {
            SYS_FILTER_LOOKUP e = new SYS_FILTER_LOOKUP();
            e.setSYS_OBJECT_STORAGE_RECORD_ID(storageRecordID);
            e.setSYS_FILTER_NUM(filterNum);
            e.setNUM(++num);
            e.setLOOKUP_RECORD_ID(m.getID());
            e.setLOOKUP_ENTITY_ID(m.getSemanticID());
            e.setLOOKUP_KEY(m.getSemanticKey());
            e.setLOOKUP_NAME(m.getSemanticName());
            em.persist(e);
        }
    }
}
