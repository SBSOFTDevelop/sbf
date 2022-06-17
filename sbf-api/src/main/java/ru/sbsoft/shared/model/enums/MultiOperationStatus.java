package ru.sbsoft.shared.model.enums;

import ru.sbsoft.shared.api.i18n.Ii18nDao;
import ru.sbsoft.shared.api.i18n.consts.SBFGeneralStr;

/**
 * Статус выполнения операции.
 *
 * @author Fedor Resnyanskiy, SBSOFT
 */
public enum MultiOperationStatus {

    /**
     * Создана, но еще не запущена. В этот момент можно установить параметры операции, время запуска
     * и прочие настройки.
     */
    CREATED,
    /**
     * Готова к запуску сервера. Все необходимые настройки выполнены, и операция готова к запуску.
     */
    READY_TO_START,
    /**
     * Сервер запустил выполнение операции.
     */
    STARTED,
    /**
     * Пользователь отменил выполнение операции. Не факт, что операция будет отменена, если сервер
     * уже зыпустил её выполнение.
     */
    CANCEL,
    /**
     * Операция успешно отменена.
     */
    CANCELED,
    /**
     * Операция успешно выполнена.
     */
    DONE,
    /**
     * Операция остановлена с ошибкой.
     */
    ERROR;

    public String caption(Ii18nDao i18nDao, String locale) {
        switch (this) {
            case CREATED:
                return i18nDao.get(locale, SBFGeneralStr.enumCreate);
            case READY_TO_START:
                return i18nDao.get(locale, SBFGeneralStr.enumRadyStart);
            case STARTED:
                return i18nDao.get(locale, SBFGeneralStr.enumStarted);
            case CANCEL:
                return i18nDao.get(locale, SBFGeneralStr.enumCancelUser);
            case CANCELED:
                return i18nDao.get(locale, SBFGeneralStr.enumCancelServer);
            case DONE:
                return i18nDao.get(locale, SBFGeneralStr.enumComplitedSecsess);
            case ERROR:
                return i18nDao.get(locale, SBFGeneralStr.enumComplitedError);
            default:
                return name();
        }
    }

}
