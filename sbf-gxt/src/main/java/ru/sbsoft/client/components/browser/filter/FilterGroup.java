package ru.sbsoft.client.components.browser.filter;

import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.dom.XElement;
import java.util.ArrayList;
import java.util.List;
import ru.sbsoft.client.I18n;
import ru.sbsoft.client.components.grid.dlgbase.Group;
import ru.sbsoft.client.components.grid.dlgbase.Unit;
import ru.sbsoft.shared.BooleanOperator;
import ru.sbsoft.shared.FilterInfo;
import ru.sbsoft.shared.FilterInfoGroup;

/**
 *
 * @author Kiselev
 */
public class FilterGroup extends Group implements IFilterInfoProvider {

    protected Label label;
    protected BooleanOperator booleanOperator;

    public FilterGroup(BooleanOperator booleanOperator) {
        this.booleanOperator = booleanOperator;
        addLeader();
    }

    @Override
    protected Leader createLeader() {
        Leader h = super.createLeader();
        label = new Label();
        final Style st = label.getElement().<XElement>cast().getStyle();
        st.setFontWeight(Style.FontWeight.BOLD);
        st.setFontSize(10, Style.Unit.PX);
        st.setPaddingTop(2, Style.Unit.PX);
        st.setPaddingLeft(4, Style.Unit.PX);
        st.setCursor(Style.Cursor.POINTER);
        h.setWidget(label);
        return h;
    }

    @Override
    protected void onAttach() {
        super.onAttach();
        Group p = getParentGroup();
        if (p instanceof FilterGroup) {
            FilterGroup g = (FilterGroup) p;
            final BooleanOperator op = g.getBooleanOperator();
            setBooleanOperator(op.getReverseOperator());
            label.setText(I18n.get(getBooleanOperator()));
        }
    }

    public BooleanOperator getBooleanOperator() {
        return booleanOperator;
    }

    public void setBooleanOperator(BooleanOperator booleanOperator) {
        this.booleanOperator = booleanOperator;
    }

    @Override
    protected void updateUnits() {
        reinitConditions();
        super.updateUnits();
    }

    private void reinitConditions() {
        for (int i = 0; i < unitContainer.getWidgetCount(); i++) {
            final Widget w = unitContainer.getWidget(i);
            if(w instanceof FilterGroup){
                final FilterGroup g = (FilterGroup) w;
                g.setBooleanOperator(getBooleanOperator().getReverseOperator());
                g.reinitConditions();
            }
        }
    }

    @Override
    public boolean isRootContainer() {
        return false;
    }

    @Override
    public boolean validate() {
        boolean result = true;
        for (int i = 0; i < unitContainer.getWidgetCount(); i++) {
            final Widget w = unitContainer.getWidget(i);
            if (w instanceof Unit) {
                result &= ((Unit) w).validate();
            }
        }
        return result;
    }

    @Override
    public FilterInfo getFilterInfo(boolean system) {
        List<FilterInfo> childs = new ArrayList<FilterInfo>();
        for (int i = 0; i < unitContainer.getWidgetCount(); i++) {
            final Widget w = unitContainer.getWidget(i);
            if (w instanceof IFilterInfoProvider) {
                FilterInfo f = ((IFilterInfoProvider) w).getFilterInfo(system);
                if (f != null) {
                    childs.add(f);
                }
            }
        }
        if (!childs.isEmpty()) {
            FilterInfoGroup r = new FilterInfoGroup(booleanOperator, childs);
            return r;
        }
        return null;
    }
    
    public void clearAll(){
        unitContainer.clear();
    }
}
