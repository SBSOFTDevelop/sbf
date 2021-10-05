package ru.sbsoft.shared;

import java.io.Serializable;

/**
 * Класс представляющий модель для хранения информации о приложении.
 * <ul>
 * <li>private String application наименование приложения</li>
 * <li>private String version - версия</li>
 * <li>private String revision - ревизия</li>
 * <li>private String timestamp - время созд. (билда)</li>
 * <li>private String description - описание</li>
 * </ul>
 *
 * @author balandin
 * @since Oct 15, 2013 7:10:04 PM
 */
public class ApplicationInfo implements Serializable {

    private String application;
    private String version;
    private String revision;
    private String timestamp;
    private String description;
    
    

    public ApplicationInfo() {
    }

        

    public String getApplication() {
        return application;
    }

    public void setApplication(String application) {
        this.application = application;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getRevision() {
        return revision;
    }

    public void setRevision(String revision) {
        this.revision = revision;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
