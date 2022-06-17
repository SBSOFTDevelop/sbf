package ru.sbsoft.shared.api.i18n;

/**
 * Класс реализует интерфейс ILocalizedString с применением локализации.
 *
 * @author Sokoloff
 */
public class LocalizedString implements ILocalizedString {

    private I18nResourceInfo resourceInfo;

    public LocalizedString() {
    }

    public LocalizedString(I18nResourceInfo resourceInfo) {
        this.resourceInfo = resourceInfo;
    }

    @Override
    public I18nResourceInfo getResourceInfo() {
        return resourceInfo;
    }

    @Override
    public String getDefaultName() {
        return null;
    }

}
