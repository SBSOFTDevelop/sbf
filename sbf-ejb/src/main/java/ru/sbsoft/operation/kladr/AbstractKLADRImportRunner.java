package ru.sbsoft.operation.kladr;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import net.sf.sevenzipjbinding.ArchiveFormat;
import net.sf.sevenzipjbinding.SevenZipException;
import ru.sbsoft.common.IO;
import ru.sbsoft.dao.IStorageDao;
import ru.sbsoft.model.StorageItem;
import ru.sbsoft.operation.AbstractOperationRunner;
import ru.sbsoft.operation.ProgressCallback;
import ru.sbsoft.processor.ServerOperationContext;
import ru.sbsoft.shared.kladr.KLADRItem;
import ru.sbsoft.shared.model.operation.OperationException;
import ru.sbsoft.system.Parameters;
import ru.sbsoft.transaction.SQLBatch;
import ru.sbsoft.generator.api.Lookup;

/**
 * Класс, представляющий методы импорта справочника КЛАДР из таблиц DBF в таблицы СУБД <code>KD_DOMAIN, KD_STREET, KD_HOUSE</code>.
 * <p>Исходные таблицы располагаются в арихиве 7Z или ARJ.</p>
 * <p> Структура файлового архива:</p>
 * <ul>
 * <li><code>KLADR.DBF</code> - справочник населенных пунктов;</li>
 * <li><code>STREET.DBF</code> - справочник улиц;</li>
 * <li><code>DOMA.DBF</code> -   справочник домов</li>
 * </ul>
 * @author balandin
 * @since Mar 11, 2013 3:55:44 PM
 */
public abstract class AbstractKLADRImportRunner extends AbstractOperationRunner {

    private static String _7Z_EXTENSION = "7Z";
    private static String _ARJ_EXTENSION = "ARJ";
    //
    private KLADRStore store;
    @Lookup
    private EntityManager entityManager;
    @Lookup
    private IStorageDao storageDao;
    private Parameters parameters;

    @Override
    public void init(ServerOperationContext operationContext) throws OperationException {
        super.init(operationContext);
        this.parameters = new Parameters(entityManager);
    }

    @Override
    public void run() throws Exception {
        Long storageId = (Long) getParametersMap().get("file").getValue();
        final StorageItem file = storageDao.getStorageItem(storageId);
        processFile(file);
    }

    private static ArchiveFormat selectArchiveFormat(String fileName) {
        final String extension = IO.extractFileExtension(fileName);
        if (_7Z_EXTENSION.equalsIgnoreCase(extension)) {
            return ArchiveFormat.SEVEN_ZIP;
        }
        if (_ARJ_EXTENSION.equalsIgnoreCase(extension)) {
            return ArchiveFormat.ARJ;
        }
        throw new IllegalArgumentException("Файл должен иметь расширение " + _7Z_EXTENSION + " или " + _ARJ_EXTENSION + ".");
    }

