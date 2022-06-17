package ru.sbsoft.shared.api.i18n;

import ru.sbsoft.shared.consts.I18nSection;

/**
 *
 * @author Sokoloff
 */
public class I18nResourceInfoObject implements I18nResourceInfo {

    private I18nSection lib;
    private String key;

    public I18nResourceInfoObject(String lib, String key) {
        this.lib = new I18nSectionObject(lib);
        this.key = key;
    }

    @Override
    public I18nSection getLib() {
        return lib;
    }

    @Override
    public String getKey() {
        return key;
    }

}
