package ru.sbsoft.client.components.browser.filter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import ru.sbsoft.client.I18n;
import ru.sbsoft.client.components.grid.SystemGrid;
import ru.sbsoft.client.components.grid.dlgbase.Caption;
import ru.sbsoft.shared.FilterInfo;
import ru.sbsoft.shared.FilterInfoGroup;
import ru.sbsoft.shared.FiltersBean;
import ru.sbsoft.shared.meta.Column;
import ru.sbsoft.shared.meta.filter.FilterDefinition;
import ru.sbsoft.shared.meta.filter.EditorFilterDefinition;
import ru.sbsoft.shared.meta.filter.FilterTemplateItem;
import ru.sbsoft.shared.meta.filter.LookupFilterDefinition;
import ru.sbsoft.shared.api.i18n.consts.SBFBrowserStr;

/**
 * Восстановление визуального представления фильтра из базы
 *
 * @author balandin
 * @since Nov 9, 2015
 */
public class RestoreFilterCommand {

    private final FilterWindow filterWindow;
    private final FilterGroup rootGroup;
    private final FiltersBean filters;
    private final ErrorStore errors = new ErrorStore();
    //
    private final Map<String, FilterInfo> sysFilterCache;
    private final List<FilterTemplateItem> dataTemplates = new ArrayList<FilterTemplateItem>();
    private final List<FilterTemplateItem> linkTemplates = new ArrayList<FilterTemplateItem>();

    public RestoreFilterCommand(FilterWindow filterWindow, FilterGroup rootGroup, FiltersBean filters) {
        this.filterWindow = filterWindow;
        this.rootGroup = rootGroup;
        this.filters = filters;
        this.sysFilterCache = new HashMap<String, FilterInfo>();
        this.sysFilterCache.putAll(filters.getSysCache());

        SystemGrid grid = filterWindow.getGrid();
        List<FilterTemplateItem> tempaltes = grid.getMetaInfo().getFilterTemplate().getItems();
        for (FilterTemplateItem tempalte : tempaltes) {
            String alias = tempalte.getAlias();
            Column column = grid.getMetaInfo().getColumnForAlias(alias);
            if (column != null) {
                tempalte.assign(column);
                dataTemplates.add(tempalte);
                continue;
            }
            FilterDefinition definition = grid.getMetaInfo().getFilterDefinitions().get(alias);
            if (definition != null) {
                tempalte.assign(definition);
                if (definition instanceof EditorFilterDefinition) {
                    dataTemplates.add(tempalte);
                } else if (definition instanceof LookupFilterDefinition) {
                    linkTemplates.add(tempalte);
                }
                continue;
            }
            throw new IllegalArgumentException();
        }
    }

    public void execute() {
        restoreSystemFilters(dataTemplates, I18n.get(SBFBrowserStr.msgAccording));
        restoreSystemFilters(linkTemplates, I18n.get(SBFBrowserStr.msgRelations));
        restoreUserFilters(filters.getUserFilters());
        errors.showErrors();
        // TODO по хорошему надо проверить что осталось в sysFilterCache и уведомить пользователя
    }

    /**
     * Восстанавливаем системную часть фильтра
     *
     * @param tempaltes
     * @param caption
     */
    private void restoreSystemFilters(List<FilterTemplateItem> tempaltes, String caption) {
        if (!tempaltes.isEmpty()) {
            rootGroup.addUnit(new Caption(caption));
            for (FilterTemplateItem tempalte : tempaltes) {
                FilterItem filterItem = rootGroup.addUnit(filterWindow.createItem(true));
                try {
                    restoreSystemFilter(filterItem, tempalte);
                } catch (FilterSetupException ex) {
                    errors.add(filterItem, null, ex);
                }
            }
        }
    }

    private void restoreSystemFilter(FilterItem item, FilterTemplateItem template) throws FilterSetupException {
        final String alias = template.getAlias();
        CustomBehavior behavior = item.initBehavior(alias, template);
        if (behavior == null) {
            throw new FilterSetupException(I18n.get(SBFBrowserStr.msgFilterNotFound), alias);
        }
        final FilterInfo f = sysFilterCache.get(alias);
        if (f != null) {
            behavior.restore(f, errors);
            sysFilterCache.remove(alias);
        }
    }

    /**
     * Восстанавливаем "пользоватеский блок"
     *
     * @param filterInfoGroup
     */
    private void restoreUserFilters(FilterInfoGroup filterInfoGroup) {
        if (filterInfoGroup != null) {
            final List<FilterInfo> childs = filterInfoGroup.getChildFilters();
            for (FilterInfo filterInfo : childs) {
                restoreUserFilter(rootGroup, filterInfo);
            }
        }
    }

    private void restoreUserFilter(FilterGroup parent, FilterInfo filterInfo) {
        if (filterInfo instanceof FilterInfoGroup) {
            FilterGroup group = parent.addUnit(new FilterGroup(null));
            for (FilterInfo filter : ((FilterInfoGroup) filterInfo).getChildFilters()) {
                restoreUserFilter(group, filter);
            }
        } else {
            FilterItem item = parent.addUnit(filterWindow.createItem());
            try {
                restoreItem(item, filterInfo);
            } catch (FilterSetupException ex) {
                errors.add(item, filterInfo, ex);
            }
        }
    }

    private void restoreItem(FilterItem filterItem, FilterInfo filterInfo) throws FilterSetupException {
        final CustomBehavior behavior = filterItem.initBehavior(filterInfo.getColumnName());
        if (behavior != null) {
            behavior.restore(filterInfo, errors);
        } else {
            errors.add(filterItem, filterInfo, I18n.get(SBFBrowserStr.msgFilterNotFound));
        }
    }
}
