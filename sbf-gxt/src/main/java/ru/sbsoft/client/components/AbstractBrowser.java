package ru.sbsoft.client.components;

/**
 *
 * @author Kiselev
 */
public abstract class AbstractBrowser implements ManagedBrowser {
    private String idBrowser;
    private String shortName;
    private String caption;

    public String getIdBrowser() {
        return idBrowser;
    }

    public void setIdBrowser(String idBrowser) {
        this.idBrowser = idBrowser;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }
}
