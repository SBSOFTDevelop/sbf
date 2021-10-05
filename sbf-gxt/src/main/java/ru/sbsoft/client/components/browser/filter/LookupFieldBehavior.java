package ru.sbsoft.client.components.browser.filter;

import java.util.List;
import ru.sbsoft.client.I18n;
import ru.sbsoft.client.components.actions.ActionManager;
import ru.sbsoft.client.components.browser.filter.fields.CustomHolder;
import ru.sbsoft.client.components.browser.filter.lookup.LookupFieldFactory;
import ru.sbsoft.client.components.form.LookupField;
import ru.sbsoft.client.components.grid.dlgbase.Item;
import ru.sbsoft.client.filter.HandlerLookup;
import ru.sbsoft.shared.FilterInfo;
import ru.sbsoft.shared.LookupFieldConfigBean;
import ru.sbsoft.shared.filter.LookUpFilterInfo;
import ru.sbsoft.shared.meta.filter.LookupFilterDefinition;
import ru.sbsoft.shared.model.LookupInfoModel;
import ru.sbsoft.shared.api.i18n.consts.SBFBrowserStr;

/**
 * Обработка элементов фильтра - лукап полей
 *
 * @author balandin
 * @since Nov 9, 2015
 */
public class LookupFieldBehavior extends CustomBehavior {

    private final LookupFilterDefinition definition;
    private final LookupField lookupField;

    public LookupFieldBehavior(FilterItem filterItem, FieldsComboBox fields, LookupFilterDefinition def) {
        super(filterItem, fields);

        this.definition = def;

        LookupFieldConfigBean d = new LookupFieldConfigBean();
        d.setGridContext(def.getGridContext() + "_" + def.getLookupType().getCode());
        d.setModifier(def.getModifier());
        d.setMultiSelect(def.isMultiSelect());

        if (def.getBrowserType() != null) {
            lookupField = new HandlerLookup(def.getAlias(), def.getBrowserType()).newInstance(d);
        } else {
            lookupField = LookupFieldFactory.create(def.getLookupType(), d);
        }
        lookupField.setWidth(400);

        if (!isSystem()) {
            filterItem.addControl(filterItem.getErrorControl(), Item.MARGINS);
            filterItem.add(lookupField, Item.MARGINS);
            CustomHolder holder = filterItem.getHoldersList().findHolder(def.getAlias());
            fields.setValue(holder, true, false);
        }

        initComplete();
    }

    @Override
    public void initFilterTemplate() {
        CustomHolder holder = filterItem.getHoldersList().findHolder(filterTemplate.getAlias());
        final String caption = holder != null ? holder.getShortTitle() : I18n.get(definition.getCaption());
        boolean required = filterTemplate == null ? false : filterTemplate.getRequired();
        filterItem.setCaption(makeCaption(caption, required));
        filterItem.addControl(filterItem.getErrorControl(), Item.MARGINS);
        filterItem.add(lookupField, Item.MARGINS);
    }

    @Override
    public String getValidateMessage() {
        if (filterTemplate != null && filterTemplate.getRequired()) {
            final List<LookupInfoModel> vals = lookupField.getLookupValues();
            if (vals == null || vals.isEmpty()) {
                return I18n.get(SBFBrowserStr.msgDataNotFilled);
            };
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
        LookUpFilterInfo f = lookupField.createLookUpFilter(definition.getAlias());
        List<LookupInfoModel> lookupsList = f.getValue();
        if (lookupsList == null || lookupsList.isEmpty()) {
            return null;
        }
        return f;
    }

    @Override
    public void restore(FilterInfo filterInfo, ErrorStore errors) {
        lookupField.setValues((List<LookupInfoModel>) filterInfo.getValue());
    }

    @Override
    public void clearValue() {
        lookupField.setValue(null, false);
    }
}
