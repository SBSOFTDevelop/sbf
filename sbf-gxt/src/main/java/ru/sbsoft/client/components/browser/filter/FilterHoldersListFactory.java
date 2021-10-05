package ru.sbsoft.client.components.browser.filter;

import java.util.List;
import ru.sbsoft.client.I18n;
import ru.sbsoft.client.components.browser.filter.fields.AbstractHoldersListFactory;
import ru.sbsoft.client.components.browser.filter.fields.CaptionHolder;
import ru.sbsoft.client.components.browser.filter.fields.FilterDefinitionHolder;
import ru.sbsoft.client.components.grid.SystemGrid;
import ru.sbsoft.client.components.grid.column.CustomColumnConfig;
import ru.sbsoft.shared.api.i18n.consts.SBFBrowserStr;
import ru.sbsoft.shared.meta.filter.FilterDefinition;
import ru.sbsoft.shared.meta.filter.FilterDefinitions;

/**
 *
 * @author Kiselev
 */
public class FilterHoldersListFactory extends AbstractHoldersListFactory<FilterHoldersList> {

    private SystemGrid grid = null;

    @Override
    public FilterHoldersList create(SystemGrid grid) {
        this.grid = grid;
        FilterHoldersList h = super.create(grid);
        // if (this == null) {
        // добавляем не-колоночные фильры
        FilterDefinitions defs = grid.getMetaInfo().getFilterDefinitions();
        addItems(h, I18n.get(SBFBrowserStr.msgFiltersAccordingTo), defs.getDataDefs());
        addItems(h, I18n.get(SBFBrowserStr.msgFiltersRelationsTo), defs.getLinkDefs());
        
// }
        return h;
    }

    @Override
    protected FilterHoldersList createInstance() {
        return new FilterHoldersList(grid);
    }

    @Override
    protected boolean isColumnMatch(CustomColumnConfig c) {
        return super.isColumnMatch(c) && c.getColumn().isFiltered();
    }

    private void addItems(FilterHoldersList hold, String caption, List<? extends FilterDefinition> items) {
        CaptionHolder h = null;
        for (FilterDefinition item : items) {
            if (!item.isHidden()) {
                FilterDefinitionHolder def = new FilterDefinitionHolder(item);
                if (h == null) {
                    h = new CaptionHolder(caption, 0);
                    hold.add(h);
                }
                h.getChilds().add(def);
                hold.add(def);
            }
        }
    }

}
