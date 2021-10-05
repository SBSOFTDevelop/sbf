package ru.sbsoft.system.cfg;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.persistence.EntityManager;
import javax.persistence.metamodel.SingularAttribute;
import ru.sbsoft.shared.GridContext;
import ru.sbsoft.shared.grid.condition.GridCustomState;
import ru.sbsoft.shared.meta.filter.StoredFilterPath;
import ru.sbsoft.shared.model.user.Group;
import ru.sbsoft.system.dao.common.utils.QueryBuilder;
import ru.sbsoft.system.dao.common.utils.StorageObjectType;
import ru.sbsoft.system.grid.SYS_OBJECT;
import ru.sbsoft.system.grid.SYS_OBJ_STORAGE;

/**
 * Базовый класс для работы с настройками
 *
 * @author balandin
 * @since Aug 6, 2015
 */
public abstract class ConfigCommand {

    private final char DEFAULT_APPLICATION_PREFIX = '-';
    //
    private final char GRID_TYPE = 'G';
    //
    protected final IConfigCommandContext context;
    protected final char applicationPrefix;
    protected final GridContext gridContext;

    public ConfigCommand(IConfigCommandContext context, String applicationPrefix, GridContext gridContext) {
        this.context = context;
        this.applicationPrefix = (applicationPrefix != null && applicationPrefix.length() > 0) ? applicationPrefix.charAt(0) : DEFAULT_APPLICATION_PREFIX;
        this.gridContext = gridContext;
    }

    protected SYS_OBJECT getSysObject(boolean createIfNotExist) {
        EntityManager em = context.getEm();
        GridContext ctx = gridContext;
       
        List<SYS_OBJECT> results = QueryBuilder.create(em)
                .add("select o from ").add(SYS_OBJECT.class).add(" o where")
                .eq("o.APPLICATION", applicationPrefix)
                .eq("and o.TYPE", GRID_TYPE)
                .eq("and o.ALIAS", ctx.getGridType().getCode())
                .eq("and o.CONTEXT", ctx.getContext())
                .eq("and o.MODIFIER", ctx.getModifiers().asString())
                .add(" order by o.RECORD_ID")
                .query().getResultList();

        SYS_OBJECT e = null;
        if (results.size() > 0) {
            e = results.get(0);
        } else if (createIfNotExist) {
            e = new SYS_OBJECT();
            e.setAPPLICATION(applicationPrefix);
            e.setTYPE(GRID_TYPE);
            e.setALIAS(ctx.getGridType().getCode());
            e.setCONTEXT(ctx.getContext());

            //для оракла и для постгресса работает по разному -в слоне честно вписывает пустую строку
            //а в оракле null 
            if (!ctx.getModifiers().asString().isEmpty()) {
                e.setMODIFIER(ctx.getModifiers().asString());
            } else {
                e.setMODIFIER(null);

            }
            em.persist(e);
        }

        // entityManager.lock(e, LockModeType.PESSIMISTIC_READ);
        return e;
    }

    protected String getApplicationPrefixStr() {
        return String.valueOf(applicationPrefix);
    }

    protected List<SYS_OBJ_STORAGE> getSysObjStores(SYS_OBJECT sysObject, String user) {
        return new StoreQueryBuilder(sysObject, user).doQuery();
    }

    protected SYS_OBJ_STORAGE getSysObjStore(SYS_OBJECT sysObject, StorageObjectType type, String user) {
        return getSysObjStore(sysObject, type, new StoreKey(user));
    }

    protected SYS_OBJ_STORAGE getOrCreateSysObjStore(SYS_OBJECT sysObject, StorageObjectType type, String user) {
        return getOrCreateSysObjStore(sysObject, type, new StoreKey(user));
    }

    protected SYS_OBJ_STORAGE getSysObjStore(SYS_OBJECT sysObject, StorageObjectType type, StoreKey storeKey) {
        List<SYS_OBJ_STORAGE> results = new StoreQueryBuilder(sysObject, storeKey).setType(type).doQuery();
        if (results.size() > 0) {
            return results.get(0);
        }
        return null;
    }

    protected SYS_OBJ_STORAGE getOrCreateSysObjStore(SYS_OBJECT sysObject, StorageObjectType type, StoreKey storeKey) {
        SYS_OBJ_STORAGE e = getSysObjStore(sysObject, type, storeKey);
        if (e == null) {
            e = new SYS_OBJ_STORAGE();
            e.setSYS_OBJECT_RECORD_ID(sysObject.getRECORD_ID());
            e.setTYPE(type.getDiscriminator());
            e.setUSER_NAME(storeKey.getOwner());
            if (storeKey.getName() != null) {
                e.setSTORAGE_NAME(storeKey.getName());
            }
            e.setIS_DEFAULT(0);
            context.getEm().persist(e);
        }
        return e;
    }

    protected int delete(Class entityClass, long storageRecordID) {
        return new QueryBuilder(context.getEm())
                .add("delete from ").add(entityClass)
                .add(" where").eq("SYS_OBJECT_STORAGE_RECORD_ID", storageRecordID)
                .query()
                .executeUpdate();
    }

    protected final StoreKey checkOwnerGranted(StoreKey key) {
        String userName = context.getUserName();
        if (!(userName.equals(key.getOwner()) || context.getUserGroups().contains(new Group(key.getOwner(), null)))) {
            throw new IllegalArgumentException("Supplied identityId '" + key.getOwner() + "' is not allowed for user '" + userName + "'");
        }
        return key;
    }

