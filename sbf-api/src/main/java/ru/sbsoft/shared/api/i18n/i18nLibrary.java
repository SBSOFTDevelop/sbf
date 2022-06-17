package ru.sbsoft.shared.api.i18n;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Контейнер для передачи интформации по локализации с сервера на клиент.
 *
 * @author Fedor Resnyanskiy, SBSOFT
 */
public class i18nLibrary implements Serializable {

    private String libraryName;
    private List<i18nMessage> messages = new ArrayList<i18nMessage>();

    public String getLibraryName() {
        return libraryName;
    }

    public void setLibraryName(String libraryName) {
        this.libraryName = libraryName;
    }

    public List<i18nMessage> getMessages() {
        if (messages == null) {
            messages = new ArrayList<i18nMessage>();
        }
        return messages;
    }

    public void setMessages(List<i18nMessage> messages) {
        this.messages = messages;
    }
}
