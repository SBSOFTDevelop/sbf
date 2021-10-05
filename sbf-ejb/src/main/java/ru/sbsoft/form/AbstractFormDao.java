package ru.sbsoft.form;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import javax.ejb.SessionContext;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.SingularAttribute;
import ru.sbsoft.dao.entity.ActRangeFields;
import ru.sbsoft.dao.entity.ActRangeFields_;
import ru.sbsoft.dao.entity.CreateInfoFields;
import ru.sbsoft.dao.entity.IActRangeEntity;
import ru.sbsoft.dao.entity.IBaseEntity;
import ru.sbsoft.shared.FilterInfo;
import ru.sbsoft.shared.exceptions.ApplicationException;
import ru.sbsoft.dao.entity.IEntityConverter;
import ru.sbsoft.dao.entity.IFormEntity;
import ru.sbsoft.dao.entity.UpdateInfoFields;
import ru.sbsoft.meta.lookup.ILookupEntity;
import ru.sbsoft.meta.lookup.ILookupModelProvider;
import ru.sbsoft.sbf.app.model.IFormModel;
import ru.sbsoft.shared.api.i18n.consts.SBFExceptionStr;
import ru.sbsoft.shared.model.LookupInfoModel;
import ru.sbsoft.dao.entity.ICreateInfoEntity;
import ru.sbsoft.dao.entity.IUpdateInfoEntity;
import ru.sbsoft.generator.api.Lookup;
import ru.sbsoft.sbf.app.model.YearMonthDay;
import ru.sbsoft.server.utils.AppException;
import ru.sbsoft.server.utils.SrvConst;
import ru.sbsoft.server.utils.YearMonthDayConverter;
import ru.sbsoft.shared.TreeNode;
import ru.sbsoft.shared.api.i18n.I18nResourceInfo;
import ru.sbsoft.shared.api.i18n.Ii18nDao;
import ru.sbsoft.shared.model.IActRange;
import ru.sbsoft.shared.model.IActRangeModel;
import ru.sbsoft.shared.model.ICreateInfoModel;
import ru.sbsoft.shared.model.IUpdateInfoModel;
import ru.sbsoft.shared.util.NameValuePair;

public abstract class AbstractFormDao<M extends IFormModel, E extends IFormEntity> extends FormProcessorBase<M> implements IEntityConverter<M, E> {

    private final Class<E> entityClass;
    private final Class<M> modelClass;

    private TemporalCheckInfo<E> temporalConfig;

    @Lookup
    private Ii18nDao i18n;

    public AbstractFormDao(Class<M> modelClass, Class<E> entityClass) {
        this.entityClass = entityClass;
        this.modelClass = modelClass;
    }

    protected final void setTemporalConfig(TemporalCheckInfo<E> temporalConfig) {
        if (!IActRangeEntity.class.isAssignableFrom(entityClass)) {
            throw new AppException("Class '%s' hasn't acton range and can't be temporal", entityClass.getName());
        }
        this.temporalConfig = temporalConfig;
    }

    //=========== IFormProcessor ================
    /**
     * Метод возвращает запись в виде сериализуемой модели из сущности по id
     *
     * @param id
     * @return модель
     */
    @Override
    public M getRecord(final BigDecimal id) {
        E entity = null;
        if (null != id) {
            entity = getEm().find(getEntityClass(), id);
        }
        if (entity == null) {
            throw new ApplicationException(SBFExceptionStr.objectNotFound, getEntityClass().getSimpleName(), String.valueOf(id.longValue()));
        }
        return entityToModel(entity);
    }

    /**
     * Метод удаляет запись из сущности по Id записи.
     *
     * @param id
     */
    @Override
    public void delRecord(final BigDecimal id) {
        if (null == id) {
            return;
        }
        final E entity = getEm().find(getEntityClass(), id);
        if (null != entity) {
            remove(entity);
        }
    }

    @Override
    public M newRecord(final List<FilterInfo> parentFilters, final BigDecimal clonableRecordID) {
        return newModel(parentFilters, clonableRecordID);
    }

