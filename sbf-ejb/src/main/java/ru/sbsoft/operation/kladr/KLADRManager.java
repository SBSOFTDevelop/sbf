package ru.sbsoft.operation.kladr;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.Objects;
import javax.persistence.EntityManager;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.store.FSDirectory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.sbsoft.system.ParameterException;
import ru.sbsoft.system.Parameters;

/**
 * Класс-Singleton управляет хранилищем КЛАДР {@link KLADRStore} во время импорта справочника.
 * <p> Используется экземпляром класса <code>AbstractKLADRImportRunner</code>, запускающим операцию импорта КЛАДР в методе {@link AbstractKLADRImportRunner#processFile(ru.sbsoft.model.StorageItem)}.
 * @author balandin
 * @since Mar 15, 2013 1:16:34 PM
 */
public class KLADRManager {

    public static String STORE_DIRECTORY_PARAM = "store.dir";
    //
    public static String KLADR_INSTANCE_NUMBER_PARAM = "kladr.instance.number";
    private final static Logger LOGGER = LoggerFactory.getLogger(KLADRManager.class);
    //
    private static volatile KLADRManager instance;
    private KLADRStore activeStore;
    private IndexReader reader;
    private IndexSearcher searcher;
    private boolean checked;
    //
    private Parameters parameters;
    //Дата последней проверки актуальности справочника
    private static long lastUpdateTime = new Date().getTime();
    private static final long updateInterval = 1000 * 60;
    private static KLADRStore prevousStore;

    private KLADRManager() {
    }

    public KLADRStore getActiveStore() {
        return activeStore;
    }

    public KLADRStore getActiveStore(EntityManager entityManager) {
        if (activeStore == null) {
            activeStore = checkActiveStore(getParameters(entityManager));
        }
        return activeStore;
    }

    public void setActiveStore(KLADRStore store) {
        if (store != activeStore) {
            this.activeStore = store;

            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException ignore) {
                }
            }
            reader = null;
            searcher = null;
        }
    }

    public synchronized static KLADRManager get() {
        if (instance == null) {
            instance = new KLADRManager();
        }
        return instance;
    }

    private /* synchronized */ Parameters getParameters(EntityManager entityManager) {
        if (parameters == null) {
            parameters = new Parameters(entityManager);
        }
        return parameters;
    }

    private synchronized void initSearcher(EntityManager entityManager) throws IOException {
        if (searcher != null && !needRefresh()) {
            return;
        }
        if(needRefresh()){
            checked = false;
            activeStore = null;
        }
        
        final KLADRStore store = getActiveStore(entityManager);
        if (store == null || (prevousStore != null && Objects.equals(prevousStore.getIndex(), store.getIndex()))) {
            return;
        }
        reader = DirectoryReader.open(FSDirectory.open(getDirectory(getParameters(entityManager), store)));
        searcher = new IndexSearcher(reader);
        prevousStore = store;
        lastUpdateTime = new Date().getTime();
    }

    public static File getDirectory(Parameters parameters, KLADRStore store) throws IOException {
        final File storeDir = getStoreDirectory();
        final String indexSubDirectoryFileName = parameters.get(store.getIndexDirectoryParam());
        final File f = new File(storeDir, indexSubDirectoryFileName);
        if (!f.exists()) {
            f.mkdir();
        }
        if (!f.isDirectory()) {
            throw new IOException("Unavailable directory " + f.getAbsolutePath());
        }
        return f;
    }

    public static File getStoreDirectory() throws IOException {
        final String storeDirFileName = System.getProperty("file.store");
        if (storeDirFileName == null) {
            throw new IOException("System property [file.store] not defined");
        }
        final File f = new File(storeDirFileName);
        if (!f.exists()) {
            throw new IOException("Directory not exists " + storeDirFileName);
        }
        if (!f.isDirectory()) {
            throw new IOException("Unavailable directory " + storeDirFileName);
        }
        return f;
    }

    public IndexSearcher getSearcher() {
        return searcher;
    }

    public IndexSearcher getSearcher(EntityManager entityManager) throws IOException {
        if (searcher == null || needRefresh()) {
            initSearcher(entityManager);
        }
        return searcher;
    }

    private KLADRStore checkActiveStore(Parameters parameters) {
        if (!checked) {
            try {
                return getActiveStore(parameters);
            } catch (ParameterException ex) {
                LOGGER.error("Error reading parameter " + KLADR_INSTANCE_NUMBER_PARAM + ":" + ex.getMessage());
            } finally {
                checked = true;
            }
        }
        return null;
    }

    public static KLADRStore getActiveStore(Parameters parameters) throws ParameterException {
        return KLADRStore.indexToStore(parameters.getInt(KLADR_INSTANCE_NUMBER_PARAM, 0));
    }
    
    private static boolean needRefresh(){
        return lastUpdateTime + updateInterval < new Date().getTime();
    }
}
