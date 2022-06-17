package ru.sbsoft.shared.meta.filter;

import ru.sbsoft.common.Strings;
import ru.sbsoft.shared.Modifier;
import ru.sbsoft.shared.api.i18n.ILocalizedString;
import ru.sbsoft.shared.interfaces.LookupType;
import ru.sbsoft.shared.interfaces.NamedGridType;

/**
 * @author balandin
 * @since Nov 3, 2015
 */
public class LookupFilterDefinition extends FilterDefinition {

    private LookupType lookupType;
    //
    private String gridContext;
    private Modifier modifier;
    private boolean multiSelect;
    private NamedGridType browserType;
    //
    private transient LookupKeyType lookupKeyType;

    public LookupFilterDefinition() {
    }

    @Override
    public String getAlias() {
        return Strings.coalesce(super.getAlias(), lookupType.getCode());
    }

    @Override
    public ILocalizedString getCaption() {
        if (super.getCaption() != null) {
            return super.getCaption();
        }
        return lookupType.getTitle();
    }

    public LookupType getLookupType() {
        return lookupType;
    }

    public void setLookupType(LookupType lookupType) {
        this.lookupType = lookupType;
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

    public LookupKeyType getKeyType() {
        return lookupKeyType;
    }

    public void setKeyType(LookupKeyType lookupKeyType) {
        this.lookupKeyType = lookupKeyType;
    }

    public LookupFilterDefinition entityKey() {
        setKeyType(LookupKeyType.ENTITY);
        return this;
    }

    public Modifier getModifier() {
        return modifier;
    }

    public void setModifier(Modifier modifier) {
        this.modifier = modifier;
    }

    public LookupFilterDefinition mode(Modifier modifier) {
        setModifier(modifier);
        return this;
    }

    public NamedGridType getBrowserType() {
        return browserType;
    }

    public void setBrowserType(NamedGridType browserType) {
        this.browserType = browserType;
    }

}
