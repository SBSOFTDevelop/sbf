package ru.sbsoft.shared.api.i18n;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Контейнер для передачи интформации по локализации с сервера на клиент.
 *
 * @author Fedor Resnyanskiy, SBSOFT
 */
public class i18nModuleInfo implements Serializable {

    private List<i18nLibrary> libraries = new ArrayList<i18nLibrary>();

    public List<i18nLibrary> getLibraries() {
        if (libraries == null) {
            libraries = new ArrayList<i18nLibrary>();
        }
        return libraries;
    }

    public void setLibraries(List<i18nLibrary> libraries) {
        this.libraries = libraries;
    }
    
}
