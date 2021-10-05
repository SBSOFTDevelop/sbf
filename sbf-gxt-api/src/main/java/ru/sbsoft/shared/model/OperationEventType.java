package ru.sbsoft.shared.model;

import ru.sbsoft.shared.api.i18n.I18nResource;
import ru.sbsoft.shared.consts.I18nType;

/**
 * Сообщения операций
 *
 * @author panarin
 */
public enum OperationEventType {

    RESULT("enumPreparedReport"),
    EXPORT("enumPreparedData"),
    //
    START("enumStartProcess", true),
    FINISH_OK("enumProcessComplit", true),
    FINISH_ERROR("enumProcessError", true),
    //
    INFO,
    DEBUG,
    WARNING,
    ERROR;
    //
    //
    //
    private final String key;
    private final boolean terminal;

    OperationEventType() {
        this(null);
    }

    OperationEventType(String key) {
        this(key, false);
    }

    OperationEventType(String key, boolean terminal) {
        this.key = key;
        this.terminal = terminal;
    }

    public String getMessage(I18nResource i18nResource) {
        return i18nResource.i18n(I18nType.GENERAL, key);
    }

    public static OperationEventType find(String name) {
        for (OperationEventType e : values()) {
            if (name.equals(e.name())) {
                return e;
            }
        }
        return null;
    }
    
    public boolean isTerminal() {
        return terminal;
    }
}
