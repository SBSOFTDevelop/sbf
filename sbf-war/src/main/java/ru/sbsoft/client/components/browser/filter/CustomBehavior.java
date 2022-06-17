package ru.sbsoft.client.components.browser.filter;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.ui.Label;
import ru.sbsoft.svc.core.client.util.Margins;
import ru.sbsoft.client.components.browser.filter.fields.CustomHolder;
import ru.sbsoft.client.components.browser.filter.fields.FieldHolder;
import ru.sbsoft.client.components.browser.filter.fields.FilterDefinitionHolder;
import ru.sbsoft.client.utils.HLD;
import ru.sbsoft.common.Strings;
import ru.sbsoft.shared.FilterInfo;
import ru.sbsoft.shared.meta.filter.EditorFilterDefinition;
import ru.sbsoft.shared.meta.filter.FilterDefinition;
import ru.sbsoft.shared.meta.filter.FilterTemplateItem;
import ru.sbsoft.shared.meta.filter.LookupFilterDefinition;

/**
 * Базовый класс одного из "поведений" элементов фильтров
 *
 * ColumnBehavior - Обработка колоночных элементов фильтра LookupFieldBehavior -
 * Обработка элементов фильтра / лукап полей EditorBehavior - Обработка
 * элементов фильтра - кастомный полей
 *
 * @author balandin
 * @since Nov 6, 2015
 */
public abstract class CustomBehavior {

    protected final FilterItem filterItem;
    protected final FieldsComboBox fields;
    //
    protected FilterTemplateItem filterTemplate;

    public CustomBehavior(FilterItem filterItem, FieldsComboBox fields) {
        this.filterItem = filterItem;
        this.fields = fields;
    }

    public abstract void initFilterTemplate() throws FilterSetupException;

    public FilterItem getFilterItem() {
        return filterItem;
    }

    public boolean isSystem() {
        return filterItem.isSystem();
    }

    public static CustomBehavior initBehavior(FilterItem item, FieldsComboBox fields, CustomHolder holder, DictionaryLoader dictionaryLoader) {
        item.removeAllEditors();
        if (holder instanceof FieldHolder) {
            return new ColumnBehavior(item, fields, ((FieldHolder) holder).getColumn(), dictionaryLoader);
        } else if (holder instanceof FilterDefinitionHolder) {
            return initBehavior(item, fields, ((FilterDefinitionHolder) holder).getDefinition());
        }
        return null;
    }

    public static CustomBehavior initBehavior(FilterItem item, FieldsComboBox fields, FilterDefinition definition) {
        item.removeAllEditors();
        if (definition instanceof EditorFilterDefinition) {
            return new EditorBehavior(item, fields, (EditorFilterDefinition) definition);
        } else if (definition instanceof LookupFilterDefinition) {
            return new LookupFieldBehavior(item, fields, (LookupFilterDefinition) definition);
        }
        return null;
    }

    protected Label makeCaption(String caption, boolean required) {
        Label l = new Label(caption + (required ? "*" : Strings.EMPTY) + ":");
        l.setWidth(283 + "px");
        l.getElement().getStyle().setProperty("textAlign", "right");
        l.getElement().getStyle().setOverflow(Style.Overflow.HIDDEN);
        l.getElement().getStyle().setProperty("whiteSpice", "nowrap");
        l.getElement().getStyle().setProperty("textOverflow", "ellipsis");
        l.setLayoutData(new HLD(-1, 1, new Margins(4, 2, 0, 1)));
        return l;
    }

    protected void initComplete() {
        Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
            @Override
            public void execute() {
                filterItem.forceLayout();
            }
        });
    }

    public boolean validate() {
        String m = getValidateMessage();
        filterItem.setError(m);
        return (m == null);
    }

    public abstract String getValidateMessage();

    public abstract FilterInfo getFilterInfo(boolean system);

    public abstract void restore(FilterInfo filterInfo, ErrorStore errors);

    public abstract void clearValue();

    public void tryClearValue() {
        if (filterTemplate == null || !filterTemplate.getRequired()) {
            clearValue();
        }
    }



    public FilterTemplateItem getFilterTemplate() {
        return filterTemplate;
    }

    public void setFilterTemplate(FilterTemplateItem filterTemplate) {
        this.filterTemplate = filterTemplate;
    }
}