    public void processFile(StorageItem storageItem) throws Exception {
        store = KLADRManager.getActiveStore(parameters);
        if (store == null || store == KLADRStore.STRORE_B) {
            store = KLADRStore.STRORE_A;
        } else {
            store = KLADRStore.STRORE_B;
        }

        info(((store == KLADRStore.STRORE_B) ? " " : "") + "Обрабатывается файл '" + storageItem.getFileName() + "'");

        ArchiveReader archiveReader = null;
        File file = null;
        try {
            file = storageDao.load(storageItem.getStorageId());
            final ArchiveFormat format = selectArchiveFormat(storageItem.getFileName());
            archiveReader = new ArchiveReader(file.getAbsolutePath(), format);

            executeUpdate("DROP INDEX KD_IDX1", true, true);
            executeUpdate("DROP INDEX KD_IDX2", true, true);
            executeUpdate("DROP INDEX KD_IDX3", true, true);

            executeUpdate("TRUNCATE TABLE KD_DOMAIN");
            SQLBatch batch = new SQLBatch(entityManager, wrap("INSERT INTO KD_DOMAIN (ID, REGION_CODE, AREA_CODE, CITY_CODE, SETTLEMENT_CODE, PREFIX, NAME, STATUS, POSTCODE, ACTUAL_CODE)"), 10) {
                @Override
                protected void setParams(Query q, long ID, Map<String, Object> record) throws SQLException {
                    String code = (String) record.get("CODE");
                    q.setParameter(nextParamIndex(), ID);
                    q.setParameter(nextParamIndex(), code.substring(0, 2));
                    q.setParameter(nextParamIndex(), code.substring(2, 5));
                    q.setParameter(nextParamIndex(), code.substring(5, 8));
                    q.setParameter(nextParamIndex(), code.substring(8, 11));
                    q.setParameter(nextParamIndex(), (String) record.get("SOCR"));
                    q.setParameter(nextParamIndex(), (String) record.get("NAME"));
                    q.setParameter(nextParamIndex(), (String) record.get("STATUS"));
                    q.setParameter(nextParamIndex(), (String) record.get("INDEX"));
                    q.setParameter(nextParamIndex(), code.substring(11, 13));
                }
            };
            load(archiveReader, batch, "KLADR.DBF", "Загрузка справочника населенных пунктов");

            executeUpdate("TRUNCATE TABLE KD_STREET");
            batch = new SQLBatch(entityManager, wrap("INSERT INTO KD_STREET (ID, REGION_CODE, AREA_CODE, CITY_CODE, SETTLEMENT_CODE, STREET_CODE, PREFIX, NAME, POSTCODE, ACTUAL_CODE)"), 10) {
                @Override
                protected void setParams(Query q, long ID, Map<String, Object> record) throws SQLException {
                    String code = (String) record.get("CODE");
                    q.setParameter(nextParamIndex(), ID);
                    q.setParameter(nextParamIndex(), code.substring(0, 2));
                    q.setParameter(nextParamIndex(), code.substring(2, 5));
                    q.setParameter(nextParamIndex(), code.substring(5, 8));
                    q.setParameter(nextParamIndex(), code.substring(8, 11));
                    q.setParameter(nextParamIndex(), code.substring(11, 15));
                    q.setParameter(nextParamIndex(), (String) record.get("SOCR"));
                    q.setParameter(nextParamIndex(), (String) record.get("NAME"));
                    q.setParameter(nextParamIndex(), (String) record.get("INDEX"));
                    q.setParameter(nextParamIndex(), code.substring(15, 17));
                }
            };
            load(archiveReader, batch, "STREET.DBF", "Загрузка справочника улиц");

            executeUpdate("TRUNCATE TABLE KD_HOUSE");
            batch = new SQLBatch(entityManager, wrap("INSERT INTO KD_HOUSE (ID, REGION_CODE, AREA_CODE, CITY_CODE, SETTLEMENT_CODE, STREET_CODE, HOUSE_CODE, PREFIX, NAME, POSTCODE)"), 10) {
                @Override
                protected void setParams(Query q, long ID, Map<String, Object> record) throws SQLException {
                    String code = (String) record.get("CODE");
                    q.setParameter(nextParamIndex(), ID);
                    q.setParameter(nextParamIndex(), code.substring(0, 2));
                    q.setParameter(nextParamIndex(), code.substring(2, 5));
                    q.setParameter(nextParamIndex(), code.substring(5, 8));
                    q.setParameter(nextParamIndex(), code.substring(8, 11));
                    q.setParameter(nextParamIndex(), code.substring(11, 15));
                    q.setParameter(nextParamIndex(), code.substring(15, 19));
                    q.setParameter(nextParamIndex(), (String) record.get("SOCR"));
                    q.setParameter(nextParamIndex(), (String) record.get("NAME"));
                    q.setParameter(nextParamIndex(), (String) record.get("INDEX"));
                }
            };
            load(archiveReader, batch, "DOMA.DBF", "Загрузка справочника домов");

        } finally {
            if (archiveReader != null) {
                archiveReader.close();
            }
            IO.delete(file);
        }

        info("Обновление индексов СУБД");
        executeUpdate("CREATE UNIQUE INDEX KD_IDX1 ON KD_DOMAIN (REGION_CODE, AREA_CODE, CITY_CODE, SETTLEMENT_CODE, ACTUAL_CODE)");
        executeUpdate("CREATE UNIQUE INDEX KD_IDX2 ON KD_STREET (REGION_CODE, AREA_CODE, CITY_CODE, SETTLEMENT_CODE, STREET_CODE, ACTUAL_CODE)");
        executeUpdate("CREATE INDEX KD_IDX3 ON KD_HOUSE (REGION_CODE, AREA_CODE, CITY_CODE, SETTLEMENT_CODE)");

        info("Формирование адресов");
        executeUpdate("DROP INDEX KD_IDX4", true, false);
        executeUpdate("TRUNCATE TABLE KD_TMP", false, false);

        TextIndexBuilder indexBuilder = new TextIndexBuilder(entityManager, store);

        List<KLADRItem> regions = indexBuilder.getRegions();
        initProgress(regions.size());
        for (int i = 0; i < regions.size(); i++) {
            checkInterruptedLazy();

            KLADRItem region = regions.get(i);
            updateProgress(i + 1, region.getFullName());
            indexBuilder.prepare(region.getCode());
            entityManager.createNativeQuery("COMMIT").executeUpdate();
        }

        info("Сортировка");
        executeUpdate("CREATE INDEX KD_IDX4 ON KD_TMP (KEYLENGTH)", false, false);

        info("Создание полнотекстового индекса");
        initProgress(indexBuilder.total());
        final File indexDirectory = KLADRManager.getDirectory(parameters, store);
        indexBuilder.build(indexDirectory, new ProgressCallback() {
            public void work(int done) throws InterruptedException {
                updateProgress(done);
                checkInterruptedLazy();
            }
        });

        parameters.setInt(KLADRManager.KLADR_INSTANCE_NUMBER_PARAM, store.getIndex());
        entityManager.createNativeQuery("COMMIT").executeUpdate();
        KLADRManager.get().setActiveStore(store);
    }