    /**
     * Метод замещает поля записи сущности полями модели.<br>
     * и заполняет системные (технологические) поля
     * <i>(UPDATE_USER,UPDATE_DATE...)</i>.
     *
     * @param model
     * @return model
     */
    @Override
    public M putRecord(final M model) {
        if (null == model) {
            return null;
        }
        E oldE = model.getId() != null ? getEm().find(entityClass, model.getId()) : null;
        if (oldE != null) {
            getEm().detach(oldE);
        }
        E entity = modelToEntity(model);
        if (null == entity) {
            throw new IllegalArgumentException("Entity {" + entityClass.getName() + "} for save is NULL!");
        }
        prepareSave(entity);
        onBeforeSave(entity, oldE, model);
        onBeforeSave(entity, oldE);
        entity = getEm().merge(entity);
        getEm().flush();
        onAfterSave(entity, oldE);
        onAfterSave(entity, oldE, model);
        M m = entityToModel(entity);
        return m;
    }

    //===================================================
    //============== Resources ===============
    @Override
    public AbstractFormDao<M, E> init(EntityManager em, SessionContext scontext) {
        super.init(em, scontext);
        return this;
    }

    //==============================================
    //=================== Data methods =============
    public final void prepareSave(E e) {
        e.checkTehnologyFields(getScontext());
        validate(e);
    }

    protected final void remove(E e) {
        if (e != null) {
            onBeforeRemove(e);
            getEm().remove(e);
            onRemove(e);
        }
    }

    protected void onBeforeRemove(E e) {
    }

    protected void onRemove(E e) {
    }

    protected void onBeforeSave(E e, E oldE, M m) {
    }

    protected void onBeforeSave(E e, E oldE) {
    }

    protected void onAfterSave(E e, E oldE, M m) {
    }

    protected void onAfterSave(E e, E oldE) {
    }

    protected abstract void fillModel(final M m, final E e);

    protected abstract void fillEntity(final E e, final M m);

    /**
     * Метод копирует поля сущности в сериализуемую модель
     *
     * @param e entity
     * @return model
     */
    protected final M entityToModel(E e) {
        return entityToModel(e, createModelInstance());
    }

    protected final M entityToModel(E e, M modelToFill) {
        M m = modelToFill;
        m.setId(e.getId());
        if ((m instanceof IActRangeModel) && (e instanceof IActRangeEntity)) {
            ActRangeFields af = ((IActRangeEntity) e).getActRangeFields();
            if (af != null) {
                ((IActRangeModel) m).setBegDate(af.getBegDate());
                ((IActRangeModel) m).setEndDate(SrvConst.FAR_FUTURE.equals(af.getEndDate()) ? null : af.getEndDate());
            }
        }
        fillModel(m, e);
        Date createDate = null;
        String createUser = null;
        Date updateDate = null;
        String updateUser = null;
        if (e instanceof ICreateInfoEntity) {
            CreateInfoFields ci = ((ICreateInfoEntity) e).getCreateInfoFields();
            if (ci != null) {
                createDate = ci.getCreateDate();
                createUser = ci.getCreateUser();
            }
        }
        if (e instanceof IUpdateInfoEntity) {
            UpdateInfoFields cu = ((IUpdateInfoEntity) e).getUpdateInfoFields();
            if (cu != null) {
                updateDate = cu.getUpdateDate();
                updateUser = cu.getUpdateUser();
            }
        }
        if (m instanceof ICreateInfoModel) {
            ((ICreateInfoModel) m).setCreateDate(createDate);
            ((ICreateInfoModel) m).setCreateUser(createUser);
        }
        if (m instanceof IUpdateInfoModel) {
            ((IUpdateInfoModel) m).setUpdateDate(updateDate);
            ((IUpdateInfoModel) m).setUpdateUser(updateUser);
        }
        return m;
    }

    @Override
    public final M toModel(E e) {
        return entityToModel(e);
    }

    public final List<M> toModel(List<E> es) {
        List<M> res = new ArrayList<>();
        if (es != null) {
            es.forEach((e) -> {
                res.add(toModel(e));
            });
        }
        return res;
    }

    public final E toEntity(M m) {
        return modelToEntity(m);
    }

    public final List<E> toEntity(List<M> ms) {
        List<E> res = new ArrayList<>();
        if (ms != null) {
            ms.forEach((m) -> {
                res.add(modelToEntity(m));
            });
        }
        return res;
    }

    @Override
    public M toModel(E e, M modelToFill) {
        return entityToModel(e, modelToFill);
    }

