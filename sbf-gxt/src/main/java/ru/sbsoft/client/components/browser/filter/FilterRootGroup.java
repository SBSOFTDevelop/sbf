package ru.sbsoft.client.components.browser.filter;

import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.event.AddEvent;
import com.sencha.gxt.widget.core.client.event.RemoveEvent;
import ru.sbsoft.client.components.grid.dlgbase.Caption;
import ru.sbsoft.client.components.grid.dlgbase.Group;
import ru.sbsoft.client.components.grid.dlgbase.Unit;
import ru.sbsoft.shared.BooleanOperator;
import ru.sbsoft.shared.api.i18n.consts.SBFBrowserStr;

/**
 *
 * @author Kiselev
 */
public class FilterRootGroup extends FilterGroup {

    private Caption userFilterCaption = null;

    public FilterRootGroup() {
        super(BooleanOperator.AND);
        removeLeader();
        unitContainer.addAddHandler(new AddEvent.AddHandler() {
            @Override
            public void onAdd(AddEvent event) {
                if (userFilterCaption == null) {
                    Widget w = event.getWidget();
                    if (w instanceof Unit) {
                        Unit u = (Unit) w;
                        if (!u.isFixed()) {
                            addUnit(userFilterCaption = new Caption(SBFBrowserStr.labelCustoms), getUnitIndex(u));
                        }
                    }
                }
            }
        });
        unitContainer.addRemoveHandler(new RemoveEvent.RemoveHandler() {
            @Override
            public void onRemove(RemoveEvent event) {
                if (userFilterCaption != null && getLastWidget() == userFilterCaption) {
                    userFilterCaption.delete();
                    userFilterCaption = null;
                }
            }
        });
    }

    @Override
    public boolean isRootContainer() {
        return true;
    }

    @Override
    public Group getRootContainer() {
        return this;
    }
}