    private void load(ArchiveReader archiveReader, SQLBatch batch, String dbfTableName, String info) throws IOException, SevenZipException, SQLException, InterruptedException {
        File entryFile = null;
        DBFReader reader = null;
        try {
            entryFile = archiveReader.read(dbfTableName);
            reader = new DBFReader(new BufferedInputStream(new FileInputStream(entryFile), 64 * 1024));

            info(info + " " + dbfTableName + " (записей: " + reader.getHeader().getRecordsCount() + ")");
            initProgress(reader.getHeader().getRecordsCount());

            Map<String, Object> record;
            while ((record = reader.getRecord()) != null) {
                checkInterruptedLazy();
                batch.addRecord(record);
                updateProgress(batch.getTotalRecordsCount());
            }

            batch.flush();
        } finally {
            if (reader != null) {
                reader.close();
            }
            if (entryFile != null) {
                entryFile.deleteOnExit();
                entryFile.delete();
            }
        }
    }

    private void executeUpdate(String SQL) throws SQLException, Exception {
        executeUpdate(SQL, false, true);
    }

    private void executeUpdate(String SQL, boolean ignoreErrors, boolean wrap) throws Exception {
        try {
            entityManager.createNativeQuery(wrap ? wrap(SQL) : SQL).executeUpdate();
        } catch (Exception exception) {
            if (!ignoreErrors) {
                throw exception;
            }
        }
    }

    private String wrap(String SQL) {
        return store.wrap(SQL);
    }
}