    /**
     * Метод копирует поля модели в сущность
     *
     * @param m
     * @return сущность
     */
    protected final E modelToEntity(M m) {
        if (m == null) {
            return null;
        }
        validateActRange(m);
        validate(m);
        E e = m.getId() != null ? getEm().find(entityClass, m.getId()) : null;
        Date createDate = null;
        String createUser = null;
        if (e instanceof ICreateInfoEntity) {
            CreateInfoFields ci = ((ICreateInfoEntity) e).getCreateInfoFields();
            if (ci != null) {
                createDate = ci.getCreateDate();
                createUser = ci.getCreateUser();
            }
        }
        if (e != null) {
            getEm().detach(e);
        } else {
            e = createEntityInstance();
        }
        if (e.getId() == null && m.getId() != null) {
            e.setId(m.getId());
        }
        if ((e instanceof IActRangeEntity) && (m instanceof IActRangeModel)) {
            ActRangeFields af = ((IActRangeEntity) e).getActRangeFields();
            if (af == null) {
                af = new ActRangeFields();
            }
            IActRangeModel rm = (IActRangeModel) m;
            if (rm.getBegDate() != null) {
                af.setBegDate(rm.getBegDate());
            }
            af.setEndDate(rm.getEndDate() != null ? rm.getEndDate() : SrvConst.FAR_FUTURE);
            ((IActRangeEntity) e).setActRangeFields(af);
        }
        fillEntity(e, m);
        //++++prevent hack from client browser
        if (e instanceof ICreateInfoEntity) {
            CreateInfoFields ci = ((ICreateInfoEntity) e).getCreateInfoFields();
            if (ci == null) {
                ci = new CreateInfoFields();
            }
            ci.setCreateDate(createDate);
            ci.setCreateUser(createUser);
            ci.fillFrom(getScontext());
            ((ICreateInfoEntity) e).setCreateInfoFields(ci);
        }
        if (e instanceof IUpdateInfoEntity) {
            UpdateInfoFields cu = ((IUpdateInfoEntity) e).getUpdateInfoFields();
            if (cu == null) {
                cu = new UpdateInfoFields();
            }
            cu.fillFrom(getScontext());
            ((IUpdateInfoEntity) e).setUpdateInfoFields(cu);
        }
        if (temporalConfig != null) {
            validate(e, temporalConfig);
        }
        return e;
    }

    private AppException appErr(I18nResourceInfo resourceInfo, String... parameters) throws AppException {
        final AppException ex = parameters != null && parameters.length > 0 ? new AppException(resourceInfo, parameters) : new AppException(resourceInfo);
        ex.initMsg(i18n);
        return ex;
    }

    private ActRangeFields toActRange(E e) {
        if (e instanceof IActRangeEntity) {
            ActRangeFields ar = ((IActRangeEntity) e).getActRangeFields();
            if (ar == null || (ar.getBegDate() == null && ar.getEndDate() == null)) {
                throw appErr(SBFExceptionStr.actRangeUndefined);
            }
            if (ar.getBegDate() == null) {
                throw appErr(SBFExceptionStr.actRangeBeginUndefined);
            }
            if (ar.getEndDate() == null) {
                throw appErr(SBFExceptionStr.actRangeEndUndefined);
            }
            return ar;
        } else {
            throw appErr(SBFExceptionStr.actRangeUnsupported);
        }
    }

    private void validate(E e, TemporalCheckInfo<E> cfg) {
        final CriteriaBuilder b = getEm().getCriteriaBuilder();
        final CriteriaQuery<E> q = b.createQuery((Class<E>) e.getClass());
        Root<E> r = q.from((Class<E>) e.getClass());
        final List<Predicate> p = new ArrayList<>();
        if (e.getId() != null) {
            p.add(b.notEqual(r.get(cfg.getIdAttr()), e.getId()));
        }
        ActRangeFields ar = toActRange(e);
        Predicate pBeg = b.lessThanOrEqualTo(r.get(cfg.getActRangeAttr()).get(ActRangeFields_.begDate), ar.getEndDate());
        Predicate pEnd = b.greaterThanOrEqualTo(r.get(cfg.getActRangeAttr()).get(ActRangeFields_.endDate), ar.getEndDate());
        p.add(pBeg);
        p.add(pEnd);
        Predicate where = cfg.getWherePredicate(b, r, e);
        if (where != null) {
            p.add(where);
        }
        q.where((Predicate[]) p.toArray(new Predicate[p.size()]));

        List<E> res = getEm().createQuery(q).getResultList();
        if (res != null && !res.isEmpty()) {
            for (NameValuePair<Function<E, ?>> pr : cfg.getUqNames()) {
                Object v1 = pr.getValue().apply(e);
                for (E re : res) {
                    Object v2 = pr.getValue().apply(re);
                    if (pr.getName() != null && Objects.equals(v1, v2)) {
                        throw appErr(SBFExceptionStr.actRangeParamIntersection, pr.getName(), v1.toString());
                    }
                }
            }
            throw appErr(SBFExceptionStr.actRangeIntersection);
        }
    }

