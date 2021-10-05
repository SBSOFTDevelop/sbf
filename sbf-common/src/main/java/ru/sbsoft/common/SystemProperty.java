package ru.sbsoft.common;

import org.slf4j.LoggerFactory;

/**
 * Описание параметра, определяемого в окружении.
 * @author Fedor Resnyanskiy, SBSOFT
 */
public class SystemProperty {

    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(SystemProperty.class);

    private final String parameterIdentifier;
    private final String defaultValue;

    public SystemProperty(String parameterIdentifier, String defaultValue) {
        this.parameterIdentifier = parameterIdentifier;
        this.defaultValue = defaultValue;
    }

    public boolean exists() {
        return System.getProperty(parameterIdentifier) != null;
    }

    public String getParameterIdentifier() {
        return this.parameterIdentifier;
    }

    public String getDefaultValue() {
        return this.defaultValue;
    }

    /**
     * Получает значение параметра из настроек окружения сервера. При их
     * отсутствии применяет значение по умолчанию.
     *
     * @return значение параметра.
     */
    public String getParameterValue() {
        return System.getProperty(this.parameterIdentifier, this.defaultValue);
    }

    /**
     * Получает значение параметра и преобразует к типу Long.
     *
     * @return значение параметра, приведенное к типу Long.
     */
    public Long getParameterLongValue() {
        String value = getParameterValue();
        try {
            return Long.parseLong(value);
        } catch (NumberFormatException ex) {
            LOGGER.warn("Cannot parse " + value + " to Long, use default value " + defaultValue);
            try {
                return Long.parseLong(getDefaultValue());
            } catch (NumberFormatException ex1) {
                LOGGER.warn("Cannot parse " + getDefaultValue() + " to Long, return null");
                return null;
            }
        }
    }

    /**
     * Получает значение параметра и преобразует к типу Integer.
     *
     * @return значение параметра, приведенное к типу Integer.
     */
    public Integer getParameterIntValue() {
        String value = getParameterValue();
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException ex) {
            LOGGER.warn("Cannot parse " + value + " to Integer, use default value " + defaultValue);
            try {
                return Integer.parseInt(getDefaultValue());
            } catch (NumberFormatException ex1) {
                LOGGER.warn("Cannot parse " + getDefaultValue() + " to Integer, return null");
                return null;
            }
        }
    }

    /**
     * Получает значение параметра и преобразует к типу Boolean.
     *
     * @return значение параметра, приведенное к типу Boolean.
     */
    public Boolean getParameterBooleanValue() {
        String value = getParameterValue();
        try {
            return Boolean.parseBoolean(value);
        } catch (NumberFormatException ex) {
            LOGGER.warn("Cannot parse " + value + " to Boolean, use default value " + defaultValue);
            try {
                return Boolean.parseBoolean(getDefaultValue());
            } catch (NumberFormatException ex1) {
                LOGGER.warn("Cannot parse " + getDefaultValue() + " to Boolean, return null");
                return null;
            }
        }
    }

}
