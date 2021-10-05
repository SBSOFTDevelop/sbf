package ru.sbsoft.shared.api.i18n;

import java.io.Serializable;

/**
 * Контейнер для передачи интформации по локализации с сервера на клиент.
 *
 * @author Fedor Resnyanskiy, SBSOFT
 */
public class i18nMessage implements Serializable {

    private String key;
    private String value;
    private int paramCount;

    public i18nMessage() {
    }

    public i18nMessage(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String name) {
        this.key = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public int getParamCount() {
        return paramCount;
    }

    public void setParamCount(int paramCount) {
        this.paramCount = paramCount;
    }

}
