package ru.sbsoft.client.components.browser.filter;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.Component;
import java.util.HashSet;
import ru.sbsoft.client.I18n;
import ru.sbsoft.client.components.browser.filter.fields.CustomHolder;
import ru.sbsoft.client.components.grid.dlgbase.HoldersStore;
import ru.sbsoft.client.components.grid.dlgbase.Item;
import ru.sbsoft.client.utils.ClientUtils;
import ru.sbsoft.shared.FilterInfo;
import ru.sbsoft.shared.api.i18n.consts.SBFBrowserStr;
import ru.sbsoft.shared.meta.filter.FilterDefinition;
import ru.sbsoft.shared.meta.filter.FilterTemplateItem;

/**
 *
 * @author Kiselev
 */
public class FilterItem extends Item implements IFilterInfoProvider {

    private CustomBehavior behavior;
    private HashSet<Component> regularControls;
    private final FilterHoldersList holdersList;

    public FilterItem(FilterHoldersList holdersList, boolean system) {
        super(new HoldersStore(holdersList));
        this.holdersList = holdersList;
        fields.addValueChangeHandler(new ValueChangeHandler<CustomHolder>() {
            @Override
            public void onValueChange(ValueChangeEvent<CustomHolder> event) {
                initBehavior(event.getValue());
            }
        });
        if (system) {
            removeLeader();
            remove(fields);
        }
    }

    public FilterHoldersList getHoldersList() {
        return holdersList;
    }

    public CustomBehavior initBehavior(String alias, FilterTemplateItem template) throws FilterSetupException {
        CustomBehavior b = initBehavior(alias);
        if (b != null) {
            b.setFilterTemplate(template);
            b.initFilterTemplate();
        }
        return b;
    }

    public CustomBehavior initBehavior(String alias) throws FilterSetupException {
        CustomHolder h = holdersList.findHolder(alias);
        if (h != null) {
            fields.setValue(h, false, true);
            return initBehavior(h);
        }
        FilterDefinition def = holdersList.getGrid().getMetaInfo().getFilterDefinitions().get(alias);
        if (def != null) {
            return initBehavior(def);
        }
        throw new FilterSetupException(I18n.get(SBFBrowserStr.msgFilterNotFound), alias);
    }

    public CustomBehavior initBehavior(CustomHolder holder) {
        return behavior = CustomBehavior.initBehavior(this, fields, holder, new DictionaryLoader(new ComponentProvider() {
            @Override
            public Component getComponent() {
                return ClientUtils.findWindow(FilterItem.this);
            }
        }, holdersList.getGrid()));
    }

    public CustomBehavior initBehavior(FilterDefinition filterDefinition) {
        return behavior = CustomBehavior.initBehavior(this, fields, filterDefinition);
    }

    public HashSet<Component> getRegularControls() {
        if (regularControls == null) {
            regularControls = new HashSet<Component>();
        }
        return regularControls;
    }

    public void addControl(Component component, HorizontalLayoutData layoutData) {
        getRegularControls().add(component);
        if (layoutData == null) {
            add(component);
        } else {
            add(component, layoutData);
        }
    }

    public boolean isSystem() {
        return isFixed();
    }

    @Override
    public boolean validate() {
        return behavior == null ? true : behavior.validate();
    }

    @Override
    public FilterInfo getFilterInfo(boolean system) {
        return behavior == null ? null : behavior.getFilterInfo(system);
    }

    public CustomBehavior getUseCase() {
        return behavior;
    }

    public boolean initialized() {
        if (getWidgetCount() > 0) {
            final Widget w = getWidget(getWidgetCount() - 1);
            if (w instanceof Component) {
                return !getRegularControls().contains((Component) w);
            }
        }
        return false;
    }

    public void removeAllEditors() {
        for (int i = getWidgetCount() - 1; i >= 0; i--) {
            if (i < getWidgetCount()) {
                Widget w = getWidget(i);
                if (w != getLeader() && w != fields) {
                    w.removeFromParent();
                }
            }
        }
    }

    public void removeEditors() {
        while (initialized()) {
            getWidget(getWidgetCount() - 1).removeFromParent();
        }
    }

    @Override
    public void clearValue() {
        if (behavior != null) {
            behavior.tryClearValue();
        }
    }
}
