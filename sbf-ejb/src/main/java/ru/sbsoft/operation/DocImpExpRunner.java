package ru.sbsoft.operation;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import ru.sbsoft.common.IO;
import ru.sbsoft.model.StorageItem;
import ru.sbsoft.shared.exceptions.ApplicationException;
import ru.sbsoft.dao.entity.IBaseEntity;
import ru.sbsoft.shared.api.i18n.consts.SBFExceptionStr;
import ru.sbsoft.shared.api.i18n.consts.SBFGeneralStr;

/**
 *
 * @author Sokoloff
 * @param <E>
 */
public abstract class DocImpExpRunner<E extends IBaseEntity & IFileContainer> extends BaseOperationRunner {

    private Class<E> entClass;
    private LoadFileDirection dir;

    protected void setEntClass(Class<E> entClass) {
        this.entClass = entClass;
    }

    protected void setDir(LoadFileDirection dir) {
        this.dir = dir;
    }

    @Override
    public void run() throws Exception {
        BigDecimal treatId = askSelectedRecord();
        E treat = notNull(em.find(entClass, treatId), getLocaleResource(SBFExceptionStr.documNotFound, String.valueOf(treatId.longValue())));
        File file = null;
        switch (dir) {
            case IMPORT:
                StorageItem storageItem = getStorageItem();
                try {
                    file = storageDao.load(storageItem.getStorageId());
                    treat.setFileName(storageItem.getFileName());
                    em.merge(treat);
                    em.flush();
                    treat.setFileBody(IO.readFile(file));
                    em.merge(treat);
                    em.flush();
                    info(getLocaleResource(SBFGeneralStr.msgFileLoaded));
                } finally {
                    IO.delete(file);
                    storageDao.clear(getOperationUsername(), getOperationCommand().getCode());
                }
                break;
            case EXPORT:
                info(getLocaleResource(SBFGeneralStr.msgFilePreparation));
                file = IO.saveFile(treat.getFileBody(), File.createTempFile("file-docum-tmp-", null));
                file.deleteOnExit();
                saveReport(file, treat.getFileName());
                break;
            default:
                throw new ApplicationException("Unknown load file direction: " + dir);
        }
    }

    private StorageItem getStorageItem() throws IOException {
        final StorageItem[] items = storageDao.find(getOperationUsername(), getOperationCommand().getCode(), null);
        IO.checkFilesNotEmpty(items.length);
        if (items.length > 1) {
            // чистим хранилище, иначе система загрузки перестает работать
            storageDao.clear(getOperationUsername(), getOperationCommand().getCode());
            throw new IOException("Найдено более одного файла. Повторите загрузку");
        }
        return items[0];
    }

}
