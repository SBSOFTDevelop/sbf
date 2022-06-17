package ru.sbsoft.dao;

import java.io.File;
import java.io.IOException;
import javax.ejb.Remote;
import ru.sbsoft.model.StorageItem;

/**
 * Интерфейс представляющий методы для работы с файловым хранилищем (сущность {@link ru.sbsoft.system.common.StorageEntity}).
 * 
 * @author balandin
 * @since Apr 5, 2013 12:50:51 PM
 */
@Remote
public interface IStorageDao {

    void clear(String user, String alias);

    long save(String user, String alias, String description, String fileName, File file) throws IOException;

    long save(String user, String alias, String fileName, File file) throws IOException;
    
    
    StorageItem[] find(String user, String alias, String fileName);
    
    StorageItem getStorageItem(long storageId);

    File load(long storageId) throws IOException;
}
