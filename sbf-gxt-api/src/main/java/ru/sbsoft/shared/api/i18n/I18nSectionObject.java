package ru.sbsoft.shared.api.i18n;

import ru.sbsoft.shared.consts.I18nSection;

/**
 *
 * @author Sokoloff
 */
public class I18nSectionObject implements I18nSection {

    private String name;

    public I18nSectionObject(String name) {
        this.name = name;
    }
    
    @Override
    public String getName() {
        return name;
    }
    
}
