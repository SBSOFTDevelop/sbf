package ru.sbsoft.dao;

import java.util.List;
import ru.sbsoft.meta.columns.consts.PropertiesEnum;
import ru.sbsoft.shared.meta.Columns;
import ru.sbsoft.shared.meta.IColumns;
import ru.sbsoft.shared.meta.filter.EditorFilterDefinition;
import ru.sbsoft.shared.meta.filter.FilterDefinition;
import ru.sbsoft.shared.meta.filter.FilterDefinitions;
import ru.sbsoft.shared.meta.filter.LookupFilterDefinition;

/**
 * Создает метаданные для грида
 *
 * @author sokolov
 */
public class MetaDataBuilder {

    public static IColumns getMeta(IMetaTemplateInfo template) {
        Columns columns = template.getColumnsInfo().getColumns();
        columns.setSort(template.getDefaultSort());
        columns.setCaption(template.getProperties().get(String.class, PropertiesEnum.CAPTION));
        columns.setFilterDefinitions(convert(template.getFilterDefinitions()));
        columns.setFilterTemplate(template.getFilterTemplate());
        columns.setTemplateClassName(template.getClass().getName());
        return columns;

    }

    private static FilterDefinitions convert(FilterDefinitions filterDefinitions) {
        List<FilterDefinition> items = filterDefinitions.getServerItems();
        for (FilterDefinition item : items) {
            if (item instanceof EditorFilterDefinition) {
                filterDefinitions.getDataDefs().add((EditorFilterDefinition) item);
            } else if (item instanceof LookupFilterDefinition) {
                filterDefinitions.getLinkDefs().add((LookupFilterDefinition) item);
            } else {
                throw new IllegalStateException();
            }
        }
        return filterDefinitions;
    }

}
