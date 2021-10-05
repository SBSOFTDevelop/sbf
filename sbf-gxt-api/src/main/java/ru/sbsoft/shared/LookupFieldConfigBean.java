package ru.sbsoft.shared;

import ru.sbsoft.shared.param.DTO;

/**
 * @author balandin
 * @since Nov 6, 2015
 */
public class LookupFieldConfigBean implements DTO {

    private String gridContext;
    private Modifier modifier;
    private boolean multiSelect;

    public LookupFieldConfigBean() {
    }

    public String getGridContext() {
        return gridContext;
    }

    public void setGridContext(String gridContext) {
        this.gridContext = gridContext;
    }

    public boolean isMultiSelect() {
        return multiSelect;
    }

    public void setMultiSelect(boolean multiSelect) {
        this.multiSelect = multiSelect;
    }
    
    public Modifier getModifier() {
        return modifier;
    }

    public void setModifier(Modifier modifier) {
        this.modifier = modifier;
    }
}