    protected final String getUser() {
        return context.getUserName();
    }

    protected final class StoreQueryBuilder {

        private final SYS_OBJECT sysObject;
        private final Set<String> user;
        private StorageObjectType[] type = null;
        private String storeName = null;
        private boolean storeNameEq = true;

        public StoreQueryBuilder(SYS_OBJECT sysObject, StorageObjectType type) {
            this(sysObject, getUser());
            setType(type);
        }

        public StoreQueryBuilder(SYS_OBJECT sysObject, StoreKey storeKey) {
            this(sysObject, storeKey != null ? storeKey.getOwner() : getUser());
            if (storeKey != null) {
                setStoreName(storeKey.getName());
            }
        }

        public StoreQueryBuilder(SYS_OBJECT sysObject, String user) {
            this(sysObject, Collections.singleton(user));
        }

        public StoreQueryBuilder(SYS_OBJECT sysObject, String... user) {
            this(sysObject, new HashSet<>(Arrays.asList(user)));
        }

        public StoreQueryBuilder(SYS_OBJECT sysObject, Set<String> user) {
            this.sysObject = sysObject;
            this.user = user;
        }

        public final StoreQueryBuilder setType(StorageObjectType... type) {
            this.type = type != null && type.length > 0 ? type : null;
            return this;
        }

        public final StoreQueryBuilder setStoreName(String storeName) {
            return this.setStoreName(storeName, true);
        }

        public final StoreQueryBuilder setStoreName(String storeName, boolean eq) {
            this.storeName = storeName;
            this.storeNameEq = eq;
            return this;
        }

        public List<SYS_OBJ_STORAGE> doQuery() {
            return finish(QueryBuilder.create(context.getEm()).add("select o "));
        }

        public <T> List<T> doQuery(SingularAttribute<SYS_OBJ_STORAGE, T> attr) {
            QueryBuilder qb = QueryBuilder.create(context.getEm());
            qb.add("select ").add("o.").add(attr.getName());
            return finish(qb);
        }

        public <T> List<T> doQuery(Class<T> c, SingularAttribute<SYS_OBJ_STORAGE, ?>... attrs) {
            QueryBuilder qb = QueryBuilder.create(context.getEm());
            qb.add("select NEW ").add(c.getName()).add("(");
            for (SingularAttribute<SYS_OBJ_STORAGE, ?> a : attrs) {
                qb.add("o.").add(a.getName()).add(", ");
            }
            qb.back(2).add(")");
            return finish(qb);
        }

        private List finish(QueryBuilder qb) {
            qb.add(" from ").add(SYS_OBJ_STORAGE.class).add(" o where")
                    .eq("o.SYS_OBJECT_RECORD_ID", sysObject.getRECORD_ID());
            if (user.size() == 1) {
                qb.eq("and o.USER_NAME", user.iterator().next());
            } else {
                qb.in("and o.USER_NAME", user);
            }
            if (type != null) {
                if (type.length == 1) {
                    qb.eq("and o.TYPE", type[0].getDiscriminator());
                } else {
                    List<Character> typeDesc = new ArrayList<>();
                    for (StorageObjectType t : type) {
                        typeDesc.add(t.getDiscriminator());
                    }
                    qb.in("and o.TYPE", typeDesc);
                }
            }
            qb.arg("and o.STORAGE_NAME",
                    storeNameEq ? QueryBuilder.OPERAND_EQ : QueryBuilder.OPERAND_NOT_EQ,
                    (storeName != null && !(storeName = storeName.trim()).isEmpty()) ? storeName : null);
            return qb.query().getResultList();
        }
    }

    protected GridCustomState getCustomState(SYS_OBJECT sysObject) {
        SYS_OBJ_STORAGE store = getSysObjStore(sysObject, StorageObjectType.CUSTOM_STATE, getUser());
        if (store != null) {
            char[] stateJson = store.getDATA();
            if (stateJson != null && stateJson.length > 0) {
                Gson gson = new Gson();
                GridCustomState state = gson.fromJson(new String(stateJson), GridCustomState.class);
                return state;
            }
        }
        return null;
    }

    protected void putCustomState(SYS_OBJECT sysObject, GridCustomState state) {
        Gson gson = new GsonBuilder().create();
        String stateJson = gson.toJson(state);
        SYS_OBJ_STORAGE store = getOrCreateSysObjStore(sysObject, StorageObjectType.CUSTOM_STATE, getUser());
        store.setDATA(stateJson.toCharArray());
        context.getEm().merge(store);
    }

    protected final class StoreKey {

        private final String owner;
        private final String name;

        public StoreKey(String owner) {
            this(owner, null);
        }

        public StoreKey(StoredFilterPath path) {
            this(path != null ? path.getIdentityName() : null, path != null ? path.getFilterName() : null);
        }

        private StoreKey(String owner, String name) {
            if (owner == null || (owner = owner.trim()).isEmpty()) {
                owner = context.getUserName();
            }
            if (name != null && (name = name.trim()).isEmpty()) {
                name = null;
            }
            this.owner = owner;
            this.name = name;
        }

        public String getOwner() {
            return owner;
        }

        public String getName() {
            return name;
        }
    }

}
