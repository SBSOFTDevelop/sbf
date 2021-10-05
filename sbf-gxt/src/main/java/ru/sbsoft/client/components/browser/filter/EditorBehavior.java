package ru.sbsoft.client.components.browser.filter;

import com.sencha.gxt.widget.core.client.Component;
import ru.sbsoft.client.I18n;
import ru.sbsoft.client.components.actions.ActionManager;
import ru.sbsoft.client.components.browser.filter.editor.FilterEditor;
import ru.sbsoft.client.components.browser.filter.editor.FilterEditorFactory;
import ru.sbsoft.client.components.browser.filter.fields.CustomHolder;
import ru.sbsoft.client.components.grid.dlgbase.Item;
import ru.sbsoft.shared.ComparisonEnum;
import ru.sbsoft.shared.FilterEditorConfigBean;
import ru.sbsoft.shared.FilterInfo;
import ru.sbsoft.shared.filter.FilterInfoImpl;
import ru.sbsoft.shared.meta.filter.EditorFilterDefinition;
import ru.sbsoft.shared.api.i18n.consts.SBFBrowserStr;

/**
 * Обработка элементов фильтра - кастомных полей
 * @author balandin
 * @since Nov 9, 2015
 */
public class EditorBehavior extends CustomBehavior {

    private final EditorFilterDefinition definition;
    private final FilterEditor filterEditor;
    private final Component component;

    public EditorBehavior(FilterItem filterItem, FieldsComboBox fields, EditorFilterDefinition definition) {
        super(filterItem, fields);
        this.definition = definition;

        FilterEditorConfigBean d = new FilterEditorConfigBean();
        d.setDescription(I18n.get(definition.getDescription()));
        d.setAlias(definition.getAlias());

        filterEditor = FilterEditorFactory.create(definition.getFilterEditorType(), d);
        component = filterEditor.getComponent();

        if (!filterItem.isSystem()) {
            filterItem.addControl(filterItem.getErrorControl(), Item.MARGINS);
            filterItem.add(component, Item.MARGINS);
        }
    }

    @Override
    public void initFilterTemplate() throws FilterSetupException {
        CustomHolder holder = filterItem.getHoldersList().findHolder(filterTemplate.getAlias());
        String caption = (holder != null) ? holder.getShortTitle() : I18n.get(definition.getCaption());
        boolean required = filterTemplate == null ? false : filterTemplate.getRequired();
        filterItem.setCaption(makeCaption(caption, required));
        filterItem.addControl(filterItem.getErrorControl(), Item.MARGINS);
        filterItem.addControl(component, Item.MARGINS);
        initComplete();
    }

    @Override
    public String getValidateMessage() {
        if (filterTemplate != null && filterTemplate.getRequired()) {
            if (filterEditor.isEmpty()) {
                return I18n.get(SBFBrowserStr.msgDataNotFilled);
            }
        }
        return null;
    }

    @Override
    public FilterInfo getFilterInfo(boolean system) {
        if (isSystem() != system) {
            return null;
        }
        if (!validate()) {
            throw new IllegalStateException();
        }
        FilterInfo f = filterEditor.getFilterInfo();
        if (f != null) {
            f.setComparison(ComparisonEnum.eq);
            f.setColumnName(definition.getAlias());
        }
        return f;
    }

    @Override
    public void restore(FilterInfo filterInfo, ErrorStore errors) {
        filterEditor.setFilterInfo(filterInfo);
    }

    @Override
    public void clearValue() {
        filterEditor.setFilterInfo(new FilterInfoImpl());
    }
}