    private void validateActRange(M m) {
        if (m instanceof IActRange) {
            IActRange ar = (IActRange) m;
            if (ar == null) {
                throw appErr(SBFExceptionStr.actRangeUndefined);
            }
            YearMonthDay beg = ar.getBegDate();
            YearMonthDay end = ar.getEndDate() != null ? ar.getEndDate() : SrvConst.FAR_FUTURE;
            if (beg == null) {
                throw appErr(SBFExceptionStr.actRangeBeginUndefined);
            }
            if (beg.after(end)) {
                throw appErr(SBFExceptionStr.actRangeBoundsReverse);
            }
            IActRange parent = getParentActRange(m);
            if (parent != null) {
                YearMonthDay pBeg = parent.getBegDate();
                YearMonthDay pEnd = parent.getEndDate();
                if (pBeg != null && (beg.before(pBeg)) || (pEnd != null && end.after(pEnd))) {
                    String pBegStr = pBeg != null ? pBeg.toIso8601() : "";
                    String pEndStr = pEnd != null ? pEnd.toIso8601() : "";
                    throw appErr(SBFExceptionStr.actRangeNotIn, pBegStr, pEndStr);
                }
            }
        }
    }

    protected void validate(M m) {
    }

    protected void validate(E e) {
    }

    protected IBaseEntity getParentItem(M m) {
        return null;
    }

    protected IBaseEntity getParentItem(List<FilterInfo> parentFilters) {
        return null;
    }

    protected IActRange getParentActRange(M m) {
        IBaseEntity e = getParentItem(m);
        return e instanceof IActRange ? (IActRange) e : null;
    }

    protected IActRange getParentActRange(List<FilterInfo> parentFilters) {
        IBaseEntity e = getParentItem(parentFilters);
        return e instanceof IActRange ? (IActRange) e : null;
    }

    protected final <A extends IActRangeEntity> IActRange getActRangeIntersection(A... es) {
        if (es == null || es.length == 0) {
            return null;
        }
        List<IActRange> rs = new ArrayList<>();
        for (A e : es) {
            ActRangeFields f = e.getActRangeFields();
            if (f != null && (f.getBegDate() != null || f.getEndDate() != null)) {
                rs.add(f);
            }
        }
        return getIntersection(rs);
    }

    protected final <R extends IActRange> IActRange getIntersection(List<R> rs) {
        if (rs == null || rs.isEmpty()) {
            return null;
        }
        return getIntersection(rs.toArray(new IActRange[rs.size()]));
    }

    protected final <R extends IActRange> IActRange getIntersection(R... rs) {
        if (rs == null || rs.length == 0) {
            return null;
        }
        YearMonthDay begDate = getMinDate(), endDate = SrvConst.FAR_FUTURE;
        for (IActRange r : rs) {
            if (r.getBegDate() != null && begDate.before(r.getBegDate())) {
                begDate = r.getBegDate();
            }
            if (r.getEndDate() != null && endDate.after(r.getEndDate())) {
                endDate = r.getEndDate();
            }
        }
        final YearMonthDay resBeg = begDate;
        final YearMonthDay resEnd = endDate;
        return begDate.after(endDate) ? null : new IActRange() {
            @Override
            public YearMonthDay getBegDate() {
                return resBeg;
            }

            @Override
            public YearMonthDay getEndDate() {
                return resEnd;
            }
        };
    }

    protected YearMonthDay getMinDate() {
        return SrvConst.DEFAULT_MIN_DATE;
    }

