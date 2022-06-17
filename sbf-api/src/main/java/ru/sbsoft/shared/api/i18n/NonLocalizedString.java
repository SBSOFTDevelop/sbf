package ru.sbsoft.shared.api.i18n;

/**
 * Класс реализует интерфейс ILocalizedString без локализации.
 *
 * @author Sokoloff
 */
public class NonLocalizedString implements ILocalizedString {

    public static NonLocalizedString EMPTY = new NonLocalizedString("");

    private String value;

    public NonLocalizedString() {
    }

    public NonLocalizedString(String value) {
        this.value = value;
    }

    @Override
    public I18nResourceInfo getResourceInfo() {
        return null;
    }

    @Override
    public String getDefaultName() {
        return value;
    }

    @Override
    public String toString() {
        return value;
    }

}
