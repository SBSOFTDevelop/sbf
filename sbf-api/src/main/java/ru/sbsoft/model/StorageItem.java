package ru.sbsoft.model;

import ru.sbsoft.shared.param.DTO;

/**
 * Модель записи файлового хранилища {@code SR_STORAGE}.
 * @author balandin
 * @since Apr 9, 2013 2:45:22 PM
 */
public class StorageItem implements DTO {

    private String user;
    private String alias;
    private String description;
    private String fileName;
    private long storageId;

    public StorageItem() {

    }

    public StorageItem(String user, String alias, String description, String fileName, long storageId) {
        this.user = user;
        this.alias = alias;
        this.description = description;
        this.fileName = fileName;
        this.storageId = storageId;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void setStorageId(long storageId) {
        this.storageId = storageId;
    }

    public String getUser() {
        return user;
    }

    public String getAlias() {
        return alias;
    }

    public String getDescription() {
        return description;
    }

    public String getFileName() {
        return fileName;
    }

    public long getStorageId() {
        return storageId;
    }
}