    protected M newModel(List<FilterInfo> parentFilters, BigDecimal clonableRecordID) {
        M m;
        if (clonableRecordID != null) {
            E e = getEm().find(entityClass, clonableRecordID);
            if (e != null) {
                m = entityToModel(e);
                m.setId(null);
                if (m instanceof ICreateInfoModel) {
                    ICreateInfoModel cm = (ICreateInfoModel) m;
                    cm.setCreateDate(null);
                    cm.setCreateUser(null);
                }
                if (m instanceof IUpdateInfoModel) {
                    IUpdateInfoModel um = (IUpdateInfoModel) m;
                    um.setUpdateDate(null);
                    um.setUpdateUser(null);
                }
            } else {
                throw new ApplicationException(SBFExceptionStr.entityNotFound, String.valueOf(clonableRecordID.longValue()));
            }
        } else {
            m = createModelInstance();
            if (m instanceof IActRangeModel) {
                IActRangeModel rm = (IActRangeModel) m;
                IActRange parent = getParentActRange(parentFilters);
                if (parent == null) {
                    rm.setBegDate(getMinDate());
                } else {
                    rm.setBegDate(parent.getBegDate() != null ? parent.getBegDate() : getMinDate());
                    YearMonthDay ed = parent.getEndDate();
                    rm.setEndDate(ed != null && SrvConst.FAR_FUTURE.equals(ed) ? null : ed);
                }
            }
        }
        init(m, parentFilters);
        if (clonableRecordID != null) {
            initCloned(m, parentFilters);
        } else {
            initNew(m, parentFilters);
        }
        return m;
    }

    protected void init(M m, List<FilterInfo> parentFilters) {
    }

    protected void initNew(M m, List<FilterInfo> parentFilters) {
    }

    protected void initCloned(M m, List<FilterInfo> parentFilters) {
    }

    //==============================================
    //=============== Class methods ================
    /**
     *
     * @return класс сущности
     */
    protected final Class<E> getEntityClass() {
        return entityClass;
    }

    protected final Class<M> getModelClass() {
        return modelClass;
    }

    private M createModelInstance() {
        try {
            return modelClass.getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | IllegalArgumentException | InvocationTargetException ex) {
            throw new RuntimeException("Can't create new data model: " + ex.getLocalizedMessage(), ex);
        }
    }

    private E createEntityInstance() {
        try {
            return entityClass.getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | IllegalArgumentException | InvocationTargetException ex) {
            throw new RuntimeException("Can't create new entity: " + ex.getLocalizedMessage(), ex);
        }
    }

    //==============================================
    //================== Utility methods ===========
    protected static final LookupInfoModel toLookup(ILookupEntity e) {
        return e != null ? e.toLookupModel() : null;
    }

    protected final LookupInfoModel toLookup(ILookupModelProvider e) {
        return e != null ? e.toLookupModel(getEm()) : null;
    }

    protected final <T extends IBaseEntity> T toEntity(LookupInfoModel m, Class<T> eClass) {
        return toEntity(m != null ? m.getID() : null, eClass);
    }

    protected final <T extends IBaseEntity> T toEntity(TreeNode<BigDecimal> n, Class<T> eClass) {
        return toEntity(n != null ? n.getKey() : null, eClass);
    }

    private final <T extends IBaseEntity> T toEntity(BigDecimal id, Class<T> eClass) {
        T res = id != null ? getEm().find(eClass, id) : null;
        if (res == null && id != null) {
            throw new PersistenceException(eClass.getName() + " with ID=" + id + " is not found.");
        }
        return res;
    }

    protected static final LocalDate toDate(YearMonthDay ymd) {
        return ymd != null ? YearMonthDayConverter.toLocalDate(ymd) : null;
    }

    protected static final YearMonthDay toYmd(LocalDate d) {
        return d != null ? YearMonthDayConverter.convert(d) : null;
    }

    protected static class TemporalCheckInfo<E extends IFormEntity> {

        private final SingularAttribute<? super E, ActRangeFields> actRangeAttr;
        private final SingularAttribute<? super E, BigDecimal> idAttr;
        private Expr where;

        public TemporalCheckInfo(SingularAttribute<? super E, BigDecimal> idAttr, SingularAttribute<? super E, ActRangeFields> actRangeAttr) {
            this.actRangeAttr = actRangeAttr;
            this.idAttr = idAttr;
        }

        public final TemporalCheckInfo where(Expr expr) {
            this.where = expr;
            return this;
        }

        SingularAttribute<? super E, ActRangeFields> getActRangeAttr() {
            return actRangeAttr;
        }

