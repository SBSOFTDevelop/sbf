package ru.sbsoft.shared.meta.filter;

import ru.sbsoft.common.Strings;
import ru.sbsoft.shared.api.i18n.ILocalizedString;
import ru.sbsoft.shared.interfaces.FilterEditorType;

/**
 * Определение пользовательского фильтра
 * @author balandin
 * @since Nov 2, 2015
 */
public class EditorFilterDefinition extends FilterDefinition {

    private FilterEditorType filterEditorType;

    public EditorFilterDefinition() {
    }

    @Override
    public String getAlias() {
        return Strings.coalesce(super.getAlias(), filterEditorType.getCode());
    }

    @Override
    public ILocalizedString getCaption() {
        if (super.getCaption() != null) {
            return super.getCaption();
        }
        return filterEditorType.getTitle();
    }

    public FilterEditorType getFilterEditorType() {
        return filterEditorType;
    }

    public void setFilterEditorType(FilterEditorType filterEditorType) {
        this.filterEditorType = filterEditorType;
    }
}
