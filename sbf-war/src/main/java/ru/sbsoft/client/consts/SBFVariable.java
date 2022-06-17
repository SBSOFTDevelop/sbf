package ru.sbsoft.client.consts;

/**
 * Список глобальных переменных.
 *
 * @author Sokoloff
 */
public enum SBFVariable implements SBFConfigKey {

    APPLICATION_TITLE,
    APPLICATION_ABOUT,
    CODE_KLADR,
    GRID_CONFIG,
    LOG_OPERATION_GRID_NAME_ID,
    EMAIL_DEVELOPER;

    @Override
    public String getKey() {
        return name();
    }

}
