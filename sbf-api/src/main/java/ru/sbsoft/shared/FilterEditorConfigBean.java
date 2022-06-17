package ru.sbsoft.shared;

import ru.sbsoft.shared.param.DTO;

/**
 * @author balandin
 * @since Nov 5, 2015
 */
public class FilterEditorConfigBean implements DTO {

    private String alias;
    private String caption;
    private String description;
    private boolean required;

    public FilterEditorConfigBean() {
    }
    
    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isRequired() {
        return required;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }
}
