package ru.sbsoft.lookup;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import ru.sbsoft.dao.entity.BaseEntity;
import ru.sbsoft.meta.lookup.ILookupEntity;
import ru.sbsoft.server.utils.SrvUtl;
import ru.sbsoft.shared.api.i18n.consts.SBFExceptionStr;
import ru.sbsoft.shared.exceptions.ApplicationException;
import ru.sbsoft.shared.model.LookupInfoModel;

/**
 *
 * @author Kiselev
 */
public class LookupHelper {

    private static final Logger LOG = Logger.getLogger(LookupHelper.class.getName());

    private static final ReentrantReadWriteLock regLock = new ReentrantReadWriteLock();

    private static final Map<Class<? extends BaseEntity>, IEntityLookupProvider> eMap = new HashMap<>();

    static {
        Map<Class<? extends BaseEntity>, List<IEntityLookupProvider>> m = SrvUtl.collectClassesById("ru.sbsoft", IEntityLookupProvider.class, null);
        for (Map.Entry<Class<? extends BaseEntity>, List<IEntityLookupProvider>> e : m.entrySet()) {
            List<IEntityLookupProvider> l = e.getValue();
            if (l != null && l.size() > 0) {
                if (l.size() > 1) {
                    LOG.log(Level.WARNING, "AutoInit. There are many lookup handlers for key [{0}]. The first one is selected: {1}", new Object[]{e.getKey().getName(), l.get(0).getClass().getName()});
                }
                reg(e.getKey(), l.get(0));
            }
        }
    }

    public static <E extends BaseEntity> void reg(Class<E> c, IEntityLookupProvider<E, ? extends LookupInfoModel> p) {
        regLock.writeLock().lock();
        try {
            eMap.put(c, p);
            LOG.log(Level.INFO, "Lookup handler for key [{0}] is registered: {1}", new Object[]{c.getName(), p.getClass().getName()});
        } finally {
            regLock.writeLock().unlock();
        }
    }

    public static <E extends BaseEntity> IEntityLookupProvider<E, ? extends LookupInfoModel> getProvider(Class<E> eClass) {
        regLock.readLock().lock();
        try {
            IEntityLookupProvider<E, ? extends LookupInfoModel> res = eMap.get(eClass);
            if (res != null) {
                return res;
            }
            Map<Class<? extends BaseEntity>, IEntityLookupProvider> superMap = new HashMap<>();
            for (Map.Entry<Class<? extends BaseEntity>, IEntityLookupProvider> e : eMap.entrySet()) {
                if (e.getKey().isAssignableFrom(eClass)) {
                    superMap.put(e.getKey(), e.getValue());
                }
            }
            if (superMap.size() > 0) {
                for (Class c = eClass.getSuperclass(); c != null; c = c.getSuperclass()) {
                    res = superMap.get(c);
                    if (res != null) {
                        return res;
                    }
                }
            }
            if (ILookupEntity.class.isAssignableFrom(eClass)) {
                return new SelfEntityLookupProvider(eClass);
            }
            throw new IllegalArgumentException("Lookup provider is not found for " + eClass.getName());
        } finally {
            regLock.readLock().unlock();
        }
    }

    public static LookupInfoModel toLookup(final EntityManager em, BaseEntity e) {
        return e == null ? null : ((IEntityLookupProvider) getProvider(e.getClass())).createLookup(e, em);
    }

    private static class SelfEntityLookupProvider implements IEntityLookupProvider {

        private final Class<? extends BaseEntity> enClass;

        public SelfEntityLookupProvider(Class<? extends BaseEntity> enClass) {
            this.enClass = enClass;
        }

        @Override
        public LookupInfoModel createLookup(BaseEntity e, EntityManager em) {
            if (e instanceof ILookupEntity) {
                return ((ILookupEntity) e).toLookupModel();
            }
            throw new ApplicationException(SBFExceptionStr.entityNotLookup);
        }

        @Override
        public LookupInfoModel createModel() {
            return new LookupInfoModel();
        }

        @Override
        public Class<? extends BaseEntity> getId() {
            return enClass;
        }

    }

}
