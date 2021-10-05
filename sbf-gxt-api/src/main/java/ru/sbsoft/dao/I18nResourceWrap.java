package ru.sbsoft.dao;
import ru.sbsoft.shared.api.i18n.Ii18nDao;
import ru.sbsoft.shared.api.i18n.I18nResource;
import ru.sbsoft.shared.api.i18n.ILocalizedString;
import ru.sbsoft.shared.consts.I18nSection;

/**
 *
 * @author Sokoloff
 */
public class I18nResourceWrap implements I18nResource {

    private final Ii18nDao i18nDao;
    private final String locale;

    public I18nResourceWrap(Ii18nDao i18nDao, String locale) {
        this.i18nDao = i18nDao;
        this.locale = locale;
    }

    @Override
    public String i18n(I18nSection library, String key) {
        return i18nDao.get(locale, library, key);
    }

    @Override
    public String i18n(ILocalizedString item) {
        return i18nDao.get(locale, item);
    }

}