        SingularAttribute<? super E, BigDecimal> getIdAttr() {
            return idAttr;
        }

        List<NameValuePair<Function<E, ?>>> getUqNames() {
            if (where != null) {
                final List<NameValuePair<Function<E, ?>>> uqNames = new ArrayList<>();
                where.fillUqNames(uqNames);
                return uqNames;
            }
            return Collections.emptyList();
        }

        Predicate getWherePredicate(CriteriaBuilder b, Root<E> r, E e) {
            return where != null ? where.toPredicate(b, r, e) : null;
        }

        public static class And<E extends IFormEntity> extends Expr<E> {

            @Override
            Predicate constructPredicate(CriteriaBuilder b, Predicate[] preds) {
                return b.and(preds);
            }
        }

        public static class Or<E extends IFormEntity> extends Expr<E> {

            @Override
            Predicate constructPredicate(CriteriaBuilder b, Predicate[] preds) {
                return b.or(preds);
            }
        }

        public static abstract class Expr<E extends IFormEntity> {

            protected final List<EqInfo<?>> attrs = new ArrayList<>();
            protected final List<Expr> exprs = new ArrayList<>();

            public <V> Expr key(SingularAttribute<? super E, V> attr, Function<E, V> func) {
                return addAttr(attr, func, null);
            }

            public <V> Expr uq(SingularAttribute<? super E, V> attr, Function<E, V> func, String name) {
                if (name == null || (name = name.trim()).isEmpty()) {
                    throw new IllegalArgumentException("Parameter name can't be empty");
                }
                return addAttr(attr, func, name);
            }

            private <V> Expr addAttr(SingularAttribute<? super E, V> attr, Function<E, V> func, String name) {
                if (attr == null) {
                    throw new IllegalArgumentException("Parameter atribute can't be null");
                }
                if (func == null) {
                    throw new IllegalArgumentException("Parameter method reference can't be null");
                }
                if (name != null && (name = name.trim()).isEmpty()) {
                    name = null;
                }
                if (name != null && !Character.isUpperCase(name.charAt(0))) {
                    char[] ch = name.toCharArray();
                    ch[0] = Character.toUpperCase(ch[0]);
                    name = String.copyValueOf(ch);
                }
                attrs.add(new EqInfo<>(attr, func, name));
                return this;
            }

            public Expr expr(Expr e) {
                exprs.add(e);
                return this;
            }

            void fillUqNames(List<NameValuePair<Function<E, ?>>> uqNames) {
                for (EqInfo<?> a : attrs) {
                    uqNames.add(new NameValuePair<>(a.getName(), a.getFunc()));
                }
                for (Expr e : exprs) {
                    e.fillUqNames(uqNames);
                }
            }

            private Predicate[] getPredicates(CriteriaBuilder b, Root<E> r, E e) {
                List<Predicate> p = new ArrayList<>();
                for (EqInfo<?> inf : attrs) {
                    Object val = inf.getFunc().apply(e);
                    p.add(val != null ? b.equal(r.get(inf.getAttr()), val) : r.get(inf.getAttr()).isNull());
                }
                for (Expr exp : exprs) {
                    Predicate pp = exp.toPredicate(b, r, e);
                    if (pp != null) {
                        p.add(pp);
                    }
                }
                return p.isEmpty() ? null : p.toArray(new Predicate[p.size()]);
            }

            Predicate toPredicate(CriteriaBuilder b, Root<E> r, E e) {
                Predicate[] preds = getPredicates(b, r, e);
                return preds != null && preds.length > 0 ? constructPredicate(b, preds) : null;
            }

            abstract Predicate constructPredicate(CriteriaBuilder b, Predicate[] preds);

            private final class EqInfo<V> {

                private final SingularAttribute<? super E, V> attr;
                private final Function<E, V> func;
                private final String name;

                public EqInfo(SingularAttribute<? super E, V> attr, Function<E, V> func) {
                    this(attr, func, null);
                }

                public EqInfo(SingularAttribute<? super E, V> attr, Function<E, V> func, String descr) {
                    this.attr = attr;
                    this.func = func;
                    this.name = descr;
                }

                public SingularAttribute<? super E, V> getAttr() {
                    return attr;
                }

                public Function<E, V> getFunc() {
                    return func;
                }

                public String getName() {
                    return name;
                }
            }

        }
    }
}
