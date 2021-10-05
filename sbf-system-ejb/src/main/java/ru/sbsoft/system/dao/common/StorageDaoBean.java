package ru.sbsoft.system.dao.common;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.security.PermitAll;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import ru.sbsoft.common.IO;
import ru.sbsoft.dao.IStorageDao;
import ru.sbsoft.model.StorageItem;
import ru.sbsoft.system.common.StorageEntity;
import ru.sbsoft.system.common.StorageEntity_;

/**
 * Класс предоставляет DAO слой для работы с сущностью
 * {@link ru.sbsoft.system.common.StorageEntity}.
 *
 * @author balandin
 * @since Apr 5, 2013 12:51:43 PM
 */
@Stateless
@Remote(IStorageDao.class)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
@PermitAll
public class StorageDaoBean implements IStorageDao {

    @PersistenceContext(unitName = ru.sbsoft.common.jdbc.Const.DEFAULT_PERSISTENCE_CTX)
    private EntityManager entityManager;

    public StorageDaoBean() {
    }

    public EntityManager getEntityManager() {
        return entityManager;
    }

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public void clear(String user, String alias) {

        clear(user, alias, null);

    }

    public void clear(String user, String alias, String fileName) {

        CriteriaBuilder b = entityManager.getCriteriaBuilder();
        CriteriaDelete<StorageEntity> cd = b.createCriteriaDelete(StorageEntity.class);
        Root<StorageEntity> rd = cd.from(StorageEntity.class);
        List<Predicate> p = new ArrayList<>();

        p.add(b.equal(rd.get(StorageEntity_.ALIAS), alias));
        p.add(b.equal(rd.get(StorageEntity_.CREATE_USER), user));

        if (fileName != null) {
            p.add(b.equal(rd.get(StorageEntity_.FILENAME), fileName));

        }
        cd.where((Predicate[]) p.toArray(new Predicate[p.size()]));
        entityManager.createQuery(cd).executeUpdate();
        entityManager.flush();

    }

    @Override
    public long save(String user, String alias, String fileName, File file) throws IOException {

        clear(user, alias, fileName);
        return save(user, alias, "???", fileName, file);
    }

    @Override
    public long save(String user, String alias, String description, String fileName, File file) throws IOException {
        // ??? Если нет описания предыдущий файл для этого алиаса==операции затирается -нельзя выполнить пакетную обработку по нескольким файлам
        if (description != null && description.startsWith("?")) {
            description = description.substring(1);
        } else {
            clear(user, alias);
        }

        StorageEntity e = new StorageEntity();
        e.setCREATE_USER(user);
        e.setCREATE_DATE(new Date());
        e.setALIAS(alias);
        e.setDESCRIPTION(description);
        e.setFILENAME(fileName);
        e.setCONTENT(IO.readFile(file));
        entityManager.persist(e);
        return e.getSTORAGE_ID();
    }

    @Override
    public StorageItem[] find(String user, String alias, String fileName) {

        CriteriaBuilder b = entityManager.getCriteriaBuilder();
        CriteriaQuery<StorageItem> q = b.createQuery(StorageItem.class);
        Root<StorageEntity> r = q.from(StorageEntity.class);

        List<Predicate> p = new ArrayList<>();
        p.add(b.equal(r.get(StorageEntity_.CREATE_USER), user));

        if (alias != null) {
            p.add(b.equal(r.get(StorageEntity_.ALIAS), alias));
        }
        if (fileName != null) {
            p.add(b.equal(r.get(StorageEntity_.FILENAME), fileName));
        }

        q.where((Predicate[]) p.toArray(new Predicate[p.size()]));

        q.select(b.construct(StorageItem.class,
                r.get(StorageEntity_.CREATE_USER),
                r.get(StorageEntity_.ALIAS),
                r.get(StorageEntity_.DESCRIPTION),
                r.get(StorageEntity_.FILENAME),
                r.get(StorageEntity_.STORAGE_ID)
        ));

        return entityManager.createQuery(q).getResultList().toArray(new StorageItem[]{});

    }

    @Override
    public StorageItem getStorageItem(long storageId) {
        return createStorageItem(entityManager.find(StorageEntity.class, storageId));
    }

    private StorageItem createStorageItem(StorageEntity e) {
        return new StorageItem(
                e.getCREATE_USER(),
                e.getALIAS(),
                e.getDESCRIPTION(),
                e.getFILENAME(),
                e.getSTORAGE_ID());
    }

    @Override
    public File load(long storageId) throws IOException {
        final StorageEntity e = entityManager.find(StorageEntity.class, storageId);
        final File file = IO.saveFile(e.getCONTENT(), File.createTempFile("file-storage-tmp-", null));
        file.deleteOnExit();
        return file;
    }

}
